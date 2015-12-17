package com.hengxuan.stock.http;

import com.hengxuan.stock.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by daizhx on 15/12/17.
 */
public class VersionUpdateParser extends HttpJsonParser{

    JSONObject data = null;

    public VersionUpdateParser(JSONObject response) {
        super(response);
        Object object =  getObject();
        if(object != null && !object.equals(null)){
            data = (JSONObject) object;
        }
    }

    public int getUpdateVersion(){
        if(data != null){
            try {
                String s = (String) data.get("versionCode");
                int versionCode = Integer.parseInt(s);
                return versionCode;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public String getUpdateMsg(){
        if(data != null){
            try {
                String msg = (String) data.get("updateDetail");
                return msg;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getUrl(){
        if(data != null){
            try {
                String url = (String) data.get("softUrl");
                url = HttpAPI.SERVER_ROOT + url;
                return url;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int getUpdateFlag(){
        if(data != null){
            try {
                int flag = (int) data.get("updateFlag");
                return flag;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
}
