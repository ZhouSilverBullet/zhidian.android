package com.sdxxtop.zhidian.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ApproverIndexBean;
import com.sdxxtop.zhidian.utils.ViewUtil;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/5/11.
 * <p>
 * 抄送
 */

public class ApplyApproverRecyclerAdapter extends BaseQuickAdapter<ApproverIndexBean.DataBean.ApproverBean, BaseViewHolder> {


    public ApplyApproverRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ApproverIndexBean.DataBean.ApproverBean item) {
        View showLayout = helper.getView(R.id.show_layout);
        ImageView imgDelete = helper.getView(R.id.item_apply_copy_img_delete);
        imgDelete.setVisibility(View.GONE);
        CircleImageView showImage = helper.getView(R.id.item_apply_copy_circle_image_view);
        TextView showText = helper.getView(R.id.item_apply_copy_user_text);
        View addLayout = helper.getView(R.id.add_layout);
        showLayout.setVisibility(View.VISIBLE);
        addLayout.setVisibility(View.GONE);
        String color = item.getImg();
        ViewUtil.setColorItemView(color, item.getName(), showText, showImage);
    }


}
