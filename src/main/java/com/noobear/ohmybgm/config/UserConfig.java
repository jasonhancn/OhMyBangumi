package com.noobear.ohmybgm.config;

import org.aeonbits.owner.Config;

/**
 * @author Jason Han
 * @version 2018-07-04 15:55
 **/

@Config.Sources({
        "file:./user.properties",
        "classpath:./user.properties"
})
public interface UserConfig extends Config {
    @Key("bgm.uid")
    @DisableFeature({DisableableFeature.PARAMETER_FORMATTING})
    String bgmUid();
}
