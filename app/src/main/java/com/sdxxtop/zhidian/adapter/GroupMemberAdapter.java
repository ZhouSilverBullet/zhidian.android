package com.sdxxtop.zhidian.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.im.UserProfile;
import com.sdxxtop.zhidian.ui.activity.ContactDetailActivity;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.BottomSelectorView;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.tencent.imsdk.ext.group.TIMGroupMemberResult;
import com.tencent.qcloud.timchat.model.UserInfo;
import com.xuxin.utils.StringUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import zhangphil.iosdialog.widget.AlertDialog;

/**
 * Created by Administrator on 2018/8/4.
 */

public class GroupMemberAdapter extends BaseQuickAdapter<UserProfile, BaseViewHolder> {
    private HashSet<UserProfile> cacheSet = new HashSet<>();
    private boolean isAdmin;
    private boolean isOwner;
    private int adminCount;

    public GroupMemberAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final UserProfile item) {
        final SwipeMenuLayout swipeMenuLayout = helper.getView(R.id.group_member_swipe_menu_layout);
        Button deleteBtn = helper.getView(R.id.group_member_delete_btn);
        LinearLayout rootLayout = helper.getView(R.id.group_member_root_layout);
        CircleImageView circleImage = helper.getView(R.id.item_group_member_circle_img);
        TextView shortNameText = helper.getView(R.id.item_group_member_short_name);
        TextView userNameText = helper.getView(R.id.item_group_member_user_name);
        TextView positionText = helper.getView(R.id.item_group_member_position);
        LinearLayout checkboxLayout = helper.getView(R.id.item_group_member_checkbox_layout);
        final CheckBox checkbox = helper.getView(R.id.item_group_member_checkbox);
        final ImageView rightIcon = helper.getView(R.id.item_group_member_right_icon);

        String identifier = item.getIdentifier();
        String name = item.getNickName();
        if (!TextUtils.isEmpty(identifier)) {
            boolean isParent = identifier.startsWith("p");
            if (isParent) {
                name = StringUtil.getParentIconName(name);
                if (TextUtils.isEmpty(name)) {
                    name = item.getNickName();
                }
            }
        }
        ViewUtil.setColorItemView(item.getFaceUrl(), name, shortNameText, circleImage);

        userNameText.setText(item.getNickName());

        long role = item.getRole();

        if (role == 1L) {
            positionText.setText("群主");
        } else if (role == 2L) {
            positionText.setText("管理员");
        } else {
            positionText.setText("");
        }

        LogUtils.e("GroupMemberAdapter", " convert role = " + role);
        if (showSelect) {
            swipeMenuLayout.setSwipeEnable(false);
            if (role == 1L || role == 2L) {
                checkboxLayout.setVisibility(View.GONE);
                rootLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            } else {
                if (bottomView != null) {
                    checkboxLayout.setVisibility(View.VISIBLE);
                }
                final boolean check = item.isCheck();
                checkbox.setChecked(check);
                rootLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isAdd = !check;
                        if (bottomView != null) {
                            if (isMaxSelectorAdmin() && isAdd) {
                                Toast.makeText(v.getContext(), "管理员不能超过7位", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            checkbox.setChecked(isAdd);

                            refreshCacheSet(isAdd, item, item.getIdentifier());
                            refreshBottomData(isAdd, item, item.getIdentifier());
                        } else {
                            checkbox.setChecked(isAdd);
                            cacheSet.add(item);
                        }

                        item.setCheck(isAdd);

                        notifyDataSetChanged();
                        if (selectAdminListener != null) {
                            selectAdminListener.onSelect();
                        }
                    }
                });
            }
        } else {
            if (!TextUtils.isEmpty(identifier) && identifier.equals(UserInfo.getInstance().getId())) {
                //如果是自己不进行侧滑
                swipeMenuLayout.setSwipeEnable(false);
            } else {
                if (isAdmin || isOwner) {
                    if (isAdmin) {
                        if (role == 1L || role == 2L) {
                            swipeMenuLayout.setSwipeEnable(false);
                        } else {
                            swipeMenuLayout.setSwipeEnable(true);
                        }
                    } else {
                        swipeMenuLayout.setSwipeEnable(true);
                    }
                } else {
                    swipeMenuLayout.setSwipeEnable(false);
                }
            }

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletePeopleGroup(item, swipeMenuLayout);
                }
            });

            rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (swipeMenuLayout.isExpand()) {
                        swipeMenuLayout.smoothClose();
                        return;
                    }
                    if (changeOwner) {
                        changeOwnerDialog(item);
                    } else {
                        skipContactDetailActivity(item);
                    }
                }
            });
        }

    }

    private void refreshBottomData(boolean isAdd, UserProfile item, String identifier) {
        if (bottomView == null) {
            return;
        }
        if (isAdd) {
            List<SelectBean> list = bottomView.getList();
            for (SelectBean bean : list) {
                if (bean.getIdentify().equals(identifier)) {
                    return;
                }
            }
            SelectBean bean = new SelectBean();
            bean.setName(item.getNickName());
            bean.setColor(item.getFaceUrl());
            bean.setIdentify(identifier);
            bean.setType(SelectBean.TYPE_USER);
            list.add(bean);
        } else {
            List<SelectBean> list = bottomView.getList();
            SelectBean tempBean = null;
            for (SelectBean bean : list) {
                if (bean.getIdentify().equals(identifier)) {
                    tempBean = bean;
                    break;
                }
            }

            if (tempBean != null) {
                list.remove(tempBean);
            }
        }

        bottomView.refreshData();
    }

    private void refreshCacheSet(boolean isAdd, UserProfile item, String identifier) {
        UserProfile tempProfile = null;
        for (UserProfile profile : cacheSet) {
            if (profile.getIdentifier().equals(identifier)) {
                tempProfile = profile;
                break;
            }
        }

        if (isAdd && tempProfile == null) {
            cacheSet.add(item);
        }

        if (!isAdd && tempProfile != null) {
            cacheSet.remove(tempProfile);
        }
    }

    private void changeOwnerDialog(final UserProfile item) {
        new AlertDialog(mContext).builder().setTitle("提示")
                .setMsg("确定选择  " + item.getNickName() + "  为新群主，你将自动放弃群主身份")
                .setPositiveButton("", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //确定就修改群主
                        changeOwner(item);
                    }
                }).setNegativeButton("", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }).show();
    }

    private void changeOwner(final UserProfile item) {
        TIMGroupManagerExt.getInstance().modifyGroupOwner(groupId, item.getIdentifier(), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Toast.makeText(mContext, "修改群主失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "修改群主成功", Toast.LENGTH_SHORT).show();
                if (changeOwnerListener != null) {
                    changeOwnerListener.onChange();
                }
            }
        });
    }

    private void deletePeopleGroup(final UserProfile item, final SwipeMenuLayout swipeMenuLayout) {
        TIMGroupManagerExt.DeleteMemberParam param = new TIMGroupManagerExt.DeleteMemberParam(groupId, Collections.singletonList(item.getIdentifier()));
        param.setReason("");
        TIMGroupManagerExt.getInstance().deleteGroupMember(param, new TIMValueCallBack<List<TIMGroupMemberResult>>() {
            @Override
            public void onError(int code, String desc) {
                Log.e(TAG, "deleteGroupMember onErr. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberResult> results) { //群组成员操作结果
                if (swipeMenuLayout != null) {
                    swipeMenuLayout.quickClose();
                }
//                for (TIMGroupMemberResult r : results) {
//                    Log.d(TAG, "result: " + r.getResult()  //操作结果:  0：删除失败；1：删除成功；2：不是群组成员
//                            + " user: " + r.getUser());    //用户帐号
//                }
                getData().remove(item);
                notifyDataSetChanged();
                if (deleteListener != null) {
                    deleteListener.onDelete(getData().size());
                }
            }
        });

    }

    public HashSet<UserProfile> getCacheSet() {
        return cacheSet;
    }

    private void skipContactDetailActivity(UserProfile item) {
        String userId;
        String studentId;
        String identifier = item.getIdentifier();
        boolean isTeacher = identifier.startsWith("t");
        if (isTeacher) {
            userId = identifier.substring(8, identifier.length());
            studentId = "";
        } else {
            studentId = identifier.substring(1, 9);
            userId = identifier.substring(9, identifier.length());
        }

        Intent intent = new Intent(mContext, ContactDetailActivity.class);

        if (isTeacher) {
            intent.putExtra("userId", userId);
            intent.putExtra("type", 11);
        } else {
            intent.putExtra("userId", userId);
            intent.putExtra("studentId", studentId);
            intent.putExtra("type", 10);
        }

        mContext.startActivity(intent);
    }

    public boolean isMaxSelectorAdmin() {
        return cacheSet.size() == 7 - adminCount;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public void setOwner(boolean owner) {
        this.isOwner = owner;
    }

    private String groupId;
    private boolean showSelect;
    private boolean changeOwner;
    private BottomSelectorView bottomView;

    public void setShowSelect(boolean showSelect) {
        this.showSelect = showSelect;
    }

    public void setChangeOwner(boolean changeOwner) {
        this.changeOwner = changeOwner;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    ChangeOwnerListener changeOwnerListener;

    public void setChangeOwnerListener(ChangeOwnerListener changeOwnerListener) {
        this.changeOwnerListener = changeOwnerListener;
    }

    public void setBottomView(BottomSelectorView bottomView) {
        this.bottomView = bottomView;
        bottomView.setBottomImRemoveClickListener(new BottomSelectorView.BottomImRemoveClickListener() {
            @Override
            public void onRemoveClick(String identify) {
                refreshCacheSet(false, null, identify);
                refreshBottomData(false, null, identify);

                for (UserProfile profile : getData()) {
                    if (profile.getIdentifier().equals(identify)) {
                        profile.setCheck(false);
                        break;
                    }
                }

                notifyDataSetChanged();
            }
        });
    }

    public void refresh(UserProfile item) {
        String identifier = item.getIdentifier();
        refreshCacheSet(true, item, identifier);
        refreshBottomData(true, item, identifier);

        for (UserProfile profile : getData()) {
            if (profile.getIdentifier().equals(identifier)) {
                profile.setCheck(true);
                break;
            }
        }

        notifyDataSetChanged();
    }

    public void setAdminCount(int adminCount) {
        this.adminCount = adminCount;
    }


    public interface ChangeOwnerListener {
        void onChange();
    }

    SelectAdminListener selectAdminListener;

    public void setSelectAdminListener(SelectAdminListener selectAdminListener) {
        this.selectAdminListener = selectAdminListener;
    }

    public interface SelectAdminListener {
        void onSelect();
    }

    private DeleteListener deleteListener;

    public void setDeleteListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public interface DeleteListener {
        void onDelete(int count);
    }
}
