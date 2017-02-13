/**
 * Movile S.A. | CONNECT > EVERYONE
 */
package com.movile.labuenavida.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartProperties {

    private static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");

    private Properties properties;

    public SmartProperties() {
        this.properties = new Properties();
    }

    public SmartProperties(Properties properties) {
        this.properties = properties;
    }

    public String getString(String property, String defaultValue) {
        String value = properties.getProperty(property);

        if (value == null) {
            SYSTEM_LOGGER.info("Property " + property + " not set. Assuming the default value: " + defaultValue);
            return defaultValue;
        } else {
            SYSTEM_LOGGER.debug("Property " + property + " - returning configured value: " + value);
            return value;
        }
    }

    public Integer getInt(String property, Integer defaultValue) {
        String value = properties.getProperty(property);

        if (value == null) {
            SYSTEM_LOGGER.info("Property " + property + " not set. Assuming the default value: " + defaultValue);
            return defaultValue;
        }
        try {
            Integer result = new Integer(value);

            SYSTEM_LOGGER.debug("Property " + property + " - returning configured value: " + result);
            return result;
        } catch (Exception e) {

            SYSTEM_LOGGER.warn("Property " + property + " set to invalid value (" + value + "). Ignoring and assuming the default value: " + defaultValue);
            return defaultValue;
        }
    }

    public Integer getInt(String property, Integer defaultValue, Integer minValue, Integer maxValue) {
        Integer value = getInt(property, defaultValue);

        if (value < minValue || value > maxValue) {

            SYSTEM_LOGGER.warn("Property " + property + " set to out of range [" + minValue + "," + maxValue + "] value (" + value
                    + "). Ignoring and assuming the default value: " + defaultValue);
            return defaultValue;
        }
        return value;
    }

    public Long getLong(String property, Long defaultValue) {
        String value = properties.getProperty(property);

        if (value == null) {
            SYSTEM_LOGGER.info("Property " + property + " not set. Assuming the default value: " + defaultValue);
            return defaultValue;
        }
        try {
            Long result = new Long(value);

            SYSTEM_LOGGER.debug("Property " + property + " - returning configured value: " + result);
            return result;

        } catch (Exception e) {

            SYSTEM_LOGGER.warn("Property " + property + " set to invalid value (" + value + "). Ignoring and assuming the default value: " + defaultValue);
            return defaultValue;
        }
    }

    public Long getLong(String property, Long defaultValue, Long minValue, Long maxValue) {
        Long value = getLong(property, defaultValue);

        if (value < minValue || value > maxValue) {

            SYSTEM_LOGGER.warn("Property " + property + " set to out of range [" + minValue + "," + maxValue
                    + "] value. Ignoring and assuming the default value: " + defaultValue);
            return defaultValue;
        }
        return value;
    }

    public Boolean getBoolean(String property, Boolean defaultValue) {
        String value = properties.getProperty(property);

        if (value == null) {
            SYSTEM_LOGGER.info("Property " + property + " not set. Assuming the default value: " + defaultValue);
            return defaultValue;
        }
        try {
            Boolean result = Boolean.valueOf(value);

            SYSTEM_LOGGER.debug("Property " + property + " - returning configured value: " + result);
            return result;

        } catch (Exception e) {
            SYSTEM_LOGGER.warn("Property " + property + " set to invalid value (" + value + "). Ignoring and assuming the default value: " + defaultValue);
            return defaultValue;
        }
    }

    public Double getDouble(String property, Double defaultValue) {
        String value = properties.getProperty(property);
        if (value == null) {

            SYSTEM_LOGGER.info("Property " + property + " not set. Assuming the default value: " + defaultValue);
            return defaultValue;
        }
        try {
            Double result = new Double(value);

            SYSTEM_LOGGER.debug("Property " + property + " - returning configured value: " + result);
            return result;
        } catch (Exception e) {

            SYSTEM_LOGGER.warn("Property " + property + " set to invalid value (" + value + "). Ignoring and assuming the default value: " + defaultValue);
            return defaultValue;
        }
    }

    public void loadProperties(String filename) throws IOException {
        File file = new File(filename);
        FileInputStream is = new FileInputStream(file);
        properties.load(is);
        is.close();
    }

    public void loadProperties(InputStream inputStream) throws IOException {
        properties.clear();

        properties.load(inputStream);
        inputStream.close();
    }

    public void loadProperties(Properties p) throws IOException {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        properties.putAll(p);
    }

    public String getString(String s) {
        String result = getString(s, null);
        if (result == null)
            throw new RuntimeException("Missing value for mandatory property " + s);
        else
            return result;
    }

    public int getInt(String s) {
        Integer result = getInt(s, null);
        if (result == null)
            throw new RuntimeException("Missing value for mandatory property " + s);
        else
            return result;
    }

    public boolean getBoolean(String s) {
        Boolean result = getBoolean(s, null);
        if (result == null)
            throw new RuntimeException("Missing value for mandatory property " + s);
        else
            return result;
    }

    public double getDouble(String s) {
        Double result = getDouble(s, null);
        if (result == null)
            throw new RuntimeException("Missing value for mandatory property " + s);
        else
            return result;
    }

    public Properties getUnderlyingProperties() {
        return properties;
    }

    public void setUnderlyingProperties(Properties p) {
        properties = p;
    }
}
