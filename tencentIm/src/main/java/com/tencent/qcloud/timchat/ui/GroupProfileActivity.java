package com.tencent.qcloud.timchat.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.permissions.RxPermissions;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMGroupAddOpt;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberRoleType;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.tencent.qcloud.presentation.presenter.GroupInfoPresenter;
import com.tencent.qcloud.presentation.viewfeatures.GroupInfoView;
import com.tencent.qcloud.timchat.R;
import com.tencent.qcloud.timchat.model.GroupInfo;
import com.tencent.qcloud.timchat.model.UserInfo;
import com.tencent.qcloud.timchat.ui.customview.QuitGroupPopView;
import com.tencent.qcloud.timchat.utils.IMAccountPicHelper;
import com.tencent.qcloud.timchat.utils.PlacedTopUtil;
import com.tencent.qcloud.timchat.utils.ViewUtil;
import com.tencent.qcloud.ui.FixedWidthLayout;
import com.tencent.qcloud.ui.TemplateTitle;
import com.tencent.qcloud.ui.TextAndSwitchView;
import com.tencent.qcloud.ui.TextAndTextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;


public class GroupProfileActivity extends BaseImActivity implements GroupInfoView, View.OnClickListener {

    private final String TAG = "GroupProfileActivity";

    private String identify, type;
    private GroupInfoPresenter groupInfoPresenter;
    private TIMGroupDetailInfo info;
    private boolean isInGroup;
    private boolean isGroupOwner;
    private final int REQ_CHANGE_NAME = 100, REQ_CHANGE_INTRO = 200;
    private TIMGroupMemberRoleType roleType = TIMGroupMemberRoleType.NotMember;
    private Map<String, TIMGroupAddOpt> allowTypeContent;
    //    private Map<Integer, TIMGroupReceiveMessageOpt> messageOptContent;
    private SparseArray<TIMGroupReceiveMessageOpt> messageOptContent;
    private TextAndTextView name;
    //    private LineControllerView intro;
    private TemplateTitle titleView;
    private FixedWidthLayout fixedWidthLayout;
    private TextAndTextView noticeText;
    private View newAddLayout;
    private CircleImageView circleImage;
    private IMAccountPicHelper accountPicHelper;
    private TextAndTextView adminSetting;
    private RxPermissions rxPermissions;
    private TextView quitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_setting);

        rxPermissions = new RxPermissions(this);

        identify = getIntent().getStringExtra("identify");
        isInGroup = GroupInfo.getInstance().isInGroup(identify);
        groupInfoPresenter = new GroupInfoPresenter(this, Collections.singletonList(identify), isInGroup);
        groupInfoPresenter.getGroupDetailInfo();
        name = (TextAndTextView) findViewById(R.id.nameText);
        titleView = (TemplateTitle) findViewById(R.id.chat_title);

        titleView.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRefresh();
            }
        });

        fixedWidthLayout = (FixedWidthLayout) findViewById(R.id.fixed_width_layout);
        newAddLayout = findViewById(R.id.new_add_layout);

        newAddLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.sdxxtop.zhidian", "com.sdxxtop.zhidian.ui.activity.PartAndParentActivity");
                intent.putExtra("activity", "group_profile_activity");
                intent.putExtra("activity_type", 1);
                intent.putExtra("identify", identify);
                startActivityForResult(intent, 102);
            }
        });

        circleImage = (CircleImageView) findViewById(R.id.group_circle_image);

        loadGroupImage();

        circleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCameraDialog();
            }
        });

        adminSetting = (TextAndTextView) findViewById(R.id.admin_setting_text);
        adminSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGroupMem = new Intent();
                intentGroupMem.setClassName("com.sdxxtop.zhidian", "com.sdxxtop.zhidian.im.GroupManagerActivity");
                intentGroupMem.putExtra("identify", identify);
                intentGroupMem.putExtra("type", type);
                intentGroupMem.putExtra("isManager", isManager());
                intentGroupMem.putExtra("isGroupOwner", isGroupOwner);
                startActivityForResult(intentGroupMem, 10);
            }
        });

        quitBtn = (TextView) findViewById(R.id.quit_btn);
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGroupOwner) {
                    deleteGroup();
                    return;
                }

                quit();
            }
        });
    }

    private void quit() {
        QuitGroupPopView popView = new QuitGroupPopView(this, findViewById(R.id.group_chat_root_layout),
                "确认退出群聊", "退出后不会通知群聊中其他成员，且不再接收此群聊消息");
        popView.setQuitListener(new Runnable() {
            @Override
            public void run() {
                //退出
                TIMGroupManager.getInstance().quitGroup(identify, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Log.e(TAG, "退群失败 i = " + i + " s = " + s);
                    }

                    @Override
                    public void onSuccess() {
                        setResult(121);
                        finish();
                    }
                });
            }
        });
    }

    private void deleteGroup() {
        QuitGroupPopView popView = new QuitGroupPopView(this, findViewById(R.id.group_chat_root_layout),
                "确认解散群聊", "");
        popView.setQuitListener(new Runnable() {
            @Override
            public void run() {
                //解散群组
                TIMGroupManager.getInstance().deleteGroup(identify, new TIMCallBack() {
                    @Override
                    public void onError(int code, String desc) {
                        //错误码 code 和错误描述 desc，可用于定位请求失败原因
                        //错误码 code 列表请参见错误码表
                        Log.d(TAG, "login failed. code: " + code + " errmsg: " + desc);
                    }

                    @Override
                    public void onSuccess() {
                        //解散群组成功
                        setResult(121);
                        finish();
                    }
                });
            }
        });

    }

    private void showCameraDialog() {
        if (!isManager()) {
            return;
        }
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    if (accountPicHelper == null) {
                        accountPicHelper = new IMAccountPicHelper(GroupProfileActivity.this, identify, type);
                    }
                    accountPicHelper.show();
                } else {
                    Toast.makeText(GroupProfileActivity.this, "请打打开相机权限再操作", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadGroupImage() {
        TIMGroupManagerExt.getInstance().getGroupDetailInfo(Collections.singletonList(identify), new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMGroupDetailInfo> timGroupDetailInfos) {
                if (timGroupDetailInfos != null && timGroupDetailInfos.size() == 1 && !isFinishing()) {
                    TIMGroupDetailInfo info = timGroupDetailInfos.get(0);
                    String faceUrl = info.getFaceUrl();
                    if (!TextUtils.isEmpty(faceUrl)) {
                        Glide.with(GroupProfileActivity.this).load(faceUrl).into(circleImage);
                    } else {
                        circleImage.setImageResource(R.drawable.head_group);
                    }
                }
            }
        });
    }

    /**
     * 显示群资料
     *
     * @param groupInfos 群资料信息列表
     */
    @Override
    public void showGroupInfo(List<TIMGroupDetailInfo> groupInfos) {
        info = groupInfos.get(0);
        final String selfIdentify = UserInfo.getInstance().getId();
        isGroupOwner = info.getGroupOwner().equals(selfIdentify);

        View rightImage = findViewById(R.id.group_right_image);
        rightImage.setVisibility(isManager() ? View.VISIBLE : View.INVISIBLE);

        if (isGroupOwner) {
            adminSetting.setVisibility(View.VISIBLE);
            quitBtn.setVisibility(View.VISIBLE);
            quitBtn.setText("解散该群");
        } else {
            adminSetting.setVisibility(View.GONE);
            quitBtn.setVisibility(View.VISIBLE);
            quitBtn.setText("退出群聊");
        }
        rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCameraDialog();
            }
        });

        roleType = GroupInfo.getInstance().getRole(identify);
        type = info.getGroupType();
        TextAndTextView member = (TextAndTextView) findViewById(R.id.member);
        if (isInGroup) {
            String content = String.valueOf(info.getMemberNum());
            member.getTextRightText().setText(content + "人");
            titleView.setTitleText("聊天信息(" + content + ")");
            member.setOnClickListener(this);
        } else {
            member.setVisibility(View.GONE);
        }
        name.getTextRightText().setText(info.getGroupName());

        messageOptContent = new SparseArray<>();
        messageOptContent.put(1, TIMGroupReceiveMessageOpt.NotReceive);
        messageOptContent.put(2, TIMGroupReceiveMessageOpt.ReceiveAndNotify);

        //消息免打扰
        TextAndSwitchView messageSwitch = (TextAndSwitchView) findViewById(R.id.message_view);
        if (GroupInfo.getInstance().isInGroup(identify)) {
            switch (GroupInfo.getInstance().getMessageOpt(identify)) {
                case NotReceive:
                    messageSwitch.getCheckBox().setChecked(true);
                    break;
                case ReceiveAndNotify:
                    messageSwitch.getCheckBox().setChecked(false);
                    break;
            }
        } else {
            messageSwitch.setVisibility(View.GONE);
        }

        messageSwitch.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchReceiveAndNotify(messageOptContent.get(1));
                } else {
                    switchReceiveAndNotify(messageOptContent.get(2));
                }
            }
        });

        //公告
        noticeText = (TextAndTextView) findViewById(R.id.notification_text);
        TIMGroupManagerExt.getInstance().getGroupDetailInfo(Collections.singletonList(identify), new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMGroupDetailInfo> timGroupDetailInfos) {
                if (timGroupDetailInfos != null && timGroupDetailInfos.size() == 1) {
                    TIMGroupDetailInfo timGroupDetailInfo = timGroupDetailInfos.get(0);
                    String groupNotification = timGroupDetailInfo.getGroupNotification();
                    noticeText.getTextRightText().setText(groupNotification);
                }
            }
        });

