package com.noobear.ohmybgm.config;

import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;

/**
 * @author Jason Han
 * @version 2018-07-06 12:33
 **/

@Slf4j
public class UserBuilder {

    public static String build() {
        UserConfig userConfig = ConfigFactory.create(UserConfig.class);
        String uid = userConfig.bgmUid();
        log.info("UID:" + uid);
        return uid;
    }

}
