package com.okta.examples.adapter.wrapper;

import com.okta.examples.adapter.clock.Timestamps;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    public static ResponseEntity<?> wrapResponseSuccess(Object data, String message, int status, HttpStatus httpStatus, String path){
        JSONObject json = new JSONObject();
        json.put("timestamp", Timestamps.getNow());
        json.put("status", status);
        json.put("data", data);
        json.put("message", message);
        json.put("path", path);

        return new ResponseEntity<>(json, httpStatus);
    }

}
