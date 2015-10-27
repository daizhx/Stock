package com.hengxuan.stock;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hengxuan.stock.model.Msg;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/24.
 */
public class ChatMsgViewAdapter extends BaseAdapter {

//    public Context mContext;
    private LayoutInflater layoutInflater;
    private List<Msg> msgList;

    public List<Msg> getData(){
        return msgList;
    }


    public ChatMsgViewAdapter(Context context,List<Msg> list){
        msgList = list;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.msg_item,null,false);
            holder = new Holder();
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.content  = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        Msg msg = msgList.get(position);
        if(msg != null){
            holder.time.setText(msg.first);
            holder.content.setText(msg.second);
        }
        return convertView;
    }

    static class Holder{
        TextView time;
        TextView content;
    }
}
