package com.movile.labuenavida.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.movile.labuenavida.properties.ApplicationProperties;

public class SubscriptionUtil {

    private static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");

    private static final Logger REQUESTS_PERFORMED_LOGGER = LoggerFactory.getLogger("requests_performed");

    public static boolean isSubscribed(String msisdn) {
        ApplicationProperties properties = ApplicationProperties.getInstance();
        String profileUrl = properties.getProfileUrl();
        profileUrl = profileUrl.replace("#phone#", msisdn);
        profileUrl = profileUrl.replace("#application#", properties.getSbsAppId() + "");
        profileUrl = profileUrl.replace("#referenceId#", properties.getSbsReferenceId() + "");

        REQUESTS_PERFORMED_LOGGER.info("[SBS] Request Profile for phone number=" + msisdn);
        String profile = HttpUtil.doGet(profileUrl);
        REQUESTS_PERFORMED_LOGGER.info("[SBS] Response Profile return {} to phone {}", profile, msisdn);

        if (profile != null && profile.contains("<error>false</error>") && profile.contains("<status>ACTIVE</status>")) {
            SYSTEM_LOGGER.info("[SBS] informed about active subscription for user: " + msisdn);
            return true;
        }

        return false;
    }

    public static boolean isSubscribedWaitingRenew(String msisdn) {
        ApplicationProperties properties = ApplicationProperties.getInstance();
        String profileUrl = properties.getProfileUrl();
        profileUrl = profileUrl.replace("#phone#", msisdn);
        profileUrl = profileUrl.replace("#application#", properties.getSbsAppId() + "");
        profileUrl = profileUrl.replace("#referenceId#", properties.getSbsReferenceId() + "");

        REQUESTS_PERFORMED_LOGGER.info("[SBS] Request Profile for phone number=" + msisdn);
        String profile = HttpUtil.doGet(profileUrl);
        REQUESTS_PERFORMED_LOGGER.info("[SBS] Response Profile return {} to phone {}", profile, msisdn);

        if (profile != null && profile.contains("<error>false</error>") && !profile.contains("<status>ACTIVE</status>")) {
            SYSTEM_LOGGER.info("[SBS] informed about inactive subscription for user: " + msisdn);
            return true;
        }

        return false;
    }
}
