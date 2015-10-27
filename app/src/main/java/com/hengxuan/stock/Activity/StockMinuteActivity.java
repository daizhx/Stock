package com.hengxuan.stock.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hengxuan.stock.R;
import com.hengxuan.stock.SubActivity;
import com.hengxuan.stock.data.SignPoint;
import com.hengxuan.stock.http.HttpAPI;
import com.hengxuan.stock.http.HttpUtils;
import com.hengxuan.stock.http.MyJsonObjectRequest;
import com.hengxuan.stock.http.SignParser;
import com.hengxuan.stock.user.User;
import com.hengxuan.stock.utils.Log;
import com.hengxuan.stock.utils.StockFormula;
import com.hengxuan.stock.widget.MinuteAmountView;
import com.hengxuan.stock.widget.StockMinuteChartView2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StockMinuteActivity extends Activity {

    //data
    private String name;
    private String code;
    private String[] data1 = new String[7];
    private float closingPrice;
//    private float[] minutePrices;
//    private int[] minuteVolumn;
    private ArrayList minutePrices = new ArrayList();
    private ArrayList minuteVolumn = new ArrayList();
    //views
    private TextView tvPrice;
    private TextView tvIncrease;
    private TextView tvIncreasePercentage;
    private TextView tvTradeVolumn;
    private TextView tvMaxPrice;
    private TextView tvMinPrice;
    private TextView tvTradeVolumnPercentage;
    private  TextView tvAveragePrice;
    private  TextView tvPrice2;
    private  TextView tvIncreasePercentage2;

    private StockMinuteChartView2 minuteChartView;
    private MinuteAmountView minuteAmountView;

    private static final int DATA1_UPDATE = 1;
    private static final int DATA2_UPDATE = 2;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == DATA1_UPDATE){
                float f = Float.parseFloat(data1[1]);
                if(f > 0){
                    tvIncrease.setTextColor(Color.RED);
                    tvIncreasePercentage.setTextColor(Color.RED);
                    tvPrice.setTextColor(Color.RED);
                }else if(f < 0){
                    tvIncrease.setTextColor(Color.GREEN);
                    tvIncreasePercentage.setTextColor(Color.GREEN);
                    tvPrice.setTextColor(Color.GREEN);
                }
                tvPrice.setText(data1[0]);
                tvIncrease.setText(data1[1]);
                tvIncreasePercentage.setText(data1[2]);

                tvTradeVolumn.setText(data1[3] + "Íò");
                tvTradeVolumnPercentage.setText(data1[4]+"%");

                float max = Float.parseFloat(data1[5]);
                float min = Float.parseFloat(data1[6]);
                if(max > closingPrice){
                    tvMaxPrice.setTextColor(Color.RED);
                }else if(max < closingPrice){
                    tvMaxPrice.setTextColor(Color.GREEN);
                }
                if(min > closingPrice){
                    tvMinPrice.setTextColor(Color.RED);
                }else if(min < closingPrice){
                    tvMinPrice.setTextColor(Color.GREEN);
                }
                tvMaxPrice.setText(data1[5]);
                tvMinPrice.setText(data1[6]);
            }

            if(msg.what == DATA2_UPDATE){
                minuteChartView.setDataAndRefresh(closingPrice, minutePrices);
                minuteAmountView.setDataAndInvalidate(closingPrice,minutePrices,minuteVolumn);
            }
            postDelayed(myThread, 60*1000);
        }
    };

    private MyThread myThread;

    class MyThread extends Thread{
        Context context;
        String code;
        public MyThread(Context context,String c){
            this.context = context;
            code = c;
        }

        @Override
        public void run() {
            HttpUtils httpUtils = HttpUtils.getInstance(context);
            String url = "http://qt.gtimg.cn/q=" + StockFormula.hostExchange(code) + code;
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response != null){
                        String[] ss = response.split("\"");
                        String[] data = ss[1].split("~");
                        closingPrice = Float.parseFloat(data[4]);
                        data1[0] = data[3];
                        data1[1] = data[31];
                        data1[2] = data[32];
                        data1[3] = data[37];
                        data1[4] = data[38];
                        data1[5] = data[33];
                        data1[6] = data[34];
//                        Message msg = handler.obtainMessage(DATA_UPDATE);
                        handler.sendEmptyMessage(DATA1_UPDATE);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("get stock data1 fail");
                }
            });
            String url2 = "http://data.gtimg.cn/flashdata/hushen/minute/" + StockFormula.hostExchange(code) + code + ".js";
            StringRequest stringRequest2 = new StringRequest(url2, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String data = response.split("\"")[1];
                    int start = data.indexOf("\n");
                    String str = data.substring(start + 1);
                    String[] ss = str.split("\n");
                    minutePrices.clear();
                    minuteVolumn.clear();
                    for(int i=1;i<ss.length;i++){
                        String s = ss[i].substring(0,ss[i].length()-3);
                        String[] data2 = s.split(" ");
                        minutePrices.add(Float.parseFloat(data2[1]));
                        minuteVolumn.add(Integer.parseInt(data2[2]));
                    }
//                    for(int i=0;i<minutePrices.size();i++){
//                        Log.i("price="+(float)minutePrices.get(i));
//                    }
                    handler.sendEmptyMessage(DATA2_UPDATE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("get stock data2 fail");
                }
            });
            httpUtils.addToRequestQueue(stringRequest);
            httpUtils.addToRequestQueue(stringRequest2);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        code = intent.getStringExtra("code");
        setContentView(R.layout.activity_stock_minute);
//        getActionBar().setTitle(name);
        setTitle(name);
        tvPrice = (TextView) findViewById(R.id.price);
        tvIncrease = (TextView) findViewById(R.id.tv_increase);
        tvIncreasePercentage = (TextView) findViewById(R.id.tv_percent);
        tvTradeVolumn = (TextView) findViewById(R.id.tv_trade_volume);
        tvMaxPrice = (TextView) findViewById(R.id.tv_max_price);
        tvMinPrice = (TextView) findViewById(R.id.tv_min_price);
        tvTradeVolumnPercentage = (TextView) findViewById(R.id.tv_trade_percent);
        tvAveragePrice = (TextView) findViewById(R.id.average_price);
        tvPrice2 = (TextView) findViewById(R.id.average_price);
        tvIncreasePercentage2 = (TextView) findViewById(R.id.tv_zf);

        minuteChartView = (StockMinuteChartView2) findViewById(R.id.minute_chart);
        minuteAmountView = (MinuteAmountView) findViewById(R.id.amount_chart);
        myThread = new MyThread(this,code);
        myThread.start();

        getBSSign(code);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(myThread != null){
            handler.removeCallbacks(myThread);
            myThread = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_minute, menu);
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
        if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //async get data and draw image in callback
    private void getBSSign(String code){
        if(code == null || code.isEmpty())return;
        HttpUtils httpUtils = HttpUtils.getInstance(this);
        User user = User.getUser(this);
        String username = user.getName();
        String url = HttpAPI.GET_BS_SIGN + "/" + username + "/" + code;
        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("getBSSign:"+response);
                SignParser signParser = new SignParser(response);
                SignPoint[] signPoints = signParser.getSignPoints();
                minuteChartView.setSignBSPoints(signPoints);
                minuteAmountView.invalidate();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
