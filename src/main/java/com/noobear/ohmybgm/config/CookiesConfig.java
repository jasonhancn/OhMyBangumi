package com.noobear.ohmybgm.config;

import org.aeonbits.owner.Config;

/**
 * @author Jason Han
 * @version 2018-07-04 15:55
 **/

@Config.Sources({
        "file:./cookies.properties",
        "classpath:./cookies.properties"
})
public interface CookiesConfig extends Config {
    @Key("chii_auth")
    @DisableFeature({DisableableFeature.PARAMETER_FORMATTING})
    @DefaultValue("")
    String chiiAuth();

    @Key("chii_cookietime")
    @DisableFeature({DisableableFeature.PARAMETER_FORMATTING})
    @DefaultValue("0")
    String chiiCookietime();

    @Key("chii_sid")
    @DisableFeature({DisableableFeature.PARAMETER_FORMATTING})
    @DefaultValue("")
    String chiiSid();
}
