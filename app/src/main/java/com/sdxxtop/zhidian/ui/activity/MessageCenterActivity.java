package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.MsgIndexBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

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

    @Override
    protected int getActivityView() {
        return R.layout.activity_message_center;
    }

    @Override
    protected void initData() {
        Params params = new Params();

        RequestUtils.createRequest().postMsgIndex(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<MsgIndexBean>() {
            @Override
            public void onSuccess(MsgIndexBean baseModel) {
                MsgIndexBean.DataBean data = baseModel.getData();
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

    private void handleData(MsgIndexBean.DataBean data) {
        MsgIndexBean.DataBean.ApplyBean apply = data.getApply();
        MsgIndexBean.DataBean.NoticeBean notice = data.getNotice();
        MsgIndexBean.DataBean.ReportBean report = data.getReport();

        if (apply != null) {
            int apply_num = apply.getApply_num();
            if (apply_num != 0) {
                if (apply_num >= 99) {
                    approverFrameText.setText("99+");
                } else {
                    approverFrameText.setText(apply_num + "");
                }
                approverFrameText.setVisibility(View.VISIBLE);
            } else {
                approverFrameText.setVisibility(View.INVISIBLE);
            }

            approverContent.setText(StringUtil.stringNotNull(apply.getTitle()));
            String update_time = apply.getUpdate_time();
            if (!TextUtils.isEmpty(update_time)) {
                approverRelate.setVisibility(View.VISIBLE);
                approverTime.setText(DateUtil.getShowTime(update_time));
            } else {
                approverRelate.setVisibility(View.GONE);
                centerLine.setVisibility(View.GONE);
            }
        } else {
            approverRelate.setVisibility(View.GONE);
            centerLine.setVisibility(View.GONE);
        }

        if (notice != null) {
            int notice_num = notice.getNotice_num();
            if (notice_num != 0) {
                if (notice_num >= 99) {
                    noticeFrameText.setText("99+");
                } else {
                    noticeFrameText.setText(notice_num + "");
                }
                noticeFrameText.setVisibility(View.VISIBLE);
            } else {
                noticeFrameText.setVisibility(View.INVISIBLE);
            }
            noticeContent.setText(StringUtil.stringNotNull(notice.getTitle()));
            String update_time = notice.getUpdate_time();
            if (!TextUtils.isEmpty(update_time)) {
                String updateTime = DateUtil.getShowTime(update_time);
                noticeTime.setText(updateTime);
                noticeRelate.setVisibility(View.VISIBLE);
            } else {
                noticeRelate.setVisibility(View.GONE);
            }
        } else {
            noticeRelate.setVisibility(View.GONE);
        }

        if (report != null) {
            int report_num = report.getReport_num();
            if (report_num != 0) {
                if (report_num >= 99) {
                    reportFrameText.setText("99+");
                } else {
                    reportFrameText.setText(report_num + "");
                }
                reportFrameText.setVisibility(View.VISIBLE);
            } else {
                reportFrameText.setVisibility(View.INVISIBLE);
            }
            reportContent.setText(StringUtil.stringNotNull(report.getTitle()));
            String update_time = report.getUpdate_time();
            if (!TextUtils.isEmpty(update_time)) {
                String updateTime = DateUtil.getShowTime(update_time);
                reportTime.setText(updateTime);
                noticeRelate3.setVisibility(View.VISIBLE);
            } else {
                noticeRelate3.setVisibility(View.GONE);
            }
        } else {
            noticeRelate3.setVisibility(View.GONE);
        }

    }
}
