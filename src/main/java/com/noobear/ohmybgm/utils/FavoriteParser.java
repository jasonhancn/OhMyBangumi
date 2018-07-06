package com.noobear.ohmybgm.utils;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jason Han
 * @version 2018-07-04 16:03
 **/

@Slf4j
public class FavoriteParser {

    public static Map<String, String> getFavoriteMap(String uid, Map<String, String> cookies) throws IOException {
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("想看", "https://bgm.tv/anime/list/%s/wish");
        typeMap.put("看过", "https://bgm.tv/anime/list/%s/collect");
        typeMap.put("在看", "https://bgm.tv/anime/list/%s/do");
        typeMap.put("搁置", "https://bgm.tv/anime/list/%s/on_hold");
        typeMap.put("抛弃", "https://bgm.tv/anime/list/%s/dropped");
        Map<String, String> favoriteMap = new HashMap<>();
        for (String typeName : typeMap.keySet()) {
            log.info("开始获取<" + typeName + ">列表");
            List<String> favoriteList = getFavoriteList(String.format(typeMap.get(typeName), uid), cookies);
            favoriteList.forEach(p -> favoriteMap.put(p, typeName));
        }
        log.info(favoriteMap.toString());
        log.info("获取收藏列表结束");
        return favoriteMap;
    }

    private static List<String> getFavoriteList(String link, Map<String, String> cookies) throws IOException {
        List<String> favoriteList = new ArrayList<>();
        Document document = Jsoup
                .connect(link)
                .cookies(cookies)
                .get();
        Elements page_inner = document.select(".page_inner");
        int pageCount = 1;
        if (page_inner.size() > 0) {
            Elements pages = page_inner.get(0).select("a.p");
            for (Element e : pages) {
                int pageNum = Integer.valueOf(e.attr("href").split("=")[1]);
                pageCount = pageNum > pageCount ? pageNum : pageCount;
            }
        }
        for (int i = 1; i <= pageCount; i++) {
            String pageLink = link + "?page=" + i;
            log.debug("开始解析第{}/{}页", i, pageCount);
            favoriteList.addAll(parseFavoritePage(pageLink, cookies));
        }
        return favoriteList;
    }

    private static List<String> parseFavoritePage(String link, Map<String, String> cookies) throws IOException {
        List<String> idList = new ArrayList<>();
        Document document = Jsoup
                .connect(link)
                .cookies(cookies)
                .get();
        Element browserItemList = document.getElementById("browserItemList");
        if (browserItemList != null) {
            Elements li = browserItemList.select("li");
            for (Element e : li) {
                idList.add(e.attr("id").replace("item_", ""));
            }
        }
        return idList;
    }

}
