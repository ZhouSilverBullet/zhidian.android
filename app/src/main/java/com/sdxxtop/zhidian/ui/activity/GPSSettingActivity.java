package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.FDLocation;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：开启GPS SwitchButton定位后，设置GPS界面
 */
public class GPSSettingActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_fanwei_show)
    TextView tvFanweiShow;
    @BindView(R.id.iv_select_fanwei)
    ImageView ivSelectFanwei;
    @BindView(R.id.tv_gps_show)
    TextView tvGpsShow;
    @BindView(R.id.iv_gps_into)
    ImageView ivGpsInto;
    @BindView(R.id.btn_save)
    Button btnSave;
    private String lt;
    private OptionPicker picker;
    private String address_name;

    @Override
    protected int getActivityView() {
        return R.layout.activity_gpssetting;
    }

    @Override
    protected void initView() {
        String otherName = getIntent().getStringExtra("otherName");
        address_name = getIntent().getStringExtra("address_name");
        if (!TextUtils.isEmpty(otherName)) {
            etName.setText(otherName);
            etName.setSelection(etName.getText().length());
        }

        String gpsSettingSize = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.GPS_SETTING_SIZE);
        if (!TextUtils.isEmpty(gpsSettingSize)) {
            tvFanweiShow.setText(gpsSettingSize);
        } else {
            tvFanweiShow.setText("300m");
        }
    }

    @Override
    protected void initData() {
        String spAddress = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.MAP_ADDRESS);
//        LogUtils.e("周map_address:", spAddress);
        if (!TextUtils.isEmpty(address_name)) {
            tvGpsShow.setText(address_name);
        } else {
            tvGpsShow.setText(spAddress);
        }

        FDLocation.getInstance().setLocationCompanyListener(new FDLocation.LocationCompanyListener() {
            @Override
            public void onAddress(AMapLocation address) {
                if (address != null) {
                    String text = tvGpsShow.getText().toString();
                    if (TextUtils.isEmpty(text)) {
                        tvGpsShow.setText(address.getAddress());
                    }
                    lt = (address.getLongitude() + "," + address.getLatitude());
                } else {
                    ToastUtil.show("请给予app定位权限");
                }
                //要清除回调，不然可能一直有回调的情况
                FDLocation.getInstance().removeAddressListener();
            }
        });
    }

    @OnClick({R.id.rl_fanwei, R.id.rl_gps_select, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_fanwei:
                onOptionPicker();
                break;
            case R.id.rl_gps_select:
                Intent intentMap = new Intent(mContext, AmapPoiActivity.class);
                startActivityForResult(intentMap, 1994);
                break;
            case R.id.btn_save:
                if (etName.getText().toString().equals("")) {
                    ToastUtil.show("请输入GPS名字");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("gn", etName.getText().toString());
                String s = tvFanweiShow.getText().toString();
                String sr = "";
                //"300m", "200m", "100m"
                switch (s) {
                    case "200m":
                        sr = 200 + "";
                        break;
                    case "100m":
                        sr = 100 + "";
                        break;
                    default:
                        sr = 300 + "";
                        break;
                }
                intent.putExtra("sr", sr);
                if (TextUtils.isEmpty(lt)) {
                    intent.putExtra("slt", "");
                } else {
                    intent.putExtra("slt", lt);
                }
                intent.putExtra("sa", tvGpsShow.getText().toString());
                setResult(300, intent);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1994 && resultCode == 10087 && data != null) {
//            ToastUtil.show(data.getStringExtra("ar"));
//            PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.MAP_ADDRESS, data.getStringExtra("ar") + data.getStringExtra("ad"));
//            LogUtils.e("蔡map_address:", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.MAP_ADDRESS));
            lt = data.getStringExtra("lt");
            tvGpsShow.setText(data.getStringExtra("ad"));
        }
    }

    /**
     * 设置打卡范围选择器
     */
    public void onOptionPicker() {
        String fanWeiShowValue = tvFanweiShow.getText().toString();
        int index = 0;
        switch (fanWeiShowValue) {
            case "200m":
                index = 1;
                break;
            case "100m":
                index = 2;
                break;
            default:
                index = 0;
                break;
        }
        String[] items = {
                "300m", "200m", "100m"
        };
        picker = new OptionPicker(mContext, items);
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setSelectedIndex(0);
        picker.setCycleDisable(true);
        picker.setTextSize(20);
        picker.setSelectedItem(items[index]);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                tvFanweiShow.setText(item);
                PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.GPS_SETTING_SIZE, item);
            }
        });
        picker.show();
    }
}
