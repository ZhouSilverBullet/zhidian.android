package com.sdxxtop.zhidian.alipush;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.sdxxtop.zhidian.eventbus.PushCenterEvent;
import com.sdxxtop.zhidian.ui.activity.MineWorkDetailActivity;
import com.sdxxtop.zhidian.ui.activity.NoticeDetail2Activity;
import com.sdxxtop.zhidian.ui.activity.SubmissionActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/6/15.
 * 处理多个
 */

public class PushCenterActivity extends AppCompatActivity {

    private String extraMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initVariables();
    }

    protected void initVariables() {
        if (getIntent() != null) {
            extraMap = getIntent().getStringExtra("extraMap");
            if (!TextUtils.isEmpty(extraMap)) {
                startToAc(extraMap);
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {
            extraMap = intent.getStringExtra("extraMap");
            if (!TextUtils.isEmpty(extraMap)) {
                startToAc(extraMap);
            } else {
                finish();
            }
        }
    }

    private void startToAc(String extraMap) {
        try {
            JSONObject jsonObject = new JSONObject(extraMap);
            int company_id = jsonObject.optInt("company_id");
            if (company_id == 0) {
                company_id = -1;
            }
            if (jsonObject.has("notice_id")) { //公告的
                final int notice_id = jsonObject.optInt("notice_id");
                Intent intent = new Intent(this, NoticeDetail2Activity.class);
                intent.putExtra("ni", notice_id);
                intent.putExtra("company_id", company_id);
                startActivity(intent);
            } else if (jsonObject.has("apply_id")) { //申请
                final int apply_id = jsonObject.optInt("apply_id");
                final int at = jsonObject.optInt("apply_type");
                Intent intent = new Intent(this, SubmissionActivity.class);
                intent.putExtra("at", at);
                intent.putExtra("apply_id", apply_id + "");
                intent.putExtra("company_id", company_id);
                startActivity(intent);
            } else if (jsonObject.has("report_id")) {
                String report_id = jsonObject.optString("report_id");
                if (!TextUtils.isEmpty(report_id)) {
                    int reportId = Integer.parseInt(report_id);
                    MineWorkDetailActivity.startWorkDetailActivity(this, 0, reportId, company_id);
                }
            } else {
                finish();
            }
        } catch (JSONException e) {
            Log.e("MyMessageReceiver", e.getMessage());
            e.printStackTrace();
            finish();
        }
    }

    @Subscribe
    public void pushCenterEvent(PushCenterEvent event) {
        finish();
    }

    public static void startActivityToReceiver(final Context context, String extraMap) {
        Intent intent = new Intent();
        intent.setClassName("com.sdxxtop.zhidian", "com.sdxxtop.zhidian.alipush.PushCenterActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("extraMap", extraMap);
        context.startActivity(intent);

    }
}
