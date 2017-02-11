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

import br.org.cesar.knot.lib.model.AbstractThingDevice;

/**
 * Created by wallace on 04/02/17.
 */

public class DrinkFountainDevice extends AbstractThingDevice {

    private String description;
    private int positionX, positionY;


    public DrinkFountainDevice(){
    }

    @Override
    public String toString() {
        String value = super.toString() + '\'' + " Description = " + getDescription() ;
        return value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }
}
