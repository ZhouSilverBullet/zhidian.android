package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.widget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lxl
 * @date 2018/4/30  17:48
 * @desc
 */
public class HorListViewScribbleAdapter extends BaseAdapter implements View.OnClickListener {


    public HorListViewScribbleAdapter(Context context, List<String> strings) {
        this.context = context;
        this.strings = strings;
    }


    private Context context;
    private List<String> strings;

    public void setListSize(int listSize) {
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return strings.size() + 1;
    }

    @Override
    public Object getItem(int position) {

        return strings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_horlist_photo, null);
            holder = new ViewHolder(convertView);

            holder.imagePhoto.setVisibility(View.GONE);
            holder.imgDelete.setVisibility(View.VISIBLE);
            holder.tvName.setVisibility(View.VISIBLE);
            holder.imageCirimg.setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position < strings.size()) {

            holder.imageCirimg.setVisibility(View.VISIBLE);
            holder.tvName.setVisibility(View.VISIBLE);
            holder.imgDelete.setVisibility(View.VISIBLE);
            holder.circleImageViewAdd.setVisibility(View.GONE);
            holder.imageViewAdd.setVisibility(View.GONE);

            holder.tvName.setText(strings.get(position).toString());
        } else {
            holder.imageCirimg.setVisibility(View.GONE);
            holder.tvName.setVisibility(View.GONE);
            holder.imgDelete.setVisibility(View.GONE);
            holder.circleImageViewAdd.setVisibility(View.VISIBLE);
            holder.imageViewAdd.setVisibility(View.VISIBLE);
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strings.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.circleImageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strings.add("添加");
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
        @BindView(R.id.circleimg_add)
        CircleImageView circleImageViewAdd;
        @BindView(R.id.image_add)
        ImageView imageViewAdd;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
