package com.sdxxtop.zhidian.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.utils.ViewUtil;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/5/11.
 * <p>
 * 抄送
 */

public class ApplyHorCopyRecyclerAdapter extends BaseQuickAdapter<SelectBean, BaseViewHolder> {


    public ApplyHorCopyRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final SelectBean item) {
        View showLayout = helper.getView(R.id.show_layout);
        CircleImageView showImage = helper.getView(R.id.item_apply_copy_circle_image_view);
        TextView showText = helper.getView(R.id.item_apply_copy_user_text);
        ImageView imgDelete = helper.getView(R.id.item_apply_copy_img_delete);

        View addLayout = helper.getView(R.id.add_layout);
        if (helper.getAdapterPosition() == getData().size() - 1) { //最后一个
            showLayout.setVisibility(View.GONE);
            addLayout.setVisibility(View.VISIBLE);
            addLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.addClick();
                    }
                }
            });
        } else {
            showLayout.setVisibility(View.VISIBLE);
            addLayout.setVisibility(View.GONE);
            String color = item.color;
            ViewUtil.setColorItemView(color, item.name, showText, showImage);
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData().remove(item);

                    notifyDataSetChanged();
                    if (deleteListener != null) {
                        deleteListener.onDelete(item);
                    }
                }
            });
        }
    }

    AddListener listener;
    DeleteListener deleteListener;

    public void setAddListener(AddListener listener) {
        this.listener = listener;
    }

    public void setDeleteListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public interface AddListener {
        void addClick();
    }

    public interface DeleteListener {
        void onDelete(final SelectBean item);
    }

}
