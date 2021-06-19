package com.aim.questionnaire.common.utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonToMap {
    public static Map<String,Object> JsonToMap(JSONObject j){
        Map<String,Object> map = new HashMap<>();
        Iterator<String> iterator = j.keys();
        while(iterator.hasNext())
        {
            String key = (String)iterator.next();
            Object value = j.get(key);
            map.put(key, value);
        }
        return map;
    }
}
