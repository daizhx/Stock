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
		menu0.setText(Html.fromHtml("ÿ��������9���֮ǰ����ȡÿ�վ�ѡ3��"));
		menu0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPaySelector();
			}
		});
		menu1.setText(Html.fromHtml("����ȫ��<font color=red>��</font>��<font color=red>��</font>�ź�ָ�꣬���Ȼ��" +
				"ʵʱ��ѡ��������û���ǰ<font color=red>3-5</font>���ӡ��������ľ�ѡ�ɣ�����ʹ��"));
		menu2.setText(Html.fromHtml("����ȫ����������ɣ���û���ʵʱ������Ϣ��" +
				"��ǰ<font color=red>3-5</font>���ӣ�����ȫ���ȡ������ǿ�Ƿ���Ϣ������ʹ��"));
		
		menu1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPaySelector();
			}
		});
		
	}
	

	
}
