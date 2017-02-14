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

import android.content.Context;

import java.util.List;

import br.org.cesar.knot.lib.connection.FacadeConnection;
import br.org.cesar.knot.lib.event.Event;
import br.org.cesar.knot.lib.model.KnotList;
import sample.knot.cesar.org.br.drinkingfountain.database.FacadeDatabase;
import sample.knot.cesar.org.br.drinkingfountain.model.DrinkFountainDevice;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterLevelData;
import sample.knot.cesar.org.br.drinkingfountain.util.LogKnotDrinkFountain;

/**
 * Created by usuario on 11/02/17.
 */

public class KnotHttpCommunication implements KnotCommunication {

    private static final String ENDPOINT = "http://172.26.67.70:3000";
    private static final String UUID_OWNER = "197b5876-7c5c-4c6e-8895-af17a5870000";
    private static final String TOKEN_OWNER = "f1788ed09e646d2cd1aef1a9582632d9e0034fff";


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
    private KnotHttpCommunication(Context context) {
        //Initializing the KNOT API
        mKnotApi = FacadeConnection.getInstance();

        // Configuring the API
        mKnotApi.setupHttp(ENDPOINT, UUID_OWNER, TOKEN_OWNER);

        //Initializing the DATABASE to save app informations
        mDrinkFountainDB = FacadeDatabase.getInstance();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static KnotHttpCommunication getInstance(Context context) {
        synchronized (lock) {
            if (sInstance == null) {
                sInstance = new KnotHttpCommunication(context);
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

            mKnotApi.httpGetDataList(UUID_OWNER, mWaterLevelData, new Event<List<WaterLevelData>>() {
                @Override
                public void onEventFinish(List<WaterLevelData> list) {
                    // insert all WaterLevelData in the DB ;
                    mDrinkFountainDB.insertWalterLevelDataList(list);
                }

                @Override
                public void onEventError(Exception e) {
                    LogKnotDrinkFountain.printE(e);
                }
            });

        }


    }

}
