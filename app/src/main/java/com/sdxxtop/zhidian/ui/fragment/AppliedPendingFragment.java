package com.sdxxtop.zhidian.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.PersonnelListAdapter;
import com.sdxxtop.zhidian.entity.TestBean;
import com.sdxxtop.zhidian.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：${刘香龙} on 2018/4/28 0028 11:34
 * 类的描述：
 */

public class AppliedPendingFragment extends BaseFragment {

    @BindView(R.id.lv_recycler_view)
    RecyclerView lvApplied;


    @Override
    protected int getFragmentView() {
        return R.layout.fragment_list_view;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

        List<TestBean> beans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestBean bean = new TestBean("长军", "迟到： 2分钟", "审核中", "2018-4-27", "", "");
            beans.add(bean);
        }

//        appliedAdapter = new PersonnelListAdapter(mContext,beans,false);
//        lvApplied.setAdapter(appliedAdapter);


    }
}
