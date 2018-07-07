package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.TablayoutFragmentPagerAdapter;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.NoticeFragment;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class Notice2Activity extends BaseActivity {
    @BindView(R.id.notice2_title_view)
    SubTitleView titleView;
    @BindView(R.id.notice2_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.notice2_view_pager)
    ViewPager viewPager;

    private List<String> titleString;
    private List<Fragment> fragmentList;

    @Override
    protected int getActivityView() {
        return R.layout.activity_notice2;
    }

    @Override
    protected void initView() {
        if (titleString == null) {
            titleString = new ArrayList<>();
        }
        titleString.add("未读");
        titleString.add("已读");

        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        }
        fragmentList.add(NoticeFragment.newInstance(2));
        fragmentList.add(NoticeFragment.newInstance(1));

        viewPager.setAdapter(new TablayoutFragmentPagerAdapter(getSupportFragmentManager(), mContext, titleString, fragmentList));
        tabLayout.setupWithViewPager(viewPager);
    }

    //用户是否有添加公告的权限
    public void userHasAddGrand(boolean isAdd) {
        if (isAdd) {
            titleView.getRightText().setText("发布公告");
            titleView.getRightText().setVisibility(View.VISIBLE);
        } else {
            titleView.getRightText().setVisibility(View.GONE);
        }
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        titleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, IssueNotice2Activity.class);
                startActivity(intent);
            }
        });
    }
}
