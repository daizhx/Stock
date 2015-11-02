package com.hengxuan.stock.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.hengxuan.stock.R;
import com.hengxuan.stock.utils.Log;
import com.hengxuan.stock.widget.StockMinuteChartView2;

/**
 * Created by Administrator on 2015/9/17.
 */
public class TestActivity extends Activity {

    String[] ss = new String[]{"aaa","bbb","ccc","ddd","aaa","bbb","ccc","ddd","aaa","bbb","ccc","ddd","aaa","bbb","ccc","ddd","aaa","bbb","ccc","ddd","aaa","bbb","ccc","ddd","aaa","bbb","ccc","ddd"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, ss));
//        stockMinuteChartView2.setUpData(20.45,testData);
        ScrollView scrollView;
    }
}
