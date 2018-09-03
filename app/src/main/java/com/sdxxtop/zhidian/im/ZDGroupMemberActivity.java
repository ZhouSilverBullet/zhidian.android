package com.sdxxtop.zhidian.im;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.GroupMemberAdapter;
import com.sdxxtop.zhidian.ui.activity.PartAndParentActivity;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.sdxxtop.zhidian.widget.BottomSelectorView;
import com.sdxxtop.zhidian.widget.SubTitleView;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupMemberRoleType;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;

public class ZDGroupMemberActivity extends BaseActivity {
    public static final String TAG = "ZDGroupMemberActivity";
    private String identify;

    @BindView(R.id.group_member_title_view)
    SubTitleView mTitleView;
    @BindView(R.id.group_member_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.group_member_search_background)
    RelativeLayout searchLayout;
    @BindView(R.id.group_member_selector_view)
    BottomSelectorView selectorBottomView;
    private GroupMemberAdapter mAdapter;
    private ArrayList<UserProfile> userProfiles;
    private boolean showSelect;
    private boolean changeOwner;
    private boolean isAdmin;
    private boolean isOwner;

    @Override
    protected int getActivityView() {
        return R.layout.activity_zdgroup_member;
    }

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            identify = getIntent().getStringExtra("identify");
            showSelect = getIntent().getBooleanExtra("show_select", false);
            changeOwner = getIntent().getBooleanExtra("change_owner", false);

            isAdmin = getIntent().getBooleanExtra("isAdmin", false);
            isOwner = getIntent().getBooleanExtra("isOwner", false);
        }

        if (showSelect) {
            mTitleView.getRightText().setVisibility(View.GONE);
            selectorBottomView.setVisibility(View.VISIBLE);
        } else {
            mTitleView.getRightText().setVisibility(View.VISIBLE);
            selectorBottomView.setVisibility(View.GONE);
        }

        if (changeOwner) {
            mTitleView.getRightText().setVisibility(View.GONE);
        }
    }

    @Override
    protected void initView() {
        mTitleView.setTitleValue("群成员");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new ItemDivider());

        mAdapter = new GroupMemberAdapter(R.layout.item_group_member_recycler);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setShowSelect(showSelect);
        mAdapter.setChangeOwner(changeOwner);
        mAdapter.setGroupId(identify);
        mAdapter.setAdmin(isAdmin);
        mAdapter.setOwner(isOwner);
        mAdapter.setBottomView(selectorBottomView);

        getUserProfile();
    }

    @Override
    protected void initEvent() {
        mTitleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PartAndParentActivity.class);
                intent.putExtra("identify", identify);
                startActivityForResult(intent, 102);
            }
        });

        mTitleView.getLeftText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRefreshFinish();
            }
        });

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userProfiles == null) {
                    return;
                }

                Intent intent = new Intent(mContext, ZDSearchGroupMemberActivity.class);
                intent.putExtra("userProfiles", userProfiles);
                intent.putExtra("show_select", showSelect);
                intent.putExtra("change_owner", changeOwner);
                intent.putExtra("identify", identify);
                if (showSelect) {
                    startActivityForResult(intent, 103);
                } else {
                    startActivityForResult(intent, 104);
                }
            }
        });

        if (showSelect) {
            selectorBottomView.getSubmitBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashSet<UserProfile> cacheSet = mAdapter.getCacheSet();
                    if (cacheSet.size() == 0) {
                        showToast("请选择成为管理员的成员");
                        return;
                    }

                    if (adminCount != 0) {
                        return;
                    }

                    for (UserProfile profile : cacheSet) {
                        setAdmin(profile, cacheSet.size());
                    }
                }
            });
        }

        mAdapter.setChangeOwnerListener(new GroupMemberAdapter.ChangeOwnerListener() {
            @Override
            public void onChange() {
                setResult(121);
                finish();
            }
        });

        mAdapter.setDeleteListener(new GroupMemberAdapter.DeleteListener() {
            @Override
            public void onDelete(int count) {
                mTitleView.setTitleValue("群成员(" + count + ")");
            }
        });
    }

    int adminCount;

    private void setAdmin(UserProfile profile, final int size) {
        TIMGroupManagerExt.ModifyMemberInfoParam modifyMemberInfoParam = new TIMGroupManagerExt.ModifyMemberInfoParam(identify, profile.getIdentifier());
        modifyMemberInfoParam.setRoleType(TIMGroupMemberRoleType.Admin);
        TIMGroupManagerExt.getInstance().modifyMemberInfo(modifyMemberInfoParam, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
                adminCount++;
                if (adminCount == size) {
                    adminCount = 0;
                    setResult(121);
                    finish();
                }
            }
        });

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

                        mAdapter.replaceData(userProfiles);
                        mAdapter.setAdminCount(adminProfileList.size());

                        mTitleView.setTitleValue("群成员(" + userProfiles.size() + ")");
                    }
                });
            }
        });
    }

    //加入人员的时候返回去的时候需要刷新
    boolean isRefreshData = false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 102 && resultCode == PartAndParentActivity.CUSTOMER_RESULT_OK && data != null) {
            getUserProfile();
            isRefreshData = true;
            //换群主成功
        } else if (requestCode == 104 && resultCode == 121) {
            setResult(121);
            finish();
            //添加管理员
        } else if (requestCode == 103 && resultCode == 122 && data != null) {
            String identifier = data.getStringExtra("identifier");
            if (!TextUtils.isEmpty(identifier)) {
                UserProfile tempProfile = null;
                for (UserProfile profile : mAdapter.getData()) {
                    if (identifier.equals(profile.getIdentifier())) {
                        if (!profile.isCheck()) {
                            if (mAdapter.isMaxSelectorAdmin()) {
                                Toast.makeText(this, "管理员不能超过7位", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        profile.setCheck(true);
                        tempProfile = profile;
                        break;
                    }
                }
                if (tempProfile != null) {
                    //刷新 相关数据源
                    mAdapter.refresh(tempProfile);
                    int indexOf = mAdapter.getData().indexOf(tempProfile);
                    if (indexOf != -1) {
                        mRecyclerView.scrollToPosition(indexOf);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        toRefreshFinish();
    }

    private void toRefreshFinish() {
        setResult(121);
        finish();
    }
}
