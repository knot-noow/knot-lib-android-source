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

import android.support.annotation.NonNull;

/**
 * Created by usuario on 11/02/17.
 */

public interface KnotCommunication {


    /**
     * Configure the server and owner of the your device net
     * @param endPoint The url that represents tha path of the knot cloud
     * @param ownerUuid The uuid of the owner
     * @param ownerToken The toke of th owner
     */
    public void setUp(@NonNull String endPoint,@NonNull String ownerUuid,@NonNull String ownerToken);

    /**
     * Get all devices of the specif owner
     */
    public void getAllDevices();


    /**
     * Get data information about device behavior
     * @param deviceUuid device identification
     */
    public void getDataByDevice(String deviceUuid);




}
