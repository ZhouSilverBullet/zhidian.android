package com.sdxxtop.zhidian.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.App;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.CompanyModifyInfoBean;
import com.sdxxtop.zhidian.entity.CompanyShowInfoBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyInfoActivity extends BaseActivity {

    @BindView(R.id.tv_company_classify)
    TextView tvCompanyClassify;
    @BindView(R.id.tv_company_address)
    TextView tvCompanyAddress;
    @BindView(R.id.tv_company_short_code)
    TextView tvCompanyShortCode;
    @BindView(R.id.iv_transfer)
    ImageView ivTransfer;
    @BindView(R.id.rl_company_short)
    RelativeLayout rlCompanyShort;
    @BindView(R.id.ll_transafer)
    LinearLayout llTransafer;
    @BindView(R.id.et_company_name)
    EditText etCompanyName;
    @BindView(R.id.et_company_short_name)
    EditText etCompanyShortName;
    private String is_manager;
    private String is_edit;
    private String ar = "";
    private String ad = "";

    @Override
    protected int getActivityView() {
        return R.layout.activity_company_info;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        postCompanyShowInfo();
    }

    @Override
    protected void initEvent() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //获取View可见区域的bottom
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                if (bottom != 0 && oldBottom != 0 && bottom - rect.bottom <= 0) {
                    etCompanyName.setCursorVisible(false);
                    etCompanyShortName.setCursorVisible(false);
                    postCompanyModifyInfo(tvCompanyClassify.getText().toString());
                } else {
                    etCompanyName.setCursorVisible(true);
                    etCompanyShortName.setCursorVisible(true);
                }
            }
        });
    }

    @OnClick({R.id.et_company_name, R.id.et_company_short_name, R.id.tv_company_classify, R.id.tv_company_address, R.id.ll_transafer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_company_name:
                if (is_manager.equals("1") || is_edit.equals("1")) {

                } else {
                    ToastUtil.show("您不具备修改公司名称的权限");
                }
                break;
            case R.id.et_company_short_name:
                if (is_manager.equals("1") || is_edit.equals("1")) {

                } else {
                    ToastUtil.show("您不具备修改公司简称的权限");
                }
                break;
            case R.id.tv_company_classify:
                if (is_manager.equals("1") || is_edit.equals("1")) {
                    onOptionPicker();
                } else {
                    ToastUtil.show("您不具备修改公司所属行业的权限");
                }
                break;
            case R.id.tv_company_address:
                if (is_manager.equals("1") || is_edit.equals("1")) {
                    if (EasyPermissions.hasPermissions(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        Intent intentMap = new Intent(mContext, AmapPoiActivity.class);
                        String stringParam = PreferenceUtils.getInstance(this).getStringParam(ConstantValue.COMPANY_JIN_WEIDU);
                        double jinDu = 0;
                        double weiDu = 0;
                        if (!TextUtils.isEmpty(stringParam)) {
                            String[] split = stringParam.split(",");
                            if (split.length == 2) {
                                weiDu = Double.parseDouble(split[0]);
                                jinDu = Double.parseDouble(split[1]);
                                intentMap.putExtra("jindu", jinDu);
                                intentMap.putExtra("weidu", weiDu);
                            }
                        }
                        startActivityForResult(intentMap, 100);
                    } else {
                        EasyPermissions.requestPermissions(mContext, "给予gps定位权限", BaseActivity.REQUEST_PERMISSION_LOACTION, Manifest.permission.ACCESS_COARSE_LOCATION);
                    }
                } else {
                    ToastUtil.show("您不具备修改公司地址的权限");
                }
                break;
            case R.id.ll_transafer:
                Intent intent1 = new Intent(mContext, CheckSafeActivity.class);
                startActivity(intent1);
                break;
        }
    }

    private void showDialog(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final EditText et = new EditText(mContext);
        et.setSingleLine();
        et.setHint(s);
        builder.setView(et);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = et.getText().toString().trim();
                if ("".equals(name)) {
                    ToastUtil.show("不能为空");
                    return;
                } else {

                }
            }

        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }


    /**
     * 公司信息展示网络请求
     */
    private void postCompanyShowInfo() {
        Params params = new Params();
        showProgressDialog("");
        RequestUtils.createRequest().postCompanyShowInfo(params.getData()).enqueue(new RequestCallback<CompanyShowInfoBean>(new IRequestListener<CompanyShowInfoBean>() {
            @Override
            public void onSuccess(CompanyShowInfoBean companyShowInfoBean) {
                closeProgressDialog();
                CompanyShowInfoBean.DataEntity data = companyShowInfoBean.getData();
                if (data != null) {
                    is_manager = data.getIs_manager() + "";//是否是高级管理员 1 是  2 不是
                    is_edit = data.getIs_edit() + "";//是否有修改公司信息权限 1 有  2 没有
                    rlCompanyShort.setVisibility("1".equals(is_edit) ? View.VISIBLE : View.GONE);
                    llTransafer.setVisibility("1".equals(is_manager) ? View.VISIBLE : View.GONE);
                    etCompanyName.setText(data.getCompany().getCompany_name());
                    etCompanyShortName.setText(data.getCompany().getShort_name());

                    if ("1".equals(is_manager) || "1".equals(is_edit)) {
                        etCompanyName.setCursorVisible(false);
                        etCompanyName.setFocusableInTouchMode(true);
                        etCompanyName.requestFocus();
                        etCompanyShortName.setCursorVisible(false);
                        etCompanyShortName.setFocusableInTouchMode(true);
                        etCompanyShortName.requestFocus();
                    } else {
                        etCompanyName.setCursorVisible(false);
                        etCompanyName.setFocusable(false);
                        etCompanyName.setFocusableInTouchMode(false);
                        etCompanyShortName.setCursorVisible(false);
                        etCompanyShortName.setFocusable(false);
                        etCompanyShortName.setFocusableInTouchMode(false);
                    }
                    switch (data.getCompany().getClassify_id()) {
                        case "0":
                            tvCompanyClassify.setText("未设置");
                            break;
                        case "1":
                            tvCompanyClassify.setText("学前教育");
                            break;
                        case "2":
                            tvCompanyClassify.setText("初中等教育");
                            break;
                        case "3":
                            tvCompanyClassify.setText("高等教育");
                            break;
                        case "4":
                            tvCompanyClassify.setText("培训");
                            break;
                        case "5":
                            tvCompanyClassify.setText("其他");
                            break;
                        default:
                            break;
                    }
                    tvCompanyAddress.setText(data.getCompany().getAddress());
                    tvCompanyShortCode.setText(data.getCompany().getShort_code());
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
                showToast(errorMsg);
            }
        }));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 10087) {
            ar = data.getStringExtra("ar");
            ad = data.getStringExtra("ad");
            ToastUtil.show(/*ar + */ad);
            tvCompanyAddress.setText(data.getStringExtra("ad"));
            postCompanyModifyInfo(tvCompanyClassify.getText().toString());
        }
    }

    /**
     * 公司所属行业选择器
     */
    public void onOptionPicker() {
        OptionPicker picker = new OptionPicker(mContext, new String[]{
                "学前教育", "初中等教育", "高等教育", "培训", "其他"
        });
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
//        picker.setSelectedIndex(0);
        picker.setCycleDisable(true);
        picker.setTextSize(20);
        picker.setCanceledOnTouchOutside(true);
        picker.setTextColor(Color.rgb(50, 150, 250));
        picker.setDividerColor(Color.rgb(153, 153, 153));
        picker.setCancelTextColor(Color.rgb(153, 153, 153));
        picker.setSubmitTextColor(Color.rgb(50, 150, 250));
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
//                tvCompanyClassify.setText(item);
                postCompanyModifyInfo(item);
            }
        });
        picker.show();
    }

    /**
     * 修改公司信息网络请求
     */
    private void postCompanyModifyInfo(final String item) {
        String cli = "";
        switch (item) {
            case "未设置":
                cli = "0";
                break;
            case "学前教育":
                cli = "1";
                break;
            case "初中等教育":
                cli = "2";
                break;
            case "高等教育":
                cli = "3";
                break;
            case "培训":
                cli = "4";
                break;
            case "其他":
                cli = "5";
                break;
        }
        Map<String, String> map = new HashMap<>();
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        map.put("cn", etCompanyName.getText().toString());
        map.put("stn", etCompanyShortName.getText().toString());
        map.put("cli", cli);
        map.put("lt", App.getAppContext().getJinDu() + "," + App.getAppContext().getWeiDu());
        map.put("ar", ar);
        map.put("ad", ad);
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postCompanyModifyInfo(base64Data).enqueue(new Callback<CompanyModifyInfoBean>() {
            @Override
            public void onResponse(Call<CompanyModifyInfoBean> call, Response<CompanyModifyInfoBean> response) {
                closeProgressDialog();
                CompanyModifyInfoBean companyModifyInfoBean = response.body();
                if (companyModifyInfoBean.getCode() == 200) {
                    tvCompanyClassify.setText(item);
                } else {
                    ToastUtil.show(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<CompanyModifyInfoBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }

}
