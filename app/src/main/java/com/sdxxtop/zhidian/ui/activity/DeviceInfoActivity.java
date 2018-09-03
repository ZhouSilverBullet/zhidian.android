package com.sdxxtop.zhidian.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ShowGpsDeviceAdapter;
import com.sdxxtop.zhidian.adapter.ShowMachineDeviceAdapter;
import com.sdxxtop.zhidian.adapter.ShowWifiDeviceAdapter;
import com.sdxxtop.zhidian.entity.DeviceIndexBean;
import com.sdxxtop.zhidian.entity.DeviceModifyBean;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：CaiCM
 * 日期：2018/4/18  时间：11:39
 * 邮箱：15010104100@163.com
 * 描述：设备管理界面
 */
public class DeviceInfoActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnTouchListener, View.OnClickListener {

    @BindView(R.id.view_sub_title_left_layout)
    LinearLayout tvRetuen;
    @BindView(R.id.view_sub_title_right_text)
    ImageView ivRight;
    @BindView(R.id.lv_wifi)
    ListView lvWifi;
    @BindView(R.id.rl_wifi)
    RelativeLayout rlWifi;
    @BindView(R.id.lv_gps)
    ListView lvGps;
    @BindView(R.id.rl_gps)
    RelativeLayout rlGps;
    @BindView(R.id.lv_machine)
    ListView lvMachine;
    @BindView(R.id.rl_machine)
    RelativeLayout rlMachine;

    private PopupWindow popupWindow;
    private View contentView;
    private ShowWifiDeviceAdapter showWifiDeviceAdapter;
    private ShowGpsDeviceAdapter showGpsDeviceAdapter;
    private ShowMachineDeviceAdapter showMachineDeviceAdapter;
    private List<DeviceIndexBean.DataEntity.WifiEntity> listWifi = new ArrayList<>();
    private List<DeviceIndexBean.DataEntity.GpsEntity> listGps = new ArrayList<>();
    private List<DeviceIndexBean.DataEntity.DeviceEntity> listMachine = new ArrayList<>();

    final static int CANCLE = 0;//取消
    private static final int DEL = 1;//删除
    final String[] Items = {"删除", "取消"};

    @Override
    protected int getActivityView() {
        return R.layout.activity_device_info;
    }

    @Override
    protected void initView() {
        showWifiDeviceAdapter = new ShowWifiDeviceAdapter(mContext, listWifi);
        lvWifi.setAdapter(showWifiDeviceAdapter);
        showWifiDeviceAdapter.notifyDataSetChanged();

        showGpsDeviceAdapter = new ShowGpsDeviceAdapter(mContext, listGps);
        lvGps.setAdapter(showGpsDeviceAdapter);
        showGpsDeviceAdapter.notifyDataSetChanged();

        showMachineDeviceAdapter = new ShowMachineDeviceAdapter(mContext, listMachine);
        lvMachine.setAdapter(showMachineDeviceAdapter);
        showMachineDeviceAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        postDeviceIndex();
    }

