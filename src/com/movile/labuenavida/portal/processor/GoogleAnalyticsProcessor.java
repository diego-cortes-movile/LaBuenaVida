/**
 * 
 */
package com.movile.labuenavida.portal.processor;

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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

import com.compera.commons.util.Criptografia;
import com.compera.portal.device.Device;
import com.compera.portal.parser.bean.Item;
import com.compera.portal.processor.Processor;
import com.compera.portal.processor.result.ProcessorResult;
import com.compera.portal.processor.result.SuccessResult;
import com.movile.labuenavida.util.Util;

/**
 * This processor will be notifying to Google analytics about users actions
 * @author Carlos Rodriguez
 * @modified by Peter Escamilla (peter.escamilla@movile.com)
 */
public class GoogleAnalyticsProcessor extends Processor {

    private static final Logger SYSTEM_LOGGER = Logger.getLogger("system");
    private static final Logger EXCEPTION_LOGGER = Logger.getLogger("exception");
    private static final Logger REQUEST_PERFORMED = Logger.getLogger("request_performed");

    /**
     * Google analytics URL
     */
    private static final String ANALITICS_URL = "http://www.google-analytics.com/collect?v=1&tid=${TrackingId}&cid=${ClientId}&t=${PageviewType}&dh=${HostName}&dp=${Page}&uip=${Ip}";
    private static final String TRACKING_ID_PARAMETER = "${TrackingId}";
    private static final String CLIENT_ID_PARAMETER = "${ClientId}";
    private static final String PAGE_VIEW_TYPE_PARAMETER = "${PageviewType}";
    private static final String HOST_NAME_PARAMETER = "${HostName}";
    private static final String PAGE_PARAMETER = "${Page}";
    private static final String IP_PARAMETER = "${Ip}";


    
    /**
     * Google analytics identifier for smartphones
     */
    private static final String SMART_TRACKING_ID_VALUE = "UA-65924440-4";
    
    /**
     * Google analytics identifier for wap
     */
    private static final String WAP_TRACKING_ID_VALUE = "UA-65924440-5";

    /**
     * Page view
     */
    private static final String PAGE_VIEW_VALUE = "pageview";

    /**
     * Host name
     */
    private static final String HOST_NAME_VALUE = "futbol.movilnet.com.ve/descargas";

    private static ArrayList<String> smartInterfaces = new ArrayList<String>();

    /*
     * (non-Javadoc)
     * @see com.compera.portal.processor.Processor#initiate(com.compera.portal.parser.bean.Item)
     */
    @Override
    public void initiate(Item item) {
        smartInterfaces = new ArrayList<String>();
        smartInterfaces.add("iphone");
        smartInterfaces.add("tablet");
    }

    /*
     * (non-Javadoc)
     * @see com.compera.portal.processor.Processor#terminate()
     */
    @Override
    public void terminate() {
    }

    /*
     * (non-Javadoc)
     * @see com.compera.portal.processor.Processor#process(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ProcessorResult process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Device device = (Device)request.getAttribute("device");
        String analyticsId;
        if (smartInterfaces.contains(device.getUserInterface())) {
            analyticsId = SMART_TRACKING_ID_VALUE;
        } else {
            analyticsId = WAP_TRACKING_ID_VALUE;
        }

        SYSTEM_LOGGER.info("Connecting with analytics to share usage data");
        String analyticsUrl = ANALITICS_URL;
        
        String ipAddress = request.getHeader("X-FORWARDED-FOR");  
        if (StringUtils.isBlank(ipAddress)) {  
            ipAddress = request.getRemoteAddr();  
        }
        
        try {
            String requestUrl = request.getAttribute("url").toString().substring(request.getAttribute("url").toString().indexOf("/") + 1, request.getAttribute("url").toString().lastIndexOf("/"));
            String msisdn = Util.getPhoneFromHeaders(request);
            if (msisdn == null) {
                msisdn = ipAddress;
            }
            if (msisdn == null) {
                if (request.getSession().getAttribute("UUID") == null) {
                    msisdn = UUID.randomUUID().toString();
                    request.getSession().setAttribute("UUID", msisdn);
                } else {
                    msisdn = request.getSession().getAttribute("UUID").toString();
                }
            } else {
                msisdn = Criptografia.criptografarAES(msisdn);
            }
            if (StringUtils.isNotEmpty(msisdn) && !requestUrl.contains("wap/view")) {
                analyticsUrl = analyticsUrl.replace(TRACKING_ID_PARAMETER, analyticsId);
                analyticsUrl = analyticsUrl.replace(CLIENT_ID_PARAMETER, msisdn);
                analyticsUrl = analyticsUrl.replace(IP_PARAMETER, ipAddress);
                analyticsUrl = analyticsUrl.replace(PAGE_VIEW_TYPE_PARAMETER, PAGE_VIEW_VALUE);
                analyticsUrl = analyticsUrl.replace(HOST_NAME_PARAMETER, HOST_NAME_VALUE);
                analyticsUrl = analyticsUrl.replace(PAGE_PARAMETER, requestUrl+"/"+mapToString(request.getParameterMap()));
                SYSTEM_LOGGER.info("Analytics URL = " + analyticsUrl);
        
                try {
                    requestAnalitycs(analyticsUrl);
                } catch (MalformedURLException ex) {
                    SYSTEM_LOGGER.error("Incorrect url used for analytics = " + analyticsUrl);
                    EXCEPTION_LOGGER.error("Incorrect url used for analytics = " + analyticsUrl);
                }
            }
        } catch (IndexOutOfBoundsException ex) {
            SYSTEM_LOGGER.error("Url could not be informed to google analytics ",ex);
        } catch (NullPointerException ex) {
            SYSTEM_LOGGER.error("Msisdn is null, URL could not be informed to google analytics ",ex);
        }
        return new SuccessResult();
    }

    public void requestAnalitycs(String srtUrl) throws MalformedURLException, IOException {
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

            REQUEST_PERFORMED.info("Response url: " + srtUrl + " Responsecode: " + conn.getResponseCode() + " apiUrl ={" + chronometer.getTime() + "} ms");
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