/*
 *
 *  Copyright (c) 2017, CESAR.
 *  All rights reserved.
 *
 *  This software may be modified and distributed under the terms
 *  of the BSD license. See the LICENSE file for details.
 *
 */

package sample.knot.cesar.org.br.drinkingfountain.model;

import android.provider.BaseColumns;

import br.org.cesar.knot.lib.model.AbstractThingData;

/**
 * Created by wallace on 04/02/17.
 */

public class WaterLevelData extends AbstractThingData {

    private int _id;
    private String waterFountainUUID;
    private float currentValue;

    public WaterLevelData(int id, String waterFountainUUID, float currentValue) {
        _id = id;
        this.waterFountainUUID = waterFountainUUID;
        this.currentValue = currentValue;
    }

    public String getWaterFountainUUID() {
        return waterFountainUUID;
    }

    public void setWaterFountainUUID(String waterFountainUUID) {
        this.waterFountainUUID = waterFountainUUID;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public static class Columns implements BaseColumns {

        /**
         * Table name for water level data
         *
         * */
        public static final String TABLE_WATER_LEVEL_DATA = "water_level_data";

        /**
         * id of walter level data
         *
         * */
        public static final String COLUMN_ID = "_id";

        /**
         * uuid of the current device
         *
         * */
        public static final String COLUMN_DRINK_FOUNTAIN_ID = "water_fountain_uuid";

        /**
         * token of the device
         *
         * */
        public static final String COLUMN_TOKEN = "current_value";

        /**
         * Timestamp column of specific data collect
         *
         * */
        public static final String COLUMN_TIMESTAMP = "time_stamp";

    }
}

