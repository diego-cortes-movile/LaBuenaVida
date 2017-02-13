package com.movile.mob.integration;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.movile.labuenavida.util.Util;

public class AtlasWS {

    private static final PropertiesConfiguration appProp = Util.getAppProperties();

    private static final Logger EXCEPTION_LOGGER = LoggerFactory.getLogger("exception");

    public static String getCarrier(String msisdn) {
        String validateURL = "";
        String result = "";
        String atlasURl = appProp.getString("atlas_url");

        try {
            validateURL = atlasURl + "getCarrier/?msisdn=" + msisdn;
            result = Util.doRequest(validateURL);
        } catch (Exception e) {
            EXCEPTION_LOGGER.error("[error][{}]", e.getMessage(), e);
        }
        return result;
    }
}
