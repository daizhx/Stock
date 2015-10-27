package com.hengxuan.stock.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hengxuan.stock.DataContract;
import com.hengxuan.stock.DatabaseHelper;
import com.hengxuan.stock.R;
import com.hengxuan.stock.data.ZXGData;
import com.hengxuan.stock.http.HttpUtils;
import com.hengxuan.stock.utils.Log;
import com.hengxuan.stock.utils.StockFormula;
import com.hengxuan.stock.widget.InterceptScrollContainer;
import com.hengxuan.stock.widget.MyHScrollView;
import com.hengxuan.stock.widget.ZXstockListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ZXGFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ZXGFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZXGFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView listView;
    private ZXstockListAdapter zXstockListAdapter;
    private ArrayList<Map<String,Object>> zxgdatalist = new ArrayList<>();
    private int httpRequestCount = 0;
    private TextView tvTtitle;
    private Button titleRightBtn;
    private EditText searchBox;
    private Button searchCancelBtn;
    private ListView searchList;
    private LinearLayout stockList;
    private View titleBar;
    private MyHScrollView headScrollView;

    private List<Map<String,Object>> suggestStockList = new ArrayList<>();
//    private ArrayList<String[]> zxgList = new ArrayList<>();//self select stock code list
    private SearchListAdapter searchListAdapter;
    private boolean isSearchNow = false;
    public static final String INTENT_ACTION_ZXG_EDIT = "com.hengxuan.stock.EDIT";
    public static final String INTENT_ACTION_STOCK_ANALYST = "com.hengxuan.stock.STOCK_ANALYST";
    public static final String EXTRA_STOCK_NAME = "name";
    public static final String EXTRA_STOCK_CODE = "code";
    private InterceptScrollContainer interceptScrollContainer;

    private static final int UPDATE_DURATION=5*1000;
    private ImageView ivDataRefreshInd;
    private static final int MSG_DATA_UPDATE = 0;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_DATA_UPDATE:
                    AnimationDrawable animationDrawable = (AnimationDrawable) ivDataRefreshInd.getDrawable();
                    animationDrawable.stop();
                    animationDrawable.start();
                    Log.i("msg update data");
                    refreshZxgDataList();
                    Message msg1 = Message.obtain();
                    msg1.what = MSG_DATA_UPDATE;
                    mHandler.sendMessageDelayed(msg1,UPDATE_DURATION);
                    break;
                default:
                    break;
            }
        }
    };

    private DatabaseHelper mDatabaseHelper;

    private static final int EDIT_REQUEST = 6;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ZXGFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ZXGFragment newInstance(String param1, String param2) {
        ZXGFragment fragment = new ZXGFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ZXGFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void showSearchView(){
        if(!isSearchNow) {
            searchCancelBtn.setVisibility(View.VISIBLE);
            searchList.setVisibility(View.VISIBLE);
            stockList.setVisibility(View.GONE);
            titleBar.setVisibility(View.GONE);
            isSearchNow = true;
        }
    }

    private void hiddenSearchView(){
        if(isSearchNow) {
            searchCancelBtn.setVisibility(View.GONE);
            searchList.setVisibility(View.GONE);
            stockList.setVisibility(View.VISIBLE);
            titleBar.setVisibility(View.VISIBLE);
            isSearchNow = false;
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(searchBox.getWindowToken(),0);
        }
    }

    /**
     *
     * @param event
     * @return horizontal return 0,vertical return 1
     */
    private int calcuOrientation(MotionEvent event){
        int size = event.getHistorySize();
        if(size == 0){
            return 1;
        }
        float x2 = event.getX();
        float x1 = event.getHistoricalX(0);
        float y2 = event.getY();
        float y1 = event.getHistoricalY(0);
        float x = x2 - x1;
        float y = y2 - y1;
        if(x > y){
            return 0;
        }else {
            return 1;
        }
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root =  inflater.inflate(R.layout.fragment_zxg, container, false);
        ivDataRefreshInd = (ImageView) root.findViewById(R.id.data_refresh_ind);
        ivDataRefreshInd.setVisibility(View.VISIBLE);
        titleRightBtn = (Button) root.findViewById(R.id.title_btn_right);
        titleRightBtn.setVisibility(View.VISIBLE);
        titleRightBtn.setText(R.string.edit);
        titleRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(INTENT_ACTION_ZXG_EDIT);
//                startActivity(intent);
                startActivityForResult(intent,EDIT_REQUEST);
            }
        });
        headScrollView = (MyHScrollView) root.findViewById(R.id.head_scroller);
        titleBar = root.findViewById(R.id.title_bar);
        listView = (ListView) root.findViewById(android.R.id.list);
        interceptScrollContainer = (InterceptScrollContainer) root.findViewById(R.id.intercept_scroll_container);
        interceptScrollContainer.setHockView(headScrollView, listView);
        zXstockListAdapter = new ZXstockListAdapter(getActivity(),zxgdatalist,headScrollView);
        listView.setAdapter(zXstockListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(INTENT_ACTION_STOCK_ANALYST);
                Map map = zxgdatalist.get(position);
                intent.putExtra(EXTRA_STOCK_NAME, (String) map.get(ZXGData.NAME));
                intent.putExtra(EXTRA_STOCK_CODE, (String) map.get(ZXGData.CODE));
                startActivity(intent);
            }
        });
