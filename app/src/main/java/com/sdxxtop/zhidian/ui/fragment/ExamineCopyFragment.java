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
 * 作者：${刘香龙} on 2018/4/28 0028 17:09
 * 类的描述：
 */
public class ExamineCopyFragment extends BaseFragment {

    @BindView(R.id.lv_recycler_view)
    RecyclerView lvApplied;

    private PersonnelListAdapter appliedAdapter;

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_list_view;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();

        List<TestBean> beans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestBean bean = new TestBean("长军","徐小白的漏打卡申请","时间：3月13","2018-4-27","妥回","");
            beans.add(bean);
        }

        appliedAdapter = new PersonnelListAdapter(mContext,beans, true);
//        lvApplied.setAdapter(appliedAdapter);
    }
}