//        TIMGroupManagerExt.ModifyGroupInfoParam modifyGroupInfoParam = new TIMGroupManagerExt.ModifyGroupInfoParam(identify);
//        String notification = modifyGroupInfoParam.getNotification();
//        if (!TextUtils.isEmpty(notification)) {
//            noticeText.getTextRightText().setText(notification);
//        }

        noticeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = noticeText.getTextRightText().getText().toString().trim();
                if (TextUtils.isEmpty(s) || s.equals("未设置")) {
                    s = "";
                }
                SendNotificationActivity.startSendNotificationActivity(GroupProfileActivity.this, identify, s, isManager());
            }
        });

        TextAndSwitchView topChatView = (TextAndSwitchView) findViewById(R.id.top_chat_view);
        boolean isIn = PlacedTopUtil.hasInTopIdentify(selfIdentify, identify);
        topChatView.getCheckBox().setChecked(isIn);
        topChatView.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PlacedTopUtil.saveTopIdentify(selfIdentify, identify);
                } else {
                    PlacedTopUtil.removeIdentify(selfIdentify, identify);
                }
            }
        });

        if (isManager() && !type.equals("Private")) {
//            opt.setCanNav(true);
//            opt.setOnClickListener(this);
            allowTypeContent = new HashMap<>();
            allowTypeContent.put(getString(R.string.chat_setting_group_auth), TIMGroupAddOpt.TIM_GROUP_ADD_AUTH);
            allowTypeContent.put(getString(R.string.chat_setting_group_all_accept), TIMGroupAddOpt.TIM_GROUP_ADD_ANY);
            allowTypeContent.put(getString(R.string.chat_setting_group_all_reject), TIMGroupAddOpt.TIM_GROUP_ADD_FORBID);
//            name.setCanNav(true);
            name.getTextRightImage().setVisibility(View.VISIBLE);
            name.setOnClickListener(this);
//            intro.setCanNav(true);
//            intro.setOnClickListener(this);
        } else {
            name.getTextRightImage().setVisibility(View.GONE);
            name.setOnClickListener(null);
        }
    }

    private void switchReceiveAndNotify(TIMGroupReceiveMessageOpt opt) {
        TIMGroupManagerExt.ModifyMemberInfoParam param = new TIMGroupManagerExt.ModifyMemberInfoParam(identify, UserInfo.getInstance().getId());
        param.setReceiveMessageOpt(opt);
        TIMGroupManagerExt.getInstance().modifyMemberInfo(param, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_change_err), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {

            }
        });
    }

    @Override
    public void showUserProfile(List<TIMUserProfile> result) {
        int imageLayoutWidth = ViewUtil.dp2px(this, 35);
        int imageWidth = ViewUtil.dp2px(this, 20);
        if (fixedWidthLayout.getChildCount() > 0) {
            fixedWidthLayout.removeAllViews();
            fixedWidthLayout.canLayout(true);
        }
        if (result != null) {
            for (TIMUserProfile timUserProfile : result) {
                String faceUrl = timUserProfile.getFaceUrl();
                String nickName = timUserProfile.getNickName();
                String identifier = timUserProfile.getIdentifier();

                if (!TextUtils.isEmpty(identifier)) {
                    boolean isParent = identifier.startsWith("p");
                    if (isParent) {
                        if (!TextUtils.isEmpty(nickName) && nickName.length() > 0) {
                            nickName = nickName.substring(0, nickName.length() - 1);
                            if (!TextUtils.isEmpty(nickName)) {
                                String[] split = nickName.split("\\(");
                                if (split.length > 1) {
                                    nickName = split[1];
                                }
                            }
                        }
                    }
                }

                if (TextUtils.isEmpty(nickName)) {
                    nickName = timUserProfile.getIdentifier();
                }
                int padding = 5;
                CircleImageView imageView = new CircleImageView(this);
                imageView.setPadding(ViewUtil.dp2px(this, padding), ViewUtil.dp2px(this, padding),
                        ViewUtil.dp2px(this, padding), ViewUtil.dp2px(this, padding));
                TextView textView = new TextView(this);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
                FrameLayout layout = new FrameLayout(this);
                layout.addView(imageView);

                FrameLayout.LayoutParams imgLayoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
                int currentWidth = ViewUtil.dp2px(this, 50 + padding);
                imgLayoutParams.width = currentWidth;
                imgLayoutParams.height = currentWidth;
                imgLayoutParams.gravity = Gravity.CENTER;

                FrameLayout.LayoutParams textLayoutParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                textLayoutParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
                textLayoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
                textLayoutParams.gravity = Gravity.CENTER;
                layout.addView(textView, textLayoutParams);

                ViewUtil.setColorItemView(faceUrl, nickName, textView, imageView);
                if (!fixedWidthLayout.canToAddView(layout, currentWidth, imageLayoutWidth, imageWidth)) {
                    break;
                }
            }

        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        /*if (v.getId() == R.id.btnChat) {
            ChatActivity.navToChat(this, identify, TIMConversationType.Group);
        } else if (v.getId() == R.id.btnDel) {
            if (isGroupOwner) {
                GroupManagerPresenter.dismissGroup(identify, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Log.i(TAG, "onError code" + i + " msg " + s);
                        if (i == 10004 && type.equals(GroupInfo.privateGroup)) {
                            Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_quit_fail_private), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_dismiss_succ), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            } else {
                GroupManagerPresenter.quitGroup(identify, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Log.i(TAG, "onError code" + i + " msg " + s);
                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_quit_succ), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        } *//*else if (v.getId() == R.id.controlOutGroup) {
            Intent intent = new Intent(this, ApplyGroupActivity.class);
            intent.putExtra("identify", identify);
            startActivity(intent);
        }*/ /*else*/
        if (v.getId() == R.id.member) {
//            Intent intentGroupMem = new Intent(this, GroupMemberActivity.class);
//            intentGroupMem.putExtra("id", identify);
//            intentGroupMem.putExtra("type", type);
//            startActivity(intentGroupMem);
            Intent intentGroupMem = new Intent();
            intentGroupMem.setClassName("com.sdxxtop.zhidian", "com.sdxxtop.zhidian.im.ZDGroupMemberActivity");
            intentGroupMem.putExtra("identify", identify);
            intentGroupMem.putExtra("type", type);
            intentGroupMem.putExtra("isOwner", isGroupOwner);
            intentGroupMem.putExtra("isAdmin", roleType == TIMGroupMemberRoleType.Admin);
            startActivityForResult(intentGroupMem, 10);
        } /*else if (v.getId() == R.id.addOpt) {
            final String[] stringList = allowTypeContent.keySet().toArray(new String[allowTypeContent.size()]);
            new ListPickerDialog().show(stringList, getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, final int which) {
                    TIMGroupManagerExt.ModifyGroupInfoParam param = new TIMGroupManagerExt.ModifyGroupInfoParam(identify);
                    param.setAddOption(allowTypeContent.get(stringList[which]));
                    TIMGroupManagerExt.getInstance().modifyGroupInfo(param, new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_change_err), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess() {
                            LineControllerView opt = (LineControllerView) findViewById(R.id.addOpt);
                            opt.setContent(stringList[which]);
                        }
                    });
                }
            });
        }*/ else if (v.getId() == R.id.nameText) {
//            name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {

            Intent intent = new Intent();
            intent.setClassName("com.sdxxtop.zhidian", "com.sdxxtop.zhidian.im.ZDEditActivity");
            intent.putExtra("count", String.valueOf(info.getMemberNum()));
            intent.putExtra("title", name.getTextRightText().getText().toString());
            intent.putExtra("identify", identify);
            startActivityForResult(intent, 104);
//                    EditActivity.navToEdit(GroupProfileActivity.this, getString(R.string.chat_setting_change_group_name), info.getGroupName(), REQ_CHANGE_NAME, new EditActivity.EditInterface() {
//                        @Override
//                        public void onEdit(final String text, TIMCallBack callBack) {
//                            TIMGroupManagerExt.ModifyGroupInfoParam param = new TIMGroupManagerExt.ModifyGroupInfoParam(identify);
//                            param.setGroupName(text);
//                            TIMGroupManagerExt.getInstance().modifyGroupInfo(param, callBack);
//                        }
//                    }, 20);

//        }
//            });
        }

//        else if (v.getId() == R.id.groupIntro) {
//            intro.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    EditActivity.navToEdit(GroupProfileActivity.this, getString(R.string.chat_setting_change_group_intro), intro.getContent(), REQ_CHANGE_INTRO, new EditActivity.EditInterface() {
//                        @Override
//                        public void onEdit(final String text, TIMCallBack callBack) {
//                            TIMGroupManagerExt.ModifyGroupInfoParam param = new TIMGroupManagerExt.ModifyGroupInfoParam(identify);
//                            param.setIntroduction(text);
//                            TIMGroupManagerExt.getInstance().modifyGroupInfo(param, callBack);
//                        }
//                    }, 20);
//
//                }
//            });
//        } else if (v.getId() == R.id.messageNotify) {
//            final String[] messageOptList = messageOptContent.keySet().toArray(new String[messageOptContent.size()]);
//            new ListPickerDialog().show(messageOptList, getSupportFragmentManager(), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, final int which) {
//                    TIMGroupManagerExt.ModifyMemberInfoParam param = new TIMGroupManagerExt.ModifyMemberInfoParam(identify, UserInfo.getInstance().getId());
//                    param.setReceiveMessageOpt(messageOptContent.get(messageOptList[which]));
//                    TIMGroupManagerExt.getInstance().modifyMemberInfo(param, new TIMCallBack() {
//                        @Override
//                        public void onError(int i, String s) {
//                            Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_change_err), Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                            LineControllerView msgNotify = (LineControllerView) findViewById(R.id.messageNotify);
//                            msgNotify.setContent(messageOptList[which]);
//                        }
//                    });
//                }
//            });
//        }
//        switch (v.getId()){
//            case R.id.btnChat:
//                ChatActivity.navToChat(this,identify, TIMConversationType.Group);
//                break;
//            case R.id.btnDel:
//                if (isGroupOwner){
//                    GroupManagerPresenter.dismissGroup(identify, new TIMCallBack() {
//                        @Override
//                        public void onError(int i, String s) {
//                            Log.i(TAG, "onError code" + i + " msg " + s);
//                            if (i == 10004 && type.equals(GroupInfo.privateGroup)){
//                                Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_quit_fail_private),Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                            Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_dismiss_succ),Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//                    });
//                }else{
//                    GroupManagerPresenter.quitGroup(identify, new TIMCallBack() {
//                        @Override
//                        public void onError(int i, String s) {
//                            Log.i(TAG, "onError code" + i + " msg " + s);
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                            Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_quit_succ),Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//                    });
//                }
//                break;
//            case R.id.controlOutGroup:
//                Intent intent = new Intent(this, ApplyGroupActivity.class);
//                intent.putExtra("identify", identify);
//                startActivity(intent);
//                break;
//            case R.id.member:
//                Intent intentGroupMem = new Intent(this, GroupMemberActivity.class);
//                intentGroupMem.putExtra("id", identify);
//                intentGroupMem.putExtra("type",type);
//                startActivity(intentGroupMem);
//                break;
//            case R.id.addOpt:
//                final String[] stringList = allowTypeContent.keySet().toArray(new String[allowTypeContent.size()]);
//                new ListPickerDialog().show(stringList,getSupportFragmentManager(), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, final int which) {
//                        TIMGroupManagerExt.ModifyGroupInfoParam param = new TIMGroupManagerExt.ModifyGroupInfoParam(identify);
//                        param.setAddOption(allowTypeContent.get(stringList[which]));
//                        TIMGroupManagerExt.getInstance().modifyGroupInfo(param, new TIMCallBack() {
//                            @Override
//                            public void onError(int i, String s) {
//                                Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_change_err),Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onSuccess() {
//                                LineControllerView opt = (LineControllerView) findViewById(R.id.addOpt);
//                                opt.setContent(stringList[which]);
//                            }
//                        });
//                    }
//                });
//                break;
//            case R.id.nameText:
//                name.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        EditActivity.navToEdit(GroupProfileActivity.this, getString(R.string.chat_setting_change_group_name), info.getGroupName(), REQ_CHANGE_NAME, new EditActivity.EditInterface() {
//                            @Override
//                            public void onEdit(final String text, TIMCallBack callBack) {
//                                TIMGroupManagerExt.ModifyGroupInfoParam param = new TIMGroupManagerExt.ModifyGroupInfoParam(identify);
//                                param.setGroupName(text);
//                                TIMGroupManagerExt.getInstance().modifyGroupInfo(param, callBack);
//                            }
//                        },20);
//
//                    }
//                });
//                break;
//            case R.id.groupIntro:
//                intro.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        EditActivity.navToEdit(GroupProfileActivity.this, getString(R.string.chat_setting_change_group_intro), intro.getContent(), REQ_CHANGE_INTRO, new EditActivity.EditInterface() {
//                            @Override
//                            public void onEdit(final String text, TIMCallBack callBack) {
//                                TIMGroupManagerExt.ModifyGroupInfoParam param = new TIMGroupManagerExt.ModifyGroupInfoParam(identify);
//                                param.setIntroduction(text);
//                                TIMGroupManagerExt.getInstance().modifyGroupInfo(param, callBack);
//                            }
//                        },20);
//
//                    }
//                });
//                break;
//            case R.id.messageNotify:
//                final String[] messageOptList = messageOptContent.keySet().toArray(new String[messageOptContent.size()]);
//                new ListPickerDialog().show(messageOptList,getSupportFragmentManager(), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, final int which) {
//                        TIMGroupManagerExt.ModifyMemberInfoParam param = new TIMGroupManagerExt.ModifyMemberInfoParam(identify, UserInfo.getInstance().getId());
//                        param.setReceiveMessageOpt(messageOptContent.get(messageOptList[which]));
//                        TIMGroupManagerExt.getInstance().modifyMemberInfo(param, new TIMCallBack() {
//                            @Override
//                            public void onError(int i, String s) {
//                                Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_change_err),Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onSuccess() {
//                                LineControllerView msgNotify = (LineControllerView) findViewById(R.id.messageNotify);
//                                msgNotify.setContent(messageOptList[which]);
//                            }
//                        });
//                    }
//                });
//                break;
//        }
    }

    //做了相应的改动应该回去更新 chatActivity的title
    private boolean isFinishToRefresh;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CHANGE_NAME) {
            if (resultCode == RESULT_OK) {
                name.getTextRightText().setText(data.getStringExtra(EditActivity.RETURN_EXTRA));
            }
        } else if (requestCode == REQ_CHANGE_INTRO) {
            if (resultCode == RESULT_OK) {
//                intro.setContent(data.getStringExtra(EditActivity.RETURN_EXTRA));
            }
            //SendNotificationActivity
        } else if (requestCode == 101 && resultCode == 101 && data != null) {
            String content = data.getStringExtra("content");
            if (noticeText != null) {
                if (TextUtils.isEmpty(content)) {
                    noticeText.getTextRightText().setText("未设置");
                } else {
                    noticeText.getTextRightText().setText(content);
                }
            }
        } else if (requestCode == 102 && resultCode == 120 && data != null) {
            Log.e(TAG, "成员添加成");
            groupInfoPresenter.getGroupDetailInfo();
        } else if (requestCode == 104 && resultCode == 121 && data != null) {
            String content = data.getStringExtra("content");
            name.getTextRightText().setText(content);
            isFinishToRefresh = true;
        } else if (requestCode == 10 && resultCode == 121) {
            groupInfoPresenter.getGroupDetailInfo();
        }

        if (accountPicHelper != null) {
            accountPicHelper.handleResult(requestCode, circleImage, data, new Runnable() {
                @Override
                public void run() {
                    loadGroupImage();
                }
            });
        }

    }


    @Override
    public void onBackPressed() {
        toRefresh();
    }

    private void toRefresh() {
        setResult(130);
        finish();
    }

    private boolean isManager() {
        return isGroupOwner || roleType == TIMGroupMemberRoleType.Admin;
    }

    private boolean isGroupOwner() {
        return roleType == TIMGroupMemberRoleType.Owner;
    }
}
