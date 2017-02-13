/*
 * Copyright (c) 2017, CESAR.
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license. See the LICENSE file for details.
 *
 *
 */

package sample.knot.cesar.org.br.drinkingfountain.util;

import android.support.annotation.NonNull;
import android.util.Log;

import br.org.cesar.knot.lib.BuildConfig;
import br.org.cesar.knot.lib.util.StringConstants;

/**
 * The type Log lib.
 */
public class LogKnotDrinkFountain {

    private static final String TAG = "knot_drink_fountain_lib";

    /**
     * Log print the given message
     *
     * @param message the message
     */
    public static void printD(@NonNull String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }

    /**
     * Log print error with message.
     *
     * @param message the message
     * @param error   the error
     */
    public static void printE(@NonNull String message, Throwable error) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, message, error);
        }
    }


    /**
     * Log print error with message and throwable.
     *
     * @param error the error
     */
    public static void printE(@NonNull Throwable error) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, StringConstants.EMPTY_STRING, error);
        }
    }
}
