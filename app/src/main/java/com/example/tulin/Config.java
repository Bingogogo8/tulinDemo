package com.example.tulin;

public class Config {
    private static final String URL_KEY = "https://openapi.tuling123.com/openapi/api/v2";
    private static final String APP_KEY = "99931b9cf87e48f4afeec8860d70cde4";   //此处是你申请的Apikey
    private static final  String USER_ID="684171";
    public static Config instance;

    public Config() {
    }

    public  String getAppKey() {
        return APP_KEY;
    }

    public  String getUrlKey() {
        return URL_KEY;
    }

    public  String getUserId() {
        return USER_ID;
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }
}
