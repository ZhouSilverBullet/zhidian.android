package com.tencent.qcloud.timchat.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMAddFriendRequest;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendStatus;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;
import com.tencent.qcloud.timchat.ui.ChatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/4.
 */

public class AddFriendUtil {
    public static final String TAG = "AddFriendUtil";

    /**
     * 添加好友
     *
     * @param identify 添加对象Identify
     * @param remark   备注名
     * @param group    分组
     * @param message  附加消息
     */
    public static void addFriend(final Context context, final String identify, String remark, String group, String message) {
        List<TIMAddFriendRequest> reqList = new ArrayList<>();
        TIMAddFriendRequest req = new TIMAddFriendRequest(identify);
        req.setAddWording(message);
        req.setRemark(remark);
        req.setFriendGroup(group);
        reqList.add(req);
        TIMFriendshipManagerExt.getInstance().addFriend(reqList, new TIMValueCallBack<List<TIMFriendResult>>() {

            @Override
            public void onError(int arg0, String arg1) {
                Log.e(TAG, "addFriend " + arg1);
            }

            @Override
            public void onSuccess(List<TIMFriendResult> resultList) {
                for (TIMFriendResult item : resultList) {
                    if (item.getIdentifer().equals(identify)) {
                        onAddFriend(context, identify, item.getStatus());
                        break;
                    }
                }
            }
        });
    }

    private static void onAddFriend(Context context, String identify, TIMFriendStatus status) {
        switch (status) {
            case TIM_ADD_FRIEND_STATUS_PENDING:
//                Toast.makeText(this, getResources().getString(com.tencent.qcloud.timchat.R.string.add_friend_succeed), Toast.LENGTH_SHORT).show();
//                finish();
                break;
            case TIM_FRIEND_STATUS_SUCC:
                ChatActivity.navToChat(context, identify, TIMConversationType.C2C);
//                Toast.makeText(this, getResources().getString(com.tencent.qcloud.timchat.R.string.add_friend_added), Toast.LENGTH_SHORT).show();
//                finish();
                break;
//            case TIM_ADD_FRIEND_STATUS_FRIEND_SIDE_FORBID_ADD:
//                Toast.makeText(this, getResources().getString(com.tencent.qcloud.timchat.R.string.add_friend_refuse_all), Toast.LENGTH_SHORT).show();
//                finish();
//                break;
//            case TIM_ADD_FRIEND_STATUS_IN_OTHER_SIDE_BLACK_LIST:
//                Toast.makeText(this, getResources().getString(com.tencent.qcloud.timchat.R.string.add_friend_to_blacklist), Toast.LENGTH_SHORT).show();
//                finish();
//                break;
            default:
                Toast.makeText(context, context.getString(com.tencent.qcloud.timchat.R.string.add_friend_error), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
