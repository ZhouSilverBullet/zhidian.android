package com.sdxxtop.zhidian.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.TablayoutFragmentPagerAdapter;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.CollectiveBusinessFragment;
import com.sdxxtop.zhidian.ui.fragment.CollectiveLeaveFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：${刘香龙} on 2018/5/4 0004 11:56
 * 类的描述：
 */
public class CollectiveLeaveActivity extends BaseActivity {

    @BindView(R.id.tab_applied)
    TabLayout tabApplied;
    @BindView(R.id.fragment_applied_viewpager)
    ViewPager viewPagerApplied;

    private List<String> strList;
    private List<Fragment> fragmentList;

    @Override
    protected int getActivityView() {
        return R.layout.activity_collectiveleave;
    }

    @Override
    protected void initData() {
        super.initData();
        strList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        //添加tab项名称
        strList.add("集体请假");
        strList.add("集体出差");

        fragmentList.add(CollectiveLeaveFragment.newInstance(6));
        fragmentList.add(CollectiveBusinessFragment.newInstance(7));

        tabApplied.setTabMode(TabLayout.MODE_FIXED);
        tabApplied.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPagerApplied.setAdapter(new TablayoutFragmentPagerAdapter(this.getSupportFragmentManager(), this, strList, fragmentList));
        tabApplied.setupWithViewPager(viewPagerApplied);
    }
}
