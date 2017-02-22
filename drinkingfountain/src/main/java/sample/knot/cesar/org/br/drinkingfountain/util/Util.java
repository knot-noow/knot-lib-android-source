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

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import br.org.cesar.knot.lib.util.LogLib;

/**
 * Created by wallace on 04/02/17.
 */

public class Util {

    // Sql comments begin with two consecutive "-" characters
    private static final String REG_COMMENT_EXPRESSION = "--";
    private static final String REG_EXPRESSION = ";";

    private static final float COMPLETE_LEVEL_PERCENT = 100;
    private static final float COMPLETE_LEVEL_LITERS = 20;

    /**
     * Split in strings the content of sql files.
     *
     * @param context
     * @param fileNames
     * @return
     */
    public static String[] getStatementSql(Context context, final String fileNames) {
        final StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream;
        BufferedReader sqlFile = null;
        try {
            inputStream = context.getAssets().open(fileNames);
            final InputStreamReader reader = new InputStreamReader(inputStream,
                    Charset.defaultCharset());
            sqlFile = new BufferedReader(reader);
            String buffer;
            while ((buffer = sqlFile.readLine()) != null) {
                //Ignore comment in sql
                if (!buffer.startsWith(REG_COMMENT_EXPRESSION)) {
                    stringBuilder.append(buffer);
                }
            }

        } catch (final IOException e) {
            LogLib.printE("Error opening SQL file", e);
        } finally {
            if (sqlFile != null) {
                try {
                    sqlFile.close();
                } catch (final IOException e) {
                }
            }
        }

        return stringBuilder.toString().split(REG_EXPRESSION);
    }

    /**
     * USed to calculate the percent of the water level
     * @param waterLevel Level in Liters
     * @return Level in percent
     */
    public static float calculateWaterLevel(float waterLevel){
        return (COMPLETE_LEVEL_PERCENT*waterLevel)/COMPLETE_LEVEL_LITERS;
    }
}
