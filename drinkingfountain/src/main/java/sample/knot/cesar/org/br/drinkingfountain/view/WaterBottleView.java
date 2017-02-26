/*
 *
 *  Copyright (c) 2017, CESAR.
 *  All rights reserved.
 *
 *  This software may be modified and distributed under the terms
 *  of the BSD license. See the LICENSE file for details.
 *
 */

package sample.knot.cesar.org.br.drinkingfountain.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import sample.knot.cesar.org.br.drinkingfountain.R;
import sample.knot.cesar.org.br.drinkingfountain.model.DrinkFountainDevice;

public class WaterBottleView extends LinearLayout {

    private final static int CALCULATE_FACTOR = 100;
    private final static int COMPLETE_WATER_LEVEL = 20;

    private final static int ROTATION_POSITIVE_VALUE = 2;
    private final static int ROTATION_NEGATIVE_VALUE = -2;

    private final static int ANIMATION_DURATION = 1000;
    private final static int Y_ANIMATION_DURATION = 1;

    private float mWaterLevel = 0;
    private View view;
    private TextView mLevelInformation;

    private View mWaterView;

    public WaterBottleView(Context context) {
        super(context);
        initView();
    }

    public WaterBottleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WaterBottleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * Initialize the WaterbottleView
     */
    private void initView() {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.waterbottle, null);

        mWaterView = view.findViewById(R.id.water_id);
        mLevelInformation = (TextView) view.findViewById(R.id.level_information);
        addView(view);

        startAnimation();

    }

    /**
     * Set the level of the water
     *
     * @param level Level of the water in the water bottle
     */
    public void setWaterHeight(float level) {
        this.mWaterLevel = level;
        calcHighOfWater(mWaterLevel);

    }

    /**
     * Set the color of height the water Bottle
     *
     * @param percent
     */
    private void calcHighOfWater(float percent) {

        percent = (percent*CALCULATE_FACTOR)/COMPLETE_WATER_LEVEL;

        long height = getHeight();
        long position = (long)(height * percent) / CALCULATE_FACTOR;
        long positionY = getHeight() - position;

        mWaterView.animate().y(positionY).setDuration(Y_ANIMATION_DURATION);


        mLevelInformation.setText(String.valueOf(mWaterLevel)+" "+getContext().getResources().getString(R.string.liters));

    }

    /**
     * Water Continuous animation
     */
    private void startAnimation() {
        calcHighOfWater(mWaterLevel);
        mWaterView.animate().rotation(ROTATION_POSITIVE_VALUE).setDuration(ANIMATION_DURATION).withEndAction(new Runnable() {
            @Override
            public void run() {
                mWaterView.animate().rotation(ROTATION_NEGATIVE_VALUE).setDuration(ANIMATION_DURATION).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        calcHighOfWater(mWaterLevel + 1);
                        startAnimation();
                    }
                });
            }
        });
    }


}
