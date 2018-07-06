package com.noobear.ohmybgm.utils;

import com.noobear.ohmybgm.entity.Anime;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.v1.*;
import org.slf4j.LoggerFactory;

/**
 * @author Jason Han
 * @version 2018-07-03 21:57
 **/

@Slf4j
public class GraphDB {

    private Driver driver;

    public GraphDB() {
        Config config = Config.build().withLogging(Slf4jLogger::new).toConfig();
        driver = GraphDatabase.driver("bolt://localhost:7687",
                AuthTokens.basic("neo4j", "123456"), config);
    }

    public void deleteAll() {
        Session session = driver.session();
        session.run("MATCH ()-[r]-() DELETE r");
        session.close();
        session = driver.session();
        session.run("MATCH (n) DELETE n");
        session.close();
    }

    public void createAnimeNode(Anime anime) {
        Session session = driver.session();
        String cypher = "CREATE (a:Anime {"
                + "bgmId:{bgmId},"
                + "nameJapanese:{nameJapanese},"
                + "nameChinese:{nameChinese},"
                + "status:{status}"
                + "})";
        session.run(cypher, Values.parameters(
                "bgmId", anime.getBgmId(),
                "nameJapanese", anime.getNameJapanese(),
                "nameChinese", anime.getNameChinese(),
                "status", anime.getStatus()
        ));
        session.close();
    }

    public void createAnimeRelation(String srcId, String dstId, String relation) {
        Session session = driver.session();
        String cypher = "MATCH (a:Anime),(b:Anime) WHERE "
                + "a.bgmId={srcId} AND b.bgmId={dstId} "
                + "CREATE p=(a)-[:Relation{type:{relation}}]->(b)";
        session.run(cypher, Values.parameters(
                "srcId", srcId,
                "dstId", dstId,
                "relation", relation
        ));
        session.close();
    }

    public void close() {
        driver.close();
    }

    private class Slf4jLogger implements Logger {

        private org.slf4j.Logger logger;

        Slf4jLogger(String s) {
            this.logger = LoggerFactory.getLogger("neo4j." + s);
        }

        @Override
        public void error(String s, Throwable throwable) {
            logger.error(s);
            throwable.printStackTrace();
        }

        @Override
        public void info(String s, Object... objects) {
            logger.info(String.format(s, objects));
        }

        @Override
        public void warn(String s, Object... objects) {
            logger.warn(String.format(s, objects));
        }

        @Override
        public void warn(String s, Throwable throwable) {
            logger.warn(s);
            throwable.printStackTrace();
        }

        @Override
        public void debug(String s, Object... objects) {
            // 阻止不受此配置控制的依赖库（如netty）的debug日志
            if (isDebugEnabled()) {
                logger.debug(String.format(s, objects));
            }
        }

        @Override
        public void trace(String s, Object... objects) {
            // 阻止不受此配置控制的依赖库（如netty）的trace日志
            if (isTraceEnabled()) {
                logger.trace(String.format(s, objects));
            }
        }

        @Override
        public boolean isTraceEnabled() {
            return false;
        }

        @Override
        public boolean isDebugEnabled() {
            return false;
        }
    }
}
