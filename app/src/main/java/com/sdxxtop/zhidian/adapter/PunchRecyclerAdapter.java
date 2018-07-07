package com.sdxxtop.zhidian.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.MainIndexBean;
import com.sdxxtop.zhidian.ui.activity.PunchCardActivity;
import com.sdxxtop.zhidian.utils.DateUtil;

/**
 * Created by Administrator on 2018/5/9.
 */

public class PunchRecyclerAdapter extends BaseQuickAdapter<MainIndexBean.DataBean.SignLogBean, BaseViewHolder> {

    private final int at;
    private String stringDate;
    private int typePunch;

    public PunchRecyclerAdapter(int layoutResId, int at) {
        super(layoutResId);
        this.at = at;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }

    public String getStringDate() {
        return stringDate;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MainIndexBean.DataBean.SignLogBean item) {
        View removeText = helper.getView(R.id.item_punch_remove_text);
        TextView cardText = helper.getView(R.id.item_punch_remove_card);
        TextView desText = helper.getView(R.id.item_punch_remove_description);

        if (at == 2) {
            desText.setText("漏打卡");
        } else if (at == 3) {
            int status = item.getStatus();
            if (status == 5) {
                desText.setText("(迟到：" + item.getMinute() + "分钟)");
            } else if (status == 6) {
                desText.setText("(早退：" + item.getMinute() + "分钟)");
            }
        }


        String sign_time = item.getSign_time();
        if (typePunch == PunchCardActivity.type_punch_index) {
            String hourTime = DateUtil.getHourTime(sign_time);
            cardText.setText(item.getSign_name() + "打卡： " + hourTime + " ");
        } else {
            cardText.setText(item.getSign_name() +"打卡： " + sign_time +" ");
        }

        removeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData().remove(item);
                notifyDataSetChanged();
            }
        });
    }

    public void setType(int typePunch) {
        this.typePunch = typePunch;
    }
}
