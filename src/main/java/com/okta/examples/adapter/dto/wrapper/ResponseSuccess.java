package com.okta.examples.adapter.dto.wrapper;

import com.okta.examples.adapter.clock.Timestamps;
import org.json.simple.JSONObject;

public class ResponseSuccess {
    public static JSONObject wrap200(Object data, String message, String path){
        JSONObject json = new JSONObject();
        json.put("timestamp", Timestamps.getNow());
        json.put("status", Integer.valueOf(200));
        json.put("data", data);
        json.put("message", message);
        json.put("path", path);

        return json;
    }

    public static JSONObject wrap201(Object data, String message, String path){
        JSONObject json = new JSONObject();
        json.put("timestamp", Timestamps.getNow());
        json.put("status", Integer.valueOf(201));
        json.put("data", data);
        json.put("message", message);
        json.put("path", path);

        return json;
    }


}
