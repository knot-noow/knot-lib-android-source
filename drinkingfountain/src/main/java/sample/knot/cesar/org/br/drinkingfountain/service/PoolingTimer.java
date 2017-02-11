/*
 * Copyright (c) 2017, CESAR.
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license. See the LICENSE file for details.
 *
 *
 */

package sample.knot.cesar.org.br.drinkingfountain.service;

import android.os.CountDownTimer;

public final class PoolingTimer {

    private PoolingListener listener;

    private static final long ONE_HOUR = 60000 * 60;
    private static final long FIVE_MINUTES = 60000 * 5;

    private CountDownTimer countDownTimer;

    private long periodPooling;

    public PoolingTimer(long intervalTick, PoolingListener listener) {
        this.listener = listener;
        this.periodPooling = intervalTick;
        countDownTimer = new Task(ONE_HOUR, intervalTick);
    }

    public PoolingTimer(PoolingListener listener) {
        this(FIVE_MINUTES, listener);
    }

    /**
     * Init the pooling
     */
    public void startPooling() {
        if (countDownTimer != null) {
            countDownTimer.cancel();

            // create a new CounterDown and start it
            countDownTimer = getTime();
            countDownTimer.start();
        }
    }

    /**
     * Stop the pooling
     */
    public void stopPooling() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private Task getTime() {
        return new Task(ONE_HOUR, periodPooling);
    }


    private class Task extends CountDownTimer {

        public Task(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            if (listener != null) {
                listener.onPoolingFinished();
            }
        }

        @Override
        public void onFinish() {
            // infinite loop
            start();
        }
    }

    /**
     * Listener used to retrieve the result of pooling
     */
    public interface PoolingListener {
        public void onPoolingFinished();
    }
}
