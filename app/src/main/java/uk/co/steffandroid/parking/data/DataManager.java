package uk.co.steffandroid.parking.data;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import io.urbanthings.datamodel.PlacePoint;
import io.urbanthings.datamodel.VehicleType;
import rx.Observable;
import rx.observables.StringObservable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import uk.co.steffandroid.parking.data.api.UrbanThingsService;
import uk.co.steffandroid.parking.data.model.CarPark;

public class DataManager {
    private static final double BRISTOL_LAT = 51.454513;
    private static final double BRISTOL_LNG = -2.587910;
    private static final double BATH_LAT = 51.375801;
    private static final double BATH_LNG = -2.359904;
    private static final int MAX_RADIUS = 10000;

    private final UrbanThingsService service;
    private final PublishSubject<Boolean> refreshSubject = PublishSubject.create();

    public DataManager(UrbanThingsService service) {
        this.service = service;
    }

    public Observable<List<CarPark>> getCarParks(Location location) {
        return refreshSubject.asObservable().startWith(true)
                .observeOn(Schedulers.io())
                .switchMap(refresh -> Observable.zip(
                        service.getCarParks(VehicleType.Car, MAX_RADIUS, BATH_LAT, BATH_LNG),
                        service.getCarParks(VehicleType.Car, MAX_RADIUS, BRISTOL_LAT, BRISTOL_LNG),
                        (responseBath, responseBristol) -> {
                            List<PlacePoint> placePoints = new ArrayList<>();
                            placePoints.addAll(responseBath.data());
                            placePoints.addAll(responseBristol.data());
                            return placePoints;
                        }))
                .flatMap(placePoints -> StringObservable.join(Observable.<PlacePoint>from(placePoints)
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
