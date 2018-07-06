package com.noobear.ohmybgm.config;

import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jason Han
 * @version 2018-07-04 15:54
 **/

@Slf4j
public class CookiesBuilder {

    public static Map<String, String> build() {
        CookiesConfig cookiesConfig = ConfigFactory.create(CookiesConfig.class);
        Map<String, String> cookies = new HashMap<>();
        cookies.put("chii_auth", cookiesConfig.chiiAuth());
        cookies.put("chii_cookietime", cookiesConfig.chiiCookietime());
        cookies.put("chii_sid", cookiesConfig.chiiSid());
        log.info("使用Cookies:" + cookies.toString());
        return cookies;
    }

}