//        registerForContextMenu(listView);
        tvTtitle = (TextView) root.findViewById(R.id.title_text);
        tvTtitle.setText(getString(R.string.select_stocks));
        searchBox = (EditText) root.findViewById(R.id.search_box);
        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchView();
            }
        });
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    showSearchView();
                    getSuggestStockList(s.toString());
                } else {
                    suggestStockList.clear();
                    hiddenSearchView();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchCancelBtn = (Button) root.findViewById(R.id.search_btn_cancle);
        searchCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBox.setText("");
                hiddenSearchView();
                searchBox.clearFocus();
//                zXstockListAdapter.notifyDataSetChanged();
            }
        });
        searchList = (ListView) root.findViewById(R.id.list_search);
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("item click...");
            }
        });
        searchList.setAdapter(searchListAdapter);
        stockList = (LinearLayout) root.findViewById(R.id.stock_list);

        return root;
    }

    private void goAnalysisActivity(int listPosition){
        Intent intent = new Intent(INTENT_ACTION_STOCK_ANALYST);
        Map map = zxgdatalist.get(listPosition);
        intent.putExtra(EXTRA_STOCK_NAME, (String) map.get(ZXGData.NAME));
        intent.putExtra(EXTRA_STOCK_CODE, (String) map.get(ZXGData.CODE));
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.context_menu_zxg,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        switch (item.getItemId()){
            case R.id.chakan:
                goAnalysisActivity(position);
                return true;
            case R.id.delete:

                //delete from db
                String code = (String) zxgdatalist.get(position).get(ZXGData.CODE);
                SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
                String selection = DataContract.ZXGEntry.COLUMN_CODE + "=?";
                String[] args = new String[]{code};
                int num = db.delete(DataContract.ZXGEntry.TABLE_NAME,selection,args);
                Log.i("del "+num + " entry from db ZXGEntry");
                zxgdatalist.remove(position);
                refreshZxgDataList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void initZxgDataList(){
        mDatabaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        String[] projection = {
                DataContract.ZXGEntry._ID,
                DataContract.ZXGEntry.COLUMN_NAME,
                DataContract.ZXGEntry.COLUMN_CODE
        };
        Cursor c = db.query(DataContract.ZXGEntry.TABLE_NAME, projection, null, null, null, null, null);
        zxgdatalist.clear();
        if(c.moveToFirst()) {
            do {
                String name = c.getString(c.getColumnIndexOrThrow(DataContract.ZXGEntry.COLUMN_NAME));
                String code = c.getString(c.getColumnIndexOrThrow(DataContract.ZXGEntry.COLUMN_CODE));
//                String[] ss = new String[]{name,code};
//                zxgList.add(ss);
                Map map = new HashMap();
                map.put(ZXGData.NAME,name);
                map.put(ZXGData.CODE,code);
                zxgdatalist.add(map);
            } while (c.moveToNext());
        }
    }

    private void refreshZxgDataList(){
        for(Map map : zxgdatalist){
            String code = (String) map.get(ZXGData.CODE);
            getStockData(code,map);
        }
    }

    private Map parseStockData(String str){
        String data = str.split("\"")[1];
        if(data == null){
            return null;
        }
        String[] ss = data.split("~");
        Map map = new HashMap();
        map.put(ZXGData.NAME, ss[1]);
        map.put(ZXGData.CODE,ss[2]);
        map.put(ZXGData.CURRENT,ss[3]);
        map.put(ZXGData.INCREASE_RATIO,ss[32]);
        map.put(ZXGData.INCREASE_AMOUNT,ss[31]);
        map.put(ZXGData.INCREASE_SPEED, null);
        map.put(ZXGData.TRADE_VOLUME,ss[36]);
        map.put(ZXGData.TRADE_PERCENT, ss[38]);
        map.put(ZXGData.VOLUMN_RATIO, null);
        map.put(ZXGData.PRICE_TO_EARNING_RATIO,ss[39]);
        map.put(ZXGData.PRICE_TO_BOOK_RATIO, ss[46]);
        map.put(ZXGData.OPENING_PRICE,ss[5]);
        map.put(ZXGData.CLOSING_PRICE, ss[4]);
        map.put(ZXGData.MAX_PRICE, ss[41]);
        map.put(ZXGData.MIN_PRICE,ss[42]);
        float[] buy = {Float.parseFloat(ss[10]),Float.parseFloat(ss[12]),Float.parseFloat(ss[14]),Float.parseFloat(ss[16]),Float.parseFloat(ss[18])};
        float[] sell = {Float.parseFloat(ss[20]),Float.parseFloat(ss[22]),Float.parseFloat(ss[24]),Float.parseFloat(ss[26]),Float.parseFloat(ss[28])};
        map.put(ZXGData.WB,StockFormula.calcWB(buy,sell));
        map.put(ZXGData.ZF, ss[43]);
        map.put(ZXGData.CIRCULATION_MARKET_VALUE, ss[44]);
        map.put(ZXGData.TOTAL_MARKET_VALUE, ss[45]);
        return map;
    }

    private void parseStockData(String str,Map map){
        String data = str.split("\"")[1];
        if(data == null){
            return;
        }
        String[] ss = data.split("~");
        map.put(ZXGData.NAME,ss[1]);
        map.put(ZXGData.CODE,ss[2]);
        map.put(ZXGData.CURRENT,ss[3]);
        map.put(ZXGData.INCREASE_RATIO,ss[32]);
        map.put(ZXGData.INCREASE_AMOUNT,ss[31]);
        map.put(ZXGData.INCREASE_SPEED,null);
        map.put(ZXGData.TRADE_VOLUME,ss[36]);
        map.put(ZXGData.TRADE_PERCENT,ss[38]);
        map.put(ZXGData.VOLUMN_RATIO,null);
        map.put(ZXGData.PRICE_TO_EARNING_RATIO,ss[39]);
        map.put(ZXGData.PRICE_TO_BOOK_RATIO,ss[46]);
        map.put(ZXGData.OPENING_PRICE,ss[5]);
        map.put(ZXGData.CLOSING_PRICE,ss[4]);
        map.put(ZXGData.MAX_PRICE,ss[41]);
        map.put(ZXGData.MIN_PRICE,ss[42]);
        float[] buy = {Float.parseFloat(ss[10]),Float.parseFloat(ss[12]),Float.parseFloat(ss[14]),Float.parseFloat(ss[16]),Float.parseFloat(ss[18])};
        float[] sell = {Float.parseFloat(ss[20]),Float.parseFloat(ss[22]),Float.parseFloat(ss[24]),Float.parseFloat(ss[26]),Float.parseFloat(ss[28])};
        map.put(ZXGData.WB,StockFormula.calcWB(buy,sell));
        map.put(ZXGData.ZF, ss[43]);
        map.put(ZXGData.CIRCULATION_MARKET_VALUE,ss[44]);
        map.put(ZXGData.TOTAL_MARKET_VALUE, ss[45]);
    }


    private void getStockData(String code, final Map map){
        HttpUtils httpUtils = HttpUtils.getInstance(getActivity());
        String url = "http://qt.gtimg.cn/q=" + StockFormula.hostExchange(code) + code;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseStockData(response, map);
                httpRequestCount--;
                if(httpRequestCount == 0){
                    zXstockListAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("get stock data fail.");
                httpRequestCount--;
                if(httpRequestCount == 0){
                    zXstockListAdapter.notifyDataSetChanged();
                }
            }
        });
        httpRequestCount++;
        httpUtils.addToRequestQueue(stringRequest);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
        searchListAdapter = new SearchListAdapter(activity,suggestStockList,R.layout.search_list_item,new String[]{"name","code","icon"},new int[]{android.R.id.text1,android.R.id.text2,R.id.add});
        //read zxg recode from db
        initZxgDataList();
        //get data from http
        refreshZxgDataList();
        Log.d("on attach");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mHandler != null) {
            Message msg = Message.obtain();
            msg.what = MSG_DATA_UPDATE;
            mHandler.sendMessageDelayed(msg, UPDATE_DURATION);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("onPause");
        if(mHandler != null){
            mHandler.removeMessages(0);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if(mHandler != null){
            mHandler.removeMessages(0);
        }
        Log.d("on detach");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void getSuggestStockList(String key){
        String url = "http://suggest3.sinajs.cn/suggest/type=11&key="+key;
        HttpUtils httpUtils = HttpUtils.getInstance(getActivity());
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("query stock = "+response);
                if(response != null){
                    parseSearchResult(response);
                    searchListAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("query stock fail");
            }
        });
        httpUtils.addToRequestQueue(stringRequest);
    }

    private void parseSearchResult(String result){
        suggestStockList.clear();
        Log.i("search result="+result);
        int start = result.indexOf("\"");
        int stop = result.lastIndexOf("\"");
        String sub = result.substring(start + 1, stop);
        if(sub.isEmpty()){
            return;
        }
        String[] ss = sub.split(";");
        for(String s:ss){
            String[] info = s.split(",");
            Map map = new HashMap();
            map.put("name",info[4]);
            map.put("code",info[2]);
            map.put("icon", R.drawable.add);
            map.put("checked",false);
            for(int i=0;i<zxgdatalist.size();i++){
                String code = (String) zxgdatalist.get(i).get(ZXGData.CODE);
                if(code.equals(info[2])) {
                    map.put("icon", R.drawable.right);
                    map.put("checked",true);
                    break;
                }
            }
            suggestStockList.add(map);
        }
    }

    public class SearchListAdapter extends SimpleAdapter {
        private List<? extends Map<String, ?>> data;
        private Context context;
        /**
         * Constructor
         *
         * @param context  The context where the View associated with this SimpleAdapter is running
         * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
         *                 Maps contain the data for each row, and should include all the entries specified in
         *                 "from"
         * @param resource Resource identifier of a view layout that defines the views for this list
         *                 item. The layout file should include at least those named views defined in "to"
         * @param from     A list of column names that will be added to the Map associated with each
         *                 item.
         * @param to       The views that should display column in the "from" parameter. These should all be
         *                 TextViews. The first N views in this list are given the values of the first N columns
         */
        public SearchListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.data = data;
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            final ImageView addIcon = (ImageView) convertView.findViewById(R.id.add);
            addIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //save in db
                    Map<String,?> map = data.get(position);
                    String name = (String) map.get("name");
                    String code = (String) map.get("code");
                    Boolean checked = (Boolean) map.get("checked");
                    Log.i("add code="+code);

                    if(zxgdatalist != null && !checked.booleanValue()) {
                        addIcon.setImageResource(R.drawable.right);
                        if(mDatabaseHelper == null) {
                            mDatabaseHelper = new DatabaseHelper(context);
                        }
                        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DataContract.ZXGEntry.COLUMN_NAME, name);
                        contentValues.put(DataContract.ZXGEntry.COLUMN_CODE, code);
                        long rowid = db.insert(DataContract.ZXGEntry.TABLE_NAME,null,contentValues);
                        if(rowid != -1 && zXstockListAdapter != null){
                            Log.i("updated zxg data list");
                            Map m = new HashMap();
                            m.put(ZXGData.NAME,name);
                            m.put(ZXGData.CODE,code);
                            zxgdatalist.add(m);
                            getStockData(code,m);
                        }
                    }
                }
            });
            return convertView;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("2--requestCode="+requestCode+",resultCode="+requestCode);
        if(requestCode == EDIT_REQUEST && resultCode == Activity.RESULT_OK){
            Log.i("edit success");
            if(mHandler != null) {
                mHandler.removeMessages(MSG_DATA_UPDATE);
                initZxgDataList();
                refreshZxgDataList();
                zXstockListAdapter.notifyDataSetChanged();
            }
        }
    }
}
