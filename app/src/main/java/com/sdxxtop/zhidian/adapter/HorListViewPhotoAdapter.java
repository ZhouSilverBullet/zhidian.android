package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.entity.LocalMedia;
import com.sdxxtop.zhidian.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lxl
 * @date 2018/4/30  17:48
 * @desc
 */
public class HorListViewPhotoAdapter extends BaseAdapter implements View.OnClickListener {

    public HorListViewPhotoAdapter(Context context, int shshowType, List<LocalMedia> localMediaList) {
        this.context = context;
        this.localMediaList = localMediaList;
    }


    private Context context;
    private List<LocalMedia> localMediaList;


    @Override
    public int getCount() {
        return localMediaList == null ? 0 : localMediaList.size();
    }

    @Override
    public Object getItem(int position) {
        return localMediaList.get(position);
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

            holder.imagePhoto.setVisibility(View.VISIBLE);
            holder.imgDelete.setVisibility(View.VISIBLE);
            holder.tvName.setVisibility(View.GONE);
            holder.imageCirimg.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        LocalMedia media = localMediaList.get(position);
        String compressPath = media.getCompressPath();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(compressPath);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            holder.imagePhoto.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        if (position == localMediaList.size() - 1) {
            holder.imgDelete.setVisibility(View.GONE);
        }
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localMediaList.remove(position);
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
