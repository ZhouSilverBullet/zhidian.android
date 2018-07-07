package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 *
 * @Class Name: TablayoutFragmentPagerAdapter
 * @Author : neo
 * @Create : 2017/10/16 0016
 * @Describe: TabLayout Viewpager 适配器
 */
public class TablayoutFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<String> list;//tab条目名字集合
    private List<Fragment> fragmentList;//tab对应的fragment集合
    public TablayoutFragmentPagerAdapter(FragmentManager fm, Context mContext, List<String> mList, List<Fragment> mFragmentList) {
        super(fm);
        this.context = mContext;
        this.list = mList;
        this.fragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }
}
