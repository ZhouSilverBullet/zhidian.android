package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.UcenterOutIndexBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/4/27  时间：19:28
 * 邮箱：15010104100@163.com
 * 描述：外勤权限侧拉筛选侧拉列表适配器
 */

public class ShowOutPowerDrawerLayoutAdapter extends BaseAdapter {

    private Context context;
    private List<UcenterOutIndexBean.DataEntity.PartEntity> list;

    public ShowOutPowerDrawerLayoutAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
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
        ShowOutPowerDrawerLayoutAdapter.MyViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_show_part, null);
            holder = new ShowOutPowerDrawerLayoutAdapter.MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ShowOutPowerDrawerLayoutAdapter.MyViewHolder) convertView.getTag();
        }

        holder.tv_item_partname.setText(list.get(position).getPart_name());
        return convertView;
    }

    public void replaceData(Collection<UcenterOutIndexBean.DataEntity.PartEntity> collection) {
        if (list != null) {
            list.clear();
            list.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void addAll(Collection<UcenterOutIndexBean.DataEntity.PartEntity> collection) {
        if (list != null) {
            list.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public List<UcenterOutIndexBean.DataEntity.PartEntity> getData() {
        return list;
    }

    static class MyViewHolder {
        TextView tv_item_partname;

        public MyViewHolder(View view) {
            tv_item_partname = (TextView) view.findViewById(R.id.tv_item_partname);
        }
    }
}
