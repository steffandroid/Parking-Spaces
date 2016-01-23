package uk.co.steffandroid.parkbristol.data.model;

import android.support.annotation.ColorRes;

import io.urbanthings.datamodel.PlacePoint;
import io.urbanthings.datamodel.ResourceStatus;
import uk.co.steffandroid.parkbristol.R;

public class CarPark {
    private String primaryCode;
    private String name;
    private double latitude;
    private double longitude;
    private String statusText;
    private int available;
    private int taken;
    private Status status;

    private CarPark(Builder builder) {
        this.primaryCode = builder.placePoint.primaryCode;
        this.name = builder.placePoint.name;
        this.latitude = builder.placePoint.lat;
        this.longitude = builder.placePoint.lng;
        this.statusText = builder.resourceStatus.statusText;
        this.available = builder.resourceStatus.availablePlaces;
        this.taken = builder.resourceStatus.takenPlaces;
        double capacity = taken + available;
        if (builder.resourceStatus.isClosed) {
            this.status = Status.CLOSED;
        } else if (taken / capacity < 0.8) {
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
        return name;
    }

    public double latitude() {
        return latitude;
    }

    public double longitude() {
        return longitude;
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

        public Builder placePoint(PlacePoint placePoint) {
            this.placePoint = placePoint;
            return this;
        }

        public Builder resourceStatus(ResourceStatus resourceStatus) {
            this.resourceStatus = resourceStatus;
            return this;
        }

        public CarPark build() {
            return new CarPark(this);
        }
    }
}
