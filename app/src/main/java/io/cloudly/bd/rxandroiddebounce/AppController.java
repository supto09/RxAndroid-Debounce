package io.cloudly.bd.rxandroiddebounce;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by supto on 7/20/17.
 */

public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
