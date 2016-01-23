package uk.co.steffandroid.parkbristol.data;

import java.util.List;

import io.urbanthings.datamodel.PlacePoint;
import io.urbanthings.datamodel.ResourceStatus;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface UrbanThingsService {
    @GET("static/stops")
    Observable<List<PlacePoint>> getCarParks(@Query("stopmodes") int vehicleType,
                                             @Query("minlat") double minLat,
                                             @Query("maxlat") double maxLat,
                                             @Query("minlong") double minLong,
                                             @Query("maxlong") double maxLong);

    @GET("rti/resources/status")
    Observable<List<ResourceStatus>> getStatus(@Query("stopids") String id);
}
