package com.mgerasymchuk.cache.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Maksym
 * Date: 01.06.13
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */
public class CustomClock implements Clock {
    private long now;

    public CustomClock() {
        now = System.currentTimeMillis();
    }

    public CustomClock(long now){
        this.now = now;
    }

    public long now(){
        return now;
    }

    public void setTime(Long time) {
        now = time;
    }

    public void plusMilliseconds(long milliseconds) {
        now += milliseconds;
    }
}
