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


public class GraphicDate {

    private int mOrder;

    private String mDateDescription;

    private int mValue;

    public GraphicDate() {
    }

    public GraphicDate(int order, String dateDescription, int value) {
        this.mOrder = order;
        this.mDateDescription = dateDescription;
        this.mValue = value;
    }

    public int getOrder() {
        return mOrder;
    }

    public void setOrder(int order) {
        this.mOrder = order;
    }

    public String getDateDescription() {
        return mDateDescription;
    }

    public void setDateDescription(String dateDescription) {
        this.mDateDescription = dateDescription;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        this.mValue = value;
    }
}
