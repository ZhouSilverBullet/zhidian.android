package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.PowerBean;

import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/5/5  时间：20:48
 * 邮箱：15010104100@163.com
 * 描述：我的部门人员适配器
 */

public class PowerAdapter extends BaseAdapter{
    private Context context;
    private List<PowerBean> list;

    public PowerAdapter(Context context, List<PowerBean> list) {
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
        MyViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_power, null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        holder.tv_item.setText(list.get(position).getName());
        holder.iv_item.setImageResource(list.get(position).getImg());

        return convertView;
    }

    static class MyViewHolder {
        TextView tv_item;
        ImageView iv_item;

        public MyViewHolder(View view) {
            tv_item = (TextView) view.findViewById(R.id.tv_item);
            iv_item = (ImageView) view.findViewById(R.id.iv_item);
        }
    }
}
