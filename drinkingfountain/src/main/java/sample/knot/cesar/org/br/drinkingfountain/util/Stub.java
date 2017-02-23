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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import br.org.cesar.knot.lib.model.KnotQueryDateData;
import br.org.cesar.knot.lib.util.DateUtils;
import sample.knot.cesar.org.br.drinkingfountain.database.FacadeDatabase;
import sample.knot.cesar.org.br.drinkingfountain.model.DrinkFountainDevice;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterLevelData;

public class Stub {
    private static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private long mTimeInMillis;
    private final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

    Calendar mDate = Calendar.getInstance();
    private int mWaterLevel = 20;

    private FacadeDatabase mDrinkFountainDB;


    public Stub() {
        mTimeInMillis = mDate.getTimeInMillis();
        mDrinkFountainDB = FacadeDatabase.getInstance();

    }

    public void executeStub(int numbOfDevice, int numberOfData) {

        for (int i = 0; i < numbOfDevice; i++) {
            generateDevice(numberOfData, i);
        }

    }

    private void generateDevice(int numberOfData, int number) {
        DrinkFountainDevice drinkFountainDevice = new DrinkFountainDevice();
        drinkFountainDevice.setUuid(String.valueOf(getUuid()));
        drinkFountainDevice.setToken("fadfdfd");
        drinkFountainDevice.setPositionX(getX());
        drinkFountainDevice.setPositionY(getY());
        drinkFountainDevice.setDescription("Bebedouro " + number);

        mDrinkFountainDB.insertDrinkFountain(drinkFountainDevice);
        mTimeInMillis = mDate.getTimeInMillis();

        for (int i = 0; i < numberOfData; i++) {
            generateData(drinkFountainDevice.getUuid());
        }
    }

    private long getUuid() {
        Calendar date = Calendar.getInstance();
        return date.getTimeInMillis();
    }

    private void generateData(String uuid) {

        try {
            KnotQueryDateData knotQueryDateData = getCurrentKnotQueryDateData();

            calcWaterLevel();

            WaterLevelData waterLevelData = new WaterLevelData();
            waterLevelData.setCurrentValue(mWaterLevel);
            waterLevelData.setWaterFountainUUID(uuid);
            waterLevelData.setTimestamp(DateUtils.getTimeStamp(knotQueryDateData));


            mDrinkFountainDB.insertWalterLevelData(waterLevelData);
//            Log.d("emidio", waterLevelData.toString());


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /***
     * Mock some data to be used on Chart integration
     *
     * @return a list of mocked WaterLevelData
     */
    public List<WaterLevelData> createMockChartData() {
        Calendar calendar = Calendar.getInstance();
        List<WaterLevelData> mockdata = new ArrayList<>();

        WaterLevelData waterLevelData = new WaterLevelData();
        waterLevelData.setTimestamp(String.valueOf(calendar.getTime().getTime()));
        waterLevelData.setCurrentValue(10);
        mockdata.add(waterLevelData);
        // mock some data
        for (int count = 1; count < 20; count++) {
            waterLevelData = new WaterLevelData();
            if (count % 5 == 0) {
                calendar.add(Calendar.DATE, 1);
            }
            waterLevelData.setTimestamp(String.valueOf(calendar.getTime().getTime()));
            waterLevelData.setCurrentValue((float) (count * 20));
            mockdata.add(waterLevelData);
        }

        waterLevelData.setTimestamp(String.valueOf(System.currentTimeMillis()));
        waterLevelData.setCurrentValue(30);
        mockdata.add(waterLevelData);

        return mockdata;
    }

    public KnotQueryDateData getCurrentKnotQueryDateData() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        mTimeInMillis = mTimeInMillis + (10 * ONE_MINUTE_IN_MILLIS);

        Date afterAddingTenMins = new Date(mTimeInMillis);

        String currentDateTime = sdf.format(afterAddingTenMins);


        Date date = sdf.parse(currentDateTime);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(afterAddingTenMins);

        KnotQueryDateData knotQueryDateData = new KnotQueryDateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));

        return knotQueryDateData;
    }

    private int getY() {
        return ThreadLocalRandom.current().nextInt(100, 2300);
    }


    private int getX() {
        return ThreadLocalRandom.current().nextInt(100, 700);
    }

    private void calcWaterLevel() {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 3);

        mWaterLevel -= randomNum;

        if (mWaterLevel <= 0) {
            mWaterLevel = 20 + mWaterLevel;
        }
    }
}
