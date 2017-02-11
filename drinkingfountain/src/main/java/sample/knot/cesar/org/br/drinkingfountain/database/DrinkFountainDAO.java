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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import sample.knot.cesar.org.br.drinkingfountain.model.DrinkFountainDevice;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterLevelData;

/**
 * Created by wallace on 04/02/17.
 */

public class DrinkFountainDAO {

    private SQLiteDatabase sqliteDatabase;


    public DrinkFountainDAO(Context context) {
        DrinkFountainDatabase database = new DrinkFountainDatabase(context);
        sqliteDatabase = database.getWritableDatabase();
    }


    /**
     * Insert a new drink fountain in KNOT database
     *
     * @param fountainDevice that will be inserted
     * @return true if inserted correctly false otherwise
     */
    public boolean insertDrinkFountain(DrinkFountainDevice fountainDevice){
        return false;
    }

    /**
     * Delete a drink fountain from database.
     *
     * @param drinkFountain the drink fountain object
     */
    public void deleteDrinkFountain(String drinkFountain) {

    }

    /**
     * Update a drinkFountain on database.
     *
     * @param drinkFountain a drinkFountain object containing the new values
     * @return true if success false otherwise
     */
    public boolean updateDrinkFountain(DrinkFountainDevice drinkFountain){
        return false;
    }

    /**
     * Gets all drink fountains.
     *
     * @return a List containing all drink fountains
     */
    public List<DrinkFountainDevice> getDrinkFountain() {
        ArrayList<DrinkFountainDevice> drinkFountainList = new ArrayList<>();
        return drinkFountainList;
    }

    /**
     * Gets the device level history
     *
     * @param drinkFountainUUID the uuid of device list
     * @return the list with lasts device history.
     */
    public List<WaterLevelData> getDeviceHistory(String drinkFountainUUID) {
        ArrayList<WaterLevelData> waterLevelList = new ArrayList<>();
        return waterLevelList;
    }
}
