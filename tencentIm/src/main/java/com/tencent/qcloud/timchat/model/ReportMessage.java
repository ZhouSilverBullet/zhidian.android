package com.tencent.qcloud.timchat.model;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.qcloud.timchat.R;
import com.tencent.qcloud.timchat.adapters.ChatAdapter;

/**
 * Created by Administrator on 2018/7/30.
 */

public class ReportMessage extends Message {

    public ReportMessage(TIMMessage message) {
        this.message = message;
    }

    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, final Context context) {
        clearView(viewHolder);
        View reportView = LayoutInflater.from(context).inflate(R.layout.message_report_view, null);
        final TIMCustomElem e = (TIMCustomElem) message.getElement(0);
        byte[] data = e.getData();
        if (data != null) {
            String s = new String(data);
            try {
                ChatBean chatBean = new Gson().fromJson(s, ChatBean.class);
                TextView titleText = (TextView) reportView.findViewById(R.id.message_report_view_title);
                TextView summaryText = (TextView) reportView.findViewById(R.id.message_report_view_summary);
                TextView planText = (TextView) reportView.findViewById(R.id.message_report_view_plan);
                TextView problemText = (TextView) reportView.findViewById(R.id.message_report_view_problem);
                RelativeLayout relativeLayout = (RelativeLayout) reportView.findViewById(R.id.message_report_view_relative);
                titleText.setText(chatBean.getTitle());
                summaryText.setText(chatBean.getSummary());
                planText.setText(chatBean.getPlan());
                String problem = chatBean.getProblem();
                if (!TextUtils.isEmpty(problem)) {
                    problemText.setVisibility(View.VISIBLE);
                    problemText.setText(problem);
                } else {
                    problemText.setVisibility(View.GONE);
                }
                final int type = chatBean.getType();
                final int reportId = chatBean.getReport_id();
                relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClassName("com.sdxxtop.zhidian", "com.sdxxtop.zhidian.ui.activity.MineWorkDetailActivity");
                        intent.putExtra("work_detail_type", type);
                        intent.putExtra("report_id", reportId);
                        context.startActivity(intent);
                    }
                });
                getBubbleView(viewHolder).addView(reportView);
            } catch (Exception ss) {
            }
        }
    }

    @Override
    public String getSummary() {
        return null;
    }

    @Override
    public void save() {

    }
}
