package com.noobear.ohmybgm.utils;

import com.noobear.ohmybgm.entity.Anime;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

/**
 * @author Jason Han
 * @version 2018-07-02 21:36
 **/

@Slf4j
public class PageParser {

    public static Anime getAnimeInfo(String bgmId, Map<String, String> cookies, String status) throws IOException {
        Anime anime = new Anime();
        anime.setBgmId(bgmId);
        Document document = Jsoup
                .connect("https://bgm.tv/subject/" + bgmId)
                .cookies(cookies)
                .get();
        if (document.title().equals("Bangumi 番组计划")) {
            log.debug("排除:" + anime.toString());
            return null;
        }
        anime.setNameJapanese(document.title().replace("| Bangumi 番组计划", "").trim());
        Elements isAnime = document.select(".focus.chl.anime");
        if (isAnime.size() == 0) {
            log.debug("排除:" + anime.toString());
            return null;
        }
        Elements tips = document.select("#infobox").select("li");
        for (Element e : tips) {
            String text = e.text();
            if (text.startsWith("中文名")) {
                anime.setNameChinese(text.replace("中文名:", "").trim());
            }
        }
        if (anime.getNameChinese() == null || "".equals(anime.getNameChinese())) {
            anime.setNameChinese(anime.getNameJapanese());
        }
        Elements rawStatus = document.select(".interest_now");
        if (rawStatus.size() > 0) {
            anime.setStatus(rawStatus.get(0).text()
                    .replace("我", "")
                    .replace("这部动画", ""));
        } else if (status != null && !status.equals("")) {
            anime.setStatus(status);
        } else {
            anime.setStatus("未看");
        }
        Elements relationBlocks = document.select("li.sep");
        for (
                Element relationBlock : relationBlocks)

        {
            String relation = relationBlock.select(".sub").get(0).text();
            for (Element relationDetail : relationBlock.select(".title")) {
                String relationId = relationDetail.attr("href").replace("/subject/", "").trim();
                anime.setRelation(relationId, relation);
            }
        }
        log.debug("动画:" + anime.toString());
        return anime;
    }

}
