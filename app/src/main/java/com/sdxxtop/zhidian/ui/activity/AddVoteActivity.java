package com.sdxxtop.zhidian.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ViewPagerAdapter;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.VoteImgFragment;
import com.sdxxtop.zhidian.ui.fragment.VoteTextFragment;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;

public class AddVoteActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.tv_retuen)
    TextView tvRetuen;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_vote_title)
    EditText etVoteTitle;
    @BindView(R.id.tv_defult_show)
    TextView tvDefultShow;
    @BindView(R.id.iv_select_range)
    ImageView ivSelectRange;
    @BindView(R.id.et_add_explain)
    EditText etAddExplain;
    @BindView(R.id.rbText)
    RadioButton rbText;
    @BindView(R.id.rbImg)
    RadioButton rbImg;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;
    @BindView(R.id.ll_text)
    LinearLayout llText;
    @BindView(R.id.ll_img)
    LinearLayout llImg;
    @BindView(R.id.vp_group)
    ViewPager vpGroup;

    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private VoteTextFragment voteTextFragment;
    private VoteImgFragment voteImgFragment;


    @Override
    protected int getActivityView() {
        return R.layout.activity_add_vote;
    }

    @Override
    protected void initView() {
        tvTitle.setText("投票");
        voteTextFragment = new VoteTextFragment();
        voteImgFragment = new VoteImgFragment();
        fragmentList.add(voteTextFragment);
        fragmentList.add(voteImgFragment);
        vpGroup.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentList));
    }

    @Override
    protected void initEvent() {
        tvRetuen.setOnClickListener(this);
        rbText.setOnClickListener(this);
        rbImg.setOnClickListener(this);
        vpGroup.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_retuen:
                finish();
                break;
            case R.id.iv_select_range:
                ToastUtil.show("进去选择范围界面");
                break;
            case R.id.rbText:
                vpGroup.setCurrentItem(0);
                break;
            case R.id.rbImg:
                vpGroup.setCurrentItem(1);
                break;
            default:
                break;
        }

    }

    @Override
    public void onPageSelected(int position) {
        rbImg.setChecked(position == 0 ? false : true);
        rbText.setChecked(position == 0 ? true : false);
        llImg.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        llText.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
