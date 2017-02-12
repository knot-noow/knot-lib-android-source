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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import sample.knot.cesar.org.br.drinkingfountain.model.DrinkFountainDevice;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterLevelData;
import sample.knot.cesar.org.br.drinkingfountain.application.DrinkApplication;

class DrinkFountainDAO {

    public static final int INVALID_POSITION = -1;
    public static final int INVALID_LEVEL = -1;
    private SQLiteDatabase sqliteDatabase;

    //DB constants
    private static final String EQUALS = " = ? ";
    private static final String DESC_CLAUSE = " DESC ";
    private static final String WHERE_DRINK_FOUNTAIN_UUID = DrinkFountainDevice.Columns.COLUMN_UUID + EQUALS;
    private static final String WHERE_WALTER_DRINK_UUID = WaterLevelData.Columns.COLUMN_DRINK_FOUNTAIN_UUID + EQUALS;

    public DrinkFountainDAO() {
        DrinkFountainDatabase database = new DrinkFountainDatabase(DrinkApplication.getContext());
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
        DrinkFountainDevice tempDevice = getDrinkFountainDeviceByUUID(fountainDevice.getUuid());
        if (tempDevice == null) {
            rowsInserted = sqliteDatabase.insert(DrinkFountainDevice.Columns.TABLE_DRINK_FOUNTAIN, null, buildContent(fountainDevice));
        } else {
            rowsInserted = updateDrinkFountainDevice(fountainDevice);
        }
        return rowsInserted;
    }

    /**
     * Delete a drink fountain from database.
     *
     * @param drinkFountainUUID the drink fountain object
     */
    public void deleteDrinkFountain(String drinkFountainUUID) {
        sqliteDatabase.beginTransaction();
        String[] args = new String[]{drinkFountainUUID};
        sqliteDatabase.delete(DrinkFountainDevice.Columns.TABLE_DRINK_FOUNTAIN,
                WHERE_DRINK_FOUNTAIN_UUID, args);
        sqliteDatabase.setTransactionSuccessful();
        sqliteDatabase.endTransaction();
    }

    /**
     * get a drink fountain device by UUID
     *
     * @param deviceUUID uuid of specific device
     * @return
     */
    private DrinkFountainDevice getDrinkFountainDeviceByUUID(String deviceUUID) {
        String[] args = new String[]{deviceUUID};
        DrinkFountainDevice drinkFountainDevice = null;
        Cursor cursor = sqliteDatabase.query(DrinkFountainDevice.Columns.TABLE_DRINK_FOUNTAIN,
                null, WHERE_DRINK_FOUNTAIN_UUID, args, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            drinkFountainDevice = buildDrinkFountainByCursor(cursor);
        }
        cursor.close();

        return drinkFountainDevice;
    }

    /**
     * Update a drinkFountain on database.
     *
     * @param drinkFountain a drinkFountain object containing the new values
     * @return true if success false otherwise
     */
    public long updateDrinkFountainDevice(DrinkFountainDevice drinkFountain) {
        String[] args = new String[]{drinkFountain.getUuid()};

        int rowsAffected = sqliteDatabase.update(DrinkFountainDevice.Columns.TABLE_DRINK_FOUNTAIN,
                buildContent(drinkFountain), WHERE_DRINK_FOUNTAIN_UUID, args);

        return rowsAffected;
    }

