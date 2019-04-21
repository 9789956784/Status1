package com.virmana.status_app_all;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import timber.log.Timber;

/**
 * Created by Tamim on 28/09/2017.
 */



public class App extends MultiDexApplication {
    private static App instance;

    @Override
    public void onCreate()
    {
        MultiDex.install(this);
        super.onCreate();
        instance = this;
        if (BuildConfig.DEBUG)
        {
            Timber.plant(new Timber.DebugTree());
        }
        Timber.i("Creating our Application");
    }

    public static App getInstance ()
    {
        return instance;
    }

    public static boolean hasNetwork ()
    {
        return instance.checkIfHasNetwork();
    }

    public boolean checkIfHasNetwork()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
