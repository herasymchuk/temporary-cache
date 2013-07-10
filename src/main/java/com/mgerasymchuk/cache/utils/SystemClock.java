package com.mgerasymchuk.cache.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Maksym
 * Date: 16.05.13
 * Time: 0:05
 * To change this template use File | Settings | File Templates.
 */
public class SystemClock implements Clock {
    public long now() {
        return System.currentTimeMillis();
    }
}
