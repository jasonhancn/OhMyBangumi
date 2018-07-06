package com.noobear.ohmybgm;

import com.noobear.ohmybgm.entity.Anime;
import com.noobear.ohmybgm.utils.GraphDB;
import com.noobear.ohmybgm.utils.PageParser;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Jason Han
 * @version 2018-07-02 22:48
 **/

@Slf4j
class Exec {

    private Map<String, String> favoriteMap;
    private Map<String, String> cookies;

    Exec(Map<String, String> favoriteMap, Map<String, String> cookies) {
        this.favoriteMap = favoriteMap;
        this.cookies = cookies;
    }

    private Queue<FutureTask<Anime>> animeQueue = new ConcurrentLinkedQueue<>();

    // 最终保留的动画信息
    private List<Anime> animeList = new ArrayList<>();

    // 被解析过的ID
    private Set<String> parsed = Collections.synchronizedSet(new HashSet<>());

    private ExecutorService executor = Executors.newFixedThreadPool(10);

    void start() {
        log.info("开始抓取动画信息");
        for (String id : favoriteMap.keySet()) {
            FutureTask<Anime> futureTask = new FutureTask<>(new Parser(id));
            animeQueue.add(futureTask);
            executor.submit(futureTask);
        }
        FutureTask<Anime> futureTask = animeQueue.poll();
        while (futureTask != null) {
            try {
                Anime anime = futureTask.get();
                if (anime != null) {
                    animeList.add(anime);
                }
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.toString());
            }
            futureTask = animeQueue.poll();
        }
        executor.shutdown();
        log.info("抓取信息完成");
        GraphDB graphDB = new GraphDB();
        log.info("清除历史数据");
        graphDB.deleteAll();
        log.info("开始添加节点");
        animeList.forEach(graphDB::createAnimeNode);
        log.info("开始添加关系");
        Set<String> idSet = new HashSet<>();
        animeList.forEach(p -> idSet.add(p.getBgmId()));
        for (Anime anime : animeList) {
            for (String relationId : anime.getRelations().keySet()) {
                if (idSet.contains(relationId)) {
                    graphDB.createAnimeRelation(anime.getBgmId(), relationId,
                            anime.getRelations().get(relationId));
                }
            }
        }
        log.info("添加完成");
        graphDB.close();
    }

    private class Parser implements Callable<Anime> {
        String id;

        Parser(String id) {
            this.id = id;
        }

        @Override
        public Anime call() {
            if (parsed.contains(id)) {
                return null;
            }
            parsed.add(id);
            Anime anime;
            while (true) {
                try {
                    anime = PageParser.getAnimeInfo(id, cookies, favoriteMap.get(id));
                    break;
                } catch (IOException ignored) {
                    log.error(id + ":连接失败，重试");
                }
            }
            if (anime != null) {
                for (String relationId : anime.getRelations().keySet()) {
                    FutureTask<Anime> relationFutureTask = new FutureTask<>(new Parser(relationId));
                    animeQueue.add(relationFutureTask);
                    executor.submit(relationFutureTask);
                }
            }
            return anime;
        }
    }
}
