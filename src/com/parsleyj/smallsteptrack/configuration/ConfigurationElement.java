package com.parsleyj.smallsteptrack.configuration;

/**
 * An interface representing an element of a configuration. Store implements this, for example.
 */
public interface ConfigurationElement {

    /**
     * Returns the name of this element
     * @return the name of the element
     */
    String getConfigurationElementName();
}
