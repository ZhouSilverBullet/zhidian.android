package com.sdxxtop.zhidian.im;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.sdxxtop.zhidian.App;
import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.alipush.AnalyticsHome;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.activity.IMDialogActivity;
import com.sdxxtop.zhidian.ui.activity.NormalLoginActivity;
import com.sdxxtop.zhidian.utils.DialogUtil;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMGroupEventListener;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushSettings;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.qcloud.presentation.business.InitBusiness;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.presentation.event.FriendshipEvent;
import com.tencent.qcloud.presentation.event.GroupEvent;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.tencent.qcloud.presentation.event.RefreshEvent;
import com.tencent.qcloud.timchat.model.FriendshipInfo;
import com.tencent.qcloud.timchat.model.GroupInfo;
import com.tencent.qcloud.timchat.model.UserInfo;
import com.tencent.qcloud.tlslibrary.service.TLSService;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;

/**
 * Created by Administrator on 2018/7/13.
 */

public class IMLoginHelper {
    public static final String TAG = "IMLoginHelper";

    static IMLoginHelper helper;

    public static IMLoginHelper getInstance() {
        if (helper == null) {
            helper = new IMLoginHelper();
        }
        return helper;
    }

    //如果客户端登陆过的初始化
    public void init() {
        InitBusiness.start(App.getContext().getApplicationContext(), 4);
        TlsBusiness.init(App.getContext().getApplicationContext());
        String id = TLSService.getInstance().getLastUserIdentifier();
        UserInfo.getInstance().setId(id);
        UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));

        TIMOfflinePushSettings settings = new TIMOfflinePushSettings();
        settings.setEnabled(false);
        TIMManager.getInstance().setOfflinePushSettings(settings);
    }

    /**
     * 账号密码登陆
     */
    public void tIMLogin(String company_id, String userid, final String signature, final IMLoginCallback callback) {
        init();
        hasLogin();
        final String identifier = "t" + company_id + userid + "";
        LogUtils.e("IMLoginHelper： identifier = " + identifier + "\nsignature =" + signature);
        TIMManager.getInstance().login(identifier, signature, new IMLoginCallback() {
            @Override
            public void onError(int i, String s) {
                if (callback != null) {
                    callback.onError(i, s);
                }
            }

            @Override
            public void onSuccess() {
                //登陆成功
                UserInfo.getInstance().setId(identifier);
                UserInfo.getInstance().setUserSig(signature);

                if (callback != null) {
                    callback.onSuccess();
                }
            }
        });
    }

    public void hasLogin() {
        //登录之前要初始化群和好友关系链缓存
        TIMUserConfig userConfig = new TIMUserConfig();
        userConfig.setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                //被踢下线
                Log.d(TAG, "receive force offline message");
                final Activity curActivity = App.getAppContext().getCurActivity();
                if (curActivity != null) {
//                    logout(curActivity);
                    Intent intent = new Intent(curActivity, IMDialogActivity.class);
                    curActivity.startActivity(intent);
//                    new AlertDialog(curActivity).builder().setMsg("你的账号已在其他终端登录,重新登录")
//                            .setTitle("提示")
//                            .setNegativeButton("取消", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    logout(curActivity);
//                                }
//                            }).setNegativeButton("重新登录", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            reLogin(curActivity);
//                        }
//                    });
//                    ToastUtil.show("你的账号已在其他终端登录,重新登录");
                }
            }

            @Override
            public void onUserSigExpired() {
                Activity tempActivity = App.getAppContext().getCurActivity();
                //用户签名过期了，需要刷新 userSig 重新登录 SDK
                final Activity curActivity = tempActivity;
                if (curActivity != null) {
                    logout(curActivity);
//                    new AlertDialog(curActivity).builder().setMsg(
//                            App.getAppContext().getString(com.tencent.qcloud.timchat.R.string.tls_expire)
//                    ).setTitle("提示").setNegativeButton("取消", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            logout(curActivity);
//                        }
//                    }).setNegativeButton("重新登录", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            reLogin(curActivity);
//                        }
//                    });
                }
            }
        })     //设置连接状态事件监听器
                .setConnectionListener(new TIMConnListener() {
                    @Override
                    public void onConnected() {
                        Log.i(TAG, "onConnected");
                    }

                    @Override
                    public void onDisconnected(int code, String desc) {
                        Log.i(TAG, "onDisconnected");
                    }

                    @Override
                    public void onWifiNeedAuth(String name) {
                        Log.i(TAG, "onWifiNeedAuth");
                    }
                }).setGroupEventListener(new TIMGroupEventListener() {
            @Override
            public void onGroupTipsEvent(TIMGroupTipsElem timGroupTipsElem) {

            }
        });

        //设置刷新监听
        RefreshEvent.getInstance().init(userConfig);
        userConfig = FriendshipEvent.getInstance().init(userConfig);
        userConfig = GroupEvent.getInstance().init(userConfig);
        userConfig = MessageEvent.getInstance().init(userConfig);
        TIMManager.getInstance().setUserConfig(userConfig);
    }

    private void logout(Activity activity) {
        TlsBusiness.logout(UserInfo.getInstance().getId());
        UserInfo.getInstance().setId(null);
        FriendshipInfo.getInstance().clear();
        GroupInfo.getInstance().clear();

        AnalyticsHome.unbindAccount();
        Intent intentNormalLogin = new Intent(activity, NormalLoginActivity.class);
        intentNormalLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intentNormalLogin);
        //腾讯im退出
        TIMManager.getInstance().logout(null);

    }

    private void reLogin(final Activity curActivity) {
        LoginBusiness.loginIm(UserInfo.getInstance().getId(), UserInfo.getInstance().getUserSig(), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Toast.makeText(curActivity, "登录失败，请稍后重试", Toast.LENGTH_SHORT).show();
                logout(curActivity);
            }

            @Override
            public void onSuccess() {
                Toast.makeText(curActivity, "登录成功", Toast.LENGTH_SHORT).show();
//                                    String deviceMan = android.os.Build.MANUFACTURER;
//                                    //注册小米和华为推送
//                                    if (deviceMan.equals("Xiaomi") && shouldMiInit()) {
//                                        MiPushClient.registerPush(getApplicationContext(), "2882303761517480335", "5411748055335");
//                                    } else if (deviceMan.equals("HUAWEI")) {
//                                        PushManager.requestToken(getApplicationContext());
//                                    }
            }
        });
    }


    public void changeUserSignature(Context context, final String companyId, final IRequestListener<BaseModel> listener) {
        final Dialog progressDialog = DialogUtil.createLoadingDialog(context, "", false);
        progressDialog.show();
        Params params = new Params();
        //这个ci必须是切换的公司id
        params.put("ci", companyId);
        RequestUtils.createRequest().postTimGetSign(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
//                progressDialog.dismiss();
                String signature = "";
                Object data = baseModel.getData();
                if (data != null) {
                    signature = (String) ((LinkedTreeMap) data).get("signature");
                }
                if (TextUtils.isEmpty(signature)) {
                    onFailure(0, "切换失败");
                } else {
                    login(companyId, AppSession.getInstance().getUserId(), signature, progressDialog, listener);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (listener != null) {
                    listener.onFailure(code, errorMsg);
                }
            }
        }));
    }

    private void login(String companyId, final String userId, final String signature, final Dialog progressDialog, final IRequestListener<BaseModel> listener) {
        final String identifier = "t" + companyId + userId + "";
        TIMManager.getInstance().login(identifier, signature, new IMLoginCallback() {
            @Override
            public void onError(int i, String s) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if (listener != null) {
                    listener.onFailure(i, s);
                }
            }

            @Override
            public void onSuccess() {
                //登陆成功
                UserInfo.getInstance().setId(identifier);
                UserInfo.getInstance().setUserSig(signature);

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if (listener != null) {
                    listener.onSuccess(null);
                }
            }
        });
    }

    public void loginOut() {
        TIMManager.getInstance().logout(null);
    }
}
