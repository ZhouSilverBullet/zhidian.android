package com.sdxxtop.zhidian.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.TablayoutFragmentPagerAdapter;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.ApplyListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：${刘香龙} on 2018/4/28 0028 10:50
 * 类的描述：我的申请
 */
public class MyAppliedActivity extends BaseActivity {

    @BindView(R.id.tab_applied)
    TabLayout tabApplied;
    @BindView(R.id.fragment_applied_viewpager)
    ViewPager viewPagerApplied;

    private List<String> strList;
    private List<Fragment> fragmentList;
    private View subTitleView;

    @Override
    protected int getActivityView() {
        return R.layout.activity_my_applied;
    }

    @Override
    protected void initView() {
        subTitleView = findViewById(R.id.my_applied_sub_title);
    }

    @Override
    protected void initData() {
        super.initData();
        strList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        //添加tab项名称
        strList.add("待审核");
        strList.add("审核通过");
        strList.add("审核驳回");

        fragmentList.add(ApplyListFragment.newInstance(0, "my", 1));
        fragmentList.add(ApplyListFragment.newInstance(1, "my", 1));
        fragmentList.add(ApplyListFragment.newInstance(2, "my", 1));

        tabApplied.setTabMode(TabLayout.MODE_FIXED);
        tabApplied.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPagerApplied.setAdapter(new TablayoutFragmentPagerAdapter(getSupportFragmentManager(), this, strList, fragmentList));
        tabApplied.setupWithViewPager(viewPagerApplied);
        viewPagerApplied.setOffscreenPageLimit(3);
    }
}
