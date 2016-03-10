package com.parsleyj.smallsteptrack;

import com.parsleyj.smallsteptrack.integerexpr.Variable;

import java.util.HashMap;

/**
 * Created by Giuseppe on 09/03/16.
 */
public class Store {
    private HashMap<String, Integer> map = new HashMap<>();

    public void write(String name, int n) {
        map.put(name, n);
    }

    public int read(String name) {
        return map.get(name);
    }

    public int read(Variable var) {
        return map.get(var.getName());
    }

    @Override
    public String toString() {
        final String[] result = {"{ "};
        map.keySet().forEach((String k) -> result[0] += k + "=" + map.get(k) + "; ");
        result[0] += "}";
        return result[0];
    }
}
