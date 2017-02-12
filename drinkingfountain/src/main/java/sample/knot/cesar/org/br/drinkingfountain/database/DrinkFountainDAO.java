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

import android.content.ContentValues;
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

    public static final int INVALID_POSITION = -1;
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
    public long insertDrinkFountain(DrinkFountainDevice fountainDevice) {
        long rowsInserted;
        rowsInserted = sqliteDatabase.insert(DrinkFountainDevice.Columns.TABLE_DRINK_FOUNTAIN, null, buildContent(fountainDevice));

        return rowsInserted;

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
    public boolean updateDrinkFountain(DrinkFountainDevice drinkFountain) {
        return false;
    }

    /**
     * Gets all drink fountains.
     *
     * @return a List containing all drink fountains
     */
    public List<DrinkFountainDevice> getDrinkFountainList() {
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

    public float getCurrentLevelByDevice(DrinkFountainDevice device) {
        return 0f;
    }

    /**
     * Parses a drinkFountainDevice object into a ContentValues object.
     *
     * @param drinkFountainDevice a drinkFountainDevice to be parsed into ContentValues object
     * @return a ContentValues object
     */
    public ContentValues buildContent(DrinkFountainDevice drinkFountainDevice) {

        ContentValues contentValues = new ContentValues();

        if (drinkFountainDevice != null) {
            contentValues.put(DrinkFountainDevice.Columns.COLUMN_ID, drinkFountainDevice.getId());

            if (drinkFountainDevice.getUuid() != null) {
                contentValues.put(DrinkFountainDevice.Columns.COLUMN_UUID, drinkFountainDevice.getUuid());
            }

            if (drinkFountainDevice.getToken() != null) {
                contentValues.put(DrinkFountainDevice.Columns.COLUMN_TOKEN, drinkFountainDevice.getToken());
            }

            if (drinkFountainDevice.getPositionX() > INVALID_POSITION) {
                contentValues.put(DrinkFountainDevice.Columns.COLUMN_POSITION_X, drinkFountainDevice.getPositionX());
            }

            if (drinkFountainDevice.getPositionY() > INVALID_POSITION) {
                contentValues.put(DrinkFountainDevice.Columns.COLUMN_POSITION_Y, drinkFountainDevice.getPositionY());
            }

            if (drinkFountainDevice.getDescription() != null) {
                contentValues.put(DrinkFountainDevice.Columns.COLUMN_DESCRIPTION, drinkFountainDevice.getDescription());
            }
        }
        return contentValues;
    }
}
