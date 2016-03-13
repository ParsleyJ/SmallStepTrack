package com.parsleyj.smallsteptrack.configuration;

import java.util.Arrays;
import java.util.HashMap;

/**
 * A collection of configuration elements. This class with its elements propagates through
 * the semantic objects of the program by calling the {@code step(Configuration configuration)} methods.
 */
public class Configuration{

    private HashMap<String, ConfigurationElement> elementsMap = new HashMap<>();

    /**
     * This constructor takes varargs arguments and uses them to create the name:configurationElement mapping
     * @param elements the configuration elements
     */
    public Configuration(ConfigurationElement... elements){
        Arrays.asList(elements).forEach((x) -> elementsMap.put(x.getConfigurationElementName(), x));
    }

    /**
     * Get a configuration element using its name as key.
     * @param name the name of the configuration element
     * @return a configuration element
     */
    public ConfigurationElement getConfigurationElement(String name){
        return elementsMap.get(name);
    }

    /**
     * Prints the stat of all the configuration elements to stdout.
     */
    public void printState() {
        elementsMap.keySet().forEach((k) -> System.out.println(k + "  =  " + elementsMap.get(k)));
    }
}
