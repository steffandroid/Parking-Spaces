package uk.co.steffandroid.parkbristol.data;

import java.util.List;

import io.urbanthings.datamodel.VehicleType;
import rx.Observable;
import uk.co.steffandroid.parkbristol.data.api.UrbanThingsService;
import uk.co.steffandroid.parkbristol.data.model.CarPark;

public class DataManager {
    private static final double MIN_LAT = 51.25399873081391;
    private static final double MAX_LAT = 51.574375555917875;
    private static final double MIN_LNG = -2.722232937812805;
    private static final double MAX_LNG = -2.2484666481614113;

    private final UrbanThingsService service;

    public DataManager(UrbanThingsService service) {
        this.service = service;
    }

    public Observable<List<CarPark>> getCarParks() {
        return service.getCarParks(VehicleType.Car, MIN_LAT, MAX_LAT, MIN_LNG, MAX_LNG)
                .flatMap(Observable::from)
                .flatMap(placePoint -> service.getStatus(placePoint.primaryCode)
                        .map(resourceStatuses -> new CarPark.Builder()
                                .placePoint(placePoint)
                                .resourceStatus(resourceStatuses.get(0))
                                .build())
                        .toList());
    }
}
