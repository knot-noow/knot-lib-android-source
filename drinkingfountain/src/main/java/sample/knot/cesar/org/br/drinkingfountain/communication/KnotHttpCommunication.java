/*
 * Copyright (c) 2017, CESAR.
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license. See the LICENSE file for details.
 *
 *
 */

package sample.knot.cesar.org.br.drinkingfountain.communication;

import java.text.ParseException;
import java.util.List;

import br.org.cesar.knot.lib.connection.FacadeConnection;
import br.org.cesar.knot.lib.event.Event;
import br.org.cesar.knot.lib.model.KnotList;
import br.org.cesar.knot.lib.model.KnotQueryData;
import br.org.cesar.knot.lib.model.KnotQueryDateData;
import br.org.cesar.knot.lib.util.DateUtils;
import br.org.cesar.knot.lib.util.LogLib;
import sample.knot.cesar.org.br.drinkingfountain.database.FacadeDatabase;
import sample.knot.cesar.org.br.drinkingfountain.model.DrinkFountainDevice;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterLevelData;
import sample.knot.cesar.org.br.drinkingfountain.util.LogKnotDrinkFountain;
import sample.knot.cesar.org.br.drinkingfountain.util.PreferenceUtil;
import sample.knot.cesar.org.br.drinkingfountain.util.Util;

public class KnotHttpCommunication implements KnotCommunication {

    private static final String ENDPOINT = PreferenceUtil.getInstance().getEndPoint();
    private static final String UUID_OWNER = PreferenceUtil.getInstance().getUuid();
    private static final String TOKEN_OWNER = PreferenceUtil.getInstance().getTonken();

    private static final Object lock = new Object();

    /**
     * Class used to access the db repository
     */
    private FacadeDatabase mDrinkFountainDB;

    /**
     * Class used to access knot LIB
     */
    public FacadeConnection mKnotApi;

    /**
     * Only Instance
     */
    private static KnotHttpCommunication sInstance;


    /**
     * Private constructor
     */
    private KnotHttpCommunication() {
        //Initializing the KNOT API
        mKnotApi = FacadeConnection.getInstance();

        // Configuring the API
        mKnotApi.setupHttp(ENDPOINT, UUID_OWNER, TOKEN_OWNER);

        //Initializing the DATABASE to save app information
        mDrinkFountainDB = FacadeDatabase.getInstance();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static KnotHttpCommunication getInstance() {
        synchronized (lock) {
            if (sInstance == null) {
                sInstance = new KnotHttpCommunication();
            }
            return sInstance;
        }
    }

    @Override
    public void getAllDevices() {
        KnotList<DrinkFountainDevice> mDrinkFountainDeviceList = new KnotList<>(DrinkFountainDevice.class);

        mKnotApi.httpGetDeviceList(mDrinkFountainDeviceList, new Event<List<DrinkFountainDevice>>() {
            @Override
            public void onEventFinish(List<DrinkFountainDevice> deviceList) {

                if (deviceList != null) {
                    mDrinkFountainDB.insertDrinkFountainList(deviceList);

                    getDataByDevice();
                }
            }

            @Override
            public void onEventError(Exception e) {
                LogKnotDrinkFountain.printE(e);
            }
        });
    }

    @Override
    public void getDataByDevice() {

        KnotList<WaterLevelData> mWaterLevelData = new KnotList<>(WaterLevelData.class);

        // get devices;
        List<DrinkFountainDevice> mDrinkFountainDeviceList = mDrinkFountainDB.getAllDrinkFountain();

        for (final DrinkFountainDevice drinkFountainDevice : mDrinkFountainDeviceList) {

            // get the last valid waterLevelData to build the query
            WaterLevelData waterLevelData = mDrinkFountainDB.getCurrentLevelByDeviceUUID(drinkFountainDevice.getUuid());

            KnotQueryDateData knotQueryDateDataStart = null;
            KnotQueryDateData knotQueryDateDataFinish = null;

            //Verify if the waterLevelData is valid
            if (waterLevelData != null) {
                String timeStamp = Util.convertMillisecondsToValidFormat(Long.parseLong(waterLevelData.getTimestamp()));

                try {
                    //Building the start date
                    knotQueryDateDataStart = DateUtils.getKnotQueryDateData(timeStamp);
                } catch (ParseException e) {
                    LogKnotDrinkFountain.printE(e);
                }

            }

            //get the current hour of the system
            try {
                knotQueryDateDataFinish = DateUtils.getCurrentKnotQueryDateData();

                KnotQueryData knotQueryData = new KnotQueryData();
                knotQueryData.setFinishDate(knotQueryDateDataFinish).
                        setStartDate(knotQueryDateDataStart);

                mKnotApi.httpGetDataList(drinkFountainDevice.getUuid(), knotQueryData, mWaterLevelData, new Event<List<WaterLevelData>>() {
                    @Override
                    public void onEventFinish(List<WaterLevelData> list) {
                        // insert all WaterLevelData in the DB ;
                        try {
                            mDrinkFountainDB.insertWalterLevelDataList(list);
                        } catch (ParseException e) {
                            LogLib.printE(e);
                        }
                    }

                    @Override
                    public void onEventError(Exception e) {
                        LogKnotDrinkFountain.printE(e);
                    }
                });
            } catch (ParseException e) {
                LogKnotDrinkFountain.printE(e);
            }


        }


    }

}
