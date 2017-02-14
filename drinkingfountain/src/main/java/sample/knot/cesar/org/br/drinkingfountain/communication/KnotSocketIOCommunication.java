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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.org.cesar.knot.lib.connection.FacadeConnection;
import br.org.cesar.knot.lib.event.Event;
import br.org.cesar.knot.lib.exception.InvalidParametersException;
import br.org.cesar.knot.lib.exception.KnotException;
import br.org.cesar.knot.lib.exception.SocketNotConnected;
import br.org.cesar.knot.lib.model.KnotList;
import br.org.cesar.knot.lib.model.KnotQueryDateData;
import br.org.cesar.knot.lib.util.DateUtils;
import sample.knot.cesar.org.br.drinkingfountain.database.FacadeDatabase;
import sample.knot.cesar.org.br.drinkingfountain.model.DrinkFountainDevice;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterLevelData;
import sample.knot.cesar.org.br.drinkingfountain.util.LogKnotDrinkFountain;

/**
 * Created by usuario on 11/02/17.
 */

public class KnotSocketIOCommunication implements KnotCommunication {

    private static final String ENDPOINT = "http://172.17.120.174:3000";
    private static final String UUID_OWNER = "197b5876-7c5c-4c6e-8895-af17a5870000";
    private static final String TOKEN_OWNER = "f1788ed09e646d2cd1aef1a9582632d9e0034fff";

    private static final String OWNER = "owner";

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
    private static KnotSocketIOCommunication sInstance;


    /**
     * Private constructor
     */
    private KnotSocketIOCommunication(Context context) {
        //Initializing the KNOT API
        mKnotApi = FacadeConnection.getInstance();

        // Configuring the API
        try {
            mKnotApi.setupSocketIO(ENDPOINT, UUID_OWNER, TOKEN_OWNER);
        } catch (SocketNotConnected socketNotConnected) {
            socketNotConnected.printStackTrace();
        }

        //Initializing the DATABASE to save app information
        mDrinkFountainDB = FacadeDatabase.getInstance();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static KnotSocketIOCommunication getInstance(Context context) {
        synchronized (lock) {
            if (sInstance == null) {
                sInstance = new KnotSocketIOCommunication(context);
            }
            return sInstance;
        }
    }

    /**
     * Authenticating the socket communication
     * @param callbackResult Callback that will receive the result
     */
    public void authenticateSocketCommunication(Event<Boolean> callbackResult) {
        try {
            mKnotApi.socketIOAuthenticateDevice(callbackResult);
        } catch (SocketNotConnected socketNotConnected) {
            LogKnotDrinkFountain.printE(socketNotConnected);
        } catch (InvalidParametersException e) {
            LogKnotDrinkFountain.printE(e);
        }
    }

    @Override
    public void getAllDevices() {
        KnotList<DrinkFountainDevice> mDrinkFountainDeviceList = new KnotList<>(DrinkFountainDevice.class);
        JSONObject query = new JSONObject();
        try {
            query.put(OWNER, UUID_OWNER);
        } catch (JSONException e) {
            LogKnotDrinkFountain.printE(e);
        }

        try {
            mKnotApi.socketIOGetDeviceList(mDrinkFountainDeviceList, query, new Event<List<DrinkFountainDevice>>() {

                @Override
                public void onEventFinish(List<DrinkFountainDevice> deviceList) {
                    mDrinkFountainDB.insertDrinkFountainList(deviceList);
                }

                @Override
                public void onEventError(Exception e) {
                    LogKnotDrinkFountain.printE(e);
                }
            });
        } catch (KnotException e) {
            LogKnotDrinkFountain.printE(e);
        } catch (SocketNotConnected socketNotConnected) {
            LogKnotDrinkFountain.printE(socketNotConnected);
        } catch (InvalidParametersException e) {
            LogKnotDrinkFountain.printE(e);
        }
    }

    @Override
    public void getDataByDevice() {

        KnotList<WaterLevelData> waterLevelDataList = new KnotList<>(WaterLevelData.class);


        // get devices;
        List<DrinkFountainDevice> mDrinkFountainDeviceList = mDrinkFountainDB.getAllDrinkFountain();

        for (final DrinkFountainDevice drinkFountainDevice : mDrinkFountainDeviceList) {

            // get the last valid waterLevelData to build the query
            WaterLevelData waterLevelData = mDrinkFountainDB.getCurrentLevelByDeviceUUID(UUID_OWNER);

            KnotQueryDateData knotQueryDateDataStart = null;
            KnotQueryDateData knotQueryDateDataFinish = null;


            //Verify if the waterLevelData is valid
            if (waterLevelData != null) {
                String timeStamp = waterLevelData.getTimestamp();

                try {
                    //Building the start date
                    knotQueryDateDataStart = DateUtils.getKnotQueryDateData(timeStamp);
                } catch (ParseException e) {
                    LogKnotDrinkFountain.printE(e);
                }

            } else {

                try {
                    //get the current hour of the system
                    knotQueryDateDataFinish = DateUtils.getCurretnKnotQueryDateData();

                    mKnotApi.socketIOGetData(waterLevelDataList, UUID_OWNER, TOKEN_OWNER, knotQueryDateDataStart, knotQueryDateDataFinish, new Event<List<WaterLevelData>>() {
                        @Override
                        public void onEventFinish(List<WaterLevelData> list) {
                            mDrinkFountainDB.insertWalterLevelDataList(list);
                        }

                        @Override
                        public void onEventError(Exception e) {
                            LogKnotDrinkFountain.printE(e);
                        }
                    });
                } catch (ParseException e) {
                    LogKnotDrinkFountain.printE(e);
                } catch (InvalidParametersException e) {
                    LogKnotDrinkFountain.printE(e);
                } catch (SocketNotConnected socketNotConnected) {
                    LogKnotDrinkFountain.printE(socketNotConnected);
                }


            }

        }


    }

}
