package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.TablayoutFragmentPagerAdapter;
import com.sdxxtop.zhidian.entity.StateIndexBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.StatInstructionsActivity;
import com.sdxxtop.zhidian.ui.fragment.StatReportFragment;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class StatReportActivity extends BaseActivity {

    @BindView(R.id.stat_report_title_view)
    SubTitleView titleView;
    @BindView(R.id.stat_report_right_icon)
    TextView rightIcon;
    @BindView(R.id.stat_report_tab)
    TabLayout tabLayout;
    @BindView(R.id.stat_report_pager)
    ViewPager viewPager;
    private List<Fragment> fragmentList;
    private List<String> mTitleList;

    private boolean isChange;

    @Override
    protected int getActivityView() {
        return R.layout.activity_stat_report;
    }

    @Override
    protected void initView() {

        fragmentList = new ArrayList<>();
        fragmentList.add(StatReportFragment.newInstance(StatReportFragment.TYPE_ALL));
        fragmentList.add(StatReportFragment.newInstance(StatReportFragment.TYPE_PART));
        fragmentList.add(StatReportFragment.newInstance(StatReportFragment.TYPE_USER));

        mTitleList = new ArrayList<>();

        mTitleList.add("全部");
        mTitleList.add("统计部门");
        mTitleList.add("个人榜单");
        viewPager.setAdapter(new TablayoutFragmentPagerAdapter(getSupportFragmentManager(), this, mTitleList, fragmentList));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void loadData() {
        Params params = new Params();
        RequestUtils.createRequest().postStatShow(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<StateIndexBean>() {
            @Override
            public void onSuccess(StateIndexBean stateIndexBean) {
                StateIndexBean.DataBean data = stateIndexBean.getData();
                if (data != null) {
                    handleData(data);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    private void handleData(StateIndexBean.DataBean data) {
        String user_stat = data.getUser_stat();
        String[] userStat = null;
        if (!TextUtils.isEmpty(user_stat)) {
            userStat = user_stat.split(",");
        }

        String part_stat = data.getPart_stat();
        String[] partStat = null;
        if (!TextUtils.isEmpty(part_stat)) {
            partStat = part_stat.split(",");
        }
        List<Integer> userList = new ArrayList<>();
        if (userStat != null) {
            for (String s : userStat) {
                if (!TextUtils.isEmpty(s)) {
                    userList.add(Integer.parseInt(s));
                }
            }
        }

        List<Integer> partList = new ArrayList<>();
        if (partStat != null) {
            for (String s : partStat) {
                if (!TextUtils.isEmpty(s)) {
                    partList.add(Integer.parseInt(s));
                }
            }
        }

        ((StatReportFragment) fragmentList.get(0)).setData(userList, partList);
        ((StatReportFragment) fragmentList.get(1)).setData(userList, partList);
        ((StatReportFragment) fragmentList.get(2)).setData(userList, partList);
    }

    public void loadControlData() {
        Params params = new Params();
        RequestUtils.createRequest().postStatIndex(params.getData())
                .enqueue(new RequestCallback<>(new IRequestListener<StateIndexBean>() {
                    @Override
                    public void onSuccess(StateIndexBean stateIndexBean) {
                        StateIndexBean.DataBean data = stateIndexBean.getData();
                        if (data != null) {
                            ((StatReportFragment) fragmentList.get(0)).loadControlData(data);
                            ((StatReportFragment) fragmentList.get(1)).loadControlData(data);
                            ((StatReportFragment) fragmentList.get(2)).loadControlData(data);
                        }
                        isChange = true;
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        showToast(errorMsg);
                    }
                }));
    }

    @Override
    protected void initEvent() {
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转说明
                Intent intent = new Intent(mContext, StatInstructionsActivity.class);
                startActivity(intent);
            }
        });

        titleView.getLeftText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isChange) {
                    handleFinish();
//                } else {
//                    finish();
//                }
            }
        });
    }

    @Override
    public void onBackPressed() {
//        if (isChange) {
        handleFinish();
//        } else {
//            super.onBackPressed();
//        }
    }

    /**
     * 返回时是否刷新页面
     */
    private void handleFinish() {
        setResult(201);
        finish();
    }
}
