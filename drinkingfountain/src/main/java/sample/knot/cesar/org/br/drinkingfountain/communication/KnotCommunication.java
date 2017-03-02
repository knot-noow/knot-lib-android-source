/*
 * Copyright (c) 2017, CESAR.
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license. See the LICENSE file for details.
 *
 *
 */

package sample.knot.cesar.org.br.drinkingfountain.communication;

/**
 * Created by usuario on 11/02/17.
 */

public interface KnotCommunication {



    /**
     * Get all devices of the specif owner
     */
    public void getAllDevices();


    /**
     * Get data information about device behavior
     */
    public void getDataByDevice();




}
