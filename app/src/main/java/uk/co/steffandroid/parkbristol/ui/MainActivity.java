package uk.co.steffandroid.parkbristol.ui;

import android.os.Bundle;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import uk.co.steffandroid.parkbristol.ParkingApp;
import uk.co.steffandroid.parkbristol.data.UrbanThingsService;

public class MainActivity extends RxAppCompatActivity {
    @Inject
    UrbanThingsService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((ParkingApp) getApplication()).component().inject(this);
    }
}
