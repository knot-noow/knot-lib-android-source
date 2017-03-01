
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import br.org.cesar.knot.lib.model.KnotQueryDateData;
import br.org.cesar.knot.lib.util.DateUtils;
import sample.knot.cesar.org.br.drinkingfountain.database.FacadeDatabase;
import sample.knot.cesar.org.br.drinkingfountain.model.DrinkFountainDevice;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterLevelData;

/**
 * Created by usuario on 16/02/17.
 */

public class Stub {
    private static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private long mTimeInMillis;
    private final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

    Calendar mDate = Calendar.getInstance();
    private int mWaterLevel = 20;

    private FacadeDatabase mDrinkFountainDB;


    public Stub(){
        mTimeInMillis = mDate.getTimeInMillis();
        mDrinkFountainDB = FacadeDatabase.getInstance();

    }

    public void executeStub(int numbOfDevice,int numberOfData){

        for (int i = 0; i < numbOfDevice; i++) {
            generateDevice(numberOfData, i);
        }

    }

    private void generateDevice(int numberOfData, int number){
        DrinkFountainDevice drinkFountainDevice = new DrinkFountainDevice();
        drinkFountainDevice.setUuid(String.valueOf(getUuid()));
        drinkFountainDevice.setToken("fadfdfd");
        drinkFountainDevice.setPositionX(getX());
        drinkFountainDevice.setPositionY(getY());
        drinkFountainDevice.setDescription("Bebedouro "+number);

        if(number%2 == 0){
            drinkFountainDevice.setFloor(0);
        }else{
            drinkFountainDevice.setFloor(1);
        }

        mDrinkFountainDB.insertDrinkFountain(drinkFountainDevice);
        mTimeInMillis = mDate.getTimeInMillis();

        for (int i = 0; i < numberOfData; i++) {
            generateData(drinkFountainDevice.getUuid());
        }
    }

    private long getUuid(){
        Calendar date = Calendar.getInstance();
        return date.getTimeInMillis();
    }

    private void generateData(String uuid){

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

    public  KnotQueryDateData getCurrentKnotQueryDateData() throws ParseException {

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

    private int getY(){
        return ThreadLocalRandom.current().nextInt(100, 2300);
    }


    private int getX(){
        return ThreadLocalRandom.current().nextInt(100, 700);
    }

    private void calcWaterLevel(){
        int randomNum = ThreadLocalRandom.current().nextInt(1, 3);

        mWaterLevel-=randomNum;

        if(mWaterLevel <= 0){
            mWaterLevel = 20 + mWaterLevel;
        }

    }
}
