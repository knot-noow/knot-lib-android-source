/*
 *
 *  Copyright (c) 2017, CESAR.
 *  All rights reserved.
 *
 *  This software may be modified and distributed under the terms
 *  of the BSD license. See the LICENSE file for details.
 *
 */

package sample.knot.cesar.org.br.drinkingfountain.database;

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
        return mDrinkFountainDAO.getDeviceHistory(drinkFountainUUID);
    }

    /**
     * Get a current level value collected of specific device.
     * @param deviceUUID the uuid of specific device
     * @return the last values collected of the device
     */
    public float getCurrentLevelByDeviceUUID(String deviceUUID) {
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

}
