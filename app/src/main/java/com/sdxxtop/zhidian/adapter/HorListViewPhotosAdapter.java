package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.utils.PictureUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：CaiCM
 * 日期：2018/5/9  时间：16:28
 * 邮箱：15010104100@163.com
 * 描述：展示多个图片适配器
 */
public class HorListViewPhotosAdapter extends BaseAdapter implements View.OnClickListener {

    public HorListViewPhotosAdapter(Context context, int shshowType, List<String> photos) {
        this.context = context;
        this.photos = photos;
    }


    private Context context;
    private List<String> photos;


    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_horlist_photos, null);
                holder = new ViewHolder(convertView);

                holder.imagePhoto.setVisibility(View.VISIBLE);
                holder.imgDelete.setVisibility(View.VISIBLE);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.imagePhoto.setImageBitmap(PictureUtils.getSmallBitmap(photos.get(position), 160, 160));

            if (position == photos.size()){
                holder.imgDelete.setVisibility(View.GONE);
            }
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photos.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public void onClick(View v) {

    }


    static class ViewHolder {
        @BindView(R.id.image_delete)
        ImageView imgDelete;
        @BindView(R.id.img_photo)
        ImageView imagePhoto;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
