package com.movile.labuenavida.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compera.commons.util.Criptografia;
import com.compera.databaseManagement.DatabaseProperties;
import com.compera.portal.processor.UserIdentifierProcessor;
import com.compera.portal.tree.Tree;
import com.movile.commons.phone.Carrier;
import com.movile.labuenavida.enums.MessageKey;
import com.movile.labuenavida.exception.LaBuenaVidaException;

/**
 * @author Marco De Campos (marco.decampos@movile.com)
 */
public class Util {
    private static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");
    private static final Logger REQUEST_PERFORMED_LOGGER = LoggerFactory.getLogger("request_performed");
    private static final Logger EXCEPTION_LOGGER = LoggerFactory.getLogger("exception");

    public static final String SERVICE_ID = "7594";
    public static final String IP_REGEX = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3} ";
    private static final int TIMEOUT = 120000;

    private static final List<String> HEADERS = new ArrayList<String>();

    static {
        HEADERS.add("HTTP_MSISDN");
        HEADERS.add("HTTP_X_MSISDN");
        HEADERS.add("HTTP_CALLING_STATION_ID");
        HEADERS.add("X-MSISDN");
        HEADERS.add("X-NOKIA-MSISDN");
        HEADERS.add("X-UP-CALLING-LINE-ID");
    }

    public static String getPhoneFromHeaders(HttpServletRequest request) {

        String foundMsisdn = request.getParameter("msisdn");

        if (StringUtils.isBlank(foundMsisdn)) {
            for (String header : HEADERS) {
                foundMsisdn = request.getHeader(header);
                if (foundMsisdn != null && !foundMsisdn.isEmpty()) {
                    SYSTEM_LOGGER.debug("Retrieved " + foundMsisdn + " from user " + header + " attribute");
                    return foundMsisdn;
                }
            }
        }

        if (StringUtils.isBlank(foundMsisdn)) {
            SYSTEM_LOGGER.warn("Dind't find telephone in headers");
        } else {
            foundMsisdn.replaceAll(IP_REGEX, "");
        }

        return foundMsisdn;

    }

    public static String doRequest(String srtUrl) throws Exception {
        StopWatch chronometer = new StopWatch();
        chronometer.start();

        REQUEST_PERFORMED_LOGGER.debug("Request url: " + srtUrl);

        StringBuilder response;

        try {
            URL url = new URL(srtUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-type", "text/xml;charset=UTF-8");
            conn.setRequestMethod("GET");
            conn.setReadTimeout(TIMEOUT);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            BufferedReader br = null;
            if (conn.getResponseCode() > 400) {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }

            StringBuffer newData = new StringBuffer();
            String s = "";
            while (null != ((s = br.readLine()))) {
                newData.append(s);
            }
            br.close();

            response = new StringBuilder(newData);

            REQUEST_PERFORMED_LOGGER.debug("Response url: " + srtUrl + " Response: " + response.toString() + " apiUrl ={" + chronometer.getTime() + "} ms");

            return response.toString();
        } finally {
            chronometer.stop();
        }
    }

    public static void doRequestAnalytics(String srtUrl) throws Exception {
        doRequestAnalytics(srtUrl, null);
    }

    public static void doRequestAnalytics(String srtUrl, String userInterface) throws Exception {
        StopWatch chronometer = new StopWatch();
        chronometer.start();

        try {
            URL url = new URL(srtUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-type", "text/xml;charset=UTF-8");
            conn.setRequestMethod("GET");
            conn.setReadTimeout(TIMEOUT);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            int code = conn.getResponseCode();
            REQUEST_PERFORMED_LOGGER.info("Called url: " + srtUrl + " returned code: " + code + ", user interface: " + userInterface);
        } finally {
            chronometer.stop();
        }
    }

    /**
     * Formats the msisdn in a Long value.
     * 
     * @param msisdn The msisdn
     * @param carrier The carrier.
     * @return The msisdn in long format.
     */
    public static Long formatMsisdn(String msisdn, Carrier carrier) {
        SYSTEM_LOGGER.info("Formatting msisdn {} for carrier {}", msisdn, carrier);

        if (StringUtils.isBlank(msisdn)) {
            throw new LaBuenaVidaException(MessageKey.EMPTY_MSISDN);
        }
        if (carrier == null) {
            throw new LaBuenaVidaException(MessageKey.INVALID_CARRIER);
        }

        String phoneNumber = StringUtils.trim(msisdn);
        if (!NumberUtils.isDigits(phoneNumber)) {
            throw new LaBuenaVidaException(MessageKey.INVALID_MSISDN);
        }
        if (!msisdn.startsWith(carrier.getCountryCode().getCountryCallingCode())) {
            phoneNumber = carrier.getCountryCode().getCountryCallingCode() + phoneNumber;
        }

        SYSTEM_LOGGER.info("Formatted msisdn: {}", phoneNumber);
        return Long.valueOf(phoneNumber);
    }

    public static Long formatMsisdn(Long msisdn, Carrier carrier) {
        return formatMsisdn(msisdn != null ? msisdn.toString() : null, carrier);
    }

    public static PropertiesConfiguration getAppProperties() {
        PropertiesConfiguration prop = new PropertiesConfiguration();

        try {
            prop.load(DatabaseProperties.class.getResourceAsStream("/application.properties"));
        } catch (ConfigurationException ex) {
            EXCEPTION_LOGGER.error(ex.getMessage());
        }

        return prop;
    }

    public static String messageTreatment(String input) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(input)) {
            byte[] parameterByte = input.getBytes("ISO-8859-15");
            input = new String(parameterByte, "UTF-8");

            if (StringUtils.countMatches(input, ".") > 4) {
                input = input.replace(".", " ");
            }

            if (input.length() > 160) {
                input = input.substring(0, 160);
            }

            return input.replaceAll("ñ", "n").replaceAll("[^\\w\\s-?!.@\\s,\\s:\\s(\\s)]", "");
        }

        return null;
    }

    public static ArrayList<Map<String, String>> arrayListStringTreatment(ArrayList<Map<String, String>> messages) throws UnsupportedEncodingException {
        for (Map<String, String> message : messages) {
            String tmp = message.get("text").replaceAll("[^\\w\\s-?!.@\\s,\\s:\\s(\\s)]", "");
            message.put("text", tmp);
        }

        return messages;
    }

    public static String getTreeProperty(Tree node, String property) {
        if (node.getProperty(property) != null) {
            return node.getProperty(property).getSimpleValue();
        }
        return null;
    }

    public static String getPhone(HttpServletRequest request) {

        String phone = (String) request.getAttribute(UserIdentifierProcessor.USER_ATTRIBUTE);

        if (StringUtils.isNotBlank(phone)) {
            SYSTEM_LOGGER.debug("Retrieved " + phone + " from user REQUEST USER attribute");
        } else {
            phone = request.getParameter("phone_redirect");
            SYSTEM_LOGGER.debug("Retrieved " + phone + " from phone_redirect REQUEST parameter");
        }

        if (StringUtils.isBlank(phone)) {
            phone = (String) request.getSession().getAttribute("phone_session");
            SYSTEM_LOGGER.debug("Retrieved " + phone + " from phone_session SESSION attribute");
        }

        if (StringUtils.isBlank(phone)) {
            String id = request.getParameter("i");
            if (StringUtils.isNotBlank(id)) {
                id = Criptografia.descriptografarNumerosMinusculos(id.trim());
                SYSTEM_LOGGER.debug("Retrieved " + phone + " from i encrypted REQUEST parameter");
            }
        }

        if (phone == null)
            SYSTEM_LOGGER.warn("Dind't find telephone");
        return phone;
    }
}
