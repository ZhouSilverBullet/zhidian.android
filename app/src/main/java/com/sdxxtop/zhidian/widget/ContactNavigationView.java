package com.sdxxtop.zhidian.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ParentListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/17.
 */

public class ContactNavigationView extends RelativeLayout {

    private RecyclerView mRecyclerView;
    private NaviAdapter naviAdapter;

    public ContactNavigationView(Context context) {
        this(context, null);
    }

    public ContactNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContactNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_contact_navigation, this, true);
        mRecyclerView = (RecyclerView) findViewById(R.id.view_contact_navigation_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        naviAdapter = new NaviAdapter(R.layout.item_contact_navigation_recycler);
        mRecyclerView.setAdapter(naviAdapter);

        naviAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (adapter instanceof NaviAdapter) {
                    List<ParentListBean.DataBeanX.NavBean> data = ((NaviAdapter) adapter).getData();
                    ParentListBean.DataBeanX.NavBean bean = data.get(position);
                    if (navigationItemClickListener != null) {
                        navigationItemClickListener.click(bean);
                    }
                }
            }
        });
    }

    public void replaceData(List<ParentListBean.DataBeanX.NavBean> data) {
        if (naviAdapter != null) {
            naviAdapter.replaceData(data);
        }
    }

    public void clearAll() {
        List<ParentListBean.DataBeanX.NavBean> data = new ArrayList<>();
        ParentListBean.DataBeanX.NavBean navBean = new ParentListBean.DataBeanX.NavBean();
        navBean.setName("全部");
        navBean.setType(-100);
        data.add(navBean);
        if (naviAdapter != null) {
            naviAdapter.replaceData(data);
        }
    }

    public int getCount() {
        if (naviAdapter != null) {
            return naviAdapter.getData().size();
        }
        return 0;
    }

    public List<ParentListBean.DataBeanX.NavBean> getAdapterData() {
        return naviAdapter.getData();
    }

    private NavigationItemClickListener navigationItemClickListener;

    public void setNavigationItemClickListener(NavigationItemClickListener navigationItemClickListener) {
        this.navigationItemClickListener = navigationItemClickListener;
    }

    public interface NavigationItemClickListener {
        void click(ParentListBean.DataBeanX.NavBean bean);
    }

    public class NaviAdapter extends BaseQuickAdapter<ParentListBean.DataBeanX.NavBean, BaseViewHolder> {
        public NaviAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, ParentListBean.DataBeanX.NavBean item) {
            View icon = helper.getView(R.id.item_contact_navigation_icon);
            TextView textView = helper.getView(R.id.item_contact_navigation_text);

            int position = getData().indexOf(item);
            if (position == getData().size() - 1) {
                textView.setTextColor(getResources().getColor(R.color.lan));
            } else {
                textView.setTextColor(getResources().getColor(R.color.texthintcolor));
            }
            if (position == 0) {
                icon.setVisibility(INVISIBLE);
            } else {
                icon.setVisibility(VISIBLE);
            }
            if (item.isTeacher()) {
                textView.setText(item.getPart_name());
            } else {
                textView.setText(item.getName());
            }
        }
    }

}
