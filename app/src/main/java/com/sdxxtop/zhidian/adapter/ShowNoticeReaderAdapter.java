package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.NoticeShowPeopleBean;
import com.sdxxtop.zhidian.widget.CircleImageView;

import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/4/3  时间：10:30
 * 邮箱：15010104100@163.com
 * 描述：公告接收详情适配器（已读）
 */

public class ShowNoticeReaderAdapter extends RecyclerView.Adapter<ShowNoticeReaderAdapter.MyViewHolder>{
    private Context context;
    private List<NoticeShowPeopleBean.DataBean.ReadBean> list;

    public ShowNoticeReaderAdapter(Context context, List<NoticeShowPeopleBean.DataBean.ReadBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recy_reader, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (list.get(position).getImg().toString().startsWith("#")) {
            holder.iv_item_recy_img.setColorFilter(Color.parseColor(list.get(position).getImg()));
        } else {
            // TODO: 2018/3/22 更换错误图
            Glide.with(context).load(list.get(position).getImg()).into(holder.iv_item_recy_img);
        }

        String name = list.get(position).getName().toString();

        if (list.get(position).getImg().toString().startsWith("#")) {
            //图片上展示的两个字
            if (name.length() >= 2) {
                holder.tv_item_recy_short_name.setText(name.substring(name.length() - 2, name.length()));
            } else {
                holder.tv_item_recy_short_name.setText(name);
            }
        }
        holder.tv_item_recy_name.setText(name);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView iv_item_recy_img;
        TextView tv_item_recy_name, tv_item_recy_short_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_item_recy_img = (CircleImageView) itemView.findViewById(R.id.iv_item_recy_img);
            tv_item_recy_name = (TextView) itemView.findViewById(R.id.tv_item_recy_name);
            tv_item_recy_short_name = (TextView) itemView.findViewById(R.id.tv_item_recy_short_name);
        }
    }
}

