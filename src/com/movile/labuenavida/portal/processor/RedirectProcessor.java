package com.movile.labuenavida.portal.processor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.compera.portal.parser.bean.Item;
import com.compera.portal.processor.Processor;
import com.compera.portal.processor.result.ProcessorResult;
import com.compera.portal.processor.result.SuccessResult;

public class RedirectProcessor extends Processor {

    enum DeviceType {
        SMARTPHONE, WEB, FEATUREPHONE, IPHONE, IPAD, ANDROID, TABLET_ANDROID, GOOGLE_BOT_MOBILE, BLACKBERRY, FACEBOOK_BOT, WPHONE
    }

    @Override
    public void initiate(Item item) {

    }

    @Override
    public void terminate() {

    }

    @Override
    public ProcessorResult process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProcessorResult result = new SuccessResult();

//        Device device = (Device) request.getAttribute("device");
//        DeviceType deviceType = getDeviceType(device.getUserAgent());
//        if (deviceType == DeviceType.WEB) {
//            result = new RedirectResult("http://padres.movilnet.com.ve/padres/index.html");
//        }
        return result;
    }

    public static DeviceType getDeviceType(String userAgent) {

        if (userAgent == null) {
            userAgent = "Mozilla";
        } else {
            userAgent = userAgent.toLowerCase();
        }

        DeviceType device = DeviceType.FEATUREPHONE;

        if (userAgent.indexOf("iphone") > -1 || userAgent.indexOf("ipod") > -1) {
            device = DeviceType.IPHONE;
        } else if (userAgent.indexOf("ipad") > -1) {
            device = DeviceType.IPAD;

        } else if (userAgent.indexOf("android") > -1) {
            float version = getAndroidVersion(userAgent);

            if (version >= 3.0f) {
                device = DeviceType.ANDROID;
            } else {
                device = DeviceType.FEATUREPHONE;
            }

            if (userAgent.toLowerCase().indexOf("mobile") < 0) {
                device = DeviceType.TABLET_ANDROID;
            }
        } else if (userAgent.indexOf("googlebot-mobile") > -1) {
            device = DeviceType.GOOGLE_BOT_MOBILE;
        } else if (userAgent.toUpperCase().indexOf("BLACKBERRY") > -1 || userAgent.toUpperCase().indexOf("BB10") > -1) {
            device = DeviceType.BLACKBERRY;
        } else if (userAgent.indexOf("facebookexternalhit") > -1) {
            device = DeviceType.FACEBOOK_BOT;
        } else if (userAgent.indexOf("windows phone") > -1) {
            device = DeviceType.WPHONE;
        } else if ((userAgent.indexOf("windows") > -1) || (userAgent.indexOf("mac") > -1) || (userAgent.indexOf("linux") > -1)) {
            device = DeviceType.WEB;
        } else {
            device = DeviceType.FEATUREPHONE;
        }

        return device;
    }

    private static float getAndroidVersion(String userAgent) {
        float version = 2.1f;

        try {
            int position = userAgent.indexOf("android");
            version = Float.parseFloat(userAgent.substring(position + 8, position + 11));
        } catch (Exception e) {
        }

        return version;
    }

}
