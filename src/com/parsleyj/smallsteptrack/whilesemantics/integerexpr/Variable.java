package com.parsleyj.smallsteptrack.whilesemantics.integerexpr;

import com.parsleyj.smallsteptrack.configurations.Configuration;
import com.parsleyj.smallsteptrack.configurations.IntegerStore;

/**
 * Semantic object representing a variable.
 */
public class Variable implements IntegerExpression {
    private String storeName;
    private String name;

    public Variable(String storeName, String name) {
        this.storeName = storeName;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public IntegerExpression step(Configuration c) {
        Integer val = ((IntegerStore) c.getConfigurationElement(storeName)).read(name);
        return new Numeral(val);
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public Integer getIntValue() {
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
