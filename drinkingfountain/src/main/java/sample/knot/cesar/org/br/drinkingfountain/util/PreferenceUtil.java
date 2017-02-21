/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package sample.knot.cesar.org.br.drinkingfountain.util;

import android.content.Context;
import android.content.SharedPreferences;

import sample.knot.cesar.org.br.drinkingfountain.application.DrinkApplication;

/**
 * The type Preference manager.
 */
public class PreferenceUtil {

    private static final String PREFERENCES_NAME = "user_info_preference";
    private static final String KEY_END_POINT = "END_POINT";
    private static final String KEY_UUID = "UUID";
    private static final String KEY_TOKEN = "TOKEN";
    private static Object lock = new Object();

    private static PreferenceUtil sInstance;

    private PreferenceUtil() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static PreferenceUtil getInstance() {
        synchronized (lock) {
            if (sInstance == null) {
                sInstance = new PreferenceUtil();
            }
            return sInstance;
        }
    }

    /**
     * Reset all sharedPreferences
     */
    public void reset() {
        getPref().edit().clear().apply();
    }

    private SharedPreferences getPref() {
        final Context context = DrinkApplication.getContext();
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Gets cloud end point.
     *
     * @return the cloud end point value
     */
    public String getEndPoint() {
        return getPref().getString(KEY_END_POINT, Util.EMPTY_STRING);
    }

    /**
     * Sets cloud end point.
     *
     * @param endPoint the cloud end point.
     */
    public void setEndPoint(String endPoint) {
        getPref().edit().putString(KEY_END_POINT, endPoint).apply();
    }

    /**
     * Gets owner UUID.
     *
     * @return the owner UUID
     */
    public String getUuid() {
        return getPref().getString(KEY_UUID, Util.EMPTY_STRING);
    }

    /**
     * Sets owner UUID.
     *
     * @param uuid the owner UUID.
     */
    public void setUuid(String uuid) {
        getPref().edit().putString(KEY_UUID, uuid).apply();
    }

    /**
     * Gets cloud end point.
     *
     * @return the cloud end point value
     */
    public String getTonken() {
        return getPref().getString(KEY_END_POINT, Util.EMPTY_STRING);
    }

    /**
     * Sets owner token.
     *
     * @param token the owner token.
     */
    public void setToken(String token) {
        getPref().edit().putString(KEY_TOKEN, token).apply();
    }


}
