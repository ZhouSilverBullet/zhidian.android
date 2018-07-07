package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ViewPagerAdapter;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.MineVoteFragment;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：CaiCM
 * 日期：2018/4/9  时间：17:39
 * 邮箱：15010104100@163.com
 * 描述：公告界面
 */
public class VoteActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.vote_title_view)
    SubTitleView titleView;
    @BindView(R.id.rb_start)
    RadioButton rbStart;
    @BindView(R.id.rb_review)
    RadioButton rbReview;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;
    @BindView(R.id.ll_notread)
    LinearLayout llNotread;
    @BindView(R.id.ll_read)
    LinearLayout llRead;
    @BindView(R.id.vp_group)
    ViewPager vpGroup;

    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private PopupWindow popupWindow;
    private View contentView;

    @Override
    protected int getActivityView() {
        return R.layout.activity_vote;
    }

    @Override
    protected void initView() {
        titleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });
        fragmentList.add(MineVoteFragment.newInstance(1));
        fragmentList.add(MineVoteFragment.newInstance(2));
        vpGroup.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentList));
    }

    @Override
    protected void initEvent() {
        rbStart.setOnClickListener(this);
        rbReview.setOnClickListener(this);
        vpGroup.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_start:
                vpGroup.setCurrentItem(0);
                break;
            case R.id.rb_review:
                vpGroup.setCurrentItem(1);
                break;
        }
    }

    /**
     * 展示投票或者打分弹框
     */
    private void showPopWindow() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.popuplayout, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        int[] location = new int[2];
        titleView.getRightText().getLocationOnScreen(location);
        int height = titleView.getRightText().getHeight();
        popupWindow.showAtLocation(titleView.getRightText(), Gravity.NO_GRAVITY, location[0] - ViewUtil.dp2px(this, 70), location[1] + height);
        popupWindow.setOutsideTouchable(true);
        darkenBackground(0.6f);
        //设置点击事件
        contentView.findViewById(R.id.mine_vote_toupiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                darkenBackground(1f);
                Intent intentVote = new Intent(mContext, AddVote2Activity.class);
                startActivity(intentVote);
            }
        });
        contentView.findViewById(R.id.mine_vote_dafen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                darkenBackground(1f);
                Intent intentMark = new Intent(mContext, AddMarkActivity.class);
                startActivity(intentMark);
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });
    }

    /**
     * 改变背景颜色
     */
    private void darkenBackground(Float bgcolor) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }


    @Override
    public void onPageSelected(int position) {
        rbReview.setChecked(position == 0 ? false : true);
        rbStart.setChecked(position == 0 ? true : false);
        llRead.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        llNotread.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
