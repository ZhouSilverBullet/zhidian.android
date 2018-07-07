package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.DeviceIndexBean;

import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/4/25  时间：11:46
 * 邮箱：15010104100@163.com
 * 描述：展示我的 设置 设备管理 考勤机信息适配器
 */

public class ShowMachineDeviceAdapter extends BaseAdapter{

    private Context context;
    private List<DeviceIndexBean.DataEntity.DeviceEntity> list;

    public ShowMachineDeviceAdapter(Context context, List<DeviceIndexBean.DataEntity.DeviceEntity> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = View.inflate(context, R.layout.item_device_machine, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_item_machine_name = (TextView) convertView.findViewById(R.id.tv_item_machine_name);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_item_machine_name.setText(list.get(position).getName());
        return convertView;
    }

    static class ViewHolder{
        TextView tv_item_machine_name;
    }
}
