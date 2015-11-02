package com.hengxuan.stock.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.hb.views.PinnedHeaderListView;
import com.hengxuan.pulltorefresh.PullToRefreshAdapterViewBase;
import com.hengxuan.pulltorefresh.PullToRefreshBase;
import com.hengxuan.pulltorefresh.PullToRefreshListView;

/**
 * Created by Administrator on 2015/10/30.
 */
public class PullToRefreshPinnedHeaderListView extends PullToRefreshListView {

    public PullToRefreshPinnedHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshPinnedHeaderListView(Context context) {
        super(context);
    }

    public PullToRefreshPinnedHeaderListView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshPinnedHeaderListView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }



    @Override
    protected PinnedHeaderListView createRefreshableView(Context context, AttributeSet attrs) {
        return new PinnedHeaderListView(context,attrs);
    }
}
