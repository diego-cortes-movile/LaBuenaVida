package com.movile.mob.to;

public class Device {

    public enum Base {
        IPHONE(true, "iPhone"),
        IPAD(true, "iPad"),
        ANDROID(true, "Android"),
        GOOGLE_BOT_MOBILE(true, "Google Bot Mobile"),
        BLACKBERRY(true, "Blackberry"),
        FACEBOOK_BOT(false, "Facebook Bot"),
        WEB(false, "Web"),
        ZONG(false, "Zong"),
        FEATURE_PHONE(false, "FeaturePhone");

        boolean mobile;

        public boolean isMobile() {
            return mobile;
        }

        String label;

        public String getLabel() {
            return label;
        }

        Base(boolean mobile, String label) {
            this.mobile = mobile;
            this.label = label;
        }
    }

    public enum Platform {
        IPHONE_3(Base.IPHONE, "3"),
        IPHONE_4(Base.IPHONE, "4"),
        IPHONE_5(Base.IPHONE, "5"),
        IPAD_3(Base.IPHONE, "3"),
        IPAD_4(Base.IPAD, "4"),
        ANDROID_2_0_OR_OLDER(Base.ANDROID, "2.0"),
        ANDROID_2_1(Base.ANDROID, "2.1"),
        ANDROID_2_2(Base.ANDROID, "2.2"),
        ANDROID_3_0(Base.ANDROID, "3.0"),
        GOOGLE_BOT_MOBILE(Base.GOOGLE_BOT_MOBILE),
        FACEBOOK_BOT(Base.FACEBOOK_BOT),
        WEB(Base.WEB),
        ZONG(Base.ZONG),
        BLACKBERRY(Base.BLACKBERRY),
        FEATURE_PHONE(Base.FEATURE_PHONE);

        Base base;

        public Base getBase() {
            return base;
        }

        String version;

        public String getVersion() {
            return version;
        }

        Platform(Base base) {
            this.base = base;
        }

        Platform(Base base, String version) {
            this(base);
            this.version = version;
        }
    }

    private Platform platform;

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
}
