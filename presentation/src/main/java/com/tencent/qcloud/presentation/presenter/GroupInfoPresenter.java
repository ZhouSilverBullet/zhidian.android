package com.tencent.qcloud.presentation.presenter;


import android.util.Log;

import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.tencent.qcloud.presentation.viewfeatures.GroupInfoView;

import java.util.ArrayList;
import java.util.List;


/**
 * 群信息逻辑
 */
public class GroupInfoPresenter implements TIMValueCallBack<List<TIMGroupDetailInfo>> {

    public static final String TAG = "GroupInfoPresenter";

    private GroupInfoView view;
    private boolean isInGroup;
    private List<String> groupIds;

    public GroupInfoPresenter(GroupInfoView view, List<String> groupIds, boolean isInGroup) {
        this.view = view;
        this.isInGroup = isInGroup;
        this.groupIds = groupIds;
    }


    public void getGroupDetailInfo() {
        if (isInGroup) {
            TIMGroupManagerExt.getInstance().getGroupDetailInfo(groupIds, this);
        } else {
            TIMGroupManagerExt.getInstance().getGroupPublicInfo(groupIds, this);
        }

        getUserProfile();
    }

    private void getUserProfile() {
        TIMGroupManagerExt.getInstance().getGroupMembers(groupIds.get(0), new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int code, String msg) {
                Log.e(TAG, "code: " + code + " msg: " + msg);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                List<String> users = new ArrayList<>();
                for (TIMGroupMemberInfo timGroupMemberInfo : timGroupMemberInfos) {
                    String user = timGroupMemberInfo.getUser();
                    users.add(user);
                }
                TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int code, String desc) {
                        //错误码 code 和错误描述 desc，可用于定位请求失败原因
                        //错误码 code 列表请参见错误码表
                        Log.e(TAG, "getUsersProfile failed: " + code + " desc");
                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> result) {
                        Log.e(TAG, "getUsersProfile succ");
//                        for (TIMUserProfile res : result) {
//                            Log.e(TAG, "identifier: " + res.getIdentifier() + " nickName: " + res.getNickName()
//                                    + " remark: " + res.getRemark());
//                        }
                        view.showUserProfile(result);
                    }
                });
            }
        });
    }


    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onSuccess(List<TIMGroupDetailInfo> timGroupDetailInfos) {
        view.showGroupInfo(timGroupDetailInfos);
    }
}
