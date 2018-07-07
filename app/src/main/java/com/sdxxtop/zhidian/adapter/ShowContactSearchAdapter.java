package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ContactSearchBean;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者：CaiCM
 * 日期：2018/3/25  时间：10:28
 * 邮箱：15010104100@163.com
 * 描述：通讯录展示搜索结果适配器
 */

public class ShowContactSearchAdapter extends BaseAdapter {

    private Context context;
    private List<ContactSearchBean.DataEntity.UserinfoEntity> list;

    public ShowContactSearchAdapter(Context context, List<ContactSearchBean.DataEntity.UserinfoEntity> list) {
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
        ShowContactSearchAdapter.MyViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_show_user3, null);
            holder = new ShowContactSearchAdapter.MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ShowContactSearchAdapter.MyViewHolder) convertView.getTag();
        }

        if (list.get(position).getImg().toString().startsWith("#")) {
            holder.iv_item_user_head.setImageDrawable(new ColorDrawable(Color.parseColor(list.get(position).getImg())));
        } else {
            Glide.with(context).load(list.get(position).getImg()).into(holder.iv_item_user_head);
        }


        if (list.get(position).getImg().toString().startsWith("#")) {
            //图片上展示的两个字
            if (list.get(position).getName().length() >= 2) {
                holder.tv_item_short_name.setText(list.get(position).getName().substring(list.get(position).getName().length() - 2, list.get(position).getName().length()));
            } else {
                holder.tv_item_short_name.setText(list.get(position).getName());
            }
        } else {
            holder.tv_item_short_name.setText("");
        }

        holder.tv_item_user_name.setText(list.get(position).getName());
        holder.tv_item_user_job.setText(list.get(position).getPosition());
        return convertView;
    }

    static class MyViewHolder {
        TextView tv_item_user_name, tv_item_user_job, tv_item_short_name;
        CircleImageView iv_item_user_head;

        public MyViewHolder(View view) {
            ImageView tv_item_right_icon = (ImageView) view.findViewById(R.id.tv_item_right_icon);
            tv_item_right_icon.setVisibility(View.GONE);
            iv_item_user_head = (CircleImageView) view.findViewById(R.id.iv_item_user_head);
            tv_item_user_name = (TextView) view.findViewById(R.id.tv_item_user_name);
            tv_item_user_job = (TextView) view.findViewById(R.id.tv_item_user_job);
            tv_item_short_name = (TextView) view.findViewById(R.id.tv_item_short_name);
        }
    }
}
