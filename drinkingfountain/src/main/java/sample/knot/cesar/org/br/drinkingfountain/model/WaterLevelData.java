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

public class WaterLevelData extends AbstractThingData {

    private long _id;
    private String waterFountainUUID;
    private float currentValue;

    public WaterLevelData(){
    }

    public String getWaterFountainUUID() {
        return waterFountainUUID;
    }

    public void setWaterFountainUUID(String waterFountainUUID) {
        this.waterFountainUUID = waterFountainUUID;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static class Columns implements BaseColumns {

        /**
         * Table name for water level data
         *
         * */
        public static final String TABLE_WATER_LEVEL_DATA = "tb_water_level_data";

        /**
         * uuid of the current device
         *
         * */
        public static final String COLUMN_DRINK_FOUNTAIN_UUID = "drink_fountain_uuid";

        /**
         * token of the device
         *
         * */
        public static final String COLUMN_CURRENT_VALUE = "current_value";

        /**
         * Timestamp column of specific data collect
         *
         * */
        public static final String COLUMN_TIMESTAMP = "time_stamp";

    }
}

