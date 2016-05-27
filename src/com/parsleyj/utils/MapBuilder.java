package com.parsleyj.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Giuseppe on 02/05/16.
 * TODO: javadoc
 */
public class MapBuilder<K, T> {
    private HashMap<K, T> hashMap = new HashMap<>();

    public MapBuilder<K, T> put(K key, T val) {
        hashMap.put(key, val);
        return this;
    }

    public Map<K, T> get() {
        return hashMap;
    }
}
