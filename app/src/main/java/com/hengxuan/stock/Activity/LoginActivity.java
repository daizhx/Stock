package com.hengxuan.stock.Activity;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hengxuan.stock.R;
import com.hengxuan.stock.R.id;
import com.hengxuan.stock.R.layout;
import com.hengxuan.stock.R.string;
import com.hengxuan.stock.R.style;
import com.hengxuan.stock.http.Des3;
import com.hengxuan.stock.http.HttpAPI;
import com.hengxuan.stock.http.HttpUtils;
import com.hengxuan.stock.http.MyJsonObjectRequest;
import com.hengxuan.stock.user.User;
import com.hengxuan.stock.utils.Log;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private ImageView ivCancel;
	private EditText user;
	private EditText pw;
	private Button login;
	private Button register;
	private ProgressDialog dialog;
	
	private String name;
	private String passwd;
	private static final int REQUEST_REGISTER = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
	}
	private void initView() {
		ivCancel = (ImageView) findViewById(R.id.iv_cancel);
		user = (EditText) findViewById(R.id.user);
		pw = (EditText) findViewById(R.id.pw);
		login = (Button) findViewById(R.id.btn_login);
		register = (Button) findViewById(R.id.btn_reg);
		
		ivCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 调用HttpUtils 进行登录
				if(checkInput()){
					login();
				}
				
			}
		});
		
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(LoginActivity.this,RegisterActivity.class), REQUEST_REGISTER);
			}
		});
	}
	
	private void login(){
		HttpUtils httpUtils = HttpUtils.getInstance(this);
		String url = HttpAPI.SIGN_IN;
        final User user = User.getUser(this);

		JSONObject params = new JSONObject();
		try {
            params.put("username", name);
			params.put("password", Des3.decode(passwd, Des3.DES3_KEY, Des3.DES3_IV));
		} catch (Exception e) {
			e.printStackTrace();
		}
        JsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
				Log.d("onSuccess:"+response);
				dialog.dismiss();
				user.hasLogin(name);
				setResult(RESULT_OK);
				finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
				dialog.dismiss();
				Toast.makeText(LoginActivity.this, R.string.not_access_server, Toast.LENGTH_LONG).show();
            }
        });
        httpUtils.addToRequestQueue(jsonObjectRequest);
		dialog = new ProgressDialog(LoginActivity.this,R.style.CustomDialog);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
	}
	
	private boolean checkInput(){
		name = user.getText().toString();
		passwd = pw.getText().toString();
		if(!name.isEmpty() && !passwd.isEmpty()){
			return true;
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("requestCode="+requestCode+",resultCode="+resultCode);
		if(requestCode == REQUEST_REGISTER){
			if(resultCode == RESULT_OK){
				finish();
			}
		}
	}
}
