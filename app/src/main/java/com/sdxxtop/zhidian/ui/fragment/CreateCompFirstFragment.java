package com.sdxxtop.zhidian.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.CreateCompanyData;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.activity.AmapPoiActivity;
import com.sdxxtop.zhidian.ui.activity.CreateCompanyActivity;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.SinglePicker;
import cn.qqtheme.framework.widget.WheelView;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 作者：CaiCM
 * 日期：2018/3/23  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：创建公司的第一个Fragment
 */
public class CreateCompFirstFragment extends BaseFragment {

    @BindView(R.id.et_comp_qname)
    EditText etCompQname;
    @BindView(R.id.et_comp_jname)
    EditText etCompJname;
    @BindView(R.id.tv_select_location)
    TextView tvSelectLocation;
    @BindView(R.id.et_comp_location)
    EditText etCompLocation;
    @BindView(R.id.tv_comp_guimo)
    TextView tvCompGuimo;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.iv_select_location_layout)
    RelativeLayout locationLayout;

    private CreateCompanyData companyData;
    private String ar;
    private String ad;
    private OptionPicker picker;

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_create_comp_first;
    }

    @Override
    protected void initView() {
        String company_size = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_SIZE);
        if (TextUtils.isEmpty(company_size)) {
            tvCompGuimo.setHint("公司规模");
        } else {
            tvCompGuimo.setText(company_size);
        }

    }

    @Override
    protected void initData() {
        companyData = ((CreateCompanyActivity) getActivity()).companyData;

        if (!TextUtils.isEmpty(companyData.ar)) {
            tvSelectLocation.setText(companyData.ar);
        }
        if (!TextUtils.isEmpty(companyData.ad)) {
            etCompLocation.setText(companyData.ad);
        }
    }

    @Override
    protected void initEvent() {
        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLocation();
            }
        });
    }

    @OnClick({R.id.btn_next, R.id.iv_select_location, R.id.tv_comp_guimo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_select_location:
                toLocation();
                break;
            case R.id.tv_comp_guimo:
                onOptionPicker();
                break;
            case R.id.btn_next:
                String compQname = etCompQname.getText().toString().trim();
                if (TextUtils.isEmpty(compQname)) {
                    ToastUtil.show("公司姓名不能为空");
                    return;
                }

                String compJname = etCompJname.getText().toString().trim();
                if (TextUtils.isEmpty(compJname)) {
                    ToastUtil.show("公司简称不能为空");
                    return;
                }
                String selectLocation = tvSelectLocation.getText().toString().trim();
                if (TextUtils.isEmpty(selectLocation)) {
                    ToastUtil.show("公司地址不能为空");
                    return;
                }
                String compLocation = etCompLocation.getText().toString().trim();
                if (TextUtils.isEmpty(compLocation)) {
                    ToastUtil.show("公司详细地址不能为空");
                    return;
                }

//                if (tvCompGuimo.getText().toString().length() == 0) {
//                    ToastUtil.show("公司规模不能为空");
//                    return;
//                }

                companyData.cn = etCompQname.getText().toString();
                companyData.stn = etCompJname.getText().toString();
                companyData.ar = tvSelectLocation.getText().toString();
                companyData.ad = etCompLocation.getText().toString();
                switch (tvCompGuimo.getText().toString()) {
                    case "30以下":
                        companyData.pn = "1";
                        break;
                    case "30~100":
                        companyData.pn = "2";
                        break;
                    case "100~300":
                        companyData.pn = "3";
                        break;
                    case "300~500":
                        companyData.pn = "4";
                        break;
                    case "500以上":
                        companyData.pn = "5";
                        break;
                    default:
                        companyData.pn = "";
                        break;
                }

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, ((CreateCompanyActivity) getActivity()).createCompSecondFragment).commit();
                ((CreateCompanyActivity) getActivity()).companyData.count = 2;
                break;
        }
    }

    private void toLocation() {
        if (EasyPermissions.hasPermissions(mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Intent intentTv = new Intent(mContext, AmapPoiActivity.class);
            startActivityForResult(intentTv, 10086);
        } else {
            EasyPermissions.requestPermissions(mContext, "请打开GPS权限再进行操作", BaseActivity.REQUEST_PERMISSION_LOACTION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    /**
     * 创建公司选择公司规模弹框
     */
    public void onOptionPicker() {
        if (picker == null) {
            picker = new OptionPicker(mContext, new String[]{
                    "30以下", "30~100", "100~300", "300~500", "500以上"
            });
            picker.setCanceledOnTouchOutside(false);
            picker.setDividerRatio(WheelView.DividerConfig.FILL);
            picker.setSelectedIndex(0);
            picker.setCycleDisable(true);
            picker.setTextSize(20);
            picker.setCanceledOnTouchOutside(true);
            picker.setTitleText("公司规模");
            picker.setTextColor(Color.rgb(50, 150, 250));
            picker.setDividerColor(Color.rgb(153, 153, 153));
            picker.setCancelTextColor(Color.rgb(153, 153, 153));
            picker.setSubmitTextColor(Color.rgb(50, 150, 250));
            picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                @Override
                public void onOptionPicked(int index, String item) {
                    tvCompGuimo.setText(item);
                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.COMPANY_SIZE, item);
                }
            });
        }
        picker.show();
    }


    public void onSinglePicker() {
        List<String> data = new ArrayList<>();
        data.add("30以下");
        data.add("30~100");
        data.add("100~300");
        data.add("300~500");
        data.add("500以上");
        SinglePicker<String> picker = new SinglePicker<>(getActivity(), data);

        picker.setCanceledOnTouchOutside(true);
        picker.setSelectedIndex(3);
        picker.setCycleDisable(false);
        picker.setTextColor(Color.rgb(50, 150, 250));
        picker.setDividerColor(Color.rgb(153, 153, 153));
        picker.setCancelTextColor(Color.rgb(153, 153, 153));
        picker.setSubmitTextColor(Color.rgb(50, 150, 250));
        picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                tvCompGuimo.setText(item);
            }
        });
        picker.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10086 && resultCode == 10087) {
            if (data != null) {
                ar = data.getStringExtra("ar");
                ad = data.getStringExtra("ad");
                String lt = data.getStringExtra("lt");
                tvSelectLocation.setText(ar);
                etCompLocation.setText(ad);
                companyData.ar = ar;
                companyData.ad = ad;
                companyData.lt = lt;
            } else {
                tvSelectLocation.setText("点击选择");
//                etCompLocation.setText("详细地址");
            }

        }
    }
}
