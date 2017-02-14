package com.movile.labuenavida.bo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.movile.commons.phone.Carrier;
import com.movile.labuenavida.util.ConfigurationProvider;
import com.movile.labuenavida.util.MovileSdkHolder;
import com.movile.labuenavida.util.Util;
import com.movile.sdk.services.sbs.model.ProfileRequest;
import com.movile.sdk.services.sbs.model.ProfileResponse;
import com.movile.sdk.services.sbs.model.Subscription;

/**
 * Operations related to subscriptions.
 * 
 * @author Yvan Lopez (yvan.lopez@movile.com)
 */
public class SubscriptionBo {

    private static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");

    private static final SubscriptionBo instance = new SubscriptionBo();

    private SubscriptionBo() {}

    public static SubscriptionBo getInstance() {
        return instance;
    }

    /**
     * Finds a {@link Subscription} with the given arguments.
     * 
     * @param msisdn Phone number.
     * @return {@link Subscription} instance or {@code null} if not found.
     */
    public Subscription findSubscription(String msisdn) {
        SYSTEM_LOGGER.info("Finding subscription for msisdn={}", msisdn);

        PropertiesConfiguration properties = ConfigurationProvider.getApplicationProperties();
        Long applicationId = properties.getLong("sbs.application.id");
        //String referenceId = properties.getString("sbs.reference.id");
        Carrier carrier = Carrier.getById(properties.getString("sbs.carrier.id"));

        Long phoneNumber = Util.formatMsisdn(msisdn, carrier);

        // Obtiene el profile sin el ReferenceId.
        ProfileRequest profileRequest = new ProfileRequest(phoneNumber.toString());
        profileRequest.setCarrierId(carrier.getId());
        profileRequest.setApplicationId(applicationId);
        //profileRequest.setReferenceId(referenceId);

        Subscription subscription = null;
        ProfileResponse response = MovileSdkHolder.getSbsClient().getSubscriptionClient().profile(profileRequest);
        if (response != null && CollectionUtils.isNotEmpty(response.getSubscriptions())) {
            subscription = response.getSubscriptions().get(0);
        }

        SYSTEM_LOGGER.info("Subscription found: {}", subscription);
        return subscription;
    }
}
