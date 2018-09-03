package com.sdxxtop.zhidian.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.utils.Date2Util;
import com.tencent.qcloud.timchat.model.Conversation;
import com.tencent.qcloud.timchat.model.NomalConversation;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/7/27.
 */

public class ConversationRecyclerAdapter extends BaseQuickAdapter<Conversation, BaseViewHolder> {
    public ConversationRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    public ConversationRecyclerAdapter(int layoutResId, @Nullable List<Conversation> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Conversation item) {
        TextView nameText = helper.getView(R.id.name);
        CircleImageView avatarImg = (CircleImageView) helper.getView(R.id.item_avatar_circle_img);
        TextView shortNameText = (TextView) helper.getView(R.id.item_conversation_short_name);
        TextView lastMessageText = (TextView) helper.getView(R.id.last_message);
        final TextView timeText = (TextView) helper.getView(R.id.message_time);
        TextView unreadText = (TextView) helper.getView(R.id.unread_num);
        helper.getConvertView().setBackgroundResource(R.drawable.recycler_item_selector);

        nameText.setText(item.getName());
        avatarImg.setImageDrawable(null);

        shortNameText.setText("");
        if (item instanceof NomalConversation) {
            ((NomalConversation) item).setImage(timeText, avatarImg, shortNameText, item);
        } else {
            avatarImg.setImageResource(item.getAvatar());
        }


        lastMessageText.setText(item.getLastMessageSummary());
//        timeText.setText(TimeUtil.getTimeStr(item.getLastMessageTime()));
        Date date = new Date(item.getLastMessageTime() * 1000);
        timeText.setText(Date2Util.getWeixinShowTime0(date));
        long unRead = item.getUnreadNum();
        if (unRead <= 0) {
            unreadText.setVisibility(View.INVISIBLE);
        } else {
            unreadText.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unRead);
            if (unRead < 10) {
                unreadText.setBackgroundDrawable(mContext.getResources().getDrawable(com.tencent.qcloud.timchat.R.drawable.point1));
            } else {
                if (unRead > 99) {
                    unreadText.setBackgroundDrawable(mContext.getResources().getDrawable(com.tencent.qcloud.timchat.R.drawable.point2));
                    unReadStr = mContext.getResources().getString(com.tencent.qcloud.timchat.R.string.time_more);
                }
            }
            unreadText.setText(unReadStr);
        }

        if (item.isTop()) {
            helper.getConvertView().setBackgroundResource(R.drawable.recycler_item_place_top_selector);
        } else {
            helper.getConvertView().setBackgroundResource(R.drawable.recycler_item_selector);
        }

        helper.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(helper.getLayoutPosition() - getHeaderLayoutCount());
                return false;
            }
        });
    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
