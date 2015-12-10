package com.hengxuan.stock.http;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/10/26.
 */
public class HttpJsonParser {
    JSONObject response;
    public HttpJsonParser(JSONObject response){
        this.response = response;
    }

    public String getCode(){
        try {
            String code = (String) response.get("code");
            return code;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getMsg(){
        try {
            String code = (String) response.get("msg");
            return code;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getObject(){
        try {
            return  response.get("object");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
