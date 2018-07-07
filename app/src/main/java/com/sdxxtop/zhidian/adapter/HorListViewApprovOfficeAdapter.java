package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ApproverIndexBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lxl
 * @date 2018/4/30  17:48
 * @desc
 */
public class HorListViewApprovOfficeAdapter extends BaseAdapter implements View.OnClickListener {


    public HorListViewApprovOfficeAdapter(Context context, List<ApproverIndexBean.DataBean.ApproverBean> approverBeanList) {
        this.context = context;
        this.approverBeanList = approverBeanList;
    }


    private Context context;
    private List<ApproverIndexBean.DataBean.ApproverBean> approverBeanList;

    public void setListSize(int listSize) {
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return approverBeanList != null ? approverBeanList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return approverBeanList.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        ApproverIndexBean.DataBean.ApproverBean approverBean = approverBeanList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_horlist_photo, null);
            holder = new ViewHolder(convertView);

            holder.imagePhoto.setVisibility(View.GONE);
            holder.imgDelete.setVisibility(View.GONE);
            holder.tvName.setVisibility(View.VISIBLE);
            holder.imageCirimg.setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(approverBean.getName());

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public void onClick(View v) {

    }


    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.image_delete)
        ImageView imgDelete;
        @BindView(R.id.img_photo)
        ImageView imagePhoto;
        @BindView(R.id.cirimg_conten)
        ImageView imageCirimg;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
