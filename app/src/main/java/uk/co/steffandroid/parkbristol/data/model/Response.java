package uk.co.steffandroid.parkbristol.data.model;

public class Response<T> {
    private boolean success;
    private T data;

    public T data() {
        return data;
    }
}
