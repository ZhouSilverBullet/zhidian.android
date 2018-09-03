package com.sdxxtop.zhidian.adapter;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ContactIndexBean;
import com.sdxxtop.zhidian.eventbus.ChangeCompanyEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.activity.ContactDetailActivity;
import com.sdxxtop.zhidian.utils.DialogUtil;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;
import com.tencent.qcloud.timchat.model.FriendshipInfo;
import com.tencent.qcloud.timchat.ui.ChatActivity;

import org.greenrobot.eventbus.EventBus;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/7/17.
 */

public class ContactRecyclerAdapter extends BaseQuickAdapter<ContactIndexBean.DataEntity.ColloectUserEntity, BaseViewHolder> {
    public static final int TYPE_MINE = 10;
    public static final int TYPE_COLLECT = 11;

    private int type;
    private boolean toConversation;
    private Dialog dialog;

    public ContactRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactIndexBean.DataEntity.ColloectUserEntity item) {
        CircleImageView circleImg = helper.getView(R.id.item_contact_circle_img);
        TextView shortNameText = helper.getView(R.id.item_contact_short_name);
        TextView userNameText = helper.getView(R.id.item_contact_user_name);
        TextView userJobText = helper.getView(R.id.item_contact_user_job);

        String img = item.getImg();
        final String name = item.getName();
        String position = item.getPosition();

        ViewUtil.setColorItemView(img, name, shortNameText, circleImg);

        if (!TextUtils.isEmpty(position)) {
            userJobText.setText(position);
            userJobText.setVisibility(View.VISIBLE);
        } else {
            userJobText.setVisibility(View.GONE);
        }

        final boolean teacher = item.isTeacher();
        final String student_name = item.getStudent_name();
        if (teacher) {
            //是教师
            userNameText.setText(name);
        } else {
            //是家长
            userNameText.setText(item.getStudent_name() + "家长(" + item.getName() + ")");
        }

        final int userid = item.getUserid();
        final String student_id = item.getStudent_id();
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toConversation) {
                    String identify;
                    String remark;
                    if (teacher) {
                        identify = "t" + AppSession.getInstance().getCompanyId() + userid;
                        remark = name;
                    } else {
                        identify = "p" + student_id + userid;
                        remark = student_name + "家长(" + name + ")";
                    }

                    if (FriendshipInfo.getInstance().isFriend(identify)) {
                        ChatActivity.navToChat(mContext, identify, TIMConversationType.C2C);
                    } else {
//                        AddFriendUtil.addFriend(mContext, identify, remark, "", "");
                        toAddFriendShip(identify, !teacher, userid + "", student_id + "", remark);
                    }
                } else {
                    if (teacher) {
                        ContactDetailActivity.startContactDetailActivityTeacher(mContext, userid + "");
                    } else {
                        ContactDetailActivity.startContactDetailActivityParent(mContext, userid + "", student_id);
                    }
                }
            }
        });

    }

    /**
     * 用于是否是conversationFragment跳转过来的
     * 跳过来的就直接去聊天界面
     *
     * @param toConversation
     */
    public void setToConversation(boolean toConversation) {
        this.toConversation = toConversation;
    }

    private void toAddFriendShip(final String identify, boolean parent, String userid, String studentId, final String remark) {
        dialog = DialogUtil.createLoadingDialog(mContext, "", true);
        dialog.show();
        Params params = new Params();
        params.put("un", AppSession.getInstance().getIdentify());
        if (parent) {
            params.put("si", StringUtil.stringNotNull(studentId));
        } else {
            params.put("ci", AppSession.getInstance().getCompanyId());
        }
        params.put("ui", StringUtil.stringNotNull(userid));
        RequestUtils.createRequest().postTimAddFriend(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                setFriendMark(identify, remark);
            }

            @Override
            public void onFailure(int code, String errorMessage) {
                closeProgressDialog();
                LogUtils.e(TAG, " code = " + code + " errorMessage = " + errorMessage);
            }
        }));
    }

    private void closeProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
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
}
