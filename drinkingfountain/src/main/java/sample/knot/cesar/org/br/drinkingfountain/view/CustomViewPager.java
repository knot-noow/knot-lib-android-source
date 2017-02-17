/*
 * Copyright (c) 2017, CESAR.
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license. See the LICENSE file for details.
 *
 *
 */

package sample.knot.cesar.org.br.drinkingfountain.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

    private boolean isSwipeEnable;

    public CustomViewPager(Context context) {
        super(context);
        this.isSwipeEnable = false;
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isSwipeEnable = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isSwipeEnable) {
            return super.onInterceptTouchEvent(ev);
        }
        // If we return false the swipe will be disabled
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isSwipeEnable) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    /**
     * Set if the swipe will be enabled
     *
     * @param isSwipeEnable true to enable the swipe on ViewPager
     */
    public void setIsSwipeEnabled(boolean isSwipeEnable) {
        this.isSwipeEnable = isSwipeEnable;
    }
}
