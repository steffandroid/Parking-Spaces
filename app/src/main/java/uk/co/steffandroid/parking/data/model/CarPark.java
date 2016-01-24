package uk.co.steffandroid.parking.data.model;

import android.location.Location;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

import io.urbanthings.datamodel.PlacePoint;
import io.urbanthings.datamodel.ResourceStatus;
import uk.co.steffandroid.parking.R;

public class CarPark implements Comparable<CarPark> {
    private String primaryCode;
    private String name;
    private LatLng latLng;
    private double distance;
    private String statusText;
    private int available;
    private int taken;
    private Status status;

    private CarPark(Builder builder) {
        this.primaryCode = builder.placePoint.primaryCode;
        this.name = builder.placePoint.name;
        this.latLng = new LatLng(builder.placePoint.lat, builder.placePoint.lng);
        if (builder.location != null) {
            Location location = new Location("");
            location.setLatitude(builder.placePoint.lat);
            location.setLongitude(builder.placePoint.lng);
            this.distance = location.distanceTo(builder.location) * 0.000621371;
        }
        this.statusText = builder.resourceStatus.statusText;
        this.available = builder.resourceStatus.availablePlaces;
        this.taken = builder.resourceStatus.takenPlaces;
        double capacity = taken + available;
        if (builder.resourceStatus.isClosed) {
            this.status = Status.CLOSED;
        } else if (taken / capacity < 0.9) {
            this.status = Status.LOADS_OF_ROOM;
        } else if (taken / capacity < 0.95) {
            this.status = Status.NOT_MUCH_ROOM;
        } else {
            this.status = Status.FULL;
        }
    }

    public String primaryCode() {
        return primaryCode;
    }

    public String name() {
        return name.replace("P+R", "Park and Ride");
    }

    public LatLng latLng() {
        return latLng;
    }

    public String distanceText() {
        if (distance != 0) {
            return new DecimalFormat("#.#").format(distance) + "mi";
        } else {
            return null;
        }
    }

    public String statusText() {
        return statusText;
    }

    public int available() {
        return available;
    }

    public int taken() {
        return taken;
    }

    public Status status() {
        return status;
    }

    @Override
    public int compareTo(@NonNull CarPark another) {
        return Double.compare(distance, another.distance);
    }

    public enum Status {
        LOADS_OF_ROOM(R.color.color_green),
        NOT_MUCH_ROOM(R.color.color_amber),
        FULL(R.color.color_red),
        CLOSED(R.color.color_grey);

        private int colorRes;

        Status(@ColorRes int colorRes) {
            this.colorRes = colorRes;
        }

        @ColorRes
        public int colorRes() {
            return colorRes;
        }
    }

    public static class Builder {
        private PlacePoint placePoint;
        private ResourceStatus resourceStatus;
        private Location location;

        public Builder placePoint(PlacePoint placePoint) {
            this.placePoint = placePoint;
            return this;
        }

        public Builder resourceStatus(ResourceStatus resourceStatus) {
            this.resourceStatus = resourceStatus;
            return this;
        }

        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        public CarPark build() {
            return new CarPark(this);
        }
    }
}
