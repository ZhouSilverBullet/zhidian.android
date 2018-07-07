package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.sdxxtop.zhidian.R;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：${刘香龙} on 2018/5/6 0006 15:50
 * 类的描述：
 */
public class SubmissPhotoAdapter extends BaseAdapter {

    public SubmissPhotoAdapter(Context context, List<Bitmap> beans) {
        this.context = context;
        this.beans = beans;
    }

    private Context context;
    private List<Bitmap> beans;

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
        SubmissPhotoAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_submiss_photo, null);
            holder = new SubmissPhotoAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SubmissPhotoAdapter.ViewHolder) convertView.getTag();
        }

        holder.imagePhoty.setImageBitmap(beans.get(position));
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.image_photo)
        ImageView imagePhoty;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
