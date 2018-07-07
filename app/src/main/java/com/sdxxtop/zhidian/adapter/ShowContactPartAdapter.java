package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ContactPartBean;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者：CaiCM
 * 日期：2018/3/27  时间：10:28
 * 邮箱：15010104100@163.com
 * 描述：组织架构部门的适配器
 * （发布公告选择接受范围也可用）
 */

public class ShowContactPartAdapter extends BaseAdapter {

    String[] partColors = {"#3296fa", "#9396fa", "#9d81ff", "#81a4f9", "#d9d6ec", "#4e76e4", "#ad8ea3"};

    private Context context;
    private ContactPartBean.DataEntity dataBean;
    private OnClickObsever obsever;

    public ShowContactPartAdapter(Context context, ContactPartBean.DataEntity dataBean, OnClickObsever obsever) {
        this.context = context;
        this.dataBean = dataBean;
        this.obsever = obsever;
    }


    @Override
    public int getCount() {
        if (dataBean.getPart() == null && dataBean.getUserinfo() == null) {
            return 0;
        } else if (dataBean.getPart() == null && dataBean.getUserinfo() != null) {
            return dataBean.getUserinfo().size();
        } else if (dataBean.getPart() != null && dataBean.getUserinfo() == null) {
            return dataBean.getPart().size();
        } else {
            return dataBean.getPart().size() + dataBean.getUserinfo().size();
        }
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
        final MyViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_show_user, null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        String name = "";

        if (position < dataBean.getPart().size() && dataBean.getPart().size() != 0) {
            holder.line.setVisibility(View.GONE);
            name = dataBean.getPart().get(position).getPart_name();

            final String img = partColors[position % 7];
            if (!TextUtils.isEmpty(img)) {
                ViewUtil.setPartItemView(img, name, holder.tv_item_short_name, holder.iv_item_user_head);
            }

            holder.tv_item_right_icon.setVisibility(View.VISIBLE);
            holder.tv_item_user_job.setVisibility(View.INVISIBLE);

        } else if ((position - dataBean.getPart().size()) < dataBean.getUserinfo().size() && dataBean.getUserinfo().size() != 0) {
            holder.tv_item_right_icon.setVisibility(View.GONE);
            if (position - dataBean.getPart().size() == 0) {
                holder.line.setVisibility(View.VISIBLE);
            } else {
                holder.line.setVisibility(View.GONE);
            }

            ContactPartBean.DataEntity.UserinfoEntity userinfoEntity = dataBean.getUserinfo().get(position - dataBean.getPart().size());
            name = userinfoEntity.getName();
            String img = userinfoEntity.getImg();

            if (!TextUtils.isEmpty(img)) {
                ViewUtil.setColorItemView(img, name, holder.tv_item_short_name, holder.iv_item_user_head);
            }
            holder.tv_item_user_job.setVisibility(View.VISIBLE);
            holder.tv_item_user_job.setText(StringUtil.stringNotNull(userinfoEntity.getPosition()));
        }

        holder.tv_item_user_name.setText(name);

        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < dataBean.getPart().size()) {
                    obsever.onClickPartCallBack(position);
                } else {
                    obsever.onClickUserCallBack(position - dataBean.getPart().size());
                }
            }
        });
        return convertView;
    }


    public interface OnClickObsever {
        void onClickPartCallBack(int position);

        void onClickUserCallBack(int position);
    }


    static class MyViewHolder {
        TextView tv_item_user_name, tv_item_user_job, tv_item_short_name, line;
        CircleImageView iv_item_user_head;
        RelativeLayout rl_item;
        ImageView tv_item_right_icon;

        public MyViewHolder(View view) {
            rl_item = (RelativeLayout) view.findViewById(R.id.rl_item);
            iv_item_user_head = (CircleImageView) view.findViewById(R.id.iv_item_user_head);
            tv_item_user_name = (TextView) view.findViewById(R.id.tv_item_user_name);
            tv_item_right_icon = (ImageView) view.findViewById(R.id.tv_item_right_icon);
            tv_item_user_job = (TextView) view.findViewById(R.id.tv_item_user_job);
            tv_item_short_name = (TextView) view.findViewById(R.id.tv_item_short_name);
            line = (TextView) view.findViewById(R.id.line);
        }
    }
}
