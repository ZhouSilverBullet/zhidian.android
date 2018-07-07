package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.DeviceAddBean;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGpsActivity extends BaseActivity {

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

    @Override
    protected int getActivityView() {
        return R.layout.activity_add_gps;
    }

    @Override
    protected void initView() {
    }

    @OnClick({R.id.rl_fanwei, R.id.rl_gps_select, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_retuen:
                finish();
                break;
            case R.id.rl_fanwei:
                onOptionPicker();
                break;
            case R.id.rl_gps_select:
                Intent intentMap = new Intent(mContext, AmapPoiActivity.class);
                startActivityForResult(intentMap, 1995);
                break;
            case R.id.btn_save:
                if (etName.getText().toString().equals("")) {
                    ToastUtil.show("请输入GPS名字");
                    return;
                }
                postDeviceAdd();
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1995 && resultCode == 10087){
            ToastUtil.show(data.getStringExtra("ad"));
            tvGpsShow.setText(data.getStringExtra("ad"));
        }
    }

    /**
     * 设置打卡范围选择器
     */
    public void onOptionPicker() {
        OptionPicker picker = new OptionPicker(mContext, new String[]{
                "300m", "200m", "100m"
        });
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setSelectedIndex(0);
        picker.setCycleDisable(true);
        picker.setTextSize(20);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                tvFanweiShow.setText(item);
            }
        });
        picker.show();
    }


    /**
     * 新增考勤设备网络请求（GPS）
     */
    private void postDeviceAdd() {
        Map<String, String> map = new HashMap<>();
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        map.put("dt", "2");
        map.put("wn", "");
        map.put("nn", "");
        map.put("bi", "");
        map.put("gn", etName.getText().toString());
        map.put("sr", tvFanweiShow.getText().toString());
        String jinDu = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.MAP_JING_DU);
        String weiDu = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.MAP_WEI_DU);
        map.put("slt", jinDu + "," + weiDu);
        map.put("sa", tvGpsShow.getText().toString());
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postDeviceAdd(base64Data).enqueue(new Callback<DeviceAddBean>() {
            @Override
            public void onResponse(Call<DeviceAddBean> call, Response<DeviceAddBean> response) {
                closeProgressDialog();
                DeviceAddBean deviceAddBean = response.body();
                if (deviceAddBean.getCode() == 200) {
                    ToastUtil.show("添加成功");
                    finish();
                } else {
                    ToastUtil.show(deviceAddBean.getMsg());
                }
            }

            @Override
            public void onFailure(Call<DeviceAddBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }
}
