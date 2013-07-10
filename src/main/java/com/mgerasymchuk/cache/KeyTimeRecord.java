package com.mgerasymchuk.cache;

/**
 * Created with IntelliJ IDEA.
 * User: Maksym
 * Date: 14.05.13
 * Time: 23:55
 * To change this template use File | Settings | File Templates.
 */
public class KeyTimeRecord<K> implements Comparable {

    private long date;
    private K key;

    public KeyTimeRecord(K key, long date) {
        this.key = key;
        this.date = date;
    }

    @Override
    public int compareTo(Object o) {
        KeyTimeRecord<K> record = (KeyTimeRecord<K>)o;
        if(this.date > record.date) {
            return 1;
        } else if(this.date < record.date) {
            return -1;
        }
        return 0;
    }

    public long getDate() {
        return date;
    }

    public K getKey() {
        return key;
    }
}
