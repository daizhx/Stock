package com.hengxuan.stock;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hengxuan.stock.Activity.ChatMsgActivity;
import com.hengxuan.stock.model.Analyst;

import java.util.List;

/**
 * Created by Administrator on 2015/8/25.
 */
public class AnalystListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<Analyst> list;
    private Context mContext;
    public AnalystListAdapter(Context context){
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }


    public AnalystListAdapter(Context context,List list){
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.analyst_list_item,null,false);
            holder = new Holder();
            holder.name = (TextView) convertView.findViewById(android.R.id.title);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.content = (TextView) convertView.findViewById(android.R.id.content);
            holder.chakan = (TextView) convertView.findViewById(R.id.chakan);
            holder.subscribe = (TextView) convertView.findViewById(R.id.subscribe);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        final Analyst analyst = list.get(position);
        holder.name.setText(analyst.name);
        holder.avatar.setImageDrawable(analyst.avatar);
        holder.content.setText(Html.fromHtml(analyst.content));
        holder.chakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatMsgActivity.class);
                intent.putExtra("id", position);
                mContext.startActivity(intent);
            }
        });
        holder.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SubActivity)mContext).showPaySelector();
            }
        });
        return convertView;
    }

    private class Holder{
        TextView name;
        ImageView avatar;
        TextView content;

        TextView chakan;
        TextView subscribe;
    }
}
