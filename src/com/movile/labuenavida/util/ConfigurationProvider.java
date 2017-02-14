package com.movile.labuenavida.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import com.movile.commons.phone.Carrier;

/**
 * Provides the configuration/properties used by the application.
 * 
 * @author Guillermo Varela (guillermo.varela@movile.com)
 */
public class ConfigurationProvider {

    public static final List<Carrier> SUPPORTED_CARRIERS = Arrays.asList(Carrier.COMCEL_CO);

    private static PropertiesConfiguration applicationProperties;
    private static PropertiesConfiguration messagesProperties;

    public static void startConfiguration() throws ConfigurationException {
        applicationProperties = buildProperties("application.properties");
        messagesProperties = buildProperties("messages.properties");
    }

    /**
     * Gets the application properties.
     * 
     * @return The properties for application.
     */
    public static PropertiesConfiguration getApplicationProperties() {
        return applicationProperties;
    }

    /**
     * Gets the messages in properties.
     * 
     * @return The properties for messages.
     */
    public static PropertiesConfiguration getMessagesProperties() {
        return messagesProperties;
    }

    /**
     * Builds the properties.
     * 
     * @param fileName The file name.
     * @return The properties.
     * @throws ConfigurationException
     */
    private static PropertiesConfiguration buildProperties(String fileName) throws ConfigurationException {
        PropertiesConfiguration properties = new PropertiesConfiguration();
        properties.setFileName(fileName);
        properties.setEncoding(StandardCharsets.UTF_8.name());
        properties.setReloadingStrategy(new FileChangedReloadingStrategy());
        properties.load();
        return properties;
    }
}
