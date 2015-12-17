package com.hengxuan.stock.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.hengxuan.stock.DataContract;
import com.hengxuan.stock.DatabaseHelper;
import com.hengxuan.stock.R;
import com.hengxuan.stock.data.ZXGData;
import com.hengxuan.stock.utils.Log;
import com.hengxuan.stock.widget.CheckableLinearLayout;
import com.mobeta.android.dslv.DragSortCursorAdapter;
import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.SimpleDragSortCursorAdapter;

import java.util.ArrayList;
import java.util.Map;

public class ZXGEditActivity extends Activity implements LoaderManager.LoaderCallbacks<ArrayList<String[]>>{
    private ActionBar mActionBar;
    private DragSortListView dragSortListView;
    private DragSortCursorAdapter dragSortCursorAdapter;
    private TextView delBtn;
    private ZXGListAdapter adapter;
    private ArrayList data;
    private static final int DATA_LOADER = 0;
    private DatabaseHelper databaseHelper;
    private boolean edited = false;

    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                public void drop(int from, int to) {
                    if (from != to) {
                        Object item = adapter.getItem(from);
                        adapter.remove(item);
                        adapter.insert(item, to);
                        dragSortListView.moveCheckState(from, to);
                    }
                }
            };

    private DragSortListView.RemoveListener onRemove =
            new DragSortListView.RemoveListener() {
                @Override
                public void remove(int which) {
//                    DragSortListView list = getListView();
//                    String item = adapter.getItem(which);
//                    adapter.remove(item);
//                    list.removeCheckState(which);
                }
            };

    private class ZXGListAdapter extends ArrayAdapter{
        private ArrayList<String[]> data;
        private Context context;
        private LayoutInflater layoutInflater;

        public ZXGListAdapter(Context context) {
            super(context,android.R.layout.simple_list_item_1);
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        public void setData(ArrayList<String[]> data){
            clear();
            if(data != null){
                addAll(data);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View root;
            if(convertView == null) {
                root = layoutInflater.inflate(R.layout.zxg_edit_item, parent, false);
            }else{
                root = convertView;
            }
            String[] ss = (String[]) getItem(position);
            TextView name = (TextView) root.findViewById(R.id.name);
            TextView code = (TextView) root.findViewById(R.id.code);
            name.setText(ss[0]);
            code.setText(ss[1]);
            return root;
        }
    }

    private static class ZXGListLoader extends AsyncTaskLoader<ArrayList<String[]>>{
        private Context context;
        public ZXGListLoader(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public ArrayList<String[]> loadInBackground() {
            ArrayList<String[]> data = new ArrayList<String[]>();
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            String[] cols =  new String[]{DataContract.ZXGEntry.COLUMN_NAME, DataContract.ZXGEntry.COLUMN_CODE,DataContract.ZXGEntry._ID};
            Cursor cursor = db.query(DataContract.ZXGEntry.TABLE_NAME,cols, null, null, null, null, null);
            if(cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.ZXGEntry.COLUMN_NAME));
                    String code = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.ZXGEntry.COLUMN_CODE));
                    data.add(new String[]{name, code});
                } while (cursor.moveToNext());
            }
            return data;
        }

        @Override
        protected void onStartLoading() {
            Log.i("onStartLoading");
            forceLoad();
        }

        @Override
        protected void onStopLoading() {
            Log.i("onStopLoading");
            cancelLoad();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxgedit);
