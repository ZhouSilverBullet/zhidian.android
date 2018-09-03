package com.sdxxtop.zhidian.widget;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.VoteReadBean;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2018/5/21.
 */

public class MineInitiateProgressView extends LinearLayout {

    private RelativeLayout pingFenLayout;
    private TextView pingFenText;
    private TextView pingFenProgress;
    private CheckBox pingCheckBox;
    private ClipDrawable clipDrawable;
    private VoteReadBean.DataBean.OptionBean optionBean;
    private boolean isVote;

    DecimalFormat df = new DecimalFormat("#.00");

    public MineInitiateProgressView(Context context) {
        this(context, null);
    }

    public MineInitiateProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MineInitiateProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_mine_initiate_progress, this, true);
        pingFenLayout = (RelativeLayout) findViewById(R.id.activity_mine_initiate_ping_fen_layout);
        pingFenText = (TextView) findViewById(R.id.activity_mine_initiate_ping_fen);
        pingFenProgress = (TextView) findViewById(R.id.activity_mine_initiate_ping_fen_progress_value);
        pingCheckBox = (CheckBox) findViewById(R.id.activity_mine_initiate_ping_check);

        pingCheckBox.setClickable(false);

        clipDrawable = ((ClipDrawable) pingFenLayout.getBackground());
    }

    public void setOptionBean(int sum, VoteReadBean.DataBean.OptionBean optionBean) {
        this.optionBean = optionBean;
        if (sum == 0) {
            sum = 1;
        }
        String option_name = optionBean.getOption_name();
        pingFenText.setText(option_name);
        int num = optionBean.getNum();
        double present = 100.0 * num / sum;
        pingFenProgress.setText(num + "(" + optionBean.getAverage() + "%)");
        clipDrawable.setLevel((int) (100 * present));
        if (optionBean.isCheck()) {
            pingCheckBox.setChecked(true);
        } else {
            pingCheckBox.setChecked(false);
        }
    }

    public void setIsVote(boolean isVote) {
        this.isVote = isVote;
    }

    public VoteReadBean.DataBean.OptionBean getOptionBean() {
        return optionBean;
    }

    public CheckBox getPingCheckBox() {
        return pingCheckBox;
    }
}
