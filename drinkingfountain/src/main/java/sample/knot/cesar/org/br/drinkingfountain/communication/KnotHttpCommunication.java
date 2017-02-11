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

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.org.cesar.knot.lib.connection.FacadeConnection;
import br.org.cesar.knot.lib.event.Event;
import sample.knot.cesar.org.br.drinkingfountain.model.DrinkFountainDevice;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterLevelData;

/**
 * Created by usuario on 11/02/17.
 */

public class KnotHttpCommunication implements KnotCommunication {

    private static final Object lock = new Object();

    public FacadeConnection mKnotApi;

    private static KnotHttpCommunication sInstance;


    /**
     * Private constructor
     */
    private KnotHttpCommunication(){
        mKnotApi = FacadeConnection.getInstance();
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
    public void setUp(@NonNull String endPoint, @NonNull String ownerUuid, @NonNull String ownerToken) {
            
    }

    @Override
    public void getAllDevices() {
        List<DrinkFountainDevice> mDrinkFountainDeviceList = new ArrayList<>();

        mKnotApi.httpGetDeviceList(mDrinkFountainDeviceList, new Event<List<DrinkFountainDevice>>() {
            @Override
            public void onEventFinish(List<DrinkFountainDevice> object) {

                //Save the list of device at the repository
                //Todo - Call db;
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
