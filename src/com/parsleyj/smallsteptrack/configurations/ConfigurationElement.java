package com.parsleyj.smallsteptrack.configurations;

/**
 * An interface representing an element of a configuration. Store implements this, for example.
 */
public interface ConfigurationElement {

    /**
     * Returns the name of this element
     * @return the name of the element
     */
    String getConfigurationElementName();

    /**
     * @return true if the element should be printed during execution, false otherwise.
     */
    default boolean toBePrinted(){
        return true;
    };
}
