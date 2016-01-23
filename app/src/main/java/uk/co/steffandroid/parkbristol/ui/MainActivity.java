package uk.co.steffandroid.parkbristol.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.steffandroid.parkbristol.ParkingApp;
import uk.co.steffandroid.parkbristol.R;
import uk.co.steffandroid.parkbristol.data.DataManager;

public class MainActivity extends RxAppCompatActivity {
    @Inject
    DataManager dataManager;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.car_park_list)
    RecyclerView carParkList;
    @Bind(R.id.error)
    View error;
    @Bind(R.id.progress)
    View progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ((ParkingApp) getApplication()).component().inject(this);

        setSupportActionBar(toolbar);

        CarParkAdapter adapter = new CarParkAdapter();
        carParkList.setAdapter(adapter);
        carParkList.setLayoutManager(new LinearLayoutManager(this));

        showProgress(true);
        dataManager.getCarParks()
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
        progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void showError() {
        carParkList.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }
}
