package com.movile.labuenavida.util;

import java.util.UUID;

import org.slf4j.MDC;

/**
 * @author Guillermo Varela (guillermo.varela@movile.com)
 */
public class LogUtil {

    private static final String STAMP_LABEL = "stamp";

    private LogUtil() {}

    public static void createStamp() {
        MDC.put(STAMP_LABEL, UUID.randomUUID().toString());
    }

    public static String getStamp() {
        return MDC.get(STAMP_LABEL);
    }

    public static void clearStamp() {
        MDC.clear();
    }
}
