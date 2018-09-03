package com.sdxxtop.zhidian.im;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;
import com.sdxxtop.zhidian.widget.TextAndTextView;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupMemberRoleType;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class GroupManagerActivity extends BaseActivity {
    public static final String TAG = "GroupManagerActivity";

    @BindView(R.id.group_manager_title_view)
    SubTitleView mTitleView;
    @BindView(R.id.group_manager_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.group_manager_right_image)
    View mManagerSkipView;
    @BindView(R.id.group_manager_transfer_owner)
    TextAndTextView transferOwner;
    private GroupManagerAdapter adapter;
    private String identify;
    private ArrayList<UserProfile> userProfiles;
    private boolean isManager;
    private boolean isGroupOwner;

    @Override
    protected int getActivityView() {
        return R.layout.activity_group_manager;
    }

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            identify = getIntent().getStringExtra("identify");
            isManager = getIntent().getBooleanExtra("isManager", false);
            isGroupOwner = getIntent().getBooleanExtra("isGroupOwner", false);
        }
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new GroupManagerAdapter(R.layout.item_group_manager_recycler);
        mRecyclerView.setAdapter(adapter);
    }

    boolean isDataSuccess;
    boolean isMaxAdmin;

    @Override
    protected void initEvent() {
        mManagerSkipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDataSuccess) {
                    return;
                }

                if (!isGroupOwner) {
                    Toast.makeText(mContext, "没有权限修改群管理", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isMaxAdmin) {
                    Toast.makeText(mContext, "此群的管理人数已满", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intentGroupMem = new Intent();
                intentGroupMem.setClassName("com.sdxxtop.zhidian", "com.sdxxtop.zhidian.im.ZDGroupMemberActivity");
                intentGroupMem.putExtra("identify", identify);
                intentGroupMem.putExtra("show_select", true);
                startActivityForResult(intentGroupMem, 11);
            }
        });

        transferOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGroupOwner) {
                    showToast("您不是群主，不能进行转让群主");
                    return;
                }

                Intent intentGroupMem = new Intent();
                intentGroupMem.setClassName("com.sdxxtop.zhidian", "com.sdxxtop.zhidian.im.ZDGroupMemberActivity");
                intentGroupMem.putExtra("identify", identify);
                intentGroupMem.putExtra("change_owner", true);
                startActivityForResult(intentGroupMem, 10);
            }
        });
    }

    @Override
    protected void initData() {
        getUserProfile();
    }

    private void getUserProfile() {
        TIMGroupManagerExt.getInstance().getGroupMembers(identify, new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int code, String msg) {
                Log.e(TAG, "code: " + code + " msg: " + msg);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                if (isFinishing()) {
                    return;
                }
                final List<String> users = new ArrayList<>();
                for (TIMGroupMemberInfo timGroupMemberInfo : timGroupMemberInfos) {
                    String user = timGroupMemberInfo.getUser();
                    users.add(user);
                    LogUtils.e(TAG, "onSuccess user = " + user);
                }

                final List<TIMGroupMemberInfo> tempGroupMemberInfos = timGroupMemberInfos;
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
                        if (isFinishing()) {
                            return;
                        }
                        for (TIMUserProfile timUserProfile : result) {
                            String identifier = timUserProfile.getIdentifier();
                            for (TIMGroupMemberInfo timGroupMemberInfo : tempGroupMemberInfos) {
                                String user = timGroupMemberInfo.getUser();
                                if (user.equals(identifier)) {
                                    TIMGroupMemberRoleType role = timGroupMemberInfo.getRole();
                                    //0是一般成员 TIMGroupMemberRoleType.Normal
                                    if (role == TIMGroupMemberRoleType.Owner) {
                                        timUserProfile.setRole(1L);
                                    } else if (role == TIMGroupMemberRoleType.Admin) {
                                        timUserProfile.setRole(2L);
                                    } else if (role == TIMGroupMemberRoleType.NotMember) {
                                        timUserProfile.setRole(3L);
                                    }
                                }
                            }
                        }

                        userProfiles = new ArrayList<>();
                        //群主
                        UserProfile owerProfile = null;
                        //管理员
                        ArrayList<UserProfile> adminProfileList = new ArrayList<>();
                        for (TIMUserProfile timUserProfile : result) {
                            UserProfile profile = new UserProfile();
                            profile.setFaceUrl(timUserProfile.getFaceUrl());
                            profile.setNickName(timUserProfile.getNickName());
                            profile.setIdentifier(timUserProfile.getIdentifier());
                            long role = timUserProfile.getRole();
                            profile.setRole(role);
                            if (role == 1L) {
                                owerProfile = profile;
                            } else if (role == 2L) {
                                adminProfileList.add(profile);
                            } else {
                                userProfiles.add(profile);
                            }
                        }

                        userProfiles.addAll(0, adminProfileList);

                        if (owerProfile != null) {
                            userProfiles.add(0, owerProfile);
                        }

                        adapter.setGroupOwner(isGroupOwner);
                        adapter.replaceData(adminProfileList);

                        closeProgressDialog();
                        mTitleView.setTitleValue("群管理 (" + userProfiles.size() + ")");

                        isDataSuccess = true;
                        isMaxAdmin = adminProfileList.size() == 7;
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 121) {
            //结束，修改了群主
            setResult(121);
            finish();
        } else if (requestCode == 11 && resultCode == 121) {
//            setResult(121);
//            finish();
            //修改管理员回来时要求刷新
            getUserProfile();
        }
    }

    private class GroupManagerAdapter extends BaseQuickAdapter<UserProfile, BaseViewHolder> {
        private boolean groupOwner;

        public GroupManagerAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final UserProfile item) {
            CircleImageView circleImg = helper.getView(R.id.item_group_manager_circle_image_view);
            TextView userText = helper.getView(R.id.item_group_manager_user_text);
            ImageView deleteImg = helper.getView(R.id.item_group_manager_img_delete);

            String nickName = item.getNickName();
            String faceUrl = item.getFaceUrl();
            ViewUtil.setColorItemView(faceUrl, nickName, userText, circleImg);
            if (groupOwner) {
                deleteImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeAdmin(item.getIdentifier());
                    }
                });
                deleteImg.setVisibility(View.VISIBLE);
            } else {
                deleteImg.setVisibility(View.GONE);
            }
        }

        public void setGroupOwner(boolean groupOwner) {
            this.groupOwner = groupOwner;
        }
    }

    private void removeAdmin(String identifier) {
        showProgressDialog("");
        TIMGroupManagerExt.ModifyMemberInfoParam modifyMemberInfoParam = new TIMGroupManagerExt.ModifyMemberInfoParam(identify, identifier);
        modifyMemberInfoParam.setRoleType(TIMGroupMemberRoleType.Normal);
        TIMGroupManagerExt.getInstance().modifyMemberInfo(modifyMemberInfoParam, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                closeProgressDialog();
            }

            @Override
            public void onSuccess() {
                if (adapter != null) {
                    getUserProfile();
                }
            }
        });
    }

}
