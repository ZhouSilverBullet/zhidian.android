package com.sdxxtop.zhidian.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.NoticeDataBean;

/**
 * 作者：CaiCM
 * 日期：2018/4/3  时间：10:30
 * 邮箱：15010104100@163.com
 * 描述：公告首页适配器
 */

public class NoticeReadAdapter extends BaseQuickAdapter<NoticeDataBean.DataBean.NoticeBean, BaseViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    public NoticeReadAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, NoticeDataBean.DataBean.NoticeBean item) {
        ImageView iv_item_notice_img = helper.getView(R.id.iv_item_notice_img);
        TextView tv_item_notice_title = helper.getView(R.id.tv_item_notice_title);
        TextView tv_item_notice_name = helper.getView(R.id.tv_item_notice_name);
        TextView tv_item_notice_time = helper.getView(R.id.tv_item_notice_time);

        Glide.with(mContext).load(item.getImg()).into(iv_item_notice_img);

        tv_item_notice_title.setText(item.getTitle());
        tv_item_notice_name.setText(item.getName());
        tv_item_notice_time.setText(item.getAdd_time());
    }

    public interface OnItemClickListener{
        void onClick( int position);
        void onLongClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this. mOnItemClickListener=onItemClickListener;
    }
}

