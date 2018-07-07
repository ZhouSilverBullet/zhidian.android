package com.sdxxtop.zhidian.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sdxxtop.zhidian.adapter.ShowOutPowerAllAdapter;
import com.sdxxtop.zhidian.entity.UcenterOutIndexBean;
import com.sdxxtop.zhidian.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/7.
 */

public abstract class BaseScrollFragment extends BaseFragment {
    protected RecyclerView mRecyclerView;
    protected GridLayoutManager manager;
    protected ShowOutPowerAllAdapter showOutPowerAllAdapter;
    private List<List<UcenterOutIndexBean.DataEntity.UserinfoEntity>> list = new ArrayList<>();
    private int mIndex = 0;
    private boolean move = false;

    protected void setRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
        manager = new GridLayoutManager(mContext, 1);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        showOutPowerAllAdapter = new ShowOutPowerAllAdapter(mContext, list);
        mRecyclerView.setAdapter(showOutPowerAllAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerViewListener());
    }

    public void toMove(int n) {
        mIndex = n;
        mRecyclerView.stopScroll();
        smoothMoveToPosition(n);
    }

    public void smoothMoveToPosition(int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        Log.e("first--->", String.valueOf(firstItem));
        Log.e("last--->", String.valueOf(lastItem));
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            Log.e("pos---->", String.valueOf(n) + "VS" + firstItem);
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            Log.e("top---->", String.valueOf(top));
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
            move = true;
        }
    }

    private class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                move = false;
                int n = mIndex - manager.findFirstVisibleItemPosition();
                Log.d("n---->", String.valueOf(n));
                if (0 <= n && n < mRecyclerView.getChildCount()) {
                    int top = mRecyclerView.getChildAt(n).getTop();
                    Log.d("top--->", String.valueOf(top));
                    mRecyclerView.smoothScrollBy(0, top);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (move) {
                move = false;
                int n = mIndex - manager.findFirstVisibleItemPosition();
                if (0 <= n && n < mRecyclerView.getChildCount()) {
                    int top = mRecyclerView.getChildAt(n).getTop();
                    mRecyclerView.scrollBy(0, top);
                }
            }
        }
    }

}
