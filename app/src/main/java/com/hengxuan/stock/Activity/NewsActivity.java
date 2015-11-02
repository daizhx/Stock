package com.hengxuan.stock.Activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.widget.TextView;

public class NewsActivity extends Activity {
	private String articleId;
	private String newsDate;
	private TextView content;
	private TextView title;
	private TextView date;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setTitle(getString(R.string.zx));
		setContentView(R.layout.activity_news);
		Intent intent = getIntent();
		articleId = intent.getStringExtra("articleId");
		newsDate = intent.getStringExtra("date");
		content = (TextView) findViewById(R.id.content);
		title = (TextView) findViewById(R.id.title);
		date = (TextView) findViewById(R.id.time);

		if(newsDate != null){
			newsDate = (String) DateFormat.format("yyyy-MM-dd", Long.parseLong(newsDate));
		}
        getText();
	}

    private void getText(){
        User user = User.getUser(this);
        final HttpUtils httpUtils = HttpUtils.getInstance(this);
        String time = (String) DateFormat.format("yyyy-MM-dd", System.currentTimeMillis());
        String url = HttpAPI.GET_ARTICLE_CONTEXT + "/"+user.getName() + "/"+articleId+"/"+time;
        JsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
					JSONObject data = (JSONObject) httpUtils.parseData(response);
					if(data != null){
						try {
							content.setText(Html.fromHtml(data.getString("content")));
							title.setText(Html.fromHtml(data.getString("title")));
							date.setText(newsDate);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("get article fail..."+error.getLocalizedMessage());
            }
        });
        httpUtils.addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
