package com.tencent.qcloud.timchat.model;

import android.content.Context;
import android.view.View;

import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMGroupTipsElemGroupInfo;
import com.tencent.imsdk.TIMMessage;
import com.tencent.qcloud.timchat.MyApplication;
import com.tencent.qcloud.timchat.R;
import com.tencent.qcloud.timchat.adapters.ChatAdapter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 群tips消息
 */
public class GroupTipMessage extends Message {


    public GroupTipMessage(TIMMessage message){
        this.message = message;
    }


    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, Context context) {
        viewHolder.leftPanel.setVisibility(View.GONE);
        viewHolder.rightPanel.setVisibility(View.GONE);
        viewHolder.systemMessage.setVisibility(View.VISIBLE);
        viewHolder.systemMessage.setText(getSummary());
    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        final TIMGroupTipsElem e = (TIMGroupTipsElem) message.getElement(0);
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<String,TIMGroupMemberInfo>> iterator = e.getChangedGroupMemberInfo().entrySet().iterator();
        switch (e.getTipsType()){
            case CancelAdmin:
            case SetAdmin:
                return "群管理员变更";
//                return MyApplication.getContext().getString(R.string.summary_group_admin_change);
            case Join:
                return "群成员变更";
//                while(iterator.hasNext()){
//                    Map.Entry<String,TIMGroupMemberInfo> item = iterator.next();
//                    stringBuilder.append(getName(item.getValue()));
//                    stringBuilder.append(" ");
//                }
//                return stringBuilder +
//                        MyApplication.getContext().getString(R.string.summary_group_mem_add);
            case Kick:
                return "群成员变更";
//                return e.getUserList().get(0) +
//                        MyApplication.getContext().getString(R.string.summary_group_mem_kick);
            case ModifyMemberInfo:
                return MyApplication.getContext().getString(R.string.summary_group_info_change);
//                while(iterator.hasNext()){
//                    Map.Entry<String,TIMGroupMemberInfo> item = iterator.next();
//                    stringBuilder.append(getName(item.getValue()));
//                    stringBuilder.append(" ");
//                }
//                return stringBuilder +
//                        MyApplication.getContext().getString(R.string.summary_group_mem_modify);
            case Quit:
                return "群成员变更";
//                return e.getOpUser() +
//                        MyApplication.getContext().getString(R.string.summary_group_mem_quit);
            case ModifyGroupInfo:
                List<TIMGroupTipsElemGroupInfo> groupInfoList = e.getGroupInfoList();
                if (groupInfoList.size() > 0) {
                    switch (groupInfoList.get(0).getType()) {
                        case ModifyName:
                            return "群名称修改";
                        case ModifyIntroduction:
                            return "群介绍修改";
                        case ModifyNotification:
                            return "群公告修改";
                        case ModifyFaceUrl:
                            return "群头像修改";
                        case ModifyOwner:
                            return "群主转让";
                    }
                }
                return MyApplication.getContext().getString(R.string.summary_group_info_change);
        }
        return "";
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

    private String getName(TIMGroupMemberInfo info){
        if (info.getNameCard().equals("")){
            return info.getUser();
        }
        return info.getNameCard();
    }
}
