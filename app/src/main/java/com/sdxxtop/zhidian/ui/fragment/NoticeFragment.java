package com.sdxxtop.zhidian.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.NoticeDataBean;
import com.sdxxtop.zhidian.eventbus.NoticeReadEvent;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.activity.Notice2Activity;
import com.sdxxtop.zhidian.ui.activity.NoticeDetail2Activity;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.ItemDivider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeFragment extends BaseFragment {

    @BindView(R.id.notice2_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.notice2_smart_refresh)
    SmartRefreshLayout smartRefreshLayout;
    private int type;
    private NoticeAdapter mAdapter;

    public NoticeFragment() {
    }

    public static NoticeFragment newInstance(int type) {
        NoticeFragment fragment = new NoticeFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initVariables() {
        if (getArguments() != null) {
            type = getArguments().getInt("type");
        }
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new ItemDivider());
    }

    @Override
    protected void initEvent() {
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (mAdapter != null) {
                    loadData(mAdapter.getData().size());
                }
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData(0);
            }
        });
    }

    @Override
    protected void initData() {
        loadData(0);
    }

    private void loadData(final int page) {
        Params params = new Params();
        params.put("ir", type);
        params.put("sp", page);
        RequestUtils.createRequest().postNoticeIndex(params.getData()).enqueue(new RequestCallback<>(mContext, new IRequestListener<NoticeDataBean>() {
            @Override
            public void onSuccess(NoticeDataBean noticeDataBean) {
                NoticeDataBean.DataBean data = noticeDataBean.getData();
                if (data != null) {
                    handleData(page, data);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    private void handleData(int page, NoticeDataBean.DataBean data) {
        List<NoticeDataBean.DataBean.NoticeBean> notice = data.getNotice();
        int is_add = data.getIs_add();
        if (is_add == 1) {
            ((Notice2Activity) mContext).userHasAddGrand(true);
        } else {
            ((Notice2Activity) mContext).userHasAddGrand(false);
        }
        if (notice == null) {
            return;
        }
        if (mAdapter == null) {
            mAdapter = new NoticeAdapter(R.layout.item_notice_recycler);
            recyclerView.setAdapter(mAdapter);
            mAdapter.addData(notice);
        } else {
            if (page == 0) {
                mAdapter.replaceData(notice);
            } else {
                mAdapter.addData(notice);
            }
        }

        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
            smartRefreshLayout.finishLoadMore();
        }
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_notice2;
    }


    class NoticeAdapter extends BaseQuickAdapter<NoticeDataBean.DataBean.NoticeBean, BaseViewHolder> {

        public NoticeAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, NoticeDataBean.DataBean.NoticeBean item) {
            ImageView imgView = helper.getView(R.id.item_notice2_recycler_img);
            TextView titleText = helper.getView(R.id.item_notice2_recycler_title);
            TextView nameText = helper.getView(R.id.item_notice2_recycler_name);
            TextView timeText = helper.getView(R.id.item_notice2_recycler_time);

            String img = item.getImg();
            if (TextUtils.isEmpty(img)) {
                imgView.setVisibility(View.GONE);
            } else {
                imgView.setVisibility(View.VISIBLE);
                String[] split = img.split(",");
                if (split.length > 0) {
                    RequestOptions placeholder = new RequestOptions().placeholder(R.mipmap.add_images);
                    Glide.with(mContext).load(split[0]).apply(placeholder).into(imgView);
                }
            }
            titleText.setText(item.getTitle());
            nameText.setText(item.getName());
            timeText.setText(DateUtil.getShowTime2(item.getAdd_time()));

            final int notice_id = item.getNotice_id();
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NoticeDetail2Activity.class);
                    intent.putExtra("ni", notice_id);
                    startActivity(intent);
                }
            });
        }
    }

    @Subscribe
    public void notifyNoticeReadEvent(NoticeReadEvent event) {
        initData();
    }
}
