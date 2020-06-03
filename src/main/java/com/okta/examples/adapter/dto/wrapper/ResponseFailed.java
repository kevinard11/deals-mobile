package com.okta.examples.adapter.dto.wrapper;

import com.okta.examples.adapter.clock.Timestamps;
import org.json.simple.JSONObject;

public class ResponseFailed {
    public static JSONObject wrap500(JSONObject obj, String message, String path){
        JSONObject json = new JSONObject();
        json.put("timestamp", Timestamps.getNow());
        json.put("status", Integer.valueOf(500));
        json.put("data", obj);
        json.put("message", message);
        json.put("path", path);

        return json;
    }

    public static JSONObject wrap404(JSONObject obj, String message, String path){
        JSONObject json = new JSONObject();
        json.put("timestamp", Timestamps.getNow());
        json.put("status", Integer.valueOf(404));
        json.put("data", obj);
        json.put("message", message);
        json.put("path", path);

        return json;
    }

    public static JSONObject wrap400(JSONObject obj, String message, String path){
        JSONObject json = new JSONObject();
        json.put("timestamp", Timestamps.getNow());
        json.put("status", Integer.valueOf(400));
        json.put("data", obj);
        json.put("message", message);
        json.put("path", path);

        return json;
    }

    public static JSONObject wrap403(JSONObject obj, String message, String path){
        JSONObject json = new JSONObject();
        json.put("timestamp", Timestamps.getNow());
        json.put("status", Integer.valueOf(403));
        json.put("data", obj);
        json.put("message", message);
        json.put("path", path);

        return json;
    }

}
