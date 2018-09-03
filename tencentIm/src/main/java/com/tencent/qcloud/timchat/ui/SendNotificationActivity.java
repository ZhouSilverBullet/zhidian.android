package com.tencent.qcloud.timchat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.tencent.qcloud.timchat.MyApplication;
import com.tencent.qcloud.timchat.R;
import com.tencent.qcloud.ui.TemplateTitle;
import com.xuxin.utils.StringUtil;

public class SendNotificationActivity extends BaseImActivity {

    private TemplateTitle titleView;
    private String identify;
    private EditText notificationEdit;
    private String noticeContent;
    private TextView notificationText;
    private boolean manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        Intent intent = getIntent();
        if (intent != null) {
            manager = intent.getBooleanExtra("manager", false);
            identify = intent.getStringExtra("identify");
            noticeContent = intent.getStringExtra("noticeContent");
        }
        initView();
    }

    private void initView() {

        notificationEdit = (EditText) findViewById(R.id.send_notification_edit);

        titleView = (TemplateTitle) findViewById(R.id.send_notification_title);
        notificationText = (TextView) findViewById(R.id.send_notification_text);
        if (manager) {
            notificationEdit.setVisibility(View.VISIBLE);
            notificationText.setVisibility(View.GONE);
        } else {
            notificationEdit.setVisibility(View.GONE);
            notificationText.setVisibility(View.VISIBLE);
            notificationText.setText(noticeContent);
            titleView.setMoreTextContext("");
        }

        notificationEdit.setText(noticeContent);
        notificationEdit.setSelection(noticeContent.length());
        titleView.setMoreTextAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
    }

    private void sendNotification() {
        final String content = notificationEdit.getText().toString();

        if (StringUtil.isEmptyWithTrim(content)) {
            Toast.makeText(SendNotificationActivity.this, "请输入公告内容", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(content) && content.equals("未设置")) {
            Toast.makeText(SendNotificationActivity.this, "公告设置成未设置", Toast.LENGTH_SHORT).show();
            return;
        }

        TIMGroupManagerExt.ModifyGroupInfoParam param = new TIMGroupManagerExt.ModifyGroupInfoParam(identify);
        param.setNotification(content);
        TIMGroupManagerExt.getInstance().modifyGroupInfo(param, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Toast.makeText(SendNotificationActivity.this, getString(R.string.chat_setting_change_err), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(MyApplication.getContext(), "群公告修改成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("content", content);
                setResult(101, intent);
                finish();
            }
        });
    }

    public static void startSendNotificationActivity(Activity context, String identify, String noticeContent, boolean manager) {
        Intent intent = new Intent(context, SendNotificationActivity.class);
        intent.putExtra("identify", identify);
        intent.putExtra("noticeContent", noticeContent);
        intent.putExtra("manager", manager);
        context.startActivityForResult(intent, 101);
    }
}
