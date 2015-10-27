package com.hengxuan.stock.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hengxuan.stock.R;
import com.hengxuan.stock.R.id;
import com.hengxuan.stock.R.layout;
import com.hengxuan.stock.R.string;
import com.hengxuan.stock.SubActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SettingsActivity extends SubActivity {
	List<Map<String,String>> list;
	String[] keys=new String[]{
		"subject","hint"	
	};
	
	@SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		if(list == null){
			list = new ArrayList<Map<String,String>>();
			HashMap<String, String> m = new HashMap<String, String>();
			m.put(keys[0], getString(R.string.subscription_center));
			list.add(m);
			m = new HashMap<String, String>();
			m.put(keys[0], getString(R.string.update));
			m.put(keys[1], getString(R.string.version_hint)+": "+"1.0.0");
			list.add(m);
			m = new HashMap<String, String>();
			m.put(keys[0], getString(R.string.about));
			list.add(m);
			m = new HashMap<String, String>();
			m.put(keys[0], getString(R.string.log_out));
			list.add(m);
		}
		setContentView(R.layout.fragment_user_setting);
		ListView listview = (ListView) findViewById(R.id.list);
		listview.setAdapter(new SimpleAdapter(this, list, R.layout.user_setting_item, keys, new int[]{R.id.text,R.id.second_text}));
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					startActivity(new Intent(SettingsActivity.this,SubscriptionActivity.class));
					break;

				default:
					break;
				}
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
//			Intent intent = new Intent(this,MainActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			NavUtils.navigateUpTo(this, intent);
			NavUtils.navigateUpFromSameTask(this);
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
