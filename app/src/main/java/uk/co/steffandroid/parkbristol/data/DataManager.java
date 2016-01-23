package uk.co.steffandroid.parkbristol.data;

import android.location.Location;

import java.util.List;

import io.urbanthings.datamodel.VehicleType;
import rx.Observable;
import rx.observables.StringObservable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import uk.co.steffandroid.parkbristol.data.api.UrbanThingsService;
import uk.co.steffandroid.parkbristol.data.model.CarPark;
import uk.co.steffandroid.parkbristol.data.model.Response;

public class DataManager {
    private static final double MIN_LAT = 51.25399873081391;
    private static final double MAX_LAT = 51.574375555917875;
    private static final double MIN_LNG = -2.722232937812805;
    private static final double MAX_LNG = -2.2484666481614113;

    private final UrbanThingsService service;
    private final PublishSubject<Boolean> refreshSubject = PublishSubject.create();

    public DataManager(UrbanThingsService service) {
        this.service = service;
    }

    public Observable<List<CarPark>> getCarParks(Location location) {
        return refreshSubject.asObservable().startWith(true)
                .observeOn(Schedulers.io())
                .switchMap(refresh -> service.getCarParks(VehicleType.Car, MIN_LAT, MAX_LAT, MIN_LNG, MAX_LNG))
                .map(Response::data)
                .flatMap(placePoints -> StringObservable.join(Observable.from(placePoints)
                        .map(placePoint -> placePoint.primaryCode), ",")
                        .flatMap(service::getStatuses)
                        .flatMap(response -> Observable.from(response.data()))
                        .toMap(resourceStatus -> resourceStatus.primaryCode)
                        .flatMap(resourceStatusMap -> Observable.from(placePoints)
                                .map(placePoint -> new CarPark.Builder()
                                        .placePoint(placePoint)
                                        .resourceStatus(resourceStatusMap.get(placePoint.primaryCode))
                                        .location(location)
                                        .build())
                                .toSortedList()))
                .cache();
    }

    public void refresh() {
        refreshSubject.onNext(true);
    }
}
