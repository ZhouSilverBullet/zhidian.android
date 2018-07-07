package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.TestBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：${刘香龙} on 2018/5/6 0006 16:56
 * 类的描述：
 */
public class OnkeyExaminAdapter extends BaseAdapter implements View.OnClickListener {

    public OnkeyExaminAdapter(Context context, List<TestBean> beans , boolean isShowStateView) {
        this.context = context;
        this.beans = beans;
        this.isShowStateView = isShowStateView;
    }

    private Context context;
    private List<TestBean> beans;
    private Boolean isShowStateView;

    @Override
    public int getCount() {
        return beans == null ? 0 : beans.size();
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
        final OnkeyExaminAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_onkey_examin, null);
            holder = new OnkeyExaminAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (OnkeyExaminAdapter.ViewHolder) convertView.getTag();
        }

        if (isShowStateView){
            holder.tvStateView.setVisibility(View.VISIBLE);
//            holder.tvStateView.setText(beans.get(position).getString5());
        }else {
            holder.tvStateView.setVisibility(View.GONE);
        }

        if (beans.get(position).isSelected()){
            holder.imageCheck.setBackgroundResource(R.mipmap.selected);
        }else {
            holder.imageCheck.setBackgroundResource(R.mipmap.unselected);
        }

        holder.tvName.setText(beans.get(position).getString1());
//        holder.tvContent.setText(beans.get(position).getString2());
//        holder.tvState.setText(beans.get(position).getString3());
//        holder.tvTime.setText(beans.get(position).getString4());

        holder.imageCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beans.get(position).isSelected()){
                    holder.imageCheck.setBackgroundResource(R.mipmap.unselected);
                }else {
                    holder.imageCheck.setBackgroundResource(R.mipmap.selected);
                }

                beans.get(position).setSelected(beans.get(position).isSelected());
            }
        });

        return convertView;
    }

    @Override
    public void onClick(View v) {

    }


    static class ViewHolder {
        @BindView(R.id.tv_applied_name)
        TextView tvName;
        @BindView(R.id.tv_applied_content)
        TextView tvContent;
        @BindView(R.id.tv_applied_state)
        TextView tvState;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_state_view)
        TextView tvStateView;
        @BindView(R.id.image_check)
        ImageView imageCheck;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
