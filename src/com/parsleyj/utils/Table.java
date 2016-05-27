package com.parsleyj.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Giuseppe on 04/04/16.
 * TODO: javadoc
 */
public class Table<Key, Value> {
    private HashMap<Key, Value> tableContent = new HashMap<>();

    public boolean put(Key key, Value value) {
        return tableContent.put(key, value) == null;
    }

    public boolean contains(Key key) {
        return tableContent.containsKey(key);
    }

    public void remove(Key key) {
        tableContent.remove(key);
    }

    public Value get(Key key) {
        return tableContent.get(key);
    }

    public Set<Key> keySet() {
        return tableContent.keySet();
    }

    public Collection<Value> values() {
        return tableContent.values();
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder("{ ");
        tableContent.keySet().forEach((k) -> result.append(k).append("=").append(tableContent.get(k)).append("; "));
        result.append("}");
        return result.toString();
    }
}
