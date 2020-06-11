package com.okta.examples.adapter.wrapper;

import com.okta.examples.adapter.clock.Timestamps;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    public static ResponseEntity<?> wrapResponseFailed(Object data,
                                                       String message,
                                                       int status,
                                                       HttpStatus httpStatus,
                                                       String path){
        JSONObject json = new JSONObject();
        json.put("timestamp", Timestamps.getNow());
        json.put("status", status);
        json.put("data", data);
        json.put("message", message);
        json.put("path", path);

        return new ResponseEntity<>(json, httpStatus);
    }

}
