package com.linlif.linechart;

import android.app.Application;
import android.content.Context;

/**
 * Created by linlif on 2019-12-20
 */
public class App extends Application {

    private static Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {

        return mContext;
    }
}
