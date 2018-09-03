package com.sdxxtop.zhidian.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.luck.picture.lib.entity.LocalMedia;
import com.sdxxtop.zhidian.R;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2018/5/9.
 */

public class HomeworkRecyclerAdapter extends BaseQuickAdapter<LocalMedia, BaseViewHolder> {

    public HomeworkRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final LocalMedia item) {
        View view = helper.getView(R.id.item_apply_hor_photo);
        ImageView horImg = helper.getView(R.id.item_apply_hor_img);
        View dele = helper.getView(R.id.item_apply_hor_dele);
        View horLayout = helper.getView(R.id.item_apply_hor_layout);

        if (item.getDuration() == -100) {
            horLayout.setVisibility(View.VISIBLE);
            dele.setVisibility(View.GONE);
            horImg.setVisibility(View.GONE);
            horLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.click();
                    }
                }
            });
        } else {
            horLayout.setVisibility(View.GONE);
            dele.setVisibility(View.VISIBLE);
            horImg.setVisibility(View.VISIBLE);
            final String compressPath = item.getCompressPath();
            Bitmap bitmap = getBitmapForPath(compressPath);
            if (bitmap != null) {
                horImg.setImageBitmap(bitmap);
            }
            dele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.delete(item);
                    }
                    getData().remove(item);
                    notifyDataSetChanged();
                }
            });
        }
    }

    HorListener listener;

    public void setListener(HorListener listener) {
        this.listener = listener;
    }

    public interface HorListener {
        void click();
        void delete(LocalMedia item);
    }

    private Bitmap getBitmapForPath(String path) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(path);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
