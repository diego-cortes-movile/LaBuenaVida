/*
 * Compera Yavox n�Time (CYNT) - 12/05/2009
 */
package com.movile.labuenavida.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.compera.commons.exception.ExceptionFormatter;
import com.compera.commons.http.HttpClientTimeout;

/**
 * @author audran
 */
public class HttpUtil {

    public static final String IDENTIFIER_PROPERTY = "identifier";

    private static final Logger EXCEPTION_LOGGER = Logger.getLogger("exception");
    private static final Logger REQUEST_PERFORMED_LOGGER = Logger.getLogger("request_performed");
    private static final Logger res = Logger.getLogger("request_answered");
    private static final Logger sys = Logger.getLogger("system");

    private static final int TIMEOUT = 120000;

    public static String doGet(String apiUrl) {
        Date startTime = new Date();

        StringBuilder sb = new StringBuilder();
        REQUEST_PERFORMED_LOGGER.info("Chamando " + apiUrl);

        try {

            HttpClientTimeout httpClient = new HttpClientTimeout(apiUrl, TIMEOUT);
            sb = httpClient.execute();

            Date endTime = new Date();

            REQUEST_PERFORMED_LOGGER.debug("Response " + "status code " + httpClient.getStatusCode() + " " + "(" + (endTime.getTime() - startTime.getTime())
                    + " ms) " + apiUrl);

            String response = null;
            if (httpClient.getStatusCode() >= 400) {
                sys.error("Response status code " + httpClient.getStatusCode() + " GET " + apiUrl);
            } else {
                response = sb.toString();
                res.debug("REQUEST " + apiUrl + "\n RESPONSE " + response);
            }

            return response;
        } catch (Exception e) {
            ExceptionFormatter.format(e, EXCEPTION_LOGGER);
            sys.error("Error " + e.getMessage() + "requesting URL " + apiUrl);
            return "ERRO> ao chamar url " + apiUrl + ": " + e.getMessage();
        }
    }

    public static int getHttpCode(String address) throws Exception {

        return getHttpCode(address, null);
    }

    public static HttpURLConnection getConnection(String address, String userAgent) throws IOException {
        URL url = new URL(address + "?&width=100&height=280");
        sys.debug("Openning connection to " + url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (StringUtils.isNotEmpty(userAgent)) {
            connection.setRequestProperty("User-Agent", userAgent);
        }

        connection.setRequestMethod("GET");
        connection.connect();
        return connection;
    }

    public static int getHttpCode(String address, String userAgent) throws Exception {
        HttpURLConnection connection = getConnection(address, userAgent);

        int code = connection.getResponseCode();
        connection.disconnect();
        return code;
    }

    public static String doJSONPost(String apiUrl, String json) {
        Date startTime = new Date();
        REQUEST_PERFORMED_LOGGER.info("Calling " + apiUrl);

        try {
            HttpClient httpClient = new HttpClient();
            StringRequestEntity requestEntity = new StringRequestEntity(json, "application/json", "UTF-8");

            PostMethod postMethod = new PostMethod(apiUrl);
            postMethod.setRequestEntity(requestEntity);

            int statusCode = httpClient.executeMethod(postMethod);
            Date endTime = new Date();

            REQUEST_PERFORMED_LOGGER
                    .debug("Response " + "status code " + statusCode + " " + "(" + (endTime.getTime() - startTime.getTime()) + " ms) " + apiUrl);

            String response = null;
            if (statusCode >= 400) {
                sys.error("Response status code " + statusCode + " GET " + apiUrl);
            } else {
                response = postMethod.getResponseBodyAsString();
                res.debug("REQUEST " + apiUrl + "\n RESPONSE " + response);
            }

            return response;
        } catch (Exception e) {
            ExceptionFormatter.format(e, EXCEPTION_LOGGER);
            sys.error("Error " + e.getMessage() + "requesting URL " + apiUrl);
            return "ERROR calling url " + apiUrl + ": " + e.getMessage();
        }
    }
}
