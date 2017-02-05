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
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.FrameLayout;

import java.util.List;

import sample.knot.cesar.org.br.drinkingfountain.R;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterBottle;


public class KnotMap extends FrameLayout {


    private static final int ITEM_SIZE = 70;
    private static final float SCALE_MIN = 0.4f;
    private static final float SCALE_MAX = 1.5f;

    private static final int NEGATIVE = -1;
    private static final int ADJUSTMENT_LIMIT = 1;

    private static final int MAP_LIMIT = 4;

    private static final int HALF = 2;

    private static final int FIRST_POSITION = 0;
    private static final int SECOND_POSITION = 1;


    private ScaleGestureDetector mScaleDetector;
    private FrameLayout mMap;
    private float mScaleFactor = 1.f;

    private static final int SECOND_POINTER_DOWN = 261;
    private static final int SECOND_POINTER_UP = 262;

    private static final int INVALID_POINTER_ID = -1;

    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;

    private float mSecondPointerLastTouchX;
    private float mSecondPointerLastTouchY;


    private int mActivePointerId = INVALID_POINTER_ID;

    public KnotMap(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    public KnotMap(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public KnotMap(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     * This method is used to initialize the mapView
     *
     * @param context context of the application
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mMap = (FrameLayout) inflater.inflate(R.layout.knot_map, this, false);
        addView(mMap);

        setClipChildren(false);

        mScaleDetector = new ScaleGestureDetector(context, new KnotMap.ScaleListener());
    }

    /**
     * This method places the items on the map
     *
     * @param mapResource      the map id
     * @param mWaterBottleList the list of items
     * @param context          the context of the application
     */
    public void fillMapWithWaterBottle(int mapResource, @NonNull List<WaterBottle> mWaterBottleList, Context context) {


        mMap.setBackgroundResource(mapResource);

        for (int i = 0; i < mWaterBottleList.size(); i++) {
            mMap.addView(waterBottleComponent(context, mWaterBottleList.get(i)));
        }

    }

    /**
     * This method is used to adapt the WaterBottle model to the WaterBottleMap
     * that is a representative component of the drinking fountain at the map
     *
     * @param context
     * @param waterBottle
     * @return
     */
    private WaterBottleMap waterBottleComponent(Context context, WaterBottle waterBottle) {
        WaterBottleMap waterBottleMap = new WaterBottleMap(context);

        LayoutParams params = new LayoutParams(mMap.getLayoutParams());

        params.height = ITEM_SIZE;
        params.width = ITEM_SIZE;

        //Set x and y of the map
        params.topMargin = waterBottle.getMapPositionY() - ITEM_SIZE / HALF;
        params.leftMargin = waterBottle.getMapPositionX() - ITEM_SIZE / HALF;

        waterBottleMap.setLayoutParams(params);

        waterBottleMap.setWaterBottle(waterBottle);

        return waterBottleMap;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (!testMapLimit()) {
            return false;
        }

        mScaleDetector.onTouchEvent(ev);

        int actionId = ev.getAction();
        float x, y = 0;


        switch (actionId) {

            case MotionEvent.ACTION_POINTER_DOWN:
                mLastTouchX = ev.getRawX();
                mLastTouchY = ev.getRawY();
                break;

            case SECOND_POINTER_UP:
                mSecondPointerLastTouchX = ev.getX(FIRST_POSITION);
                mSecondPointerLastTouchY = ev.getY(FIRST_POSITION);
                break;

            case SECOND_POINTER_DOWN:
                mSecondPointerLastTouchX = ev.getX(SECOND_POSITION);
                mSecondPointerLastTouchY = ev.getY(SECOND_POSITION);
                break;

            case MotionEvent.ACTION_DOWN:
                x = ev.getX(FIRST_POSITION);
                y = ev.getY(FIRST_POSITION);

                mLastTouchX = x;
                mLastTouchY = y;

                mPosX = mMap.getX();
                mPosY = mMap.getY();

                break;


            case MotionEvent.ACTION_MOVE:

                x = ev.getX(FIRST_POSITION);
                y = ev.getY(FIRST_POSITION);


                if (!mScaleDetector.isInProgress()) {
                    mPosX = mPosX + (x - mLastTouchX);
                    mPosY = mPosY + (y - mLastTouchY);

                    mMap.setX(mPosX);
                    mMap.setY(mPosY);
                }

                mLastTouchX = x;
                mLastTouchY = y;
                break;

            case MotionEvent.ACTION_UP:
                mActivePointerId = INVALID_POINTER_ID;
                break;

            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER_ID;
                break;

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

                final int newPointerIndex = pointerIndex == FIRST_POSITION ? SECOND_POSITION : FIRST_POSITION;
                mLastTouchX = mSecondPointerLastTouchX;
                mLastTouchY = mSecondPointerLastTouchY;
                mActivePointerId = ev.getPointerId(newPointerIndex);
                break;
            }
        }
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float tempScale = mScaleFactor;

            tempScale *= detector.getScaleFactor();

            mScaleFactor = (mScaleFactor + tempScale) / HALF;

            mScaleFactor = Math.max(SCALE_MIN, Math.min(mScaleFactor, SCALE_MAX));

            mMap.setScaleX(mScaleFactor);
            mMap.setScaleY(mScaleFactor);

            return true;
        }
    }

    /**
     * Verify if the map is in the valid area of the screen
     *
     * @return if the maps is in the valid area
     */
    private boolean testMapLimit() {
        boolean result = false;


        int leftLimit = (int) (((mMap.getWidth()) - (mMap.getHeight() / MAP_LIMIT)) * mScaleFactor * NEGATIVE);
        int rightLimit = (int) ((getWidth() - (mMap.getHeight() / MAP_LIMIT)) * mScaleFactor);
        int topLimit = (int) (((mMap.getHeight()) - (mMap.getHeight() / MAP_LIMIT)) * mScaleFactor * NEGATIVE);
        int bottomLimit = (int) ((getHeight() - (mMap.getHeight() / MAP_LIMIT)) * mScaleFactor);


        if (mMap.getX() > leftLimit && mMap.getX() < rightLimit) {
            result = true;
        } else {
            if (mMap.getX() < 0) {
                mMap.setX(leftLimit + ADJUSTMENT_LIMIT);
            } else {
                mMap.setX(rightLimit - ADJUSTMENT_LIMIT);
            }
        }

        if (mMap.getY() > topLimit && mMap.getY() < bottomLimit) {
            result = true;
        } else {
            if (mMap.getY() < 0) {
                mMap.setY(topLimit + ADJUSTMENT_LIMIT);
            } else {
                mMap.setY(bottomLimit - ADJUSTMENT_LIMIT);
            }
        }

        return result;

    }

}