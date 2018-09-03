package com.sdxxtop.zhidian.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.StudentClassBean;

/**
 * Created by Administrator on 2018/7/24.
 */

public class PopStudentRecyclerAdapter extends BaseQuickAdapter<StudentClassBean.DataBean.ClassBean, BaseViewHolder> {

    public PopStudentRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, StudentClassBean.DataBean.ClassBean item) {
        TextView classNameText = helper.getView(R.id.item_pop_class_text_name);

        classNameText.setText(item.getClass_name() + "出勤");

        //点击回调
        helper.addOnClickListener(R.id.item_pop_class_delete);
    }


}
