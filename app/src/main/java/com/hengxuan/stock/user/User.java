package com.hengxuan.stock.user;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
    private static User user;
    private Context context;
    private String name;
    private boolean isLogin = false;
    private String id;

    public synchronized static User getUser(Context context){
        if(user == null){
            user = new User(context);
        }
        return user;
    }

    public User(Context c){
        context = c;
    }
	/**
	 * get the user name
	 * @return name or null
	 */
	public String getName(){
		if(name==null){
			SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
			name = sp.getString("name", null);
		}
		return name;
	}
	
	public void hasLogout(){
		isLogin = false;
		SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		sp.edit().putBoolean("islogin", false).commit();
	}
	
	public boolean isLogin(){
		if(!isLogin){
			SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
			isLogin = sp.getBoolean("islogin", false);
		}
		return isLogin;
	}
	
	public void hasLogin(String name){
        if(!isLogin()) {
            SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            sp.edit().putBoolean("islogin", true).putString("name", name).commit();
            isLogin = true;
        }
	}

    public String getId(){
        if(id==null){
//            SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
//            id = sp.getString("id", null);
            SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
            id = sp.getString("user_id",null);
        }
        return id;
    }

}
