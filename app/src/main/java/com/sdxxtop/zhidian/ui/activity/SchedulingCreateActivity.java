package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.eventbus.SchedulingFinishEvent;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.widget.SubTitleView;
import com.sdxxtop.zhidian.widget.TextAndEditView;
import com.sdxxtop.zhidian.widget.TextAndTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashSet;
import java.util.List;

import butterknife.BindView;

/**
 * 新建排班
 */
public class SchedulingCreateActivity extends BaseActivity {

    @BindView(R.id.scheduling_title_view)
    SubTitleView titleView;
    @BindView(R.id.scheduling_edit_text)
    TextAndEditView editEditText;
    @BindView(R.id.scheduling_text_text)
    TextAndTextView textText;
    @BindView(R.id.scheduling_btn)
    Button schedulingBtn;
    private HashSet<Integer> partListSet;
    private HashSet<Integer> userListSet;

    @Override
    protected int getActivityView() {
        return R.layout.activity_scheduling_create;
    }

    @Override
    protected void initView() {

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        textText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NoticeReciveRangeActivity.class);
                intent.putExtra("partSelectNotIn", NoticeReciveRangeActivity.PART_SELECTOR_NOT_CLICK);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void initEvent() {
        schedulingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String editValue = editEditText.getEditText().getText().toString();
                if (TextUtils.isEmpty(editValue)) {
                    showToast("请填写规则名称");
                    return;
                }

                String userValue = getUserValue();
                String partValue = getPartValue();

                if (TextUtils.isEmpty(userValue) && TextUtils.isEmpty(partValue)) {
                    showToast("请选择适用范围");
                    return;
                }

                Intent intent = new Intent(mContext, SchedulingListActivity.class);
                intent.putExtra("part_value", partValue);
                intent.putExtra("user_value", userValue);
                intent.putExtra("editValue", editValue);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == NoticeReciveRangeActivity.NOTICE_RESULT_OK && data != null) {
            List<SelectBean> selectData = (List<SelectBean>) data.getSerializableExtra("selectData");
            partListSet = (HashSet<Integer>) data.getSerializableExtra("partList");
            userListSet = (HashSet<Integer>) data.getSerializableExtra("userList");
            String value = "";
            if (selectData != null) {
                for (int i = 0; i < selectData.size(); i++) {
                    SelectBean selectBean = selectData.get(i);
                    if (i == selectData.size() - 1) {
                        value = value + selectBean.getName();
                    } else {
                        value = value + selectBean.getName() + ",";
                    }
                }
            }

            textText.getTextRightText().setText(value);
        }
    }


    private String getUserValue() {
        String value = "";
        if (userListSet == null) {
            return value;
        }
        for (Integer integer : userListSet) {
            value = value + integer + ",";
        }
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    private String getPartValue() {
        String value = "";
        if (partListSet == null) {
            return value;
        }
        for (Integer integer : partListSet) {
            value = value + integer + ",";
        }
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    @Subscribe
    public void schedulingFinish(SchedulingFinishEvent event) {
        finish();
    }
}
