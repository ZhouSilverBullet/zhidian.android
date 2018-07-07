package com.sdxxtop.zhidian.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.DeviceModifyInfoBean;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.FDLocation;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyGpsActivity extends BaseActivity {

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
    private String di;
    private String ogn;
    private OptionPicker picker;
    private String sign_range;
    private String address;

    @Override
    protected int getActivityView() {
        return R.layout.activity_modify_gps;
    }

    @Override
    protected void initView() {
        super.initView();
        if (EasyPermissions.hasPermissions(mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
            FDLocation.getInstance();
        } else {
            EasyPermissions.requestPermissions(this, "请开启gps权限", BaseActivity.REQUEST_PERMISSION_LOACTION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        if (requestCode == BaseActivity.REQUEST_PERMISSION_LOACTION) {
            FDLocation.getInstance();
        }
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        di = intent.getStringExtra("di");
        ogn = intent.getStringExtra("gn");
        sign_range = intent.getStringExtra("sign_range");
        address = intent.getStringExtra("address");
        etName.setText(ogn);
        etName.setSelection(etName.getText().length());
        if (!TextUtils.isEmpty(sign_range)) {
            tvFanweiShow.setText(sign_range);
        }
        tvGpsShow.setText(address);
    }

    @OnClick({R.id.rl_fanwei, R.id.rl_gps_select, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_fanwei:
                String s = tvFanweiShow.getText().toString();
                int position;
                switch (s) {
                    case "100m":
                        position = 2;
                        break;
                    case "200m":
                        position = 1;
                        break;
                    default:
                        position = 0;
                        break;
                }
                onOptionPicker(position);
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
                postDeviceModifyInfo();
//                Intent intent = new Intent();
//                intent.putExtra("gn", etName.getText().toString());
//                intent.putExtra("sr", tvFanweiShow.getText().toString());
//                intent.putExtra("slt", "110,40");
//                intent.putExtra("sa", tvGpsShow.getText().toString());
//                setResult(300, intent);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1994 && resultCode == 10087) {
            ToastUtil.show(data.getStringExtra("ad"));
            tvGpsShow.setText(data.getStringExtra("ad"));
        }
    }

    /**
     * 设置打卡范围选择器
     */
    public void onOptionPicker(int position) {
        if (picker == null) {
            picker = new OptionPicker(mContext, new String[]{
                    "300m", "200m", "100m"
            });
            picker.setCanceledOnTouchOutside(false);
            picker.setDividerRatio(WheelView.DividerConfig.FILL);
            picker.setCycleDisable(true);
            picker.setTextSize(20);
            picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                @Override
                public void onOptionPicked(int index, String item) {
                    tvFanweiShow.setText(item);
                }
            });
        }
        picker.setSelectedIndex(position);
        picker.show();
    }


    /**
     * 修改考勤设备信息网络请求（GPS）
     */
    private void postDeviceModifyInfo() {
        String fanweiShowString = tvFanweiShow.getText().toString();
        int fanwei;
        switch (fanweiShowString) {
            case "100m":
                fanwei = 100;
                break;
            case "200m":
                fanwei = 200;
                break;
            default:
                fanwei = 300;
                break;
        }


        Map<String, String> map = new HashMap<>();
        map.put("di", di);
        map.put("dt", "2");
        map.put("wn", "");
        map.put("nn", "");
        map.put("bi", "");
        map.put("gn", etName.getText().toString());
        map.put("sr", fanwei + "");
        String jinDu = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.MAP_JING_DU);
        String weiDu = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.MAP_WEI_DU);
        map.put("slt", jinDu + "," + weiDu);
        map.put("sa", tvGpsShow.getText().toString());
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postDeviceModifyInfo(base64Data).enqueue(new Callback<DeviceModifyInfoBean>() {
            @Override
            public void onResponse(Call<DeviceModifyInfoBean> call, Response<DeviceModifyInfoBean> response) {
                closeProgressDialog();
                DeviceModifyInfoBean deviceModifyInfoBean = response.body();
                if (deviceModifyInfoBean.getCode() == 200) {
                    ToastUtil.show("修改成功");
                    finish();
                } else {
                    ToastUtil.show(deviceModifyInfoBean.getMsg());
                }
            }

            @Override
            public void onFailure(Call<DeviceModifyInfoBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }
}
