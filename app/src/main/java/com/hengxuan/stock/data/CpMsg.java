package com.hengxuan.stock.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/27.
 */
public class CpMsg {
    String msg;
    public CpMsg(String content){
        try {
            JSONObject json = new JSONObject(content);
        } catch (JSONException e) {
            msg = content;
        }

    }

    @Override
    public String toString() {
        return msg;
    }
}
