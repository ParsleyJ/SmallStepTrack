package com.parsleyj.smallsteptrack.configurations;

import com.parsleyj.smallsteptrack.program.Program;
import com.parsleyj.smallsteptrack.whilesemantics.integerexpr.Variable;

import java.util.HashMap;

/**
 * The memory used by the {@link Program} during execution "step-by-step".
 * This is in fact usually passed as argument to {@code step()} methods, wrapped inside a configuration,
 * in order to allow expressions and commands to write and read from the store.
 * The store behaves like a Map. The variable name is used as key, and values are {@link Integer}.
 */
public class IntegerStore implements ConfigurationElement{
    private HashMap<String, Integer> map = new HashMap<>();
    private String storeName;

    public IntegerStore(String storeName){

        this.storeName = storeName;
    }

    /**
     * Writes a value in the store.
     * @param name the name of the variable, used as key.
     * @param n the value to be saved.
     */
    public void write(String name, int n) {
        map.put(name, n);
    }

    /**
     * Reads a value from the store.
     * @param name the name of the variable, used as key.
     * @return the value relative to the specified key, or
     *          {@code null} if no values are assigned to that key.
     */
    public Integer read(String name) {
        return map.get(name);
    }

    /**
     * Reads a value from the store. Equivalent to {@code write(var.getName())}.
     * @param var the Variable object, which name is used as key.
     * @return the value relative to the specified variable, or
     *          {@code null} if no values are assigned to that variable.
     */
    public Integer read(Variable var) {
        return map.get(var.getName());
    }

    /**
     * Returns a simple {@link String} representation of the store, with its contents.
     * @return the text string representation of the store.
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder("{ ");

        map.keySet().forEach((k) -> result.append(k).append("=").append(map.get(k)).append("; ")); //lambdas! yay!

        result.append("}");
        return result.toString();
    }

    @Override
    public String getConfigurationElementName() {
        return storeName;
    }


}
