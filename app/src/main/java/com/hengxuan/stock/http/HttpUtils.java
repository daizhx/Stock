package com.hengxuan.stock.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.hengxuan.stock.model.News;
import com.hengxuan.stock.model.Stock;
import com.hengxuan.stock.utils.Log;


/**
 *
 * @author daizhx
 *
 */
public class HttpUtils {
	private static HttpUtils mInstance;
	private ImageLoader mImageLoader;
	private RequestQueue mRequestQueue;
	private static Context mCtx;

	private HttpUtils(Context context){
		mCtx = context;
		mRequestQueue = getmRequestQueue();
		mImageLoader = new ImageLoader(mRequestQueue,new ImageLoader.ImageCache(){
			private final LruCache<String,Bitmap> cache = new LruCache<String,Bitmap>(20);
			@Override
			public Bitmap getBitmap(String url) {
				return cache.get(url);
			}

			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				cache.put(url,bitmap);
			}
		});

	}

	public static synchronized HttpUtils getInstance(Context context){
		if(mInstance == null){
			mInstance = new HttpUtils(context);
		}
		return mInstance;
	}

	public RequestQueue getmRequestQueue(){
		if(mRequestQueue == null){
			mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req){
		getmRequestQueue().add(req);
	}

	public ImageLoader getImageLoader(){
		return mImageLoader;
	}



	
	/**
	 * 
	 * @param response
	 * @return null or JSON data
	 */
	public Object parseData(JSONObject response){
		try {
			int code = response.getInt("code");
			if(code == 1){
				return response.get("object");
			}else{
				String msg = response.getString("msg");
				Log.d(msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public JSONArray getNewsList(JSONObject object){
		try {
			return object.getJSONArray("data");
		} catch (JSONException e) {
			return null;
		}
	}

	private String findStockNumber(String str){
		int index = str.indexOf("=");
		return str.substring(index-6, index);
	}
	private String quoteString(String str){
		String ret = null;
		int start = str.indexOf("\"");
		int end = str.indexOf("\"", start+1);
		return str.substring(start+1, end);
	}

    public String getUrl(String myurl) throws IOException{
        InputStream is = null;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(1000);
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            if (response == 200) {
                is = conn.getInputStream();
                String contentString = read(is);
                return contentString;
            } else {
                return null;
            }
        }finally {
            if(is != null){
                is.close();
            }
        }
    }

    public JSONObject getJsonUrl(String url) throws JSONException, IOException {
        String s = getUrl(url);
        JSONObject jsonObject = new JSONObject(s);
        return jsonObject;
    }

    private String read(InputStream is) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Reader  reader = new InputStreamReader(is,"UTF-8");
        char[] buffer = new char[1024];
        int len = -1;
        while ((len = reader.read(buffer)) != -1){
            stringBuilder.append(buffer,0,len);
        }
        return stringBuilder.toString();
    }
}
