package com.movile.labuenavida.properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Peter Escamilla (peter.escamilla@movile.com)
 */
abstract class CustomProperties extends PropertiesConfiguration {

    protected static final Logger EXCEPTIONS_LOGGER = LoggerFactory.getLogger("exceptions");
    protected static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");

    /**
     * Gets an instance from {@link CustomProperties} class
     * @return {@link CustomProperties} object
     */
    protected static CustomProperties getInstance() {
        return null;
    }

    /**
     * Reload all properties from property file.
     */
    protected void reloadProperties() {
        try {
            this.load();
        } catch (ConfigurationException e) {
            SYSTEM_LOGGER.info("Error trying to get propertiy file");
            EXCEPTIONS_LOGGER.error(e.getMessage());
        }
    }

}
