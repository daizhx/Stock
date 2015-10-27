package com.hengxuan.stock.Activity;

import java.lang.reflect.Field;

import com.hengxuan.stock.R;
import com.hengxuan.stock.R.id;
import com.hengxuan.stock.R.layout;
import com.hengxuan.stock.R.string;
import com.hengxuan.stock.SubActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class RedeemActivity extends SubActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_redeem);
		verifyPassword();
	}
	
	private void verifyPassword(){
		View contentView =  getLayoutInflater().inflate(R.layout.alert_dialog_edittext, null, false);
		final EditText editText = (EditText) contentView.findViewById(R.id.edit);
		final AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("请输入会员名").setView(contentView).create();
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok), new OnClickListener() {
			
			@TargetApi(Build.VERSION_CODES.GINGERBREAD) @Override
			public void onClick(DialogInterface dialog, int which) {
				//do not dismiss the alertdailog
				try {
					Field field = alertDialog.getClass().getSuperclass().getDeclaredField("mShowing");
					//必须设置true以后才能设置下面的field
					field.setAccessible(true);
					//showing alertDialog 
					field.set(alertDialog, false);
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(editText.getText().toString().isEmpty()){
					Toast.makeText(RedeemActivity.this, getString(R.string.input_pw), Toast.LENGTH_SHORT).show();
				}else{
					//调用httputils验证密码
//					alertDialog.dismiss();//test
					//dismiss alert dialog
					try {
						Field field = alertDialog.getClass().getSuperclass().getDeclaredField("mShowing");
						field.setAccessible(true);
						//dismissing alertDialog
						field.set(alertDialog, true);
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK){
					return true;
				}
				return false;
			}
		});
		alertDialog.show();
	}
}
