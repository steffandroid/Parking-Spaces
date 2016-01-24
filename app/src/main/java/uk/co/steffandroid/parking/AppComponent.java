package uk.co.steffandroid.parking;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.steffandroid.parking.data.DataModule;
import uk.co.steffandroid.parking.data.api.ApiModule;
import uk.co.steffandroid.parking.ui.MainActivity;

@Singleton
@Component(modules = {DataModule.class, ApiModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
}
