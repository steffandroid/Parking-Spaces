package uk.co.steffandroid.parkbristol;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.steffandroid.parkbristol.data.DataModule;
import uk.co.steffandroid.parkbristol.ui.MainActivity;

@Singleton
@Component(modules = DataModule.class)
public interface AppComponent {
    void inject(MainActivity mainActivity);
}
