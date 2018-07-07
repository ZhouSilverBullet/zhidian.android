package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.MsgNoticeBean;
import com.sdxxtop.zhidian.eventbus.MessageCenterEvent;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class MessageCenterNoticeActivity extends BaseActivity {

    private boolean isFirst;
    @BindView(R.id.message_center_notice_title_view)
    SubTitleView titleView;
    @BindView(R.id.message_center_notice_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.message_center_notice_refresh)
    SmartRefreshLayout refreshLayout;
    private MessageNoticeAdapter mAdapter;

    @Override
    protected int getActivityView() {
        return R.layout.activity_message_center_notice;
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initEvent() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
        params.put("sp", page);
        RequestUtils.createRequest().postMsgNotice(params.getData()).enqueue(new RequestCallback<MsgNoticeBean>(new IRequestListener<MsgNoticeBean>() {
            @Override
            public void onSuccess(MsgNoticeBean msgNoticeBean) {

                if (!isFirst) { //通知主页刷新
                    isFirst = true;
                    EventBus.getDefault().post(new MessageCenterEvent());
                }
                MsgNoticeBean.DataBean data = msgNoticeBean.getData();
                if (data != null && data.getNotice() != null) {
                    List<MsgNoticeBean.DataBean.NoticeBean> notice = data.getNotice();
                    if (mAdapter == null) {
                        mAdapter = new MessageNoticeAdapter(R.layout.item_message_center_notice_recycler);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.addData(notice);
                    } else {
                        if (page == 0) {
                            mAdapter.replaceData(notice);
                        } else {
                            mAdapter.addData(notice);
                        }
                    }
                }
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    class MessageNoticeAdapter extends BaseQuickAdapter<MsgNoticeBean.DataBean.NoticeBean, BaseViewHolder> {
        public MessageNoticeAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, MsgNoticeBean.DataBean.NoticeBean item) {
            TextView time = helper.getView(R.id.item_message_center_notice_time);
            TextView title = helper.getView(R.id.item_message_center_notice_title);
            ImageView imgContent = helper.getView(R.id.item_message_center_notice_img_content);
            TextView textContent = helper.getView(R.id.item_message_center_notice_text_content);
            TextView textLook = helper.getView(R.id.item_message_center_notice_text_look);

            time.setText(DateUtil.getShowTime(item.getAdd_time()));
            title.setText(item.getTitle());
            String img = item.getImg();
            if (!TextUtils.isEmpty(img)) {
                String[] split = img.split(",");
                Glide.with(mContext).load(split[0]).into(imgContent);
                imgContent.setVisibility(View.VISIBLE);
            } else {
                imgContent.setVisibility(View.GONE);
            }
            final int notice_id = item.getNotice_id();
            textContent.setText(item.getContent());
            textLook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NoticeDetail2Activity.class);
                    intent.putExtra("ni", notice_id);
                    startActivity(intent);
                }
            });
        }
    }

}
