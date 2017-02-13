package com.movile.mob.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.movile.mob.to.Device;

public class DeviceIdentifier {
    private final static String USER_AGENT_ATTRIBUTE = "User-Agent";

    public static Device id(HttpServletRequest request) {

        String userAgent = request.getHeader(USER_AGENT_ATTRIBUTE);

        if (userAgent == null) {
            userAgent = "Mozilla";
        } else {
            userAgent = userAgent.toLowerCase();
        }

        Device device = new Device();

        if (userAgent.indexOf("iphone") > -1 || userAgent.indexOf("ipod") > -1) {
            int iosVersion = getIOSVersion(userAgent);
            if (iosVersion == 5) {
                device.setPlatform(Device.Platform.IPHONE_5);
            } else if (iosVersion == 4) {
                device.setPlatform(Device.Platform.IPHONE_4);
            } else {
                device.setPlatform(Device.Platform.IPHONE_3);
            }
        } else if (userAgent.indexOf("ipad") > -1) {
            if (getIOSVersion(userAgent) == 4) {
                device.setPlatform(Device.Platform.IPAD_4);
            } else {
                device.setPlatform(Device.Platform.IPAD_3);
            }

        } else if (userAgent.indexOf("android") > -1) {
            float version = getAndroidVersion(userAgent);

            if (version >= 3.0f) {
                device.setPlatform(Device.Platform.ANDROID_3_0);
            } else if (version >= 2.2f && version < 3.0f) {
                device.setPlatform(Device.Platform.ANDROID_2_2);
            } else if (version == 2.1f) {
                device.setPlatform(Device.Platform.ANDROID_2_1);
            } else {
                device.setPlatform(Device.Platform.FEATURE_PHONE);
            }

        } else if (userAgent.indexOf("googlebot-mobile") > -1) {
            device.setPlatform(Device.Platform.GOOGLE_BOT_MOBILE);

        } else if (userAgent.indexOf("facebookexternalhit") > -1) {
            device.setPlatform(Device.Platform.FACEBOOK_BOT);

        } else if ((userAgent.indexOf("windows") > -1) || (userAgent.indexOf("mac") > -1) || (userAgent.indexOf("linux") > -1)) {
            device.setPlatform(Device.Platform.WEB);
        } else {
            device.setPlatform(Device.Platform.FEATURE_PHONE);
        }

        return device;

    }

    private static int getIOSVersion(String userAgent) {
        int version = 3;

        try {
            Matcher matcher = Pattern.compile("(?!cpu\\s.*\\bos)\\s[0-9_]+\\s(?=like)").matcher(userAgent);
            if (matcher.find()) {
                char iosMajor = matcher.group().trim().charAt(0);
                if (iosMajor == '4') {
                    version = 4;
                } else if (iosMajor == '5' || iosMajor == '6') {
                    version = 5;
                }
            }
        } catch (Exception e) {
        }

        return version;
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
