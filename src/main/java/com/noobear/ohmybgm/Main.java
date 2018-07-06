package com.noobear.ohmybgm;

import com.noobear.ohmybgm.config.CookiesBuilder;
import com.noobear.ohmybgm.config.UserBuilder;
import com.noobear.ohmybgm.utils.FavoriteParser;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * @author Jason Han
 * @version 2018-06-30 17:33
 **/

@Slf4j
public class Main {

    public static void main(String[] args) {
        Map<String, String> cookies = CookiesBuilder.build();
        String uid = UserBuilder.build();
        Map<String, String> favoriteMap = null;
        try {
            favoriteMap = FavoriteParser.getFavoriteMap(uid, cookies);
        } catch (IOException e) {
            log.error("解析收藏列表失败:" + e.toString());
            System.exit(1);
        }
        new Exec(favoriteMap, cookies).start();
    }

}