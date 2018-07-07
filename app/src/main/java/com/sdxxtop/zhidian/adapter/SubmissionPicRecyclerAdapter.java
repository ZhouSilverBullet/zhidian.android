package com.sdxxtop.zhidian.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.ui.activity.PhotoViewActivity;

/**
 * Created by Administrator on 2018/5/9.
 */

public class SubmissionPicRecyclerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SubmissionPicRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final String item) {
        ImageView horImg = helper.getView(R.id.item_apply_hor_img);
        Glide.with(mContext).load(item).into(horImg);

        horImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoViewActivity.start(mContext, item);
            }
        });
    }

}
