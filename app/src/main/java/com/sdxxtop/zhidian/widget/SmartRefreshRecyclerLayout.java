package com.sdxxtop.zhidian.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.sdxxtop.zhidian.R;

/**
 * Created by Administrator on 2018/7/18.
 */

public class SmartRefreshRecyclerLayout extends LinearLayout {

    private SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerView mRecyclerView;

    public SmartRefreshRecyclerLayout(Context context) {
        this(context, null);
    }

    public SmartRefreshRecyclerLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartRefreshRecyclerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_smart_refresh_recycler, this, true);
        mSmartRefreshLayout = ((SmartRefreshLayout) findViewById(R.id.view_smart_layout));
        mRecyclerView = ((RecyclerView) findViewById(R.id.view_smart_recycler));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

}
