package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.MessageCenterRecyclerAdapter;
import com.sdxxtop.zhidian.entity.MsgIndexBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.util.List;

import butterknife.BindView;

public class MessageCenterActivity extends BaseActivity {

    @BindView(R.id.message_center_title_view)
    SubTitleView titleView;
    @BindView(R.id.message_center_frame_text)
    TextView approverFrameText;
    @BindView(R.id.message_center_frame)
    FrameLayout approverFrame;
    @BindView(R.id.message_center_relat)
    RelativeLayout approverRelate;
    @BindView(R.id.message_center_line)
    View centerLine;
    @BindView(R.id.message_center_title)
    TextView approverTitle;
    @BindView(R.id.message_center_content)
    TextView approverContent;
    @BindView(R.id.message_center_time)
    TextView approverTime;
    @BindView(R.id.message_center_frame_text1)
    TextView noticeFrameText;
    @BindView(R.id.message_center_frame1)
    FrameLayout noticeFrame;
    @BindView(R.id.message_center_relat1)
    RelativeLayout noticeRelate;
    @BindView(R.id.message_center_title1)
    TextView noticeTitle;
    @BindView(R.id.message_center_content1)
    TextView noticeContent;
    @BindView(R.id.message_center_time1)
    TextView noticeTime;
    @BindView(R.id.message_center_frame_text3)
    TextView reportFrameText;
    @BindView(R.id.message_center_frame3)
    FrameLayout noticeFrame3;
    @BindView(R.id.message_center_relat3)
    RelativeLayout noticeRelate3;
    @BindView(R.id.message_center_title3)
    TextView reportTitle;
    @BindView(R.id.message_center_content3)
    TextView reportContent;
    @BindView(R.id.message_center_time3)
    TextView reportTime;

    @BindView(R.id.message_center_recycler)
    RecyclerView msgRecyclerView;
    private MessageCenterRecyclerAdapter msgAdapter;

    @Override
    protected int getActivityView() {
        return R.layout.activity_message_center;
    }

    @Override
    protected void initView() {
        super.initView();
        msgRecyclerView.addItemDecoration(new ItemDivider());
        msgRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        msgAdapter = new MessageCenterRecyclerAdapter(R.layout.item_message_center_recycler);
        msgRecyclerView.setAdapter(msgAdapter);
    }

    @Override
    protected void initData() {
        Params params = new Params();

        RequestUtils.createRequest().postMsgIndex(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<MsgIndexBean>() {
            @Override
            public void onSuccess(MsgIndexBean baseModel) {
                List<MsgIndexBean.DataBean> data = baseModel.getData();
                if (data != null) {
                    handleData(data);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    @Override
    protected void initEvent() {
        approverRelate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageCenterApprovalActivity.class);
                startActivity(intent);
                approverFrameText.setVisibility(View.INVISIBLE);
            }
        });

        noticeRelate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageCenterNoticeActivity.class);
                startActivity(intent);
                noticeFrameText.setVisibility(View.INVISIBLE);
            }
        });

        noticeRelate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MineWorkListActivity.startMineWorkListActivity(mContext);
                reportFrameText.setVisibility(View.INVISIBLE);
            }
        });


        titleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageNoticeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleData(List<MsgIndexBean.DataBean> data) {
        msgAdapter.replaceData(data);
    }
}
