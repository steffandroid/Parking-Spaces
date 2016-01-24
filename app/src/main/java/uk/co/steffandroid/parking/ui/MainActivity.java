package uk.co.steffandroid.parking.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.steffandroid.parking.ParkingApp;
import uk.co.steffandroid.parking.R;
import uk.co.steffandroid.parking.data.DataManager;

public class MainActivity extends RxAppCompatActivity {
    @Inject
    DataManager dataManager;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.car_park_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.car_park_list)
    RecyclerView carParkList;
    @Bind(R.id.error)
    View error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ((ParkingApp) getApplication()).component().inject(this);

        setSupportActionBar(toolbar);

        CarParkAdapter adapter = new CarParkAdapter(this, carPark -> {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + carPark.name());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });
        carParkList.setAdapter(adapter);
        carParkList.setLayoutManager(new LinearLayoutManager(this));

        refreshLayout.setOnRefreshListener(dataManager::refresh);

        showProgress(true);
        RxPermissions.getInstance(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .filter(granted -> granted)
                .flatMap(granted -> new ReactiveLocationProvider(this).getLastKnownLocation())
                .firstOrDefault(null)
                .flatMap(dataManager::getCarParks)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(carParks -> {
                    showProgress(false);
                    adapter.addAll(carParks);
                }, throwable -> {
                    showError();
                });
    }

    private void showProgress(boolean isLoading) {
        carParkList.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        error.setVisibility(View.GONE);
        refreshLayout.post(() -> refreshLayout.setRefreshing(isLoading));
    }

    private void showError() {
        carParkList.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        refreshLayout.setRefreshing(false);
    }
}
