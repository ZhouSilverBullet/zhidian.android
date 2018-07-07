package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.TablayoutFragmentPagerAdapter;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.ApplyListFragment;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：${刘香龙} on 2018/4/28 0028 15:14
 * 类的描述：
 */
public class ExamineActivity extends BaseActivity {

    public static final String EXAMINE_TYPE = "type";

    public static final int pending = 0;
    public static final int processed = 1;
    public static final int copy = 2;

    @BindView(R.id.rly_onekey)
    RelativeLayout rlyOnkey;
    @BindView(R.id.examine_title_view)
    SubTitleView titleView;

    @BindView(R.id.tab_examine)
    TabLayout tabApplied;
    @BindView(R.id.fragment_examine_viewpager)
    ViewPager viewPagerApplied;
    @BindView(R.id.examine_search_edit)
    EditText searchEdit;
    @BindView(R.id.examine_search_cancel)
    TextView searchCancelText;

    private List<String> strList;
    private List<Fragment> fragmentList;

    private String editChangeTextValue;

    @Override
    protected int getActivityView() {
        return R.layout.activity_examine;
    }

    @Override
    protected void initView() {
        titleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showWindow(null);
                handleBeforeFragment();
            }
        });
    }

    private void handleBeforeFragment() {
        int currentItem = viewPagerApplied.getCurrentItem();
        ApplyListFragment fragment = (ApplyListFragment) fragmentList.get(currentItem);
        fragment.showWindow();
    }

    @Override
    protected void initData() {
        super.initData();
        strList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        //添加tab项名称
        strList.add("待处理");
        strList.add("已处理");
        strList.add("抄送");

        fragmentList.add(ApplyListFragment.newInstance(pending, "approval", 1));
        fragmentList.add(ApplyListFragment.newInstance(processed, "approval", 1));
        fragmentList.add(ApplyListFragment.newInstance(copy, "approval", 1));

        tabApplied.setTabMode(TabLayout.MODE_FIXED);
        tabApplied.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPagerApplied.setAdapter(new TablayoutFragmentPagerAdapter(getSupportFragmentManager(), this, strList, fragmentList));
        tabApplied.setupWithViewPager(viewPagerApplied);
        viewPagerApplied.setOffscreenPageLimit(3);

        viewPagerApplied.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    rlyOnkey.setVisibility(View.VISIBLE);
                } else {
                    rlyOnkey.setVisibility(View.GONE);
                }
                refreshFragment(editChangeTextValue);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        rlyOnkey.setOnClickListener(this);

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //加入0.4s的间隔
                editChangeTextValue = "";
                if (!TextUtils.isEmpty(s)) {
                    editChangeTextValue = s.toString();
                    searchCancelText.setVisibility(View.VISIBLE);
                } else {
                    searchCancelText.setVisibility(View.GONE);
                }
                refreshFragment(editChangeTextValue);
            }
        });

        searchCancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.setText("");
//                editChangeTextValue = "";
//                refreshFragment(editChangeTextValue);
            }
        });
    }

    private void refreshFragment(String editChangeTextValue) {
        int currentItem = viewPagerApplied.getCurrentItem();
        ApplyListFragment fragment = (ApplyListFragment) fragmentList.get(currentItem);
        fragment.searchName(editChangeTextValue);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.rly_onekey:
                Intent intent = new Intent(this, OneKeyExamination2Activity.class);
                intent.putExtra(EXAMINE_TYPE, 0);
                startActivity(intent);
                break;
        }
    }

}
