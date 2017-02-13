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
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.org.cesar.knot.lib.connection.FacadeConnection;
import br.org.cesar.knot.lib.event.Event;
import br.org.cesar.knot.lib.exception.InvalidParametersException;
import br.org.cesar.knot.lib.exception.KnotException;
import br.org.cesar.knot.lib.exception.SocketNotConnected;
import br.org.cesar.knot.lib.model.ThingList;
import sample.knot.cesar.org.br.drinkingfountain.database.FacadeDatabase;
import sample.knot.cesar.org.br.drinkingfountain.model.DrinkFountainDevice;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterLevelData;
import sample.knot.cesar.org.br.drinkingfountain.util.LogKnotDrinkFountain;

/**
 * Created by usuario on 11/02/17.
 */

public class KnotSocketIOCommunication implements KnotCommunication {

    private static final String ENDPOINT = "http://192.168.0.109:3000";
    private static final String UUID_OWNER = "542c3564-360f-4173-8b7e-e165670d0000";
    private static final String TOKEN_OWNER = "1042db5745b7800404a813407022e886322f89d8";

    private static final String OWNER = "owner";


    private static final Object lock = new Object();

    private FacadeDatabase mDrinkFountainDB;

    public FacadeConnection mKnotApi;

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

        //Initializing the DATABASE to save app informations
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

    public void authenticateSocketCommunication() {
        try {
            mKnotApi.socketIOAuthenticateDevice(new Event<Boolean>() {
                @Override
                public void onEventFinish(Boolean object) {
                    getAllDevices();
                }

                @Override
                public void onEventError(Exception e) {
                    LogKnotDrinkFountain.printE(e);
                }
            });
        } catch (SocketNotConnected socketNotConnected) {
            LogKnotDrinkFountain.printE(socketNotConnected);
        } catch (InvalidParametersException e) {
            LogKnotDrinkFountain.printE(e);
        }
    }


    @Override
    public void getAllDevices() {
        List<DrinkFountainDevice> mDrinkFountainDeviceList = new ArrayList<>();
        JSONObject js = new JSONObject();
        try {
            js.put(OWNER, UUID_OWNER);
        } catch (JSONException e) {
            LogKnotDrinkFountain.printE(e);
        }


        try {
            mKnotApi.socketIOGetDeviceList(mDrinkFountainDeviceList, js, new Event<List<DrinkFountainDevice>>() {

                @Override
                public void onEventFinish(List<DrinkFountainDevice> object) {
                    //TODO - Call insertAllDevices;
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
    public void getDataByDevice(String deviceUuid) {

        List<WaterLevelData> mWaterLevelDatas = new ArrayList<>();


        List<DrinkFountainDevice> mDrinkFountainDeviceList = mDrinkFountainDB.getAllDrinkFountain();

        for (final DrinkFountainDevice drinkFountainDevice : mDrinkFountainDeviceList) {

            mDrinkFountainDB.getCurrentLevelByDeviceUUID(drinkFountainDevice.getUuid());

        }


    }

}
