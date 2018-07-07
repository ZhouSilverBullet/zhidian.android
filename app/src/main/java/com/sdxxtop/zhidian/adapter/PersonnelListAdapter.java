package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.TestBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：${刘香龙} on 2018/4/28 0028 11:37
 * 类的描述：
 */
public class PersonnelListAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;

    public PersonnelListAdapter(Context context, List<TestBean> beans , boolean isShowStateView) {
        this.context = context;
        this.beans = beans;
        this.isShowStateView = isShowStateView;
    }
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_applied, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (isShowStateView){
            holder.tvStateView.setVisibility(View.VISIBLE);
//            holder.tvStateView.setText(beans.get(position).getString5());
        }else {
            holder.tvStateView.setVisibility(View.GONE);
        }

        holder.tvName.setText(beans.get(position).getString1());
//        holder.tvContent.setText(beans.get(position).getString2());
//        holder.tvState.setText(beans.get(position).getString3());
//        holder.tvTime.setText(beans.get(position).getString4());
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


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
