package com.sdxxtop.zhidian.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.TablayoutFragmentPagerAdapter;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.AttendanceAverageFragment;
import com.sdxxtop.zhidian.ui.fragment.AttendanceDetailFragment;
import com.sdxxtop.zhidian.ui.fragment.AttendanceStatisticalFragment;
import com.sdxxtop.zhidian.ui.fragment.AttendanceWatchFragment;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MineAttendanceActivity extends BaseActivity {

    @BindView(R.id.attendance_title_view)
    SubTitleView titleView;
    @BindView(R.id.attendance_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.attendance_applied_viewpager)
    ViewPager viewPager;
    private List<Fragment> fragments;
    private List<String> stringList;

    @Override
    protected int getActivityView() {
        return R.layout.activity_mine_attendance;
    }

    @Override
    protected void initView() {
        fragments = new ArrayList<>();

        fragments.add(new AttendanceWatchFragment());
        fragments.add(new AttendanceStatisticalFragment());
        fragments.add(new AttendanceAverageFragment());
        fragments.add(new AttendanceDetailFragment());

        stringList = new ArrayList<>();
        stringList.add("考勤视图");
        stringList.add("考勤统计");
        stringList.add("平均工时");
        stringList.add("考勤明细");

        viewPager.setAdapter(new TablayoutFragmentPagerAdapter(getSupportFragmentManager(), mContext, stringList, fragments));
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);
    }
}
