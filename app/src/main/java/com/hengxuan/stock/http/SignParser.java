package com.hengxuan.stock.http;

import com.hengxuan.stock.data.SignPoint;
import com.hengxuan.stock.utils.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Administrator on 2015/10/26.
 */
public class SignParser extends HttpJsonParser{
    JSONArray data = null;
    public SignParser(JSONObject response){
        super(response);
        Log.d(response.toString());
        Object object = getObject();
        Log.d(object.toString());
        if(object != null && !object.equals(null)){
            data = (JSONArray) object;
        }
    }

    public int getSignNum(){
        if(data == null)return 0;
        return data.length();
    }

    public SignPoint[] getSignPoints(){
        if(data == null)return null;
        SignPoint[] signPoints = new SignPoint[data.length()];
        for(int i=0;i<data.length();i++) {
            try {
                JSONObject jsonObject = (JSONObject) data.get(i);
                long l = jsonObject.getLong("date");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(l);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                String sign = jsonObject.getString("hintMsg");
                SignPoint signPoint = new SignPoint(hour,minute);
                if(sign.equals("Âò")){
                    signPoint.setSignId(SignPoint.BUY);
                }else if(sign.equals("Âô")){
                    signPoint.setSignId(SignPoint.SELL);
                }

                signPoints[i] = signPoint;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signPoints;
    }
}
