package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：CaiCM
 * 日期：2018/3/23  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：开启WIFI SwitchButton后，设置WIFI设备
 */
public class WIFISettingActivity extends BaseActivity {

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
    private String otherName;

    @Override
    protected int getActivityView() {
        return R.layout.activity_wifisetting;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        otherName = intent.getStringExtra("otherName");
        if (!TextUtils.isEmpty(otherName)) {
            otherName = otherName.substring(7,otherName.length());
        }
        etOname.setText(StringUtil.stringNotNull(otherName));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager != null) {
            wifiInfo = wifiManager.getConnectionInfo();
            bssid = wifiInfo.getBSSID();
            wifiName = wifiInfo.getSSID();
            if (wifiName.contains("unknown")) {
                tvWifiShow.setText("未连接WiFi");
                etOname.setText("未连接WiFi");
            } else {
                wifiName = wifiName.replace("\"", "");
                tvWifiShow.setText(wifiName);
                if (TextUtils.isEmpty(otherName)) {
                    etOname.setText(wifiName);
                }
            }
        } else {
            showToast("获取wifi信息失败");
        }
    }

    @OnClick({R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if ("未连接WiFi".equals(wifiName)) {
                    ToastUtil.show("请连接WiFi后，再进入当前界面");
                    return;
                }
                String s = etOname.getText().toString();
                if (StringUtil.isEmptyWithTrim(s)) {
                    ToastUtil.show("请输入别名");
                    return;
                }
                LogUtils.e("LOG", "ssid+ss" + bssid);
                Intent intent = new Intent();
                intent.putExtra("wn", tvWifiShow.getText().toString());
                intent.putExtra("nn", s);
                intent.putExtra("bi", bssid);
                setResult(200, intent);
                finish();
                break;
        }
    }
}
