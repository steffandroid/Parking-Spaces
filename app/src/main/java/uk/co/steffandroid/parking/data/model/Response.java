package uk.co.steffandroid.parking.data.model;

public class Response<T> {
    private boolean success;
    private T data;

    public T data() {
        return data;
    }
}
