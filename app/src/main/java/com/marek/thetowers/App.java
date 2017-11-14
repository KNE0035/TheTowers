package com.marek.thetowers;

import android.app.Application;

import com.marek.thetowers.model.GameUtil;

/**
 * Created by Marek on 12/11/2017.
 */

public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        GameUtil.initialize(this);
    }
}