    @Override
    protected void initEvent() {
        lvWifi.setOnItemClickListener(this);
        lvGps.setOnItemClickListener(this);
        lvMachine.setOnItemClickListener(this);
        lvWifi.setOnItemLongClickListener(this);
        lvGps.setOnItemLongClickListener(this);
        lvMachine.setOnItemLongClickListener(this);
        tvRetuen.setOnClickListener(this);
        ivRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_sub_title_left_layout:
                finish();
                break;
            case R.id.view_sub_title_right_text:
                showPopWindow();
                break;
            case R.id.pop_vote:
                popupWindow.dismiss();
                darkenBackground(1f);
                Intent intentWifi = new Intent(mContext, AddGpsActivity.class);
                startActivity(intentWifi);
                break;
            case R.id.pop_mark:
                popupWindow.dismiss();
                darkenBackground(1f);
                Intent intentGps = new Intent(mContext, AddWifiActivity.class);
                startActivity(intentGps);
                break;
            default:
                break;
        }
    }


    /**
     * 设备管理主页网络请求
     */
    private void postDeviceIndex() {
        Map<String, String> map = new HashMap<>();
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postDeviceIndex(base64Data).enqueue(new Callback<DeviceIndexBean>() {
            @Override
            public void onResponse(Call<DeviceIndexBean> call, Response<DeviceIndexBean> response) {
                closeProgressDialog();
                DeviceIndexBean deviceIndexBean = response.body();
                if (deviceIndexBean.getCode() != 200) {
                    ToastUtil.show(response.body().getMsg());
                    return;
                }
                if (deviceIndexBean.getData().getWifi() != null) {
                    listWifi.clear();
                    listWifi.addAll(deviceIndexBean.getData().getWifi());
                    showWifiDeviceAdapter.notifyDataSetChanged();
                }
                if (deviceIndexBean.getData().getGps() != null) {
                    listGps.clear();
                    listGps.addAll(deviceIndexBean.getData().getGps());
                    showGpsDeviceAdapter.notifyDataSetChanged();
                }
                if (deviceIndexBean.getData().getDevice() != null) {
                    listMachine.clear();
                    listMachine.addAll(deviceIndexBean.getData().getDevice());
                    showMachineDeviceAdapter.notifyDataSetChanged();
                }

                rlWifi.setVisibility(showWifiDeviceAdapter.getCount() == 0 ? View.GONE : View.VISIBLE);
                rlGps.setVisibility(showGpsDeviceAdapter.getCount() == 0 ? View.GONE : View.VISIBLE);
                rlMachine.setVisibility(showMachineDeviceAdapter.getCount() == 0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onFailure(Call<DeviceIndexBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_wifi:
                Intent intentWifi = new Intent(mContext, ModifyWifiActivity.class);
                intentWifi.putExtra("di", listWifi.get(position).getDevice_id() + "");
                intentWifi.putExtra("name", listWifi.get(position).getName());
                intentWifi.putExtra("wifi_name", listWifi.get(position).getWifi_name());
                startActivity(intentWifi);
                break;
            case R.id.lv_gps:
                Intent intentGps = new Intent(mContext, ModifyGpsActivity.class);
                intentGps.putExtra("di", listGps.get(position).getDevice_id() + "");
                intentGps.putExtra("gn", listGps.get(position).getName());
                intentGps.putExtra("address", listGps.get(position).getAddress());
                intentGps.putExtra("longitude", listGps.get(position).getLongitude());
                intentGps.putExtra("sign_range", listGps.get(position).getSign_range() + "m");
                startActivity(intentGps);
                break;
            case R.id.lv_machine:
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_wifi:
                final String device_idW = listWifi.get(position).getDevice_id() + "";
                new AlertDialog.Builder(this)
                        .setItems(Items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    postDeviceModify(device_idW, "1", position);
                                } else {
                                }
                            }
                        })
                        .create().show();
                break;
            case R.id.lv_gps:
                final String device_idG = listGps.get(position).getDevice_id() + "";
                new AlertDialog.Builder(this)
                        .setItems(Items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    postDeviceModify(device_idG, "2", position);
                                } else {
                                }
                            }
                        })
                        .create().show();
                break;
            case R.id.lv_machine:
                final String device_idM = listMachine.get(position).getDevice_id() + "";
                new AlertDialog.Builder(this)
                        .setItems(Items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    postDeviceModify(device_idM, "3", position);
                                } else {
                                }
                            }
                        })
                        .create().show();
                break;
            default:
                break;
        }
        return true;
    }


    /**
     * 修改考勤设备状态网络请求（GPS）
     */
    private void postDeviceModify(String di, final String dt, final int position) {
        Map<String, String> map = new HashMap<>();
        map.put("di", di);
        map.put("dt", dt);
        map.put("ot", "2");
        map.put("st", "");
        String base64Data = NetUtil.getBase64Data(map);
        RequestUtils.getInstance().buildRequest().postDeviceModify(base64Data).enqueue(new Callback<DeviceModifyBean>() {
            @Override
            public void onResponse(Call<DeviceModifyBean> call, Response<DeviceModifyBean> response) {
                DeviceModifyBean deviceModifyBean = response.body();
                if (deviceModifyBean.getCode() == 200) {
                    ToastUtil.show("删除成功");
                    switch (dt) {
                        case "1":
                            listWifi.remove(position);
                            showWifiDeviceAdapter.notifyDataSetChanged();
                            break;
                        case "2":
                            listGps.remove(position);
                            showGpsDeviceAdapter.notifyDataSetChanged();
                            break;
                        case "3":
                            listMachine.remove(position);
                            showMachineDeviceAdapter.notifyDataSetChanged();
                            break;
                    }
                } else {
                    ToastUtil.show(deviceModifyBean.getMsg());
                }
            }

            @Override
            public void onFailure(Call<DeviceModifyBean> call, Throwable t) {
                ToastUtil.show("网络错误");
            }
        });
    }


    /**
     * Gps设备和WiFi设备
     */
    private void showPopWindow() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.popuplayout_manager, null);
        contentView.setOnTouchListener(this);
        popupWindow = new PopupWindow(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        popupWindow.setOutsideTouchable(true);
        darkenBackground(0.6f);
        //设置点击事件
        TextView pop_vote = (TextView) contentView.findViewById(R.id.pop_vote);
        TextView pop_mark = (TextView) contentView.findViewById(R.id.pop_mark);
        pop_vote.setOnClickListener(this);
        pop_mark.setOnClickListener(this);
        popupWindow.showAsDropDown(ivRight);
    }

    /**
     * 改变背景颜色
     */
    private void darkenBackground(Float bgcolor) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
            darkenBackground(1f);
        }
        return false;
    }
}
