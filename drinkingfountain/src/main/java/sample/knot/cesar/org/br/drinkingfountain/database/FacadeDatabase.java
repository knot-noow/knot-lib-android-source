/*
 * Copyright (c) 2017, CESAR.
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license. See the LICENSE file for details.
 *
 *
 */

package sample.knot.cesar.org.br.drinkingfountain.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sample.knot.cesar.org.br.drinkingfountain.model.DrinkFountainDevice;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterLevelData;

public class FacadeDatabase {

    private static FacadeDatabase sInstance;
    private DrinkFountainDAO mDrinkFountainDAO;

    private FacadeDatabase() {
        mDrinkFountainDAO = new DrinkFountainDAO();
    }

    /**
     * Create a static instance of facade
     *
     * @return the facade instance
     */
    public synchronized static FacadeDatabase getInstance() {
        if (sInstance == null) {
            sInstance = new FacadeDatabase();
        }
        return sInstance;
    }

    /**
     * Insert a new drink fountain in database
     *
     * @param fountainDevice that will be inserted
     * @return true if inserted correctly false otherwise
     */
    public long insertDrinkFountain(DrinkFountainDevice fountainDevice) {
        return mDrinkFountainDAO.insertDrinkFountain(fountainDevice);
    }

    /**
     * Insert a list of drink fountain in database
     *
     * @param fountainDeviceList that will be inserted
     * @return true if inserted correctly false otherwise
     */
    public long insertDrinkFountainList(List<DrinkFountainDevice> fountainDeviceList) {
        return mDrinkFountainDAO.insertDrinkFountainList(fountainDeviceList);
    }

    /**
     * Delete a drink fountain from database.
     *
     * @param drinkFountainUUID the drink fountain object
     */
    public void deleteDrinkFountain(String drinkFountainUUID) {
        mDrinkFountainDAO.deleteDrinkFountain(drinkFountainUUID);
    }

    /**
     * Update a drinkFountain on database.
     *
     * @param drinkFountain a drinkFountain object containing the new values
     * @return true if success false otherwise
     */
    public long updateDrinkFountainDevice(DrinkFountainDevice drinkFountain) {
        return mDrinkFountainDAO.updateDrinkFountainDevice(drinkFountain);
    }

    /**
     * Gets all drink fountains.
     *
     * @return a List containing all drink fountains
     */
    public List<DrinkFountainDevice> getAllDrinkFountain() {
        return mDrinkFountainDAO.getAllDrinkFountain();
    }

    /**
     * Gets the device level history
     *
     * @param drinkFountainUUID the uuid of device list
     * @return the list with lasts device history.
     */
    public List<WaterLevelData> getDeviceHistory(String drinkFountainUUID) {
        Calendar calendar = Calendar.getInstance();
        List<WaterLevelData> mockdata = new ArrayList<>();

        //1
        WaterLevelData waterLevelData = new WaterLevelData();
        waterLevelData.setTimestamp(String.valueOf(calendar.getTime().getTime()));
        waterLevelData.setCurrentValue(10);
        mockdata.add(waterLevelData);
        // mock some data
        for (int count = 1; count < 20; count++) {
            waterLevelData = new WaterLevelData();
            if (count % 5 == 0) {
                calendar.add(Calendar.DATE, 1);
            }
            Log.d("lopes", "date::"+calendar.getTime().toString());
            waterLevelData.setTimestamp(String.valueOf(calendar.getTime().getTime()));
            waterLevelData.setCurrentValue((float) (count * 20));
            mockdata.add(waterLevelData);
        }

        waterLevelData.setTimestamp(String.valueOf(System.currentTimeMillis()));
        waterLevelData.setCurrentValue(30);
        mockdata.add(waterLevelData);

        return mockdata;
        //return mDrinkFountainDAO.getDeviceHistory(drinkFountainUUID);
    }

    /**
     * Get a current level value collected of specific device.
     *
     * @param deviceUUID the uuid of specific device
     * @return the object last collected of the specific device
     */
    public WaterLevelData getCurrentLevelByDeviceUUID(String deviceUUID) {
        return mDrinkFountainDAO.getCurrentLevelByDeviceUUID(deviceUUID);
    }

    /**
     * Insert a new walter level data in database
     *
     * @param waterLevelData that will be inserted
     * @return the row index affected
     */
    public long insertWalterLevelData(WaterLevelData waterLevelData) {
        return mDrinkFountainDAO.insertWalterLevelData(waterLevelData);
    }

    /**
     * Insert a list of walter level in database
     *
     * @param waterLevelDataList that will be inserted
     * @return the row index affected
     */
    public long insertWalterLevelDataList(List<WaterLevelData> waterLevelDataList) {
        return mDrinkFountainDAO.insertWalterLevelDataList(waterLevelDataList);
    }

}
