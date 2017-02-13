package com.movile.labuenavida.util;

import com.movile.sdk.MovileSDK;
import com.movile.sdk.services.chub.ChubClient;
import com.movile.sdk.services.sbs.SBSClient;
import com.movile.sdk.services.smartchannel.SmartChannelClient;

/**
 * Holds the available MovileSDK clients.
 * 
 * @author Yvan Lopez (yvan.lopez@movile.com)
 */
public class MovileSdkHolder {

    private static MovileSdkHolder instance = new MovileSdkHolder();

    private SBSClient sbsClient;
    private SmartChannelClient smartChannelClient;
    private ChubClient cHubClient;

    private MovileSdkHolder() {}

    public static void initialize() {
        MovileSDK movileSDK = new MovileSDK("movile-sdk.properties");
        instance.sbsClient = movileSDK.getSBS();
        instance.smartChannelClient = movileSDK.getSmartChannelClient();
        instance.cHubClient = movileSDK.getChub();
    }

    public static SBSClient getSbsClient() {
        return instance.sbsClient;
    }

    public static SmartChannelClient getSmartChannelClient() {
        return instance.smartChannelClient;
    }

    public static ChubClient getcHubClient() {
        return instance.cHubClient;
    }
}
