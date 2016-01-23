package uk.co.steffandroid.parkbristol;

import android.app.Application;

public class ParkingApp extends Application {
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder().build();
    }

    public AppComponent component() {
        return component;
    }
}
