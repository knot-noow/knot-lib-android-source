package sample.knot.cesar.org.br.drinkingfountain.model;

/*
 *
 *  Copyright (c) 2017, CESAR.
 *  All rights reserved.
 *
 *  This software may be modified and distributed under the terms
 *  of the BSD license. See the LICENSE file for details.
 *
 */

public class WaterBottle {

    public static final long DANGEROUS = 6;
    public static final long ATTENTION = 10;


    public long mlevelOfWater;
    private int mapPositionX;
    private int mapPositionY;

    public WaterBottle(long levelOfWater){
        this.mlevelOfWater = levelOfWater;
    }


    public int getMapPositionX() {
        return mapPositionX;
    }

    public void setMapPositionX(int mapPositionX) {
        this.mapPositionX = mapPositionX;
    }

    public int getMapPositionY() {
        return mapPositionY;
    }

    public void setMapPositionY(int mapPositionY) {
        this.mapPositionY = mapPositionY;
    }
}
