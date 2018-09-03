package com.sdxxtop.zhidian.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.VoteReadBean;
import com.warkiz.widget.IndicatorSeekBar;

/**
 * Created by Administrator on 2018/5/21.
 */

public class MineInitiateSeekBarView extends LinearLayout {

    private TextView name;
    private IndicatorSeekBar seekBar;
    private boolean type;  //是否是我发起
    private TextView submit;
    private boolean isVote;
    private boolean selectorSingle;
    private VoteReadBean.DataBean.OptionBean optionBean;
    private boolean isSendShow;

    public MineInitiateSeekBarView(Context context) {
        this(context, null);
    }

    public MineInitiateSeekBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MineInitiateSeekBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_mine_initiate_seek_bar, this, true);

        name = (TextView) findViewById(R.id.mine_seekbar_name);
        seekBar = (IndicatorSeekBar) findViewById(R.id.mine_indicator_seek);
        submit = (TextView) findViewById(R.id.mine_seekbar_submit);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (type || isVote) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setData(int maxScore, final VoteReadBean.DataBean.OptionBean optionBean) {
        this.optionBean = optionBean;
        String option_name = optionBean.getOption_name();
        if (!TextUtils.isEmpty(option_name)) {
            name.setText(option_name);
        } else {
            name.setVisibility(GONE);
        }

        int score = 0;
        if (isSendShow) { //发布即可见，直接显示平均值，不然就显示自己的分数
            score = optionBean.getNum();
        } else {
            score = optionBean.getScore();
        }
        if (score > maxScore) {
            score = maxScore;
        }
        seekBar.setMax(maxScore);
        seekBar.setProgress(score);

        if (type) {
            seekBar.setClickable(false);
            submit.setVisibility(GONE);
        } else { //是去评议 需要显示这个
            seekBar.setClickable(true);
            submit.setVisibility(VISIBLE);
        }

        if (selectorSingle) { //长度为1的情况下需要隐藏这个提交按钮
            submit.setVisibility(GONE);
        }

        final boolean isVote = optionBean.getIs_vote() == 1;
        this.isVote = isVote;
        if (isVote) { //1.已经投 2.未投
            submit.setText("已评");
            submit.setTextColor(getResources().getColor(R.color.texthintcolor));
            submit.setBackgroundResource(R.drawable.item_mine_seek_gray_bg);
        } else {
            submit.setText("评分");
            submit.setTextColor(getResources().getColor(R.color.blue));
            submit.setBackgroundResource(R.drawable.item_mine_seek_blue_bg);
        }

        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVote) {
//                    ToastUtil.show("您已经投注");
                    return;
                }
                int progress = seekBar.getProgress();
                if (progress == 0) {
                    progress = -1; //默认 -1
                }
                if (submitListener != null) {
                    submitListener.onSubmit(progress, optionBean);
                }
            }
        });
    }

    public void setSendShow(boolean isSendShow) {
        this.isSendShow = isSendShow;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    private SubmitListener submitListener;

    public void setSubmitListener(SubmitListener submitListener) {
        this.submitListener = submitListener;
    }

    public void setIsVote(boolean isVote) {
        this.isVote = isVote;
    }

    public void setSelectorSingle(boolean selectorSingle) {
        this.selectorSingle = selectorSingle;
    }

    public interface SubmitListener {
        void onSubmit(int progress, VoteReadBean.DataBean.OptionBean optionBean);
    }

    public int getProgress() {
        return seekBar.getProgress();
    }

    public int getOptionId() {
        int option_id = 0;
        if (optionBean != null) {
            option_id = optionBean.getOption_id();
        }
        return option_id;
    }
}
