package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.NoticeShowPeopleBean;
import com.sdxxtop.zhidian.utils.ViewUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者：CaiCM
 * 日期：2018/4/3  时间：10:30
 * 邮箱：15010104100@163.com
 * 描述：公告接收详情适配器（未读）
 */

public class ShowNoticeNotReaderAdapter extends RecyclerView.Adapter<ShowNoticeNotReaderAdapter.MyViewHolder> {
    private Context context;
    private List<NoticeShowPeopleBean.DataBean.NotReadBean> list;

    public ShowNoticeNotReaderAdapter(Context context, List<NoticeShowPeopleBean.DataBean.NotReadBean> list) {
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

        String img = list.get(position).getImg();
        String name = list.get(position).getName();
        ViewUtil.setColorItemView(img, name, holder.tv_item_recy_short_name, holder.iv_item_recy_img);
        holder.tv_item_recy_name.setText(name);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
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

