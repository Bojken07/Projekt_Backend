package com.bojken.Projekt.backend.response;

public class IntegerResponse implements Response{

    private Integer averageRuntime;

    public IntegerResponse (Integer integer) {
        this.averageRuntime = integer;
    }

    public Integer getAverageRuntime() {
        return averageRuntime;
    }

    public void setAverageRuntime(Integer averageRuntime) {
        this.averageRuntime = averageRuntime;
    }

}
