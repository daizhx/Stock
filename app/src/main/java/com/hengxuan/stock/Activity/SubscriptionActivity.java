package com.hengxuan.stock.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hengxuan.stock.R;
import com.hengxuan.stock.SubActivity;
import com.hengxuan.stock.R.drawable;
import com.hengxuan.stock.R.id;
import com.hengxuan.stock.R.layout;
import com.hengxuan.stock.R.string;
import com.hengxuan.stock.application.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SubscriptionActivity extends SubActivity {
	
	private TextView menu0;
	private TextView menu1;
	private TextView menu2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setTitle(getString(string.subscription_center));
		setContentView(R.layout.activity_subscription);
		menu0 = (TextView) findViewById(R.id.menu0);
		menu1 = (TextView) findViewById(R.id.menu1);
		menu2 = (TextView) findViewById(R.id.menu2);
		menu0.setText(Html.fromHtml("每个交易日9点半之前，获取每日精选3股"));
		menu0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPaySelector();
			}
		});
		menu1.setText(Html.fromHtml("解锁全部<font color=red>冲</font>、<font color=red>仓</font>信号指标，优先获得" +
				"实时精选，比免费用户提前<font color=red>3-5</font>分钟。更多更快的精选股，终身使用"));
		menu2.setText(Html.fromHtml("解锁全部机构买入股，获得机构实时买入信息，" +
				"提前<font color=red>3-5</font>分钟，让你全面获取股市最强涨幅信息，终身使用"));
		
		menu1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPaySelector();
			}
		});
		
	}
	

	
}
