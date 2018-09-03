package com.tencent.qcloud.timchat.model;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.qcloud.timchat.MyApplication;
import com.tencent.qcloud.timchat.R;
import com.tencent.qcloud.timchat.adapters.ChatAdapter;
import com.tencent.qcloud.timchat.utils.PreferenceUtils;
import com.xuxin.http.ConstantValue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 自定义消息
 */
public class CustomMessage extends Message {


    private String TAG = getClass().getSimpleName();

    private final int TYPE_TYPING = 14;
    private final int TYPE_REPORT = 1;

    private Type type;
    private String desc;
    private String data;

    public CustomMessage(TIMMessage message) {
        this.message = message;
        TIMCustomElem elem = (TIMCustomElem) message.getElement(0);
        parse(elem.getData());

    }

    public CustomMessage(Type type) {
        message = new TIMMessage();
        String data = "";
        JSONObject dataJson = new JSONObject();
        try {
            switch (type) {
                case TYPING:
                    dataJson.put("userAction", TYPE_TYPING);
                    dataJson.put("actionParam", "EIMAMSG_InputStatus_Ing");
                    data = dataJson.toString();
            }
        } catch (JSONException e) {
            Log.e(TAG, "generate json error");
        }
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(data.getBytes());
        message.addElement(elem);
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    private void parse(byte[] data) {
        type = Type.INVALID;
        try {
            String str = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(str);
            int action = jsonObj.getInt("userAction");
            switch (action) {
                case TYPE_REPORT:
                    type = Type.REPORT;
                    break;
                case TYPE_TYPING:
                    type = Type.TYPING;
                    this.data = jsonObj.getString("actionParam");
                    if (this.data.equals("EIMAMSG_InputStatus_End")) {
                        type = Type.INVALID;
                    }
                    break;
            }

        } catch (IOException | JSONException e) {
            Log.e(TAG, "parse json error");

        }
    }

    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, final Context context) {
        switch (type) {
            case REPORT:
                setReportView(viewHolder, context);
                break;
        }
        showStatus(viewHolder);
    }

    private void setReportView(ChatAdapter.ViewHolder viewHolder, final Context context) {
        clearView(viewHolder);
        if (checkRevoke(viewHolder)) return;
        View reportView = LayoutInflater.from(context).inflate(R.layout.message_report_view, null);
        final TIMCustomElem e = (TIMCustomElem) message.getElement(0);
        byte[] data = e.getData();
        if (data != null) {
            String s = new String(data);
            try {
                View rootLayout = reportView.findViewById(R.id.message_report_view_root_layout);
                View line = reportView.findViewById(R.id.message_report_view_line);
                int color = MyApplication.getContext().getResources().getColor(isSelf() ? R.color.chat_bubble_color : R.color.white);
                int lineColor = MyApplication.getContext().getResources().getColor(isSelf() ? R.color.white : R.color.im_line_color);
                rootLayout.setBackgroundColor(color);
                line.setBackgroundColor(lineColor);
                ChatBean chatBean = new Gson().fromJson(s, ChatBean.class);
                TextView titleText = (TextView) reportView.findViewById(R.id.message_report_view_title);
                TextView summaryText = (TextView) reportView.findViewById(R.id.message_report_view_summary);
                TextView planText = (TextView) reportView.findViewById(R.id.message_report_view_plan);
                TextView problemText = (TextView) reportView.findViewById(R.id.message_report_view_problem);
                RelativeLayout relativeLayout = (RelativeLayout) reportView.findViewById(R.id.message_report_view_relative);
                TextView reviewerText = (TextView) reportView.findViewById(R.id.message_report_view_reviewer);
                titleText.setText(chatBean.getTitle());
                final int type = chatBean.getType();
                String summaryDec = "";
                String planDec = "";
                String problemDec = "3.工作中的问题：";
                switch (type) {
                    case 1:
                        summaryDec = "1.今日工作总结：";
                        planDec = "2.明日工作计划：";
                        break;
                    case 2:
                        summaryDec = "1.本周工作总结：";
                        planDec = "2.下周工作计划：";
                        break;
                    default:
                        summaryDec = "1.本月工作总结：";
                        planDec = "2.下月工作计划：";
                        break;
                }

                summaryText.setText(summaryDec + chatBean.getSummary());
                planText.setText(planDec + chatBean.getPlan());
                String problem = chatBean.getProblem();
                if (!TextUtils.isEmpty(problem)) {
                    problemText.setVisibility(View.VISIBLE);
                    problemText.setText(problemDec + problem);
                } else {
                    problemText.setVisibility(View.GONE);
                }

                String reviewer_id = chatBean.getReviewer_id();
                if (!TextUtils.isEmpty(reviewer_id)) {
                    String[] split = reviewer_id.split(",");
                    String stringParam = PreferenceUtils.getInstance(context).getStringParam(ConstantValue.USER_ID);
                    boolean isReviewer = false;
                    for (String userId : split) {
                        if (!TextUtils.isEmpty(userId) && userId.equals(stringParam)) {
                            isReviewer = true;
                            break;
                        }
                    }
                    if (isReviewer) {
                        reviewerText.setText("批读");
                    } else {
                        reviewerText.setText("");
                    }
                } else {
                    reviewerText.setText("");
                }

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

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        String summary = getRevokeSummary();
        if (summary != null) return summary;
        switch (type) {
            case REPORT:
                summary = "[工作汇报]";
                break;
        }
        return summary;
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

    public enum Type {
        TYPING,
        INVALID,
        REPORT
    }
}
