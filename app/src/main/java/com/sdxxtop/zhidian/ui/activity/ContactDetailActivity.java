package com.sdxxtop.zhidian.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ContactCollectBean;
import com.sdxxtop.zhidian.entity.ContactRemoveBean;
import com.sdxxtop.zhidian.entity.ContactUserInfoBean;
import com.sdxxtop.zhidian.eventbus.ChangeCompanyEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.ContactTextView;
import com.sdxxtop.zhidian.widget.KnowHeightScrollView;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMAddFriendRequest;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendStatus;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;
import com.tencent.qcloud.presentation.event.FriendshipEvent;
import com.tencent.qcloud.timchat.model.FriendshipInfo;
import com.tencent.qcloud.timchat.ui.ChatActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zhangphil.iosdialog.widget.AlertDialog;

/**
 * 作者：CaiCM
 * 日期：2018/3/24  时间：10:28
 * 邮箱：15010104100@163.com
 * 描述：联系人详情界面
 */
public class ContactDetailActivity extends BaseActivity implements KnowHeightScrollView.ScrollViewListener {

    @BindView(R.id.tv_return)
    LinearLayout tvReturn;
    @BindView(R.id.title_rl)
    RelativeLayout titleRlayout;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.cb_collect)
    ImageView cbCollect;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.iv_phone)
    ImageView ivPhone;
    //    @BindView(R.id.tv_position)
