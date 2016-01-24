package uk.co.steffandroid.parking.data;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.steffandroid.parking.data.api.UrbanThingsService;

@Module
public class DataModule {
    @Provides
    @Singleton
    DataManager provideDataManager(UrbanThingsService urbanThingsService) {
        return new DataManager(urbanThingsService);
    }
}
