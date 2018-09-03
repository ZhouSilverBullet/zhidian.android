package com.sdxxtop.zhidian.utils;

import android.text.TextUtils;

import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.xuxin.entry.ParentUserBean;
import com.xuxin.utils.CollectUtil;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Administrator on 2018/8/2.
 */

public class PartAndParentHelper {
    public static void toAddPeople(String identify, String myIdentify, HashSet<Integer> userSet, HashSet<Integer> partSet,
                                   HashSet<Integer> typeSet, HashMap<String,
            ParentUserBean> parentMap, IRequestListener<BaseModel> listener) {
        String userSetString = CollectUtil.getIntegerSetToString(userSet);
        String partSetString = CollectUtil.getIntegerSetToString(partSet);
        String typeSetString = CollectUtil.getIntegerSetToString(typeSet);
        String parentMapString = CollectUtil.getParentMapToString(parentMap);

        joinGroup(identify, myIdentify, userSetString, partSetString, typeSetString, parentMapString, listener);
    }

    private static void joinGroup(String identify, String myIdentify, String selectUserId, String selectPartId, String selectTypeId, String parentMapId, final IRequestListener<BaseModel> listener) {
        Params params = new Params();
        String path;

        //当前人的id
        params.put("oi", params.getUserId());
        params.put("ui", selectUserId);
        params.put("pi", selectPartId);
        params.put("ti", selectTypeId);
        params.put("sui", parentMapId);

        //这个是两个好友进行加群
        if (!TextUtils.isEmpty(myIdentify) && !TextUtils.isEmpty(identify)) {
            boolean isTeacher = myIdentify.startsWith("t");
            String[] split = selectUserId.split(",");
            String[] split1 = parentMapId.split(",");
            if (isTeacher) {
                String userId = myIdentify.substring(8, myIdentify.length());
                if (!isContainInArray(split, userId)) {
                    selectUserId = selectUserId + "," + userId;
                }
                params.put("ui", selectUserId);
            } else {
                String studentId = myIdentify.substring(1, 9);
                String userId = myIdentify.substring(9, myIdentify.length());
                String studentUserId = studentId + "|" + userId;
                if (!isContainInArray(split1, studentUserId)) {
                    parentMapId = parentMapId + "," + studentUserId;
                }
                params.put("sui", parentMapId);
            }

            isTeacher = identify.startsWith("t");
            if (isTeacher) {
                String userId = identify.substring(8, identify.length());
                if (!isContainInArray(split, userId)) {
                    selectUserId = selectUserId + "," + userId;
                }
                params.put("ui", selectUserId);
            } else {
                String studentId = identify.substring(1, 9);
                String userId = identify.substring(9, identify.length());
                String studentUserId = studentId + "|" + userId;
                if (!isContainInArray(split1, studentUserId)) {
                    parentMapId = parentMapId + "," + studentUserId;
                }
                params.put("sui", parentMapId);
            }

            path = "createGroup";
        } else if (!TextUtils.isEmpty(identify)) {
            params.put("gi", identify);
            path = "addGroupMember";
        } else {
            path = "createGroup";
        }

        RequestUtils.createRequest().postTimGroup(path, params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                ToastUtil.show("成功");
                if (listener != null) {
                    listener.onSuccess(baseModel);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                if (listener != null) {
                    listener.onFailure(code, errorMsg);
                }
            }
        }));
    }

    private static boolean isContainInArray(String[] split, String userId) {
        if (split == null || split.length == 0) {
            return false;
        }

        if (TextUtils.isEmpty(userId)) {
            return true;
        }

        for (String s : split) {
            if (userId.equals(s)) {
                return false;
            }
        }

        return false;
    }
}
