package com.parsleyj.smallsteptrack.configurations;

import java.util.ArrayDeque;
import java.util.Collections;

/**
 * A configuration element representing an input stream of object of a generic type T.
 * This is called "Static" because it is meant to be initialized before the execution
 * of the program: when the execution is started, only the program is meant to do changes
 * in the stream.
 */
public class StaticInputStream<T> implements ConfigurationElement {

    private String name;
    private ArrayDeque<T> tArrayDeque = new ArrayDeque<>();

    /**
     * @param name the name of this configuration element
     * @param ints the elements to be added to the stream
     */
    @SafeVarargs
    public StaticInputStream(String name, T... ints) {
        this.name = name;
        Collections.addAll(tArrayDeque, ints);
    }

    /**
     * Gets the number of the remaining elements in the stream.
     * @return the number of the remaining elements in the stream.
     */
    public Integer length(){
        return tArrayDeque.size();
    }

    /**
     * Removes the first element from the stream and returns it
     * @return the popped element
     */
    public T pop(){
        return tArrayDeque.pop();
    }

    @Override
    public String getConfigurationElementName() {
        return name;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder("{ ");

        tArrayDeque.forEach((i) -> result.append(i).append(", "));

        result.append("}");
        return result.toString();
    }
}
