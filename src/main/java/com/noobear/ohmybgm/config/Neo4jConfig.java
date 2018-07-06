package com.noobear.ohmybgm.config;

import org.aeonbits.owner.Config;

/**
 * @author Jason Han
 * @version 2018-07-06 12:57
 **/

@Config.Sources({
        "file:./neo4j.properties",
        "classpath:./neo4j.properties"
})
public interface Neo4jConfig extends Config {
    @Config.Key("host")
    @Config.DisableFeature({Config.DisableableFeature.PARAMETER_FORMATTING})
    @Config.DefaultValue("")
    String host();

    @Config.Key("port")
    @Config.DisableFeature({Config.DisableableFeature.PARAMETER_FORMATTING})
    @Config.DefaultValue("7687")
    String port();

    @Config.Key("username")
    @Config.DisableFeature({Config.DisableableFeature.PARAMETER_FORMATTING})
    @Config.DefaultValue("neo4j")
    String username();

    @Config.Key("password")
    @Config.DisableFeature({Config.DisableableFeature.PARAMETER_FORMATTING})
    @Config.DefaultValue("neo4j")
    String password();
}
