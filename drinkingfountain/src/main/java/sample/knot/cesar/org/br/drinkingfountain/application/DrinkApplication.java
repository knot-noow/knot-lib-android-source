package sample.knot.cesar.org.br.drinkingfountain.application;

import android.app.Application;
import android.content.Context;

public class DrinkApplication extends Application {

    private static Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    /**
     * Gets context.
     *
     * @return the context
     */
    public static Context getContext() {
        return mContext;
    }
}
