package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.DeviceIndexBean;
import com.sdxxtop.zhidian.entity.DeviceModifyBean;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：CaiCM
 * 日期：2018/4/25  时间：11:46
 * 邮箱：15010104100@163.com
 * 描述：展示我的 设置 设备管理 GPS信息适配器
 */

public class ShowGpsDeviceAdapter extends BaseAdapter {

    private Context context;
    private List<DeviceIndexBean.DataEntity.GpsEntity> list;

    public ShowGpsDeviceAdapter(Context context, List<DeviceIndexBean.DataEntity.GpsEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_device_gps, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_item_gps_name = (TextView) convertView.findViewById(R.id.tv_item_gps_name);
            viewHolder.tv_item_gps_info = (TextView) convertView.findViewById(R.id.tv_item_gps_info);
            viewHolder.line = convertView.findViewById(R.id.line);
            viewHolder.cb_item_gps = (CheckBox) convertView.findViewById(R.id.cb_item_gps);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DeviceIndexBean.DataEntity.GpsEntity gpsEntity = list.get(position);
        viewHolder.cb_item_gps.setChecked(gpsEntity.getStatus() == 1 ? true : false);
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.cb_item_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalViewHolder.cb_item_gps.isChecked()) {
                    ToastUtil.show("修改为正常状态");
                    postDeviceModify("1", position);
                } else {
                    postDeviceModify("2", position);
                    ToastUtil.show("修改为锁定状态");
                }
            }
        });
        String name = gpsEntity.getName();
        String address = gpsEntity.getAddress();
        if (TextUtils.isEmpty(name)) {
            name = address;
        }
        viewHolder.tv_item_gps_name.setText(name);
        viewHolder.tv_item_gps_info.setText(address);
        if (position == list.size() - 1) {
            viewHolder.line.setVisibility(View.GONE);
        } else {
            viewHolder.line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tv_item_gps_name, tv_item_gps_info;
        View line;
        CheckBox cb_item_gps;
    }

    /**
     * 修改考勤设备状态网络请求（GPS）
     */
    private void postDeviceModify(String st, int position) {
        Map<String, String> map = new HashMap<>();
        map.put("di", list.get(position).getDevice_id() + "");
        map.put("dt", "2");
        map.put("ot", "1");
        map.put("st", st);
        String base64Data = NetUtil.getBase64Data(map);
        RequestUtils.getInstance().buildRequest().postDeviceModify(base64Data).enqueue(new Callback<DeviceModifyBean>() {
            @Override
            public void onResponse(Call<DeviceModifyBean> call, Response<DeviceModifyBean> response) {
                DeviceModifyBean deviceModifyBean = response.body();
                if (deviceModifyBean.getCode() == 200) {
                    ToastUtil.show("修改成功");
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
}
