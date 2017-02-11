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

import java.util.ArrayList;
import java.util.List;

import br.org.cesar.knot.lib.connection.FacadeConnection;
import br.org.cesar.knot.lib.event.Event;
import sample.knot.cesar.org.br.drinkingfountain.database.DrinkFountainDAO;
import sample.knot.cesar.org.br.drinkingfountain.model.DrinkFountainDevice;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterLevelData;

/**
 * Created by usuario on 11/02/17.
 */

public class KnotHttpCommunication implements KnotCommunication {

    private static final String ENDPOINT = "";
    private static final String UUID_OWNER = "";
    private static final String TOKEN_OWNER = "";


    private static final Object lock = new Object();
    private DrinkFountainDAO mDrinkFountainDAO;

    public FacadeConnection mKnotApi;

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
        mDrinkFountainDAO = new DrinkFountainDAO(context);
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
        List<DrinkFountainDevice> mDrinkFountainDeviceList = new ArrayList<>();

        mKnotApi.httpGetDeviceList(mDrinkFountainDeviceList, new Event<List<DrinkFountainDevice>>() {
            @Override
            public void onEventFinish(List<DrinkFountainDevice> deviceList) {

                if(deviceList!=null){
                    for (DrinkFountainDevice drinkFountainDevice:deviceList) {
                        mDrinkFountainDAO.insertDrinkFountain(drinkFountainDevice);
                    }
                }
            }

            @Override
            public void onEventError(Exception e) {

            }
        });
    }

    @Override
    public void getDataByDevice(String deviceUuid) {

        List<WaterLevelData> mWaterLevelDatas = new ArrayList<>();

        mKnotApi.httpGetDataList(deviceUuid, mWaterLevelDatas, new Event<List<WaterLevelData>>() {
            @Override
            public void onEventFinish(List<WaterLevelData> object) {

                //Save the list of data at the repository
                //Todo - Call db;
            }

            @Override
            public void onEventError(Exception e) {

            }
        });

    }

}