//    TextView tvPosition;
//    @BindView(R.id.tv_part_name)
//    TextView tvPartName;
//    @BindView(R.id.tv_sex)
//    TextView tvSex;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.btn_remove)
    Button btnRemove;
    @BindView(R.id.iv_img)
    CircleImageView ivImg;
    @BindView(R.id.tv_short_name)
    TextView tvShortName;
    @BindView(R.id.contact_position)
    ContactTextView contactPosition;
    @BindView(R.id.contact_partment)
    ContactTextView contactPartment;
    @BindView(R.id.contact_sex)
    ContactTextView contactSex;
    @BindView(R.id.contact_work_place)
    ContactTextView contactWorkPlace;
    @BindView(R.id.contact_email)
    ContactTextView contactEmail;
    @BindView(R.id.contact_qq)
    ContactTextView contactQq;
    @BindView(R.id.contact_wx)
    ContactTextView contactWx;
    @BindView(R.id.contact_mingzu)
    ContactTextView contactMingzu;
    @BindView(R.id.contact_jiguan)
    ContactTextView contactJiguan;
    @BindView(R.id.contact_birthday)
    ContactTextView contactBirthday;
    @BindView(R.id.contact_idcard)
    ContactTextView contactIdcard;
    @BindView(R.id.contact_merry)
    ContactTextView contactMerry;
    @BindView(R.id.contact_address)
    ContactTextView contactAddress;
    @BindView(R.id.contact_contact)
    ContactTextView contactContact;
    @BindView(R.id.contact_part_name)
    ContactTextView contactPartName;
    @BindView(R.id.contact_grade_name)
    ContactTextView contactGradeName;
    @BindView(R.id.contact_class_name)
    ContactTextView contactClassName;
    @BindView(R.id.contact_relation)
    ContactTextView contactRelation;

    @BindView(R.id.contact_device_layout)
    RelativeLayout deviceLayout;

    @BindView(R.id.know_height_scroll)
    KnowHeightScrollView scrollView;
    @BindView(R.id.ll_background)
    LinearLayout llBackground;


    @BindView(R.id.contact_send_btn)
    Button sendBtn;
    @BindView(R.id.contact_voice_btn)
    Button voiceBtn;

    private String userid;
    private String is_collect;
    private ContactUserInfoBean userInfoBean;
    private int height;
    private String studentId;
    private int type;
    private String skipActivityName;

    private static final String TAG = "ContactDetailActivity";

    @Override
    protected int getActivityView() {
        return R.layout.activity_contact_detail;
    }

    @Override
    protected void initView() {
        statusBar(false);
        if (isVersionMoreKitkat()) {
            titleRlayout.setPadding(0, ViewUtil.getStatusHeight(mContext), 0, 0);
        }
        llBackground.setAlpha(0);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void initData() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        userid = intent.getStringExtra("userId");
        studentId = intent.getStringExtra("studentId");
        skipActivityName = intent.getStringExtra("activity");
        postUserInfo();
    }

    @Override
    protected void initEvent() {
        height = ViewUtil.dp2px(mContext, 150);
        scrollView.setScrollViewListener(this);
    }

    @OnClick({R.id.tv_return, R.id.tv_phone, R.id.btn_remove, R.id.cb_collect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_return:
                finish();
                break;
            case R.id.tv_phone:
                break;
//            case R.id.iv_phone:
//                callPhone();
//                break;
            case R.id.btn_remove:
                contactRemove();
                break;
            case R.id.cb_collect:
                if (userInfoBean != null && userInfoBean.getData() != null) {
                    ContactUserInfoBean.DataBean data = userInfoBean.getData();
                    collectPerson(data.getIs_collect() == 1 ? "2" : "1");
                }
                break;
        }
    }

    /**
     * 弹框调用解绑网络请求
     */
    private void contactRemove() {
        new AlertDialog(mContext)
                .builder()
                .setTitle("解除绑定")
                .setMsg("确定解除绑定设备吗\n重新登录可再次绑定设备")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> map = new HashMap<>();
                        //PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID)
                        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
                        map.put("ui", userid);
                        map.put("cui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
                        String base64Data = NetUtil.getBase64Data(map);
                        showProgressDialog("");
                        RequestUtils.getInstance().buildRequest().postContactRemove(base64Data).enqueue(new Callback<ContactRemoveBean>() {
                            @Override
                            public void onResponse(Call<ContactRemoveBean> call, Response<ContactRemoveBean> response) {
                                closeProgressDialog();
                                ContactRemoveBean contactRemoveBean = response.body();
                                if (contactRemoveBean.getCode() == 200) {
                                    ToastUtil.show(response.body().getMsg());
                                    tvDeviceName.setText("未设置");
                                    btnRemove.setClickable(false);
                                    btnRemove.setVisibility(View.INVISIBLE);
                                } else {
                                    ToastUtil.show(response.body().getMsg());
                                }
                            }

                            @Override
                            public void onFailure(Call<ContactRemoveBean> call, Throwable t) {
                                closeProgressDialog();
                                ToastUtil.show("网络错误");
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //填写事件
                    }
                }).show();
    }

    /**
     * 弹框调用打电话功能
     */
    private void callPhone() {
        String msg = tvPhone.getText().toString();
        if (!TextUtils.isEmpty(msg)) {
            StringBuilder sb = new StringBuilder();
            sb.append(msg).insert(7, " ").insert(3, " ");
            msg = sb.toString();
        }
        new AlertDialog(mContext)
                .builder()
                .setMsg(msg)
                .setPositiveButton("呼叫", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvPhone.getText().toString()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //填写事件
                    }
                }).show();
    }


    /**
     * 联系人详情网络请求
     */
    private void postUserInfo() {
        String path = "userInfo";
        Params params = new Params();
        if (type == TYPE_PARENT) {
            deviceLayout.setVisibility(View.GONE);
            params.put("si", studentId);
            path = "parentInfo";
        }
        params.put("ui", userid);
        params.put("cu", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        RequestUtils.createRequest().postContactUserInfo(path, params.getData()).enqueue(new RequestCallback<>(new IRequestListener<ContactUserInfoBean>() {
            @Override
            public void onSuccess(ContactUserInfoBean contactUserInfoBean) {
                userInfoBean = contactUserInfoBean;
                ContactUserInfoBean.DataBean data = contactUserInfoBean.getData();
                if (data != null) {
                    handleData(data);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    private void handleData(ContactUserInfoBean.DataBean data) {
        ContactUserInfoBean.DataBean.UserinfoBean userinfo = data.getUserinfo();
        String device_name = data.getDevice_name();
        boolean driverNameIsEmpty = !TextUtils.isEmpty(device_name);
        int is_self = data.getIs_self();
        int is_manager = data.getIs_manager();
        if (is_self == 1 || is_manager == 1) {
            if (driverNameIsEmpty) {
                btnRemove.setClickable(true);
                btnRemove.setBackgroundResource(R.drawable.btn_make_suer);
            } else {
                btnRemove.setClickable(false);
                btnRemove.setVisibility(View.INVISIBLE);
            }
        } else {
            btnRemove.setClickable(false);
            btnRemove.setVisibility(View.INVISIBLE);
        }
        if (driverNameIsEmpty) {
            tvDeviceName.setText(device_name);
        } else {
            tvDeviceName.setText("未设置");
        }

        if (userinfo == null) {
            showToast("用户信息获取失败");
            return;
        }

        final String name = userinfo.getName();
        final String student_name = userinfo.getStudent_name();

        if (is_self == 1) {
            sendBtn.setVisibility(View.GONE);
        } else {
            sendBtn.setVisibility(View.VISIBLE);
            //发送消息
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String identify;
                    String message;
                    String group;
                    final String remark;
                    if (type == TYPE_PARENT) {
                        identify = "p" + studentId + userid;
                        remark = student_name + "家长(" + name + ")";
                    } else {
                        identify = "t" + AppSession.getInstance().getCompanyId() + userid;
                        remark = name;
                    }

                    if (FriendshipInfo.getInstance().isFriend(identify)) {
                        ChatActivity.navToChat(mContext, identify, TIMConversationType.C2C);
                    } else {
                        showProgressDialog("");
                        toAddFriendShip(identify, remark);
                    }
                }
            });
        }

        if (userinfo.getIs_hide_mobile() == 1) {

        } else {
            ivPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPhone();
                }
            });
        }


        cbCollect.setImageResource(data.getIs_collect() == 2 ? R.mipmap.collect : R.mipmap.collected);
        final String img = userinfo.getImg();
        ViewUtil.setColorItemView(img, name, tvShortName, ivImg);
        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(img) && !img.startsWith("#")) {
                    PhotoViewActivity.start(mContext, img);
                }
            }
        });
        if (type == TYPE_PARENT) {
            tvName.setText(student_name + "家长(" + name + ")");
        } else {
            tvName.setText(name);
        }
        String mobile = userinfo.getMobile();
        tvPhone.setText(TextUtils.isEmpty(mobile) ? "未设置" : mobile);
        //职位
        String position = userinfo.getPosition();
