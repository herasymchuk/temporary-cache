package com.mgerasymchuk.cache;

import com.mgerasymchuk.cache.utils.Clock;
import com.mgerasymchuk.cache.utils.SystemClock;

import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Maksym
 * Date: 14.05.13
 * Time: 23:53
 * To change this template use File | Settings | File Templates.
 */
public class TemporaryCache2<K,V> extends AbstractMap<K,V> implements Cloneable, Serializable {

    private PriorityQueue<KeyTimeRecord<K>> timeQueue;
    private HashMap<K,V> hashMap;
    private Clock clock = new SystemClock();
    private long defaultExpirationPeriod = 10000; // 10 sec

    public TemporaryCache2() {
        timeQueue = new PriorityQueue<KeyTimeRecord<K>>();
        hashMap = new HashMap<K, V>();
    }

    public TemporaryCache2(Clock clock) {
        this();
        this.clock = clock;
    }

    @Override
    public int size() {
        invalidateExpiredItems();
        return hashMap.size();
    }

    @Override
    public boolean isEmpty() {
        invalidateExpiredItems();
        return hashMap.isEmpty();
    }

    @Override
    public V get(Object key) {
        invalidateExpiredItems();
        return hashMap.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        invalidateExpiredItems();
        return hashMap.containsKey(key);
    }

    public V put(K key, V value) {
        return put(key, value, defaultExpirationPeriod);
    }

    public V put(K key, V value, long expirationPeriod) {
        invalidateExpiredItems();
        timeQueue.offer(new KeyTimeRecord<K>(key, getExpirationDate(expirationPeriod)));
        return hashMap.put(key, value);
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        putAll(m, defaultExpirationPeriod);
    }

    public void putAll(Map<? extends K, ? extends V> m, long expirationPeriod) {
        int numKeysToBeAdded = m.size();
        if (numKeysToBeAdded == 0)
            return;
        for (K key : m.keySet()) {
            timeQueue.offer(new KeyTimeRecord<K>(key, getExpirationDate(expirationPeriod)));
        }
        hashMap.putAll(m);
    }

    @Override
    public V remove(Object key) {
        invalidateExpiredItems();
        return hashMap.remove(key);
    }

    @Override
    public void clear() {
        timeQueue.clear();
        hashMap.clear();
    }

    @Override
    public boolean containsValue(Object value) {
        invalidateExpiredItems();
        return hashMap.containsValue(value);
    }

    @Override
    public Set<K> keySet() {
        invalidateExpiredItems();
        return hashMap.keySet();
    }

    @Override
    public Collection<V> values() {
        invalidateExpiredItems();
        return hashMap.values();
    }

    @Override
    public Set<Entry<K,V>> entrySet() {
        invalidateExpiredItems();
        return hashMap.entrySet();
    }

    public void setDefaultExpirationPeriod(long defaultExpirationPeriod) {
        this.defaultExpirationPeriod = Math.abs(defaultExpirationPeriod);
    }

    public long getDefaultExpirationPeriod() {
        return defaultExpirationPeriod;
    }

    private void invalidateExpiredItems() {
        long currentDate = getCurrentDate();
        KeyTimeRecord<K> headItem;
        while(timeQueue.size() > 0) {
            headItem = timeQueue.peek();
            if(headItem.getDate() < currentDate) {
                removeItem(headItem.getKey());
            } else {
                break;
            }
        }
    }

    private V removeItem(Object key) {
        removeItemFromTimeQueueByKey(key);
        return hashMap.remove(key);
    }

    private void removeItemFromTimeQueueByKey(Object key) {
        KeyTimeRecord<K> record;
        for(Iterator<KeyTimeRecord<K>> queueIterator = timeQueue.iterator(); queueIterator.hasNext();) {
            record = queueIterator.next();
            if(record.getKey().equals(key)) {
                timeQueue.remove(record);
                break;
            }
        }
    }

    private long getCurrentDate() {
        return clock.now();
    }

    private long getExpirationDate(long period) {
        return getCurrentDate() + period;
    }
}
