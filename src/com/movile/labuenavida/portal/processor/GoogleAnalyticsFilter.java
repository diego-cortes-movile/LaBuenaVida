package com.movile.labuenavida.portal.processor;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.compera.commons.util.Criptografia;
import com.compera.portal.device.Device;
import com.compera.portal.parser.bean.Item;
import com.compera.portal.processor.Processor;
import com.compera.portal.processor.result.ProcessorResult;
import com.compera.portal.processor.result.SuccessResult;
import com.movile.labuenavida.util.GoogleAnalyticsUtil;
import com.movile.labuenavida.util.Util;

/**
 * @author Peter Escamilla (peter.escamilla@movile.com)
 */
public class GoogleAnalyticsFilter extends Processor {

    private static final Logger EXCEPTION_LOGGER = Logger.getLogger("exception");

    private static final String ANALITYCS_VERSION = "1";
    private static final String ANALITYCS_EVENT_PAGEVIEW = "pageview";
    private static final String ANALITYCS_EVENT_EVENT = "event";

    private static final String SAT_ORIGIN = "sat";

    private static final String USER_ID_PARAMETER = "userid";
    private static final String SEPARATOR_PARAMETER = "&";
    private static final String chat_url = "http://www.google-analytics.com/collect?";

    public static final String WAP_TRACKING_ID_VALUE = "UA-65924440-5";
    
    private static Map<String, String> parametersTitle = new HashMap<String, String>();

    private static ArrayList<String> smartInterfaces = new ArrayList<String>();

    @Override
    public void initiate(Item item) {
        smartInterfaces = new ArrayList<String>();
        smartInterfaces.add("iphone");
        smartInterfaces.add("tablet");
        smartInterfaces.add("xhtml3G");
    	parametersTitle.put("menu", "menu");
    }

    @Override
    public void terminate() {
    }

    @Override
    public ProcessorResult process(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        Device device = (Device)request.getAttribute("device");
        String analyticsId;
        String userInterface;

        if (device == null) {
            userInterface = null;
            analyticsId = WAP_TRACKING_ID_VALUE;
        } else {
            userInterface = device.getUserInterface();
            if (smartInterfaces.contains(device.getUserInterface())) {
                analyticsId = GoogleAnalyticsUtil.SMART_TRACKING_ID_VALUE;
                return new SuccessResult();
            } else {
                analyticsId = WAP_TRACKING_ID_VALUE;
            }
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {

                try {
                    HttpServletRequest httpRequest = (HttpServletRequest) request;
                    String userAgent = request.getHeader("user-agent");
                    String url = httpRequest.getRequestURL().toString();

                    String ipAddress = request.getHeader("X-FORWARDED-FOR");
                    if (StringUtils.isBlank(ipAddress)) {
                        ipAddress = request.getRemoteAddr();
                    }

                    String parametersStr = "";

                    String msisdn = Util.getPhoneFromHeaders(request);
                    if (StringUtils.isEmpty(msisdn)) {
                        msisdn = request.getParameter(USER_ID_PARAMETER);
                    }
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

                    if (httpRequest.getQueryString() != null) {
                        parametersStr = httpRequest.getQueryString();
                    }
                    StringBuilder psb = new StringBuilder();

                    Enumeration<?> parameters = request.getParameterNames();

                    while (parameters.hasMoreElements()) {
                        String name = (String) parameters.nextElement();
                        String value = request.getParameter(name);

                        if (name.equals("chatid")) {
                            psb.append(name).append("=").append(value).append(SEPARATOR_PARAMETER);
                        }
                        if (name.equals("roomid")) {
                            psb.append(name).append("=").append(value).append(SEPARATOR_PARAMETER);
                        }
                    }

                    String[] urlStr = url.split("/");

                    String event = urlStr[urlStr.length - 2];
                    String action = urlStr[urlStr.length - 1];

                    if (event.equals("..")) {
                        event = SAT_ORIGIN;
                    }

                    parametersStr = parametersTitle.get(action) != null ? parametersTitle.get(action) : parametersStr;

                    StringBuilder asb = new StringBuilder();
                    asb.append(chat_url);
                    asb.append("v=").append(ANALITYCS_VERSION).append(SEPARATOR_PARAMETER);
                    asb.append("tid=").append(URLEncoder.encode(analyticsId, "UTF-8")).append(SEPARATOR_PARAMETER);
                    asb.append("cid=").append(msisdn).append(SEPARATOR_PARAMETER);
                    asb.append("ua=").append(userAgent.replace(" ", "%20")).append(SEPARATOR_PARAMETER);

                    if (action.equals("download")) {
                        asb.append("t=").append(ANALITYCS_EVENT_EVENT).append(SEPARATOR_PARAMETER);
                        asb.append("ec=").append(event).append("/").append(action).append(SEPARATOR_PARAMETER);
                        asb.append("ea=").append("public").append(SEPARATOR_PARAMETER);
                    } else {
                        asb.append("t=").append(ANALITYCS_EVENT_PAGEVIEW).append(SEPARATOR_PARAMETER);
                        asb.append("dh=").append(event).append(SEPARATOR_PARAMETER);
                        asb.append("dp=/").append(event).append("/").append(action).append(SEPARATOR_PARAMETER);
                        if (parametersStr.equals("")) {
                            if (psb.toString().equals("")) {
                                asb.append("dt=").append(URLEncoder.encode(action, "UTF-8"));
                            } else {
                                asb.append("dt=").append(URLEncoder.encode(psb.toString(), "UTF-8"));
                            }
                        } else {
                            asb.append("dt=").append(URLEncoder.encode(parametersStr, "UTF-8"));
                        }
                    }

                    if (StringUtils.isNotBlank(ipAddress)) {
                        asb.append(SEPARATOR_PARAMETER).append("uip=")
                                .append(URLEncoder.encode(ipAddress.replace("172.22.4.194, ", "").replace("172.22.4.193, ", ""), "UTF-8"));
                    }

                    Util.doRequestAnalytics(asb.toString(), userInterface);
                } catch (Exception e) {
                    EXCEPTION_LOGGER.error("error sending analytics", e);
                }
//            }
//        }).start();

        return new SuccessResult();
    }
}
