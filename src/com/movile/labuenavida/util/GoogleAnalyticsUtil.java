/**
 * 
 */
package com.movile.labuenavida.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.compera.commons.util.Criptografia;
import com.compera.portal.device.Device;

/**
 * @author Peter Escamilla (peter.escamilla@movile.com)
 *
 */
public class GoogleAnalyticsUtil {

    private static final Logger SYSTEM_LOGGER = Logger.getLogger("system");
    private static final Logger EXCEPTION_LOGGER = Logger.getLogger("exception");
    private static final Logger REQUEST_PERFORMED = Logger.getLogger("request_performed");


    /**
     * Google analytics URL
     */
    private static final String ANALITICS_URL =
            "http://www.google-analytics.com/collect?v=1&tid=${TrackingId}&cid=${ClientId}&t=${PageviewType}&dh=${HostName}&dp=${Page}&ec=${Category}&ea=${Action}&el=${Label}&uip=${Ip}&ua=${Ua}";
    private static final String TRACKING_ID_PARAMETER = "${TrackingId}";
    private static final String CLIENT_ID_PARAMETER = "${ClientId}";
    private static final String PAGE_VIEW_TYPE_PARAMETER = "${PageviewType}";
    private static final String HOST_NAME_PARAMETER = "${HostName}";
    private static final String PAGE_PARAMETER = "${Page}";
    private static final String CATEGORY_PARAMETER = "${Category}";
    private static final String ACTION_PARAMETER = "${Action}";
    private static final String LABEL_PARAMETER = "${Label}";
    private static final String IP_PARAMETER = "${Ip}";
    private static final String USER_AGENT_PARAMETER = "${Ua}";


    /**
     * Google analytics identifier for smartphones
     */
    public static final String SMART_TRACKING_ID_VALUE = "UA-65924440-4";
    
    /**
     * Google analytics identifier for wap
     */
    public static final String WAP_TRACKING_ID_VALUE = "UA-65924440-5";

    /**
     * Host name
     */
    private static final String HOST_NAME_VALUE = "futbol.movilnet.com.ve/descargas";

    private static ArrayList<String> smartInterfaces = new ArrayList<String>();

    public static void sendGoogleAnalyticsEvent(HttpServletRequest request, HttpServletResponse response, String type, String category, String action,
            String label) throws ServletException, IOException {

        smartInterfaces = new ArrayList<String>();
        smartInterfaces.add("iphone");
        smartInterfaces.add("tablet");
        
        Device device = (Device)request.getAttribute("device");
        String analyticsId;
        String userInterface;
        
        if (device == null) {
            userInterface = null;
            analyticsId = WAP_TRACKING_ID_VALUE;
        } else {
            userInterface = device.getUserInterface();
            if (smartInterfaces.contains(device.getUserInterface())) {
                analyticsId = SMART_TRACKING_ID_VALUE;
            } else {
                analyticsId = WAP_TRACKING_ID_VALUE;
            }
        }

        final HttpServletRequest newRequest = request;
        final String newType = type;
        final String newCategory = category;
        final String newAction = action;
        final String newLabel2 = label;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                try {
                    String analyticsUrl = ANALITICS_URL;
                    String userAgent = newRequest.getHeader("user-agent");
                    String ipAddress = newRequest.getHeader("X-FORWARDED-FOR");
                    if (StringUtils.isBlank(ipAddress)) {
                        ipAddress = newRequest.getRemoteAddr();
                    }

                    int counter = 0;
                    if (newRequest.getAttribute("url") == null) {
                        newRequest.setAttribute("url", "NA");
                    }
                    for (int i = 0; i < newRequest.getAttribute("url").toString().length(); i++) {
                        if (newRequest.getAttribute("url").toString().charAt(i) == '/') {
                            counter++;
                        }
                    }

                    String msisdn = Util.getPhoneFromHeaders(newRequest);
                    if (msisdn == null) {
                        msisdn = ipAddress;
                    }

                    if (msisdn == null) {
                        if (newRequest.getSession() == null || newRequest.getSession().getAttribute("UUID") == null) {
                            msisdn = UUID.randomUUID().toString();
                            newRequest.getSession(true).setAttribute("UUID", msisdn);
                        } else {
                            msisdn = newRequest.getSession().getAttribute("UUID").toString();
                        }
                    } else {
                        msisdn = Criptografia.criptografarAES(msisdn);
                    }
                    if (StringUtils.isNotEmpty(msisdn)) {
                        analyticsUrl = analyticsUrl.replace(TRACKING_ID_PARAMETER, analyticsId);
                        analyticsUrl = analyticsUrl.replace(CLIENT_ID_PARAMETER, msisdn);
                        analyticsUrl = analyticsUrl.replace(PAGE_VIEW_TYPE_PARAMETER, newType);
                        analyticsUrl = analyticsUrl.replace(CATEGORY_PARAMETER, newCategory.replace(" ", "%20"));
                        analyticsUrl = analyticsUrl.replace(ACTION_PARAMETER, StringUtils.isNotEmpty(newAction) ? newAction : "NA");
                        String newLabel = newLabel2 == null ? "NA" : newLabel2;
                        analyticsUrl = analyticsUrl.replace(LABEL_PARAMETER, newLabel);
                        analyticsUrl = analyticsUrl.replace(IP_PARAMETER, ipAddress);
                        analyticsUrl = analyticsUrl.replace(HOST_NAME_PARAMETER, HOST_NAME_VALUE);
                        analyticsUrl = analyticsUrl.replace(PAGE_PARAMETER, "/" + mapToString(newRequest.getParameterMap()));
                        analyticsUrl = analyticsUrl.replace(USER_AGENT_PARAMETER, userAgent.replace("/", "%2F").replace(" ", "%20"));

                        try {
                            analyticsUrl = analyticsUrl.replaceAll(" ", "");
                            requestAnalitycs(analyticsUrl, userInterface);
                        } catch (Exception ex) {
                            SYSTEM_LOGGER.error("Incorrect url used for analytics = " + analyticsUrl);
                            EXCEPTION_LOGGER.error("Incorrect url used for analytics = " + analyticsUrl);
                            return;
                        }
                    }
                } catch (IndexOutOfBoundsException ex) {
                    SYSTEM_LOGGER.error("Url could not be informed to google analytics", ex);
                    EXCEPTION_LOGGER.error("Url could not be informed to google analytics", ex);
                    return;
                } catch (NullPointerException ex) {
                    SYSTEM_LOGGER.error("Msisdn is null, URL could not be informed to google analytics ", ex);
                    EXCEPTION_LOGGER.error("Something is null, URL could not be informed to google analytics , ", ex);
                    return;
                }
                return;
//            }
//        }).start();
    }

    public static void requestAnalitycs(String srtUrl, String userInterface) throws MalformedURLException, IOException {
        StopWatch chronometer = new StopWatch();
        chronometer.start();
        REQUEST_PERFORMED.info("RequestAnalitycs url: " + srtUrl);

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(srtUrl).openConnection();
            conn.setRequestProperty("Content-type", "text/xml;charset=UTF-8");
            conn.setRequestMethod("GET");
            conn.setReadTimeout(140000);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            REQUEST_PERFORMED.info("Response url: " + srtUrl + " Responsecode: " + conn.getResponseCode() + " apiUrl ={" + chronometer.getTime() + "} ms, user interface: " + userInterface);
        } finally {
            chronometer.stop();
        }
    }

    public static String mapToString(Map<String, String[]> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("-");
            }
            String value = map.get(key)[0];
            try {
                stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
                stringBuilder.append(":");
                stringBuilder.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }
        return stringBuilder.toString();
    }
}
