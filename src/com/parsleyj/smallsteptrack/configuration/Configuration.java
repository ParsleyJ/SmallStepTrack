package com.parsleyj.smallsteptrack.configuration;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Giuseppe on 13/03/16.
 */
public class Configuration{

    private boolean stuck = false;
    private HashMap<String, ConfigurationElement> elementsMap = new HashMap<>();

    public Configuration(ConfigurationElement... elements){
        Arrays.asList(elements).forEach((x) -> elementsMap.put(x.getConfigurationElementName(), x));
    }

    public void setStuck(){
        setStuck(true);
    }

    public void setStuck(boolean s){
        stuck = s;
    }

    public boolean isStuck(){
        return stuck;
    }

    public ConfigurationElement getConfigurationElement(String name){
        return elementsMap.get(name);
    }

    public void printState() {
        elementsMap.keySet().forEach((k) -> System.out.println(k + "  =  " + elementsMap.get(k)));
    }
}
