package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.DeviceModifyInfoBean;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyWifiActivity extends BaseActivity {

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
    private String di;
    private String wifi_name;

    @Override
    protected int getActivityView() {
        return R.layout.activity_modify_wifi;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        di = intent.getStringExtra("di");
        wifi_name = intent.getStringExtra("wifi_name");
        etOname.setText(wifi_name);
        etOname.setSelection(etOname.getText().length());
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager != null) {
            wifiInfo = wifiManager.getConnectionInfo();
            bssid = wifiInfo.getBSSID();
            wifiName = wifiInfo.getSSID();
            tvWifiShow.setText(wifiName.replace("\"", ""));
        }
    }

    @OnClick({R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if ("<unknown ssid>".equals(tvWifiShow.getText().toString())) {
                    ToastUtil.show("请连接WiFi后，在进入当前界面");
                    return;
                }
                if (etOname.getText().toString().equals("")) {
                    ToastUtil.show("请输入别名");
                    return;
                }
                postDeviceModifyInfo();
                break;
        }
    }

    /**
     * 修改考勤设备信息网络请求（WIFI）
     */
    private void postDeviceModifyInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("di", di);
        map.put("dt", "1");
        map.put("wn", tvWifiShow.getText().toString().replace("\"", ""));
        map.put("nn", etOname.getText().toString());
        map.put("bi", bssid);
        map.put("gn", "");
        map.put("sr", "");
        map.put("slt", "");
        map.put("sa", "");
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
