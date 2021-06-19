package com.aim.questionnaire.common.utils;

import com.baidu.aip.nlp.AipNlp;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompareText {
    public static final String APP_ID = "24398881";
    public static final String API_KEY = "fhaB89lGxIp4Tlf3IulfxOGO";
    public static final String SECRET_KEY = "eqFFxEAY245fXaMNqqzkgaXbCcrbrgQR";

    public static Map<String,Object> compareText(Map<String,String> map) {
        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("model", "CNN");
        JSONObject res = client.simnet(map.get("text_1"), map.get("text_2"), options);
        return JsonToMap.JsonToMap(res);
    }
}

