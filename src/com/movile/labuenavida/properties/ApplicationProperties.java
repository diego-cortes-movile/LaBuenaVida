package com.movile.labuenavida.properties;

import org.apache.commons.configuration.ConfigurationException;


/**
 * @author Peter Escamilla (peter.escamilla@movile.com)
 */
public class ApplicationProperties extends CustomProperties {

    private static final String COUNTRY = "app.country";
    private static final String CARRIER = "app.carrier";
    private static final String SBS_APPLICATION_ID = "sbs.app.id";
    private static final String CHUB_APPLICATION_ID = "chub.app.id";
    private static final String CURRENCY = "sbs.currency";
    private static final String BILLING_URL = "sbs.bill.url";
    private static final String CHARGE = "sbs.charge";
    private static final String SBS_VALID_IPS = "sbs.valid.ips";
    private static final String CLARO_VALID_IPS = "app.claro.valid.ip";
    private static final String SBS_LARGE_ACCOUNT = "sbs.large.account";
    private static final String SBS_FINAL_LARGE_ACCOUNT = "sbs.final.large.account";
    private static final String PROFILE_URL = "sbs.profile.url";
    private static final String PROFILE_WITH_INACTIVES_URL = "sbs.profilewithinactives.url";
    private static final String SBS_REFERENCE_ID = "reference.id.sbs";
    private static final String LOGIN_APPLICATION_ID = "app.id.login";
    private static final String SITE_URL = "site_url";
    private static final String ATLAS_CALLBACK_CONFIGURATION = "atlas.callback.configuration";
    private static final String ATLAS_PROFILE = "atlas.profile";

    private static ApplicationProperties instance;

    /**
     * Gets an instance from {@link ApplicationProperties} class
     * 
     * @return {@link ApplicationProperties} object
     */
    public static ApplicationProperties getInstance() {
        if (instance == null) {
            instance = new ApplicationProperties();
        }
        instance.reloadProperties();
        return instance;
    }

    /**
     * Constructor
     */
    private ApplicationProperties() {
        super();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        
        this.setFileName(classLoader.getResource("application.properties").toString());
    }

    public String getCountry() {
        return this.getString(COUNTRY);
    }

    public String getCarrier() {
        return this.getString(CARRIER);
    }

    public int getSbsAppId() {
        return this.getInt(SBS_APPLICATION_ID);
    }

    public int getChubAppId() {
        return this.getInt(CHUB_APPLICATION_ID);
    }

    public int getCurrency() {
        return this.getInt(CURRENCY);
    }

    public int getSbsReferenceId() {
        return this.getInt(SBS_REFERENCE_ID);
    }

    public int getLoginAppId() {
        return this.getInt(LOGIN_APPLICATION_ID);
    }

    public String getSiteURL() {
        return this.getString(SITE_URL);
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return this.getString(BILLING_URL);
    }

    /**
     * @return the charge
     */
    public String getCharge() {
        return this.getString(CHARGE);
    }

    /**
     * @return the SBS valid ip(s)
     */
    public String[] getSBSValidIps() {
        try {
            this.refresh();
        } catch (ConfigurationException e) {
            SYSTEM_LOGGER.info("Error trying to refresh property file");
            EXCEPTIONS_LOGGER.error(e.getMessage());
        }
        return this.getString(SBS_VALID_IPS).split(";");
    }

    /**
     * @return the SBS largeAccount
     */
    public String getSBSLargeAccount() {
        return this.getString(SBS_LARGE_ACCOUNT);
    }

    /**
     * @return the SBS FINAL largeAccount
     */
    public String getSBSFinalLargeAccount() {
        return this.getString(SBS_FINAL_LARGE_ACCOUNT);
    }

    /**
     * @return the claro valid ip(s)
     */
    public String getClaroValidIps() {
        try {
            this.refresh();
            return this.getString(CLARO_VALID_IPS);
        } catch (ConfigurationException e) {
            SYSTEM_LOGGER.info("Error trying to refresh property file");
            EXCEPTIONS_LOGGER.error(e.getMessage());
            return null;
        }
    }

    /**
     * @return the profile url
     */
    public String getProfileUrl() {
        return this.getString(PROFILE_URL);
    }

    /**
     * @return the profile url
     */
    public String getProfileWithInactivesUrl() {
        return this.getString(PROFILE_WITH_INACTIVES_URL);
    }

    public String getAtlasCallbackConfiguration() {
        return this.getString(ATLAS_CALLBACK_CONFIGURATION);
    }

    public String getAtlasProfile() {
        return this.getString(ATLAS_PROFILE);
    }
}
