package com.movile.labuenavida.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static Map<Long, PropertiesConfiguration> propertiesByCarrier = new HashMap<Long, PropertiesConfiguration>(SUPPORTED_CARRIERS.size());

    public static void startConfiguration() throws ConfigurationException {
        applicationProperties = buildProperties("application.properties");
        messagesProperties = buildProperties("messages.properties");

        for (Carrier carrier : SUPPORTED_CARRIERS) {
            propertiesByCarrier.put(carrier.getId(), buildProperties("carrier." + carrier.getId() + ".properties"));
        }
    }

    /**
     * @return
     */
    public static PropertiesConfiguration getApplicationProperties() {
        return applicationProperties;
    }

    /**
     * @return
     */
    public static PropertiesConfiguration getMessagesProperties() {
        return messagesProperties;
    }

    /**
     * @param fileName
     * @return
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