    /**
     * Gets all drink fountains.
     *
     * @return a List containing all drink fountains
     */
    public List<DrinkFountainDevice> getAllDrinkFountain() {
        ArrayList<DrinkFountainDevice> drinkFountainList = new ArrayList<>();

        Cursor cursor = sqliteDatabase.query(DrinkFountainDevice.Columns.TABLE_DRINK_FOUNTAIN,
                null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    DrinkFountainDevice currentDevice = buildDrinkFountainByCursor(cursor);

                    drinkFountainList.add(currentDevice);
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }
            cursor.close();
        }
        return drinkFountainList;
    }

    /**
     * build a content value object from a drink fountain object.
     *
     * @param drinkFountainDevice a drinkFountainDevice to be parsed into ContentValues object
     * @return a ContentValues object
     */
    private ContentValues buildContent(DrinkFountainDevice drinkFountainDevice) {

        ContentValues contentValues = new ContentValues();

        if (drinkFountainDevice != null) {
            contentValues.put(DrinkFountainDevice.Columns._ID, drinkFountainDevice.getId());

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

    /**
     * build a drinkFountainDevice object from a cursor object.
     *
     * @param drinkCursor a cursor to be parsed into drink fountain object
     * @return a ContentValues object
     */
    private DrinkFountainDevice buildDrinkFountainByCursor(Cursor drinkCursor) {
        DrinkFountainDevice drinkFountainDevice = null;

        if (drinkCursor != null) {

            drinkFountainDevice = new DrinkFountainDevice();

            int indexId = drinkCursor.getColumnIndex(DrinkFountainDevice.Columns._ID);
            int indexUUID = drinkCursor.getColumnIndex(DrinkFountainDevice.Columns.COLUMN_UUID);
            int indexToken = drinkCursor.getColumnIndex(DrinkFountainDevice.Columns.COLUMN_TOKEN);
            int indexPositionX = drinkCursor.getColumnIndex(DrinkFountainDevice.Columns.COLUMN_POSITION_X);
            int indexPositionY = drinkCursor.getColumnIndex(DrinkFountainDevice.Columns.COLUMN_POSITION_Y);
            int indexDescription = drinkCursor.getColumnIndex(DrinkFountainDevice.Columns.COLUMN_DESCRIPTION);

            drinkFountainDevice.setId(drinkCursor.getLong(indexId));
            drinkFountainDevice.setUuid(drinkCursor.getString(indexUUID));
            drinkFountainDevice.setToken(drinkCursor.getString(indexToken));
            drinkFountainDevice.setPositionX(drinkCursor.getInt(indexPositionX));
            drinkFountainDevice.setPositionY(drinkCursor.getInt(indexPositionY));
            drinkFountainDevice.setDescription(drinkCursor.getString(indexDescription));

        }
        return drinkFountainDevice;
    }

    // ----------------------------------- WALTER LEVEL METHODS -----------------------------------

    /**
     * Insert a new walter level in database
     *
     * @param waterLevelData that will be inserted
     * @return the row index affected
     */
    public long insertWalterLevelData(WaterLevelData waterLevelData) {
        return sqliteDatabase.insert(WaterLevelData.Columns.TABLE_WATER_LEVEL_DATA, null,
                buildContentValuesByWalterLevel(waterLevelData));
    }

    /**
     * Insert a list of walter level in database
     *
     * @param waterLevelDataList that will be inserted
     * @return the row index affected
     */
    public long insertWalterLevelDataList(List<WaterLevelData> waterLevelDataList) {
        int rowsInserted = 0;

        sqliteDatabase.beginTransaction();
        for (WaterLevelData currentWalterLevelData : waterLevelDataList) {
            if (currentWalterLevelData != null) {
                long rowId = insertWalterLevelData(currentWalterLevelData);

                if (rowId != -1) {
                    rowsInserted++;
                }
            }
        }

        if (rowsInserted > 0 ) {
            sqliteDatabase.setTransactionSuccessful();
        }
        sqliteDatabase.endTransaction();

        return rowsInserted;
    }


    /**
     * Gets the device level history
     *
     * @param drinkFountainUUID the uuid of device list
     * @return the list with lasts device history.
     */
    public List<WaterLevelData> getDeviceHistory(String drinkFountainUUID) {
        ArrayList<WaterLevelData> waterLevelList = new ArrayList<>();

        String[] args = new String[]{drinkFountainUUID};

        Cursor cursor = sqliteDatabase.query(WaterLevelData.Columns.TABLE_WATER_LEVEL_DATA,
                null, WHERE_WALTER_DRINK_UUID, args, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    WaterLevelData currentWalterLevel = buildWalterLevelDataByCursor(cursor);

                    waterLevelList.add(currentWalterLevel);
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }
            cursor.close();
        }

        return waterLevelList;
    }

    /**
     * get a current level value collected of specific device.
     *
     * @param deviceUUID the uuid of specific device
     * @return the last values collected of the device
     */
    public float getCurrentLevelByDeviceUUID(String deviceUUID) {
        String[] args = new String[]{deviceUUID};
        WaterLevelData walterLevelData = null;

        Cursor cursor = sqliteDatabase.query(WaterLevelData.Columns.TABLE_WATER_LEVEL_DATA,
                null, WHERE_WALTER_DRINK_UUID, args, null, null, WaterLevelData.Columns.COLUMN_TIMESTAMP + DESC_CLAUSE);
        if (cursor != null && cursor.moveToFirst()) {

            walterLevelData = buildWalterLevelDataByCursor(cursor);
        }
        cursor.close();

        return walterLevelData != null ? walterLevelData.getCurrentValue() : INVALID_LEVEL;
    }

    /**
     * build a content value object from a walter level object.
     *
     * @param waterLevelData a walter level to be parsed into ContentValues object
     * @return a ContentValues object
     */
    private ContentValues buildContentValuesByWalterLevel(WaterLevelData waterLevelData) {

        ContentValues contentValues = new ContentValues();

        if (waterLevelData != null) {
            contentValues.put(DrinkFountainDevice.Columns._ID, waterLevelData.getId());

            if (waterLevelData.getWaterFountainUUID() != null) {
                contentValues.put(WaterLevelData.Columns.COLUMN_DRINK_FOUNTAIN_UUID, waterLevelData.getWaterFountainUUID());
            }

            if (waterLevelData.getCurrentValue() != INVALID_LEVEL) {
                contentValues.put(WaterLevelData.Columns.COLUMN_CURRENT_VALUE, waterLevelData.getCurrentValue());
            }

            if (waterLevelData.getTimestamp() != null) {
                contentValues.put(WaterLevelData.Columns.COLUMN_TIMESTAMP, waterLevelData.getTimestamp());
            }
        }
        return contentValues;
    }

    /**
     * Parse to a walter level object from cursor.
     *
     * @param walterLevelCursor a cursor to be parsed into walter level object
     * @return a ContentValues object
     */
    private WaterLevelData buildWalterLevelDataByCursor(Cursor walterLevelCursor) {
        WaterLevelData waterLevelCollected = null;

        if (walterLevelCursor != null) {

            waterLevelCollected = new WaterLevelData();

            int indexId = walterLevelCursor.getColumnIndex(WaterLevelData.Columns._ID);
            int indexDrinkFountainUUID = walterLevelCursor.getColumnIndex(
                    WaterLevelData.Columns.COLUMN_DRINK_FOUNTAIN_UUID);
            int indexCurrentValue = walterLevelCursor.getColumnIndex(WaterLevelData.Columns.COLUMN_CURRENT_VALUE);
            int indexTimestamp = walterLevelCursor.getColumnIndex(WaterLevelData.Columns.COLUMN_TIMESTAMP);

            waterLevelCollected.setId(walterLevelCursor.getLong(indexId));
            waterLevelCollected.setWaterFountainUUID(walterLevelCursor.getString(indexDrinkFountainUUID));
            waterLevelCollected.setCurrentValue(walterLevelCursor.getFloat(indexCurrentValue));
            waterLevelCollected.setTimestamp(walterLevelCursor.getString(indexTimestamp));

        }
        return waterLevelCollected;
    }
}
