package com.noobear.ohmybgm.config;

import org.aeonbits.owner.ConfigFactory;

/**
 * @author Jason Han
 * @version 2018-07-06 12:58
 **/

public class Neo4jBuilder {

    public static Neo4jConfig build() {
        return ConfigFactory.create(Neo4jConfig.class);
    }

}
