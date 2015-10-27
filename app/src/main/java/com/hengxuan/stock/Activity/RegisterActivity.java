package com.hengxuan.stock.Activity;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hengxuan.stock.R;
import com.hengxuan.stock.R.id;
import com.hengxuan.stock.R.layout;
import com.hengxuan.stock.SubActivity;
import com.hengxuan.stock.http.HttpAPI;
import com.hengxuan.stock.http.HttpUtils;
import com.hengxuan.stock.http.MyJsonObjectRequest;
import com.hengxuan.stock.user.User;
import com.hengxuan.stock.utils.Log;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private EditText etUsername;
	private EditText etTel;
	private EditText etPW;
	private EditText etPW2;
	
	private String username;
	private String tel;
	private String pw;
	private String pw2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
	}

	private void initView() {
		ImageView ivCancel = (ImageView) findViewById(R.id.iv_cancel);
		ivCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		Button btnRegister = (Button) findViewById(R.id.btn_reg);
		btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkInput()){
					register();
				}else{
                    Toast.makeText(RegisterActivity.this,R.string.confirm_pw_fail,Toast.LENGTH_SHORT).show();
                }
			}
		});
		etUsername = (EditText) findViewById(R.id.user);
		etTel = (EditText) findViewById(R.id.tel);
		etPW = (EditText) findViewById(R.id.pw);
		etPW2 = (EditText) findViewById(R.id.pw2);
	}
	
	//检查输入
	private boolean checkInput(){
		//TODO
		username = etUsername.getText().toString();
		tel = etTel.getText().toString();
		pw = etPW.getText().toString();
		pw2 = etPW2.getText().toString();
		
		if(pw.equals(pw2)){
			return true;
		}else{
			return false;
		}
	}
	
	//通用HttpUtils 注册
	private void register(){
        HttpUtils httpUtils = HttpUtils.getInstance(this);
        String url = HttpAPI.REGISTER;
		JSONObject param = new JSONObject();
        try {
            param.put("username",username);
            param.put("phone",tel);
            param.put("password", pw);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(Request.Method.POST, url, param, new Response.Listener<JSONObject>() {
            User user = User.getUser(RegisterActivity.this);
            @Override
            public void onResponse(JSONObject response) {
                Log.d("register success");
				user.hasLogin(username);
				setResult(RESULT_OK);
				finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("register failure");
            }
        });
        httpUtils.addToRequestQueue(jsonObjectRequest);
	}
}
