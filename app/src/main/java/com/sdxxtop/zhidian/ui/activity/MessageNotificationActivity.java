package com.sdxxtop.zhidian.ui.activity;

import android.widget.CheckBox;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.widget.TextAndTextView;

import butterknife.BindView;

public class MessageNotificationActivity extends BaseActivity {

    @BindView(R.id.message_notification_message_notice_switch)
    CheckBox noticeCheckBox;
    @BindView(R.id.message_notification_notice_nouse_switch)
    CheckBox noUseCheckBox;
    @BindView(R.id.message_notification_notice_nouse_start)
    TextAndTextView noUseStartText;
    @BindView(R.id.message_notification_notice_nouse_end)
    TextAndTextView noUseEndText;

    @Override
    protected int getActivityView() {
        return R.layout.activity_message_notification;
    }

    @Override
    protected void initView() {
        super.initView();
    }
}
