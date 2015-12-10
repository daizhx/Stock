package com.hengxuan.stock.Activity;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.hengxuan.stock.ChatMsgViewAdapter;
import com.hengxuan.stock.DataContract;
import com.hengxuan.stock.DatabaseHelper;
import com.hengxuan.stock.R;
import com.hengxuan.stock.SubActivity;
import com.hengxuan.stock.application.Constants;
import com.hengxuan.stock.http.HttpAPI;
import com.hengxuan.stock.http.HttpUtils;
import com.hengxuan.stock.model.Msg;
import com.hengxuan.stock.utils.Log;

import java.util.LinkedList;
import java.util.List;

public class ChatMsgActivity extends Activity {

    private ListView mList;
    private TextView tvHint;
    private String title;
    private LinkedList<Msg> msgList = new LinkedList<Msg>();
    private ChatMsgViewAdapter mChatMsgViewAdapter;
    //分析师Id
    private int mId = -1;
    private String newestMsg = null;
    private Cursor cursor = null;

    private void getHistoricalData(){
        HttpUtils httpUtils = HttpUtils.getInstance(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent= getIntent();
        mId = intent.getIntExtra("id",-1);
        if(mId == Constants.JBSTOCK_ID){
            title =getString(R.string.jubaoyiqi);
            initCursor("JBSTOCK");
        }else if(mId == Constants.NNSTOCK_ID){
            title = getString(R.string.niuniuchaogu);
            initCursor("NNSTOCK");

        }else{
            //wrong
            finish();
        }
        setTitle(title);
        newestMsg = intent.getStringExtra("content");
        Log.d("mId="+mId+",msg="+newestMsg);
//        //TODO Msg没有时间戳
//        if(newestMsg != null) {
//            String time = (String) DateFormat.format("yyyy-MM-dd hh:mm:ss", System.currentTimeMillis());
//            Msg msg = new Msg(time,newestMsg);
//            msgList.add(msg);
//        }

        setContentView(R.layout.activity_info);
        mList = (ListView) findViewById(android.R.id.list);
        tvHint = (TextView) findViewById(R.id.hint);
        mChatMsgViewAdapter = new ChatMsgViewAdapter(this,msgList);
        mList.setAdapter(mChatMsgViewAdapter);
        registerForContextMenu(mList);
//        new DataLoader().execute();
        getData();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_del,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        if(item.getItemId() == R.id.menu_del){
            int position = adapterContextMenuInfo.position;
            Msg msg = msgList.get(position);
            delData(""+msg.id);
            return true;
        }else {
            return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cursor != null){
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    private class DataLoader extends AsyncTask<Void,Void,Integer> {


        @Override
        protected Integer doInBackground(Void... params) {
            return getData();
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == 0){
                mList.setVisibility(View.INVISIBLE);
                tvHint.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initCursor(String name){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projection = {DataContract.CpMsgEntry._ID,DataContract.CpMsgEntry.COLUMN_TIME, DataContract.CpMsgEntry.COLUMN_CONTENT};
        String sortOrder = DataContract.CpMsgEntry.COLUMN_TIME + " ASC";
        String section = DataContract.CpMsgEntry.COLUMN_NAME + " = ?";
        String[] sectionArgs = {name};
        cursor = db.query(DataContract.CpMsgEntry.TABLE_NAME,projection,section,sectionArgs,null,null,sortOrder);
        cursor.moveToLast();
    }

    private int getData(){
        if(cursor != null) {
            int size = cursor.getCount();
            if(size == 0)return size;
            if(size > 20){
                for(int i=0;i<20;i++){
                    int id = cursor.getInt(cursor.getColumnIndex(DataContract.CpMsgEntry._ID));
                    String time = cursor.getString(cursor.getColumnIndex(DataContract.CpMsgEntry.COLUMN_TIME));
                    String content = cursor.getString(cursor.getColumnIndex(DataContract.CpMsgEntry.COLUMN_CONTENT));
                    Msg msg = new Msg(time,content);
                    msg.id = id;
                    msgList.addFirst(msg);
                    cursor.moveToPrevious();
                }
            }else{
                for(int i=0;i<size;i++){
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DataContract.CpMsgEntry._ID));
                    String time = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.CpMsgEntry.COLUMN_TIME));
                    String content = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.CpMsgEntry.COLUMN_CONTENT));
                    Msg msg = new Msg(time,content);
                    msg.id = id;
                    msgList.addFirst(msg);
                    cursor.moveToPrevious();
                }
            }
            mChatMsgViewAdapter.notifyDataSetChanged();
            return size;
        }
        return 0;
    }

    private void delData(String id){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String where = DataContract.CpMsgEntry._ID + " = ?";
        db.delete(DataContract.CpMsgEntry._ID,where,new String[]{id});
    }
}
