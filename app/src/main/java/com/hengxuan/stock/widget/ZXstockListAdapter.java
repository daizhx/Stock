package com.hengxuan.stock.widget;

import android.content.ContentProvider;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hengxuan.stock.DatabaseHelper;
import com.hengxuan.stock.R;
import com.hengxuan.stock.data.ZXGData;
import com.hengxuan.stock.http.HttpUtils;
import com.hengxuan.stock.utils.Log;
import com.hengxuan.stock.utils.StockFormula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2015/9/22.
 */
public class ZXstockListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Map<String,Object>> data;
    private MyHScrollView headScroller;
//    private ArrayList<String[]> zxgList;

    class OnScrollChangedListenerImp implements MyHScrollView.OnScrollChangedListener {
        MyHScrollView mScrollViewArg;

        public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
            mScrollViewArg = scrollViewar;
        }

        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
            mScrollViewArg.smoothScrollTo(l, t);
        }
    };

    public ZXstockListAdapter(Context context,ArrayList<Map<String,Object>> data){
        this.context = context;
        this.data = data;
    }

    public ZXstockListAdapter(Context context,ArrayList<Map<String,Object>> data,MyHScrollView headScroller){
        this.context = context;
        this.data = data;
        this.headScroller = headScroller;
    }

//    public ZXstockListAdapter(Context context,ArrayList<String[]> data,MyHScrollView headScroller){
//        this.context = context;
//        this.zxgList = data;
//        this.headScroller = headScroller;
//    }

    public ArrayList getData(){
        return data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.zxg_list_item, null, false);
            holder = new Holder();
            holder.myHScrollView = (MyHScrollView) convertView.findViewById(R.id.scrollView);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.current = (TextView) convertView.findViewById(R.id.current);
            holder.increase_ratio = (TextView) convertView.findViewById(R.id.increase_ratio);
            holder.increase_amount = (TextView) convertView.findViewById(R.id.increase_amount);
            holder.increase_speed = (TextView) convertView.findViewById(R.id.increase_speed);
            holder.trade_volume = (TextView) convertView.findViewById(R.id.trade_volume);
            holder.trade_percent = (TextView) convertView.findViewById(R.id.trade_ratio);
            holder.trade_ratio = (TextView) convertView.findViewById(R.id.trade_ratio);
            holder.price_to_earning_ratio = (TextView) convertView.findViewById(R.id.price_to_earning_ratio);
            holder.price_to_book_ratio = (TextView) convertView.findViewById(R.id.price_to_book_ratio);
            holder.opening_price = (TextView) convertView.findViewById(R.id.opening_price);
            holder.closing_price = (TextView) convertView.findViewById(R.id.closing_price);
            holder.max_price = (TextView) convertView.findViewById(R.id.max_price);
            holder.min_price = (TextView) convertView.findViewById(R.id.min_price);
            holder.wb = (TextView) convertView.findViewById(R.id.wb);
            holder.zf = (TextView) convertView.findViewById(R.id.zf);
            holder.circulation_market_value = (TextView) convertView.findViewById(R.id.circulation_market_value);
            holder.total_market_value = (TextView) convertView.findViewById(R.id.total_market_value);

            headScroller.AddOnScrollChangedListener(new OnScrollChangedListenerImp(holder.myHScrollView));
            holder.myHScrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    headScroller.onTouchEvent(event);
                    return false;
                }
            });
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        Map map = data.get(position);
        bindData(holder,map);

        return convertView;
    }




    class Holder{
        MyHScrollView myHScrollView;
        TextView name;
        TextView code;
        TextView current;
        TextView increase_ratio;
        TextView increase_amount;
        TextView increase_speed;
        TextView trade_volume;
        TextView trade_percent;
        TextView trade_ratio;
        TextView price_to_earning_ratio;
        TextView price_to_book_ratio;
        TextView opening_price;
        TextView closing_price;
        TextView max_price;
        TextView min_price;
        TextView wb;
        TextView zf;
        TextView circulation_market_value;
        TextView total_market_value;
    }

    private void bindData(Holder holder,Map map){
        String name = (String) map.get(ZXGData.NAME);
        String code = (String) map.get(ZXGData.CODE);
        if(name != null && code != null) {
            holder.name.setText(name);
            holder.code.setText(code);
        }
        String str = (String) map.get(ZXGData.CURRENT);
        if(str != null) {
            float current = Float.valueOf(str);
            float close_price = (float) Float.parseFloat((String) map.get(ZXGData.CLOSING_PRICE));
            holder.current.setText(String.valueOf(map.get(ZXGData.CURRENT)));
            if (current > close_price) {
                holder.current.setTextColor(Color.RED);
            } else if (current < close_price) {
                holder.current.setTextColor(Color.GREEN);
            }
            float increase_ratio = (float) Float.parseFloat((String) map.get(ZXGData.INCREASE_RATIO));
            holder.increase_ratio.setText(String.valueOf(increase_ratio) + "%");
            if (increase_ratio > 0) {
                holder.increase_ratio.setTextColor(Color.RED);
            } else if (increase_ratio < 0) {
                holder.increase_ratio.setTextColor(Color.GREEN);
            }
            float increase_amount = (float) Float.parseFloat((String) map.get(ZXGData.INCREASE_AMOUNT));
            holder.increase_amount.setText(String.valueOf(increase_amount));
            if (increase_amount > 0) {
                holder.increase_amount.setTextColor(Color.RED);
            } else if (increase_amount < 0) {
                holder.increase_amount.setTextColor(Color.GREEN);
            }
            holder.increase_speed.setText(map.get(ZXGData.INCREASE_SPEED) + "%");
            holder.trade_volume.setText(map.get(ZXGData.TRADE_VOLUME) + "Эђ");
            holder.trade_percent.setText(map.get(ZXGData.TRADE_PERCENT) + "%");
            String lb = (String) map.get(ZXGData.VOLUMN_RATIO);
            if (lb != null) {
                float volumn_ratio = (float) Float.parseFloat(lb);
                holder.trade_ratio.setText(String.valueOf(volumn_ratio) + "%");
                if (volumn_ratio > 1) {
                    holder.trade_ratio.setTextColor(Color.RED);
                } else if (volumn_ratio < 1) {
                    holder.trade_ratio.setTextColor(Color.GREEN);
                }
            }
            holder.price_to_earning_ratio.setText((String) map.get(ZXGData.PRICE_TO_EARNING_RATIO));
            holder.price_to_book_ratio.setText((CharSequence) map.get(ZXGData.PRICE_TO_BOOK_RATIO));
            float opening_price = (float) Float.parseFloat((String) map.get(ZXGData.OPENING_PRICE));
            holder.opening_price.setText(String.valueOf(opening_price));
            if (opening_price > close_price) {
                holder.opening_price.setTextColor(Color.RED);
            } else if (opening_price < close_price) {
                holder.opening_price.setTextColor(Color.GREEN);
            }
            holder.closing_price.setText((CharSequence) map.get(ZXGData.CLOSING_PRICE));
            float max_price = (float) Float.parseFloat((String) map.get(ZXGData.MAX_PRICE));
            holder.max_price.setText(String.valueOf(map.get(ZXGData.MAX_PRICE)));
            if (max_price > close_price) {
                holder.max_price.setTextColor(Color.RED);
            } else if (max_price < close_price) {
                holder.max_price.setTextColor(Color.GREEN);
            }
            float min_price = (float) Float.parseFloat((String) map.get(ZXGData.MIN_PRICE));
            holder.min_price.setText(String.valueOf(map.get(ZXGData.MIN_PRICE)));
            if (min_price > close_price) {
                holder.min_price.setTextColor(Color.RED);
            } else if (min_price < close_price) {
                holder.min_price.setTextColor(Color.GREEN);
            }
            float wb = (float) Float.parseFloat((String) map.get(ZXGData.WB));
            holder.wb.setText(String.valueOf(wb * 100) + "%");
            //TODO how to decide color
            holder.zf.setText(map.get(ZXGData.ZF) + "%");
            holder.circulation_market_value.setText(map.get(ZXGData.CIRCULATION_MARKET_VALUE) + "вк");
            holder.total_market_value.setText(map.get(ZXGData.TOTAL_MARKET_VALUE) + "вк");
        }
    }
}