//        contactPosition.getValueText().setText(TextUtils.isEmpty(position) ? "未设置" : position);
        setContactValue(contactPosition, position);
        //部门
        String part_name = data.getPart_name();
        setContactValue(contactPartment, part_name);
//        contactPartment.getValueText().setText(TextUtils.isEmpty(part_name) ? "未设置" : part_name);
        //性别
        if (type == TYPE_PARENT) {
            contactSex.setVisibility(View.GONE);
        } else {
            if (userinfo.getSex() == 0) {
                contactSex.getValueText().setText("未设置");
            } else {
                contactSex.getValueText().setText(userinfo.getSex() == 1 ? "男" : "女");
            }
        }

        String email = userinfo.getEmail();
        String wechat = userinfo.getWechat();
        String qq = userinfo.getQq();
        String mingZu = userinfo.getNational();
        String guanji = userinfo.getHome_place();
        String birthday = userinfo.getBirthday();
        String idcard = userinfo.getId_card();
        String marriage = userinfo.getMarriage();
        String address = userinfo.getAddress();
        String contact = userinfo.getContact();
        String workPlace = userinfo.getWork_place();
        String partName = userinfo.getPart_name();
        String relation = userinfo.getRelation();
        String grade_name = userinfo.getGrade_name();
        String class_name = userinfo.getClass_name();

        setContactValue(contactEmail, email);
        setContactValue(contactWx, wechat);
        setContactValue(contactQq, qq);
        setContactValue(contactMingzu, mingZu);
        setContactValue(contactJiguan, guanji);
        setContactValue(contactBirthday, birthday);
        setContactValue(contactIdcard, idcard);
        setContactValue(contactAddress, address);
        setContactValue(contactContact, contact);
        setContactValue(contactWorkPlace, workPlace);
        setContactValue(contactPartName, partName);
        setContactValue(contactRelation, relation);
        setContactValue(contactGradeName, grade_name);
        setContactValue(contactClassName, class_name);

        if (marriage != null) {
            switch (marriage) {
                case "1":
                    contactMerry.getValueText().setText("已婚");
                    break;
                case "2":
                    contactMerry.getValueText().setText("未婚");
                    break;
                case "3":
                    contactMerry.getValueText().setText("离异");
                    break;
                case "4":
                    contactMerry.getValueText().setText("丧偶");
                    break;
                default:
                    contactMerry.getValueText().setText("未设置");
                    break;
            }
        }
    }

    private void toAddFriendShip(final String identify, final String remark) {
        Params params = new Params();
        params.put("un", AppSession.getInstance().getIdentify());
        if (type == TYPE_PARENT) {
            params.put("si", StringUtil.stringNotNull(studentId));
        } else {
            params.put("ci", AppSession.getInstance().getCompanyId());
        }
        params.put("ui", StringUtil.stringNotNull(userid));
        showProgressDialog("");
        RequestUtils.createRequest().postTimAddFriend(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {

                setFriendMark(identify, remark);

//                TIMFriendshipManagerExt.getInstance().getFriendGroups(null, new TIMValueCallBack<List<TIMFriendGroup>>() {
//                    @Override
//                    public void onError(int i, String s) {
//                        closeProgressDialog();
//                    }
//
//                    @Override
//                    public void onSuccess(final List<TIMFriendGroup> timFriendGroups) {
//                        if (timFriendGroups != null && timFriendGroups.size() > 0) {
//                            TIMFriendGroup timFriendGroup = timFriendGroups.get(0);
//                            String groupName = timFriendGroup.getGroupName();
//                            TIMFriendshipManagerExt.getInstance().addFriendsToFriendGroup(groupName, Collections.singletonList(identify), new TIMValueCallBack<List<TIMFriendResult>>() {
//                                @Override
//                                public void onError(int code, String errorMessage) {
//                                    closeProgressDialog();
//                                    LogUtils.e(TAG, " code = " + code + " errorMessage = " + errorMessage);
//                                }
//
//                                @Override
//                                public void onSuccess(List<TIMFriendResult> resultList) {
//                                    if (resultList != null && resultList.size() > 0) {
//                                        setFriendMark(identify, remark);
//                                    }
//                                }
//                            });
//                        } else {
//                            TIMFriendshipManagerExt.getInstance().createFriendGroup(Collections.singletonList("xuxin_group"), Collections.singletonList(identify), new TIMValueCallBack<List<TIMFriendResult>>() {
//                                @Override
//                                public void onError(int code, String desc) {
//                                    closeProgressDialog();
//                                    Log.e(TAG, "modifySnsProfile failed: " + code + " desc" + desc);
//                                }
//
//                                @Override
//                                public void onSuccess(List<TIMFriendResult> resultList) {
//                                    if (resultList != null && resultList.size() > 0) {
//                                        setFriendMark(identify, remark);
//                                    }
//                                }
//                            });
//                        }
//                    }
//                });
            }

            @Override
            public void onFailure(int code, String errorMessage) {
                closeProgressDialog();
                LogUtils.e(TAG, " code = " + code + " errorMessage = " + errorMessage);
            }
        }));
    }

    private void setFriendMark(final String identify, String remark) {
        TIMFriendshipManagerExt.ModifySnsProfileParam param = new TIMFriendshipManagerExt.ModifySnsProfileParam(identify);
        param.setRemark(remark);
        TIMFriendshipManagerExt.getInstance().modifySnsProfile(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                closeProgressDialog();
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                Log.e(TAG, "modifySnsProfile failed: " + code + " desc" + desc);
            }

            @Override
            public void onSuccess() {
                closeProgressDialog();
                EventBus.getDefault().post(new ChangeCompanyEvent());
                Log.e(TAG, "modifySnsProfile succ");
                ChatActivity.navToChat(mContext, identify, TIMConversationType.C2C);
            }
        });
    }

    private void setContactValue(ContactTextView textView, String value) {
        if (value != null) {
            if ("".equals(value)) {
                value = "未设置";
            }
            textView.getValueText().setText(value);
        }
    }


    /**
     * 收藏与取消收藏操作
     */
    private void collectPerson(final String collectType) {
        Map<String, String> map = new HashMap<>();
        //PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID)
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        if (type == TYPE_PARENT) {
            map.put("si", studentId);
        }
        map.put("cui", userid);
        map.put("ct", collectType);//新增收藏
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.createRequest().postContactCollect(base64Data).enqueue(new Callback<ContactCollectBean>() {
            @Override
            public void onResponse(Call<ContactCollectBean> call, Response<ContactCollectBean> response) {
                closeProgressDialog();
                ContactCollectBean contactCollectBean = response.body();
                if (contactCollectBean.getCode() == 200) {
                    postUserInfo();
                    if (collectType.equals("1")) {
                        ToastUtil.show("新增收藏");
                    } else {
                        ToastUtil.show("取消收藏");
                    }

                } else {
                    ToastUtil.show(contactCollectBean.getMsg());
                }
            }

            @Override
            public void onFailure(Call<ContactCollectBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }

    @Override
    public void onScrollChanged(KnowHeightScrollView scrollView, int x, int y, int oldx, int oldy) {
        float percent = 0;
        if (height != 0) {
            percent = (y * 1f) / (height * 1f);
        }
        if (percent >= 0 && percent <= 1) {
            llBackground.setAlpha(percent);
        } else if (percent > 1) {
            llBackground.setAlpha(1);
        }
    }

    /**
     * 添加好友
     *
     * @param identify 添加对象Identify
     * @param remark   备注名
     * @param group    分组
     * @param message  附加消息
     */
    private String isAdd = null;

    public void addFriend(final String identify, String remark, String group, String message) {
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
                        onAddFriend(identify, item.getStatus());
                    }
                }

                if (!TextUtils.isEmpty(isAdd)) {
                    FriendshipEvent.getInstance().OnFriendGroupUpdate(null);
                    ChatActivity.navToChat(mContext, identify, TIMConversationType.C2C);
                    LogUtils.e(TAG, "" + FriendshipInfo.getInstance().isFriend(identify));
                }
            }
        });
    }

    private void onAddFriend(String identify, TIMFriendStatus status) {
        switch (status) {
            case TIM_ADD_FRIEND_STATUS_PENDING:
//                Toast.makeText(this, getResources().getString(com.tencent.qcloud.timchat.R.string.add_friend_succeed), Toast.LENGTH_SHORT).show();
//                finish();
                break;
            case TIM_FRIEND_STATUS_SUCC:

                isAdd = "isAdd";
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
                Toast.makeText(this, getResources().getString(com.tencent.qcloud.timchat.R.string.add_friend_error), Toast.LENGTH_SHORT).show();
                break;
        }
    }


    public static final int TYPE_PARENT = 10;
    public static final int TYPE_TEACHER = 11;

    /**
     *
     */
    public static void startContactDetailActivityParent(Context context, String userid, String studentId) {
        Intent intent = new Intent(context, ContactDetailActivity.class);
        intent.putExtra("userId", userid);
        intent.putExtra("studentId", studentId);
        intent.putExtra("type", TYPE_PARENT);
        context.startActivity(intent);
    }

    /**
     *
     */
    public static void startContactDetailActivityTeacher(Context context, String userid) {
        Intent intent = new Intent(context, ContactDetailActivity.class);
        intent.putExtra("userId", userid);
        intent.putExtra("type", TYPE_TEACHER);
        context.startActivity(intent);
    }
}
