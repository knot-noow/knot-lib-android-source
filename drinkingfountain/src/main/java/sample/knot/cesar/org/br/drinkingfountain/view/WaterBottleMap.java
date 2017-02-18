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

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import sample.knot.cesar.org.br.drinkingfountain.R;
import sample.knot.cesar.org.br.drinkingfountain.database.FacadeDatabase;
import sample.knot.cesar.org.br.drinkingfountain.model.DrinkFountainDevice;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterLevelData;


public class WaterBottleMap extends FrameLayout {

    private static final String SCALE_X = "scaleX";
    private static final String SCALE_Y = "scaleY";
    private static final String ALPHA = "alpha";


    private static final float SCALE_VALUE = 3f;
    private static final float ALPHA_VALUE = 0f;

    private static final int STROKE_SIZE = 8;

    private static final int DURATION = 2000;

    private View view;
    private FrameLayout mFrameLayout;

    public WaterBottleMap(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public WaterBottleMap(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public WaterBottleMap(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * Initialize the view
     *
     * @param context the application context
     */
    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mFrameLayout = (FrameLayout) inflater.inflate(R.layout.waterbottle_map, null);

        view = mFrameLayout.findViewById(R.id.circle);

        addView(mFrameLayout);

        setAnimation();

        setClipChildren(false);
    }

    /**
     * This method Animates the circle that is in the drinking fountain
     */
    private void setAnimation() {

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, SCALE_X, SCALE_VALUE);
        scaleX.setRepeatCount(ObjectAnimator.INFINITE);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, SCALE_Y, SCALE_VALUE);
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);


        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, ALPHA, ALPHA_VALUE);
        alpha.setRepeatCount(ObjectAnimator.INFINITE);

        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(scaleX, scaleY, alpha);
        animation.setDuration(DURATION);
        animation.start();

    }

    /**
     * This method is used to change the color of the WaterbottleView
     *
     * @param water The object
     */
    public void setWaterBottle(@NonNull DrinkFountainDevice water) {
        float waterLevel = 0;

        WaterbottleView bottle = (WaterbottleView) mFrameLayout.findViewById(R.id.water_bottle_map);
        WaterLevelData waterLevelData = FacadeDatabase.getInstance().getCurrentLevelByDeviceUUID(water.getUuid());

        if(waterLevelData!=null){
            waterLevel = waterLevelData.getCurrentValue();
        }

        bottle.setWaterHeight(waterLevel);

        GradientDrawable backgroundGradient = (GradientDrawable) view.getBackground();


        if (waterLevel < water.DANGEROUS) {
            backgroundGradient.setStroke(STROKE_SIZE, Color.RED);
        } else if (waterLevelData.getCurrentValue() < water.ATTENTION) {
            backgroundGradient.setStroke(STROKE_SIZE, Color.YELLOW);
        } else {
            backgroundGradient.setStroke(STROKE_SIZE, Color.BLUE);
        }

    }

}
