package com.movile.labuenavida.enums;

/**
 * Keys for application messages.
 * 
 * @author Yvan Lopez (yvan.lopez@movile.com)
 */
public enum MessageKey {

    EMPTY_MSISDN("empty.msisdn"),
    INVALID_MSISDN("invalid.msisdn"),
    INVALID_CARRIER("invalid.carrier"), 
    EMPTY_SUBSCRIPTION("empty.subscription");

    private String key;

    private MessageKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
