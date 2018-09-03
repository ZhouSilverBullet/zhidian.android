package com.tencent.qcloud.timchat.model;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.qcloud.timchat.MyApplication;
import com.tencent.qcloud.timchat.R;
import com.tencent.qcloud.timchat.ui.ChatActivity;
import com.tencent.qcloud.timchat.utils.ViewUtil;
import com.xuxin.utils.StringUtil;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 好友或群聊的会话
 */
public class NomalConversation extends Conversation {


    private TIMConversation conversation;


    //最后一条消息
    private Message lastMessage;


    public NomalConversation(TIMConversation conversation) {
        this.conversation = conversation;
        type = conversation.getType();
        identify = conversation.getPeer();
    }


    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }


    @Override
    public int getAvatar() {
        switch (type) {
            case C2C:
                return R.drawable.head_other;
            case Group:
                return R.drawable.group;
        }
        return 0;
    }

    public void setImage(final TextView timeText, final CircleImageView avatarImg, final TextView shortNameText, final Conversation item) {
        timeText.setTag(item);
        switch (type) {
            case C2C:
                TIMFriendshipManager.getInstance().getUsersProfile(Collections.singletonList(getIdentify()), new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                        if (timUserProfiles != null && timUserProfiles.size() == 1) {
                            TIMUserProfile profile = timUserProfiles.get(0);
                            String faceUrl = "";
                            String identifier = "";
                            if (profile != null) {
                                faceUrl = profile.getFaceUrl();
                                identifier = profile.getIdentifier();
                            }
                            if ((timeText.getTag()).equals(item)) {
                                if (!TextUtils.isEmpty(faceUrl) && !TextUtils.isEmpty(identifier)) {
                                    boolean isParent = identifier.startsWith("p");
                                    String name = getName();
                                    if (isParent) {
                                        if (profile != null) {
                                            name = StringUtil.getParentIconName(profile.getNickName());
                                            if (TextUtils.isEmpty(name)) {
                                                name = getName();
                                            }
                                        } else {
                                            name = name.split("家长")[0];
                                        }
                                    }
                                    ViewUtil.setColorItemView(faceUrl, name, shortNameText, avatarImg);
                                } else {
                                    avatarImg.setImageResource(getAvatar());
                                }
                            }
                        }
                    }
                });
                break;
            case Group:
                TIMGroupManagerExt.getInstance().getGroupDetailInfo(Collections.singletonList(getIdentify()), new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess(List<TIMGroupDetailInfo> timGroupDetailInfos) {
                        if (timGroupDetailInfos != null && timGroupDetailInfos.size() == 1) {
                            TIMGroupDetailInfo info = timGroupDetailInfos.get(0);
                            String faceUrl = info.getFaceUrl();
                            if ((timeText.getTag()).equals(item)) {
                                if (!TextUtils.isEmpty(faceUrl)) {
                                    Glide.with(timeText.getContext()).load(faceUrl).into(avatarImg);
                                } else {
                                    avatarImg.setImageResource(getAvatar());
                                }
                            }
                        }
                    }
                });
                break;
        }

    }

    /**
     * 跳转到聊天界面或会话详情
     *
     * @param context 跳转上下文
     */
    @Override
    public void navToDetail(Context context) {
        ChatActivity.navToChat(context, identify, type);
    }

    /**
     * 获取最后一条消息摘要
     */
    @Override
    public CharSequence getLastMessageSummary() {
        TIMConversationExt ext = new TIMConversationExt(conversation);
        if (ext.hasDraft()) {
            TextMessage textMessage = new TextMessage(ext.getDraft());
            if (lastMessage == null || lastMessage.getMessage().timestamp() < ext.getDraft().getTimestamp()) {
                String draft = MyApplication.getContext().getString(R.string.conversation_draft);
                CharSequence summary = textMessage.getSummary();
                String s = draft + summary;
                if (!TextUtils.isEmpty(s)) {
                    SpannableStringBuilder builder = new SpannableStringBuilder(s);
                    ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#FA4848"));
                    builder.setSpan(redSpan, 0, draft.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    if (!TextUtils.isEmpty(summary) && summary.length() > 0) {
                        builder.append(summary);
//                        builder.setSpan(blackSpan, draft.length() + 1, s.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
                    return builder;
                }
                return s;
            } else {
                return lastMessage.getSummary();
            }
        } else {
            if (lastMessage == null) return "";
            return lastMessage.getSummary();
        }
    }

    /**
     * 获取名称
     */
    @Override
    public String getName() {
        if (type == TIMConversationType.Group) {
            name = GroupInfo.getInstance().getGroupName(identify);
            if (name.equals("")) name = identify;
        } else {
            FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
            name = profile == null ? identify : profile.getName();
        }
        return name;
    }


    /**
     * 获取未读消息数量
     */
    @Override
    public long getUnreadNum() {
        if (conversation == null) return 0;
        TIMConversationExt ext = new TIMConversationExt(conversation);
        return ext.getUnreadMessageNum();
    }

    /**
     * 将所有消息标记为已读
     */
    @Override
    public void readAllMessage() {
        if (conversation != null) {
            TIMConversationExt ext = new TIMConversationExt(conversation);
            ext.setReadMessage(null, null);
        }
    }


    /**
     * 获取最后一条消息的时间
     */
    @Override
    public long getLastMessageTime() {
        TIMConversationExt ext = new TIMConversationExt(conversation);
        if (lastMessage == null) {
            return 0;
        }
        TIMMessage message = lastMessage.getMessage();
        if (ext.hasDraft()) {
            if (lastMessage == null || message.timestamp() < ext.getDraft().getTimestamp()) {
                return ext.getDraft().getTimestamp();
            } else {
                return message.timestamp();
            }
        }
        if (lastMessage == null) return 0;
        if (message == null) {
            return 0;
        }
        return message.timestamp();
    }

    /**
     * 获取会话类型
     */
    public TIMConversationType getType() {
        return conversation.getType();
    }

}
