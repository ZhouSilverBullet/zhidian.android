package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ContactIndexBean;
import com.sdxxtop.zhidian.utils.ViewUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者：CaiCM
 * 日期：2018/3/25  时间：10:28
 * 邮箱：15010104100@163.com
 * 描述：我的部门人员适配器
 */

public class ShowMyUserAdapter extends BaseAdapter {

    private Context context;
    private List<ContactIndexBean.DataEntity.ColloectUserEntity> list;

    public ShowMyUserAdapter(Context context, List<ContactIndexBean.DataEntity.ColloectUserEntity> list) {
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
        ShowMyUserAdapter.MyViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_show_user, null);
            holder = new ShowMyUserAdapter.MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ShowMyUserAdapter.MyViewHolder) convertView.getTag();
        }
        String img = list.get(position).getImg();
        String name = list.get(position).getName();

        if (!TextUtils.isEmpty(img)) {
            ViewUtil.setColorItemView(img, name, holder.tv_item_short_name, holder.iv_item_user_head);
        }

        holder.tv_item_user_name.setText(name);
        holder.tv_item_user_job.setText(list.get(position).getPosition());
        return convertView;
    }

    static class MyViewHolder {
        TextView tv_item_user_name, tv_item_user_job, tv_item_short_name;
        CircleImageView iv_item_user_head;
        ImageView tv_item_right_icon;

        public MyViewHolder(View view) {

            tv_item_right_icon = (ImageView) view.findViewById(R.id.tv_item_right_icon);
            tv_item_right_icon.setVisibility(View.GONE);
            iv_item_user_head = (CircleImageView) view.findViewById(R.id.iv_item_user_head);
            tv_item_user_name = (TextView) view.findViewById(R.id.tv_item_user_name);
            tv_item_user_job = (TextView) view.findViewById(R.id.tv_item_user_job);
            tv_item_short_name = (TextView) view.findViewById(R.id.tv_item_short_name);
        }
    }
}
