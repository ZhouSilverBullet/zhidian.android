package com.sdxxtop.zhidian.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.utils.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/5/11.
 * <p>
 * 抄送
 */

public class ApplyCollectRecyclerAdapter extends BaseMultiItemQuickAdapter<SelectBean, BaseViewHolder> {

    public ApplyCollectRecyclerAdapter(@Nullable List<SelectBean> data) {
        super(data);

        addItemType(SelectBean.TYPE_USER, R.layout.item_notice_recve_user_recycler);
        addItemType(SelectBean.TYPE_PART, R.layout.item_notice_recve_part_recycler);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectBean item) {
        switch (item.type) {
            case SelectBean.TYPE_USER:
                ImageView imageView = helper.getView(R.id.item_user_img);
                TextView textView = helper.getView(R.id.item_user_text);
                String color = item.color;
                if (color.startsWith("#")) {
                    textView.setText(StringUtil.stringSubName(item.name));
                    imageView.setImageDrawable(new ColorDrawable(Color.parseColor(color)));
                } else {
                    textView.setText("");
                    Glide.with(mContext).load(color).into(imageView);
                }
//                final Integer id = item.id;
//                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        adapter.selectUserMap.remove(id);
//                        adapter.selectUserSet.remove((Integer) id);
//                        adapter.notifyDataSetChanged();
//                        List<SelectBean> selectData = adapter.getSelectData();
//                        replaceData(selectData);
//                    }
//                });

                break;
            case SelectBean.TYPE_PART:
                helper.setText(R.id.item_part_text, item.name);
//                final Integer ids = item.id;
//                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        adapter.selectPartMap.remove(ids);
//                        adapter.selectPartSet.remove((Integer) ids);
//                        adapter.notifyDataSetChanged();
//                        List<SelectBean> selectData = adapter.getSelectData();
//                        replaceData(selectData);
//                    }
//                });
                break;
        }
    }
}
