package com.sdxxtop.zhidian.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.MsgIndexBean;
import com.sdxxtop.zhidian.ui.activity.MessageCenterApprovalActivity;
import com.sdxxtop.zhidian.ui.activity.MessageCenterNoticeActivity;
import com.sdxxtop.zhidian.ui.activity.MineWorkListActivity;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.StringUtil;

/**
 * Created by Administrator on 2018/7/27.
 */

public class MessageCenterRecyclerAdapter extends BaseQuickAdapter<MsgIndexBean.DataBean, BaseViewHolder> {
    public MessageCenterRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MsgIndexBean.DataBean item) {
        ImageView centerImg = helper.getView(R.id.item_message_center_img);
        TextView redText = helper.getView(R.id.item_message_center_frame_text);
        TextView titleText = helper.getView(R.id.item_message_center_title);
        TextView contentText = helper.getView(R.id.item_message_center_content);
        TextView timeText = helper.getView(R.id.item_message_center_time);

        //1:申请 2:公告 3:工作汇报
        int type = item.getType();
        int count = 0;
        if (type == 1) {
            count = item.getApply_num();
            titleText.setText("我的审批");
            centerImg.setImageResource(R.drawable.message_approval);
            helper.convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MessageCenterApprovalActivity.class);
                    mContext.startActivity(intent);
                    item.setNum(0);
                    notifyDataSetChanged();

                    clickCallback();
                }
            });
        } else if (type == 2) {
            count = item.getNotice_num();
            titleText.setText("公告");
            centerImg.setImageResource(R.drawable.message_notice);

            helper.convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MessageCenterNoticeActivity.class);
                    mContext.startActivity(intent);
                    item.setNum(0);
                    notifyDataSetChanged();

                    clickCallback();
                }
            });
        } else if (type == 3) {
            count = item.getReport_num();
            titleText.setText("工作汇报");
            centerImg.setImageResource(R.drawable.work_report);

            helper.convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MineWorkListActivity.startMineWorkListActivity(mContext);
                    item.setNum(0);
                    notifyDataSetChanged();

                    clickCallback();
                }
            });
        }

        String unReadStr = String.valueOf(count);
        if (count <= 0) {
            redText.setVisibility(View.GONE);
        } else {
            if (count < 10) {
                redText.setBackgroundDrawable(mContext.getResources().getDrawable(com.tencent.qcloud.timchat.R.drawable.point1));
            } else {
                if (count > 99) {
                    redText.setBackgroundDrawable(mContext.getResources().getDrawable(com.tencent.qcloud.timchat.R.drawable.point2));
                    unReadStr = mContext.getResources().getString(com.tencent.qcloud.timchat.R.string.time_more);
                }
            }
            if (count > 99) {
                unReadStr = mContext.getResources().getString(com.tencent.qcloud.timchat.R.string.time_more);
            }
            redText.setVisibility(View.VISIBLE);
            redText.setText(unReadStr);
        }

        contentText.setText(StringUtil.stringNotNull(item.getTitle()));
        String update_time = item.getUpdate_time();
        if (!TextUtils.isEmpty(update_time)) {
            timeText.setText(DateUtil.getShowTime(update_time));
        }
    }

    private void clickCallback() {
        if (messageReadClickCallback != null) {
            messageReadClickCallback.readClick();
        }
    }

    public int getRedCount() {
        if (getData().size() == 0) {
            return 0;
        }
        int count = 0;
        for (MsgIndexBean.DataBean dataBean : getData()) {
//            if (dataBean.getType() == 1) {
                count = count + dataBean.getNum();
//            } else if (dataBean.getType() == 2) {
//                count = count + dataBean.getNotice_num();
//            } else if (dataBean.getType() == 3) {
//                count = count + dataBean.getReport_num();
//            }
        }
        return count;
    }

    private MessageReadClickCallback messageReadClickCallback;

    public void setMessageReadClickCallback(MessageReadClickCallback messageReadClickCallback) {
        this.messageReadClickCallback = messageReadClickCallback;
    }

    public interface MessageReadClickCallback {
        void readClick();
    }


}
