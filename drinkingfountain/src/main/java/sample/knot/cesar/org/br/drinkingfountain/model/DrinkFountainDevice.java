/*
 * Copyright (c) 2017, CESAR.
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license. See the LICENSE file for details.
 *
 *
 */

package sample.knot.cesar.org.br.drinkingfountain.model;

import android.provider.BaseColumns;

import br.org.cesar.knot.lib.model.AbstractThingDevice;

public class DrinkFountainDevice extends AbstractThingDevice {

    public static final long DANGEROUS = 6;
    public static final long ATTENTION = 10;

    private String description;
    private int positionX, positionY, floor;


    public DrinkFountainDevice(){
    }

    @Override
    public String toString() {
        String value = super.toString() + " Description = " + getDescription() +
                " PositionX = " + getPositionX() + " PositionY = " + getPositionY()+ " Floor = "+getFloor();
        return value;
    }

    /**
     * Get the description of drink fountain device
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of drink fountain device
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the position Y of drink fountain device
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * Get the position Y of drink fountain device
     * @param positionY the specific Y position
     */
    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    /**
     * Get the position X of drink fountain device
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Get the position X of drink fountain device
     * @param positionX the specific Y position
     */
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public static class Columns implements BaseColumns {

        /**
         * Table name for drink fountain
         *
         * */
        public static final String TABLE_DRINK_FOUNTAIN = "tb_drink_fountain";

        /**
         * uuid of the device
         *
         * */
        public static final String COLUMN_UUID = "uuid";

        /**
         * token of the device
         *
         * */
        public static final String COLUMN_TOKEN = "token";

        /**
         * position X of the device
         *
         * */
        public static final String COLUMN_POSITION_X = "position_x";

        /**
         * position Y of the device
         *
         * */
        public static final String COLUMN_POSITION_Y = "position_y";

        /**
         * description of the device
         *
         * */
        public static final String COLUMN_DESCRIPTION = "description";

        /**
         * Floor of drink walter is placed
         *
         * */
        public static final String COLUMN_FLOOR= "floor";

    }
}
