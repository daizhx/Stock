package com.hengxuan.stock.Activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hengxuan.stock.R;
import com.hengxuan.stock.SubActivity;
import com.hengxuan.stock.http.HttpAPI;
import com.hengxuan.stock.http.HttpJsonParser;
import com.hengxuan.stock.http.HttpResponseParser;
import com.hengxuan.stock.http.HttpUtils;
import com.hengxuan.stock.http.MyJsonObjectRequest;
import com.hengxuan.stock.utils.Log;

import org.json.JSONObject;

public class DpgdActivity extends Activity {

    TextView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpgd);
        content = (TextView) findViewById(R.id.content);
        setContent();
    }

    //set content async
    void setContent(){
        HttpUtils httpUtils = HttpUtils.getInstance(this);
        String url = HttpAPI.GET_DPGD_CONTENT;
        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HttpJsonParser httpJsonParser = new HttpJsonParser(response);
                String s = (String) httpJsonParser.getObject();
                content.setText(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        httpUtils.addToRequestQueue(myJsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_dpgd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
