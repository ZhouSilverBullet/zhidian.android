package com.sdxxtop.zhidian.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.UserCourseBean;
import com.sdxxtop.zhidian.utils.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/7/19.
 */

public class SelectionSubjectAdapter extends BaseQuickAdapter<UserCourseBean.DataBean.CourseBean, BaseViewHolder> {

    public SelectionSubjectAdapter(int layoutResId, @Nullable List<UserCourseBean.DataBean.CourseBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final UserCourseBean.DataBean.CourseBean item) {
        final TextView textSelectorText = helper.getView(R.id.item_stat_text_selector);
        TextView textName = helper.getView(R.id.item_stat_text_name);
        setChecked(textSelectorText, item.isCheck());
        textName.setText(StringUtil.stringNotNull(item.getCourse_name()));
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //单选
                boolean check = item.isCheck();
                for (UserCourseBean.DataBean.CourseBean courseBean : getData()) {
                    courseBean.setCheck(false);
                }

                item.setCheck(!check);
                setChecked(textSelectorText, !check);
                notifyDataSetChanged();
            }
        });

    }

    private void setChecked(TextView textView, boolean isCheck) {
        if (isCheck) {
            textView.setBackgroundResource(R.drawable.selected);
        } else {
            textView.setBackgroundResource(R.drawable.unselected);
        }
    }
}
