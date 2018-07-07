package com.sdxxtop.zhidian.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.AccountAdapter;
import com.sdxxtop.zhidian.alipush.AnalyticsHome;
import com.sdxxtop.zhidian.entity.AccountBean;
import com.sdxxtop.zhidian.entity.NormalLoginBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.DeviceUtil;
import com.sdxxtop.zhidian.utils.PhoneTextWatcher;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：13:28
 * 邮箱：15010104100@163.com
 * 描述：普通登陆
 */
public class NormalLoginActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.tv_regist)
    TextView tvRegist;
    @BindView(R.id.iv_user_dimg)
    ImageView ivUserDimg;
    @BindView(R.id.et_phonenum)
    EditText etPhonenum;
    @BindView(R.id.iv_select)
    ImageView ivSelect;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.cb_eye)
    CheckBox cbEye;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_forgetpwd)
    TextView tvForgetpwd;
    @BindView(R.id.line)
    TextView line;
    @BindView(R.id.iv_del_phone)
    ImageView ivDelPhone;
    @BindView(R.id.iv_del_pwd)
    ImageView ivDelPwd;

    private String device_name;
    private String device_no;
    private int lenPhone;

    @Override
    protected void onResume() {
        super.onResume();
        etPhonenum.setText(PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_NAME));
        etPwd.setText(PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.PASSWORD));
    }

    @Override
    protected int getActivityView() {
        return R.layout.activity_normal_login;
    }

    @Override
    protected void initView() {
//        //获取注册界面传过来的信息
//        Intent intent = getIntent();
//        etPhonenum.setText(intent.getStringExtra("un"));
//        etPwd.setText(intent.getStringExtra("pw"));
        //获取当前设备名称
        device_name = Build.MODEL;
        //设置手机号格式
        etPhonenum.addTextChangedListener(new PhoneTextWatcher(etPhonenum, ivDelPhone, etPwd));
        //设置EditText长度限制
        etPhonenum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        etPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.DEVICE_NAME, device_name);

        //获取当前设备的IMEI
        if (EasyPermissions.hasPermissions(mContext, Manifest.permission.READ_PHONE_STATE)) {
            DeviceUtil.getDeviceNo(mContext);
        } else {
            EasyPermissions.requestPermissions(mContext, "请开启读取手机权限", PERMISSION_FOR_PHONE_STATE, Manifest.permission.READ_PHONE_STATE);
        }

    }

    @Override
    protected void initData() {
        int timeTemp = PreferenceUtils.getInstance(mContext).getIntParam(ConstantValue.LOGIN_TIME_TEMP);
        if (timeTemp != -1 && timeTemp < System.currentTimeMillis()) {
            //测试用暂关此功能
//            postAutoLogin();
        }
    }

    @Override
    protected void initEvent() {
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    ivDelPwd.setVisibility(View.GONE);
                } else {
                    if (etPwd.isFocused()) {
                        ivDelPwd.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        etPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !TextUtils.isEmpty(etPwd.getText().toString())) {
                    ivDelPwd.setVisibility(View.VISIBLE);
                } else {
                    ivDelPwd.setVisibility(View.GONE);
                }
            }
        });

        etPhonenum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !TextUtils.isEmpty(etPhonenum.getText().toString())) {
                    ivDelPhone.setVisibility(View.VISIBLE);
                } else {
                    ivDelPhone.setVisibility(View.GONE);
                }
            }
        });

        cbEye.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.tv_regist, R.id.iv_select, R.id.btn_login, R.id.tv_forgetpwd, R.id.iv_del_phone, R.id.iv_del_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_regist:
                Intent intent = new Intent(NormalLoginActivity.this, RegistFirstActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_del_phone:
                etPhonenum.setText("");
                break;

            case R.id.iv_del_pwd:
                etPwd.setText("");
                break;
            case R.id.iv_select:
                initPopop();
                break;

            case R.id.tv_forgetpwd:
                Intent intentForget = new Intent(NormalLoginActivity.this, ForgetPWDActivity.class);
                startActivity(intentForget);
                break;

            case R.id.btn_login:

                if (etPhonenum.getText().toString().length() == 0 && etPwd.getText().toString().length() == 0) {
                    ToastUtil.show("请输入正确的手机号码！");
                    return;
                }
                if (etPhonenum.getText().toString().length() < 13) {
                    ToastUtil.show("请输入正确的手机号码！");
                    return;
                }
                if (etPwd.getText().toString().length() < 6) {
                    ToastUtil.show("密码格式错误");
                    return;
                }
                if (etPwd.getText().toString().length() > 16) {
                    ToastUtil.show("密码格式错误");
                    return;
                }
                postNormalLogin();
                break;
        }
    }

    /**
     * 调取普通登陆请求
     */
    private void postNormalLogin() {
        String phone = etPhonenum.getText().toString().replace(" ", "");
        Params params = new Params();
        params.removeKey("ui");
        params.removeKey("ci");
        //登陆要查询本地的ci
        params.put("ci", AppSession.getInstance().getCompanyIdForPhone(etPhonenum.getText().toString()));
        params.put("un", phone);
        params.put("pw", etPwd.getText().toString());
        params.put("pi", ConstantValue.pi);
        params.put("dn", DeviceUtil.getDeviceNo(mContext));
        params.put("dm", DeviceUtil.getDeviceName());
        showProgressDialog("正在登录");

        RequestUtils.createRequest().postNormalLogin(params.getData()).enqueue(new RequestCallback<NormalLoginBean>(new IRequestListener<NormalLoginBean>() {
            @Override
            public void onSuccess(NormalLoginBean loginBean) {
                closeProgressDialog();
                NormalLoginBean.DataEntity data = loginBean.getData();
                if (data == null) {
                    ToastUtil.show("登录失败");
                    return;
                }

                showToast(data.getMsg());

                String phone = etPhonenum.getText().toString().replace(" ", "");
                PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.USER_NAME, phone);
                PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.PASSWORD, etPwd.getText().toString());
                PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.LOGIN_TIME_TEMP, data.getExpire_time());
                PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.LOGIN_TOKEN, data.getAuto_token());
                String company_id = data.getCompany_id();
                if (TextUtils.isEmpty(company_id)) {
                    company_id = "";
                }

//                PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.COMPANY_ID, company_id);

                AnalyticsHome.bindAccount(phone);

                int userid = data.getUserid();

                //登陆完成之后进行存储相关登陆数据
                AppSession.getInstance().addOrRefreshAccountBean(etPhonenum.getText().toString(), etPwd.getText().toString(), company_id, userid + "");


                //判断是否有company_id 来决定跳转到哪个界面（5.11 21:22 ccm改过）
                if (TextUtils.isEmpty(company_id)) {
                    Intent intent = new Intent(NormalLoginActivity.this, LoginSuccessActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(NormalLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
                ToastUtil.show(errorMsg);
            }
        }));
    }

    /**
     * 调取自动登陆请求
     */
    private void postAutoLogin() {
        Params params = new Params();
        params.putDeviceNo();
        params.put("at", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.LOGIN_TOKEN));
        params.put("pi", ConstantValue.pi);
        params.put("dm", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.DEVICE_NAME));
        showProgressDialog("正在登录");
        RequestUtils.createRequest().postAutoLogin(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<NormalLoginBean>() {
            @Override
            public void onSuccess(NormalLoginBean normalLoginBean) {
                closeProgressDialog();
                NormalLoginBean.DataEntity data = normalLoginBean.getData();
                if (data != null) {
                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.LOGIN_TIME_TEMP, data.getExpire_time());
                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.LOGIN_TOKEN, data.getAuto_token());
                    String company_id = data.getCompany_id();
                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.COMPANY_ID, company_id);
                    if ("0".equals(company_id)) {
                        Intent intent = new Intent(NormalLoginActivity.this, LoginSuccessActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(NormalLoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
                showToast(errorMsg);
            }
        }));
    }

    /*
     * 设置密码的显隐
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int passwordLength = etPwd.getText().length();
        etPwd.setInputType(isChecked ?
                (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) :
                (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
        etPwd.setSelection(passwordLength);
    }

    /**
     * 初始化登陆历史记录
     */
    private void initPopop() {

        String stringParam = AppSession.getInstance().getHistoryAccountString();
        if (stringParam.length() == 0 || stringParam.length() == 2) {
            ToastUtil.show("无历史登录记录");
            return;
        }
        final List<AccountBean> item = new Gson().fromJson(stringParam, new TypeToken<List<AccountBean>>() {
        }.getType());
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_select_account, null);
        ListView lvAccount = (ListView) contentView.findViewById(R.id.lvAccount);
        AccountAdapter adapter = new AccountAdapter(mContext, item);
        lvAccount.setAdapter(adapter);
        final PopupWindow mPopWindow = new PopupWindow(contentView, line.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setTouchable(true);
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopWindow.showAsDropDown(line, 0, 0);
        lvAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPopWindow.dismiss();
                etPhonenum.setText(item.get(position).getAccount());
                etPwd.setText(item.get(position).getPassword());
            }
        });
        adapter.setAdapterClickListener(new AccountAdapter.AdapterClickListener() {
            @Override
            public void adapterClick(int count) {
                if (count == 0) {
                    mPopWindow.dismiss();
                }
            }
        });
    }
}