//        mActionBar = getActionBar();
//        mActionBar.setCustomView(R.layout.custom_action_bar);
//        TextView title = (TextView) findViewById(R.id.title);
//        title.setText(R.string.zxg_edit);
//        ImageView icBack = (ImageView) findViewById(R.id.ic_back);
//        icBack.setVisibility(View.VISIBLE);
//        TextView rightBtn = (TextView) findViewById(R.id.title_right_btn);
//        rightBtn.setVisibility(View.VISIBLE);
//        rightBtn.setText(R.string.save);
//        rightBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //SAVE
//                save();
//                setResult(RESULT_OK);
//                finish();
//            }
//        });

        dragSortListView = (DragSortListView) findViewById(android.R.id.list);
        adapter = new ZXGListAdapter(this);
        dragSortListView.setAdapter(adapter);
        dragSortListView.setDropListener(onDrop);

        databaseHelper = new DatabaseHelper(this);
        if(savedInstanceState == null) {
            Log.i("start load data");
            getLoaderManager().initLoader(DATA_LOADER, null, this);
        }else{
            Log.i("restore data");
            ArrayList<String> data = (ArrayList<String>) savedInstanceState.get("data");
            ArrayList<String[]> data1 = new ArrayList<String[]>();
            for(int i=0;i<data.size();i=i+2){
                String[] ss = new String[]{data.get(i),data.get(i+1)};
                data1.add(ss);
            }
            adapter.setData(data1);
            adapter.notifyDataSetChanged();
        }

//        int[] ids = new int[]{R.id.name,R.id.code};
//        SQLiteDatabase db = databaseHelper.getReadableDatabase();
//        String[] cols =  new String[]{DataContract.ZXGEntry.COLUMN_NAME, DataContract.ZXGEntry.COLUMN_CODE,DataContract.ZXGEntry._ID};
//        Cursor cursor = db.query(DataContract.ZXGEntry.TABLE_NAME,cols, null, null, null, null, null);
//        dragSortCursorAdapter = new SimpleDragSortCursorAdapter(this, R.layout.zxg_edit_item, cursor, cols, ids, 0);
//        dragSortListView.setAdapter(dragSortCursorAdapter);
//        dragSortListView.setDropListener(onDrop);

//        data = new ArrayList<String[]>();
//        adapter = new ZXGListAdapter(this);
//        adapter.setData(data);
//        dragSortListView.setAdapter(adapter);

        delBtn = (TextView) findViewById(R.id.del_btn);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = dragSortListView.getCheckedItemCount();
                Log.d("checked count=" + count);
                SparseBooleanArray sparseBooleanArray = dragSortListView.getCheckedItemPositions();
                for(int i=0;i<count;i++){
                    Log.d(sparseBooleanArray.toString());
                    int index = sparseBooleanArray.indexOfValue(true);
                    if (index < 0) {
                        break;
                    }else {
                        int key = sparseBooleanArray.keyAt(index);
                        Object item = adapter.getItem(key-i);
                        Log.d("del zxg:" + key);
//                        dragSortListView.removeCheckState(key);
                        dragSortListView.setItemChecked(key,false);
                        adapter.setNotifyOnChange(false);
                        adapter.remove(item);
                    }
                }
                adapter.setNotifyOnChange(true);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int count = adapter.getCount();
        Log.i("onSaveInstanceState:"+count);
        ArrayList<String> data = new ArrayList<String>();
        for(int i=0;i<count;i++) {
            String[] ss = (String[]) adapter.getItem(i);
            data.add(ss[0]);
            data.add(ss[1]);
        }
        outState.putStringArrayList("data",data);
    }


    private void save() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(DataContract.ZXGEntry.TABLE_NAME,null,null);
        int count = adapter.getCount();
        for(int i=0;i<count;i++){
            String[] ss = (String[]) adapter.getItem(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataContract.ZXGEntry.COLUMN_NAME,ss[0]);
            contentValues.put(DataContract.ZXGEntry.COLUMN_CODE,ss[1]);
            db.insert(DataContract.ZXGEntry.TABLE_NAME,null,contentValues);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_zxgedit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {
            save();
            setResult(RESULT_OK);
            finish();
            return true;
        }

        if(id == R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        return new ZXGListLoader(ZXGEditActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String[]>> loader, ArrayList<String[]> data) {
        for(String[] ss : data){
            Log.d("--"+ss[0]+"--"+ss[1]);
        }
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }

}
