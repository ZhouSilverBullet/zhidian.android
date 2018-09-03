package com.sdxxtop.zhidian.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.VoteReadBean;

/**
 * Created by Administrator on 2018/5/21.
 */

public class MineInitiateMarkView extends LinearLayout {

    private RatingBar scoreRating;
    private TextView ratingText;
    private int rating;
    private TextView submit;
    private TextView name;
    private VoteReadBean.DataBean.OptionBean optionBean;
    private boolean isVote;
    private boolean isShowBtn;
    private boolean selectorSingle;
    private boolean isSendShow;

    public MineInitiateMarkView(Context context) {
        this(context, null);
    }

    public MineInitiateMarkView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MineInitiateMarkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_mine_initiate_mark, this, true);

        scoreRating = (RatingBar) findViewById(R.id.item_mine_initiate_score_rating);
        ratingText = (TextView) findViewById(R.id.item_mine_initiate_score_text);
        name = (TextView) findViewById(R.id.mine_score_name);
        submit = (TextView) findViewById(R.id.mine_score_submit);

    }

    private void setRatingValue(int rating) {
        String value = "";
        switch (rating) {
            case 1:
                value = "很差";
                break;
            case 2:
                value = "差";
                break;
            case 3:
                value = "一般";
                break;
            case 4:
                value = "好";
                break;
            case 5:
                value = "非常好";
                break;
            default:
                value = "非常差";
                break;
        }
        ratingText.setText(rating + "分    " + value);
    }

    public void setOptionBean(final VoteReadBean.DataBean.OptionBean optionBean) {
        this.optionBean = optionBean;
        int score = 0;
        if (isSendShow) { //发布即可见，直接显示平均值，不然就显示自己的分数
            score = optionBean.getNum();
        } else {
            score = optionBean.getScore();
        }

        scoreRating.setStar(score);
        setRatingValue(score);

        if (isVote) {
            if (selectorSingle) { //有对象的时候需要自己再判断一次
                if (optionBean.getIs_vote() == 1) {
                    scoreRating.setClickable(false);
                }
            } else {
                scoreRating.setClickable(false);
            }
        } else {
            //从我发起的过来的不进行处理
            scoreRating.setClickable(isShowBtn);
            scoreRating.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
                @Override
                public void onRatingChange(float ratingCount) {
                    rating = (int) ratingCount;
                    setRatingValue(rating);
                }
            });
        }

        String option_name = optionBean.getOption_name();
        if (TextUtils.isEmpty(option_name) || selectorSingle) {
            submit.setVisibility(GONE);
            name.setText("最后得分");
        } else {
            final boolean isVote = optionBean.getIs_vote() == 1;
            if (isVote) { //1.已经投 2.未投
                submit.setText("已评");
                submit.setTextColor(getResources().getColor(R.color.texthintcolor));
                submit.setBackgroundResource(R.drawable.item_mine_seek_gray_bg);
            } else {
                submit.setText("评分");
                submit.setTextColor(getResources().getColor(R.color.blue));
                submit.setBackgroundResource(R.drawable.item_mine_seek_blue_bg);
            }

            submit.setVisibility(isShowBtn ? VISIBLE : GONE);
            name.setText(option_name);

            submit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isVote) {
//                        ToastUtil.show("您已经投注");
                        return;
                    }
                    if (rating == 0) {
                        rating = -1; //默认 -1
                    }
                    if (markListener != null) {
                        markListener.mark(rating, optionBean);
                    }
                }
            });

        }
    }

    public void setIsVote(boolean isVote) {
        this.isVote = isVote;
    }

    public void setSelectorSingle(boolean selectorSingle) {
        this.selectorSingle = selectorSingle;
    }


    public int getRatingCount() {
        if (rating == 0) {
            rating = -1;
        }
        return rating;
    }

    public int getOptionId() {
        int option_id = 0;
        if (optionBean != null) {
            option_id = optionBean.getOption_id();
        }
        return option_id;
    }

    private MarkListener markListener;

    public void setMarkListener(MarkListener markListener) {
        this.markListener = markListener;
    }

    //用于显示右边评分按钮
    public void setShowPinfenBtn(boolean isShowBtn) {
        this.isShowBtn = isShowBtn;
    }

    public void setSendShow(boolean isSendShow) {
        this.isSendShow = isSendShow;
    }

    public interface MarkListener {
        void mark(int rating, VoteReadBean.DataBean.OptionBean optionBean);
    }
}
