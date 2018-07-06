package com.noobear.ohmybgm.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jason Han
 * @version 2018-07-02 21:29
 **/

@Data
public class Anime {
    private String bgmId;
    private String nameJapanese;
    private String nameChinese;
    private String status;
    private Map<String, String> relations = new HashMap<>();

    public void setRelation(String id, String relation) {
        relations.put(id, relation);
    }
}
