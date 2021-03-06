package com.mikhaildev.test.taskmanager.network;


/**
 * Created by meroshkin on 05.08.2014.
 * This is a wrapper class for delivering result to fragment/activity
 */
public class AsyncResult< D > {
    private Exception exception;
    private D data;

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    public void setData(D data) {
        this.data = data;
    }

    public D getData() {
        return data;
    }

}
