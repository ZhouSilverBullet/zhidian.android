package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.UcenterOutIndexBean;

import java.util.Collection;
import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/4/28  时间：11:30
 * 邮箱：15010104100@163.com
 * 描述：外勤权限适配器（全部）
 */

public class ShowOutPowerAllAdapter extends RecyclerView.Adapter<ShowOutPowerAllAdapter.MyViewHolder> {
    private Context context;
    private List<List<UcenterOutIndexBean.DataEntity.UserinfoEntity>> list;
    private List<UcenterOutIndexBean.DataEntity.PartEntity> partList;

    public ShowOutPowerAllAdapter(Context context, List<List<UcenterOutIndexBean.DataEntity.UserinfoEntity>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_outpower, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.item_rv.setAdapter(new GridAdapter(context, list.get(position), mOnCallBackListener));
        holder.tv_item_partname.setText(list.get(position).get(0).getPart_name());
        holder.view.setVisibility(position + 1 == list.size() ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void replaceData(Collection<List<UcenterOutIndexBean.DataEntity.UserinfoEntity>> collection) {
        if (list != null) {
            list.clear();
            list.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public List<List<UcenterOutIndexBean.DataEntity.UserinfoEntity>> getData() {
        return list;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_partname;
        RecyclerView item_rv;
        View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item_partname = (TextView) itemView.findViewById(R.id.tv_item_partname);
            view = itemView.findViewById(R.id.view);
            item_rv = (RecyclerView) itemView.findViewById(R.id.item_rv);
            RecyclerView.LayoutManager manager = new GridLayoutManager(itemView.getContext(), 4);
            manager.setAutoMeasureEnabled(true);
            item_rv.setLayoutManager(manager);
        }
    }

    public void setPartList(List<UcenterOutIndexBean.DataEntity.PartEntity> partList) {
        this.partList = partList;
    }

    public List<UcenterOutIndexBean.DataEntity.PartEntity> getPartList() {
        return partList;
    }

    private GridAdapter.OnCallBackListener mOnCallBackListener;

    public void setmOnCallBackListener(GridAdapter.OnCallBackListener onCallBackListener) {
        this.mOnCallBackListener = onCallBackListener;
    }
}

