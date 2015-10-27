package com.hengxuan.stock;

import com.hengxuan.stock.Activity.PayActivity;
import com.hengxuan.stock.application.Constants;
import com.hengxuan.stock.utils.Log;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * all the activity extend this activity except main activity
 * @author daizhixin
 */
public class SubActivity extends FragmentActivity {
	private ActionBar mActionBar;
	private ImageView icBack;
	private TextView mTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActionBar = getActionBar();
		mActionBar.setCustomView(R.layout.custom_action_bar);
		icBack = (ImageView) findViewById(R.id.ic_back);
		icBack.setVisibility(View.VISIBLE);
		icBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitle = (TextView) mActionBar.getCustomView().findViewById(R.id.title);
	}
	
	public void setTitle(String s){
		mTitle.setText(s);
	}

    protected void showPaySelector(){
        ListView listView = new ListView(this);
        List<Map<String,Object>> data = new ArrayList();
        Map map = new HashMap();
        map.put("icon", R.drawable.zfb);
        map.put("text", getString(R.string.zfbzf));
        data.add(map);
        Map map2 = new HashMap();
        map2.put("icon", R.drawable.wx);
        map2.put("text", getString(R.string.wxzf));
        data.add(map2);

        listView.setAdapter(new SimpleAdapter(this, data, R.layout.pay_selector_item, new String[]{"icon", "text"}, new int[]{R.id.icon, R.id.text}));
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(R.string.pay_hint).setView(listView).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }).create();
        alertDialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SubActivity.this, PayActivity.class);
                if (position == 0) {
                    intent.putExtra("type", Constants.PAY_TYPE_ZFB);
                } else if (position == 1) {
                    intent.putExtra("type", Constants.PAY_TYPE_WX);
                }
                startActivity(intent);
                if(alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });
    }
}
