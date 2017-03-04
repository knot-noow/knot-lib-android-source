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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import br.org.cesar.knot.lib.util.LogLib;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterLevelData;


public class Util {

    private static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";


    // Sql comments begin with two consecutive "-" characters
    private static final String REG_COMMENT_EXPRESSION = "--";
    private static final String REG_EXPRESSION = ";";
    public static final String EMPTY_STRING = "";

    private static final float COMPLETE_LEVEL_PERCENT = 100;
    private static final float COMPLETE_LEVEL_LITERS = 20;

    private static final int WEEK = 7;

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

    /**
     * Convert Time in DB format date to in milliseconds
     * @param timeStamp Time in DB format
     * @return Time in milliseconds
     * @throws ParseException
     */
    public  static String convertDBFormatToMilliseconds(String timeStamp) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        Date date = sdf.parse(timeStamp);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return String.valueOf(calendar.getTimeInMillis());
    }

    /**
     * Convert Time in milliseconds to DB format date
     * @param timeStamp Time in milliseconds
     * @return Time with DB format
     */
    public  static String convertMillisecondsToValidFormat(Long timeStamp)  {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        return sdf.format(calendar.getTime());
    }

    /**
     * Get a number that is result of the join between two numbers
     * @param time Time in milliseconds
     * @return A number
     */
    public static int getDateByTime(String time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(time));

        int month = calendar.get(Calendar.MONTH);
        int day   = calendar.get(Calendar.DAY_OF_MONTH);

        return Integer.parseInt(month+""+day);
    }

    /**
     * Get a week in milliseconds
     * @param waterLevelData
     * @return The date interval;
     */
    public static long getDateLimit(WaterLevelData waterLevelData){
        long sevenDays = TimeUnit.DAYS.toMillis(WEEK);
        long currentDate = Long.parseLong(waterLevelData.getTimestamp());

        return currentDate - sevenDays;
    }

}
