package com.sdxxtop.zhidian.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.CreateCompanyData;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.activity.CreateCompanyActivity;
import com.sdxxtop.zhidian.ui.activity.GPSSettingActivity;
import com.sdxxtop.zhidian.ui.activity.WIFISettingActivity;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.DeviceUtil;
import com.sdxxtop.zhidian.utils.FDLocation;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.sdxxtop.zhidian.utils.PreferenceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 作者：CaiCM
 * 日期：2018/3/23  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：创建公司的第二个Fragment
 */
public class CreateCompSecondFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.cb_wifi)
    CheckBox cbWifi;
    @BindView(R.id.tv_wifi_show)
    TextView tvWifiShow;
    @BindView(R.id.rl_wifi_open)
    RelativeLayout rlWifiOpen;
    @BindView(R.id.tv_wifi)
    TextView tvWifi;
    @BindView(R.id.cb_gps)
    CheckBox cbGps;
    @BindView(R.id.tv_gps_show)
    TextView tvGpsShow;
    @BindView(R.id.rl_gps_open)
    RelativeLayout rlGpsOpen;
    @BindView(R.id.tv_gps)
    TextView tvGps;
    @BindView(R.id.btn_next)
    Button btnNext;

    private String wn;
    private String bi;
    private String wifi_name;
    private CreateCompanyData companyData;
    private String gn;
    private String sr;
    private String slt;
    private String sa;

    private String gpsCardName;

    //gps的
    String[] gps = new String[]{
            "300m", "200m", "100m"
    };
    private String jinDuAndWeiDu;

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_create_comp_second;
    }

    @Override
    protected void initView() {
        companyData = ((CreateCompanyActivity) getActivity()).companyData;
    }

    @Override
    protected void initData() {
//        if (TextUtils.isEmpty(wn)) {
//            tvWifiShow.setText("设置WiFi:");
//        } else {
//            tvWifiShow.setText("设置WiFi:" + wn);
//        }

        //wifi设置
        if (!TextUtils.isEmpty(companyData.nn)) {
            tvWifiShow.setText("设置WiFi:" + companyData.nn);
            rlWifiOpen.setVisibility(View.VISIBLE);
        } else {
            tvWifiShow.setText("设置WiFi:");
        }

        if (!TextUtils.isEmpty(companyData.gn)) {
            tvGpsShow.setText(companyData.gn);
            rlGpsOpen.setVisibility(View.VISIBLE);
        } else {
            tvGpsShow.setText("");
        }

        String wifiName = tvWifiShow.getText().toString();
        //刚刚进来的时候
        if (!TextUtils.isEmpty(wifiName) && wifiName.length() == 7) {
            String getWifiName = DeviceUtil.getWifiName(mContext);
            tvWifiShow.setText("设置WiFi:" + getWifiName);
            companyData.nn = getWifiName;
            companyData.wn = getWifiName;
            companyData.bi = DeviceUtil.getWifiId(mContext);
        }
        String gpsSize = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.GPS_SETTING_SIZE);
        if (!TextUtils.isEmpty(gpsSize)) {
            switch (gpsSize) {
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
        } else {
            sr = 300 + "";
        }

        if (EasyPermissions.hasPermissions(mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
            location();
        } else {
            EasyPermissions.requestPermissions(this, "请为知点开启gps权限", BaseActivity.REQUEST_PERMISSION_LOACTION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void location() {
        FDLocation.getInstance().location();
        FDLocation.getInstance().setLocationCompanyListener(new FDLocation.LocationCompanyListener() {
            @Override
            public void onAddress(AMapLocation address) {
                if (address != null) {
                    String address1 = address.getAddress();
                    tvGpsShow.setText(address1);

                    gpsCardName = address.getPoiName();
                    companyData.sr = sr;
                    jinDuAndWeiDu = (address.getLongitude() + "," + address.getLatitude());
                    companyData.slt = jinDuAndWeiDu;
                    companyData.sa = address1;
                    companyData.gn = address.getPoiName();
                }
                FDLocation.getInstance().removeLocationCompanyListener();
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        if (requestCode == BaseActivity.REQUEST_PERMISSION_LOACTION) {
            location();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        super.onPermissionsDenied(requestCode, perms);
        if (requestCode == BaseActivity.REQUEST_PERMISSION_LOACTION) {
//            showToast("知点未被允许gps权限，将有很多功能无法使用");
        }
    }

    @OnClick({R.id.rl_wifi_open, R.id.rl_gps_open, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_wifi_open:
                if (EasyPermissions.hasPermissions(mContext, Manifest.permission.ACCESS_WIFI_STATE)) {
                    Intent intentWIFI = new Intent(getActivity(), WIFISettingActivity.class);
                    intentWIFI.putExtra("otherName", tvWifiShow.getText().toString());
                    startActivityForResult(intentWIFI, 10001);
                } else {
                    EasyPermissions.requestPermissions(mContext, "请开启wifi权限", BaseActivity.REQUEST_PERMISSION_WIFI, Manifest.permission.ACCESS_WIFI_STATE);
                }

                break;

            case R.id.rl_gps_open:
                Intent intentGPS = new Intent(getActivity(), GPSSettingActivity.class);
                intentGPS.putExtra("otherName", gpsCardName);
                intentGPS.putExtra("address_name", sa);
                startActivityForResult(intentGPS, 10003);
                break;
            case R.id.btn_next:

                if (!cbWifi.isChecked()) {  //没选中wifi清除缓存的wifi
                    companyData.wn = "";
                    companyData.nn = "";
                    companyData.bi = "";
                }

                if (!cbGps.isChecked()) {  //没选中GPS清除缓存的gps
                    companyData.gn = "";
                    companyData.sr = "";
                    companyData.slt = "";
                    companyData.sa = "";
                }

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, ((CreateCompanyActivity) getActivity()).createCompThirdFragment).commit();
                ((CreateCompanyActivity) getActivity()).companyData.count = 3;
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cbWifi.setOnCheckedChangeListener(this);
        cbGps.setOnCheckedChangeListener(this);
    }

    /*
         *监听checkbook的状态事件
         */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.cb_wifi) {
            if (isChecked) {
                rlWifiOpen.setVisibility(View.VISIBLE);
//                wifi_name = tvWifiShow.getText().toString();
//                companyData.wn = wifi_name;
//                if (bi != null)
//                    companyData.bi = bi;
            } else {
                rlWifiOpen.setVisibility(View.GONE);
            }
        } else {
            if (isChecked) {
                rlGpsOpen.setVisibility(View.VISIBLE);

//                if (gn != null) {
//                    tvGpsShow.setText(gn);
//                    companyData.gn = gn;
//                }
//                if (sr != null)
//                    companyData.sr = sr;
//                if (slt != null)
//                    companyData.slt = slt;
//                if (sa != null)
//                    companyData.sa = sa;
            } else {
                rlGpsOpen.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001 && resultCode == 200 && data != null) {
            wn = data.getStringExtra("wn");
            String nn = data.getStringExtra("nn");
            bi = data.getStringExtra("bi");
            tvWifiShow.setText("设置WiFi:" + nn);
            LogUtils.e("log", wn);
            LogUtils.e("log", bi);
            companyData.wn = wn;
            companyData.bi = bi;
            companyData.nn = nn;

        } else if (requestCode == 10003 && resultCode == 300 && data != null) {
            gn = data.getStringExtra("gn");
            sr = data.getStringExtra("sr");
            slt = data.getStringExtra("slt");
            sa = data.getStringExtra("sa");
            tvGpsShow.setText(sa);

            gpsCardName = gn;
            companyData.gn = gn;
            companyData.sr = sr;
            companyData.slt = slt;
            companyData.sa = sa;
        }
    }
}
