/*
 *
 *  Copyright (c) 2017, CESAR.
 *  All rights reserved.
 *
 *  This software may be modified and distributed under the terms
 *  of the BSD license. See the LICENSE file for details.
 *
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
     * Gets owner token.
     *
     * @return the owner token
     */
    public String getTonken() {
        return getPref().getString(KEY_TOKEN, Util.EMPTY_STRING);
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
