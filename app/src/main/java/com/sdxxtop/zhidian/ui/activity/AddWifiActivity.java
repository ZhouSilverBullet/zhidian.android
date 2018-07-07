package com.sdxxtop.zhidian.ui.activity;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddWifiActivity extends BaseActivity {

    @BindView(R.id.tv_wifi_show)
    TextView tvWifiShow;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.et_oname)
    EditText etOname;
    @BindView(R.id.rl_oname)
    RelativeLayout rlOname;
    @BindView(R.id.btn_save)
    Button btnSave;
    private WifiInfo wifiInfo;
    private String bssid = "";
    private String wifiName = "";

    @Override
    protected int getActivityView() {
        return R.layout.activity_add_wifi;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        bssid = wifiInfo.getBSSID();
        wifiName = wifiInfo.getSSID();
        if (wifiName.equals("<unknown ssid>")) {
            tvWifiShow.setText("未连接WiFi");
        } else {
            tvWifiShow.setText(wifiName.replace("\"", ""));
        }
    }

    @OnClick({R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if ("未连接WiFi".equals(tvWifiShow.getText().toString())) {
                    ToastUtil.show("请连接WiFi后，在进入当前界面");
                    return;
                }
                if (etOname.getText().toString().equals("")) {
                    ToastUtil.show("请输入别名");
                    return;
                }
                postDeviceAdd();
                finish();
                break;
        }
    }

    /**
     * 新增考勤设备网络请求（WIFI）
     */
    private void postDeviceAdd() {
        Map<String, String> map = new HashMap<>();
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        map.put("dt", "1");
        map.put("wn", tvWifiShow.getText().toString());
        map.put("nn", etOname.getText().toString());
        map.put("bi", bssid);
        map.put("gn", "");
        map.put("sr", "");
        map.put("slt", "");
        map.put("sa", "");
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
