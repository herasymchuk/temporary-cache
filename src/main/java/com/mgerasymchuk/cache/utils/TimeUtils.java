package com.mgerasymchuk.cache.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Maksym
 * Date: 17.06.13
 * Time: 22:36
 * To change this template use File | Settings | File Templates.
 */
public class TimeUtils {
    public static Long getRelativeTime(Integer deltaTime) {
        return System.currentTimeMillis() + deltaTime;
    }
}
