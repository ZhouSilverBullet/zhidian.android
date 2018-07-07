package com.sdxxtop.zhidian.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.PersonnelListAdapter;
import com.sdxxtop.zhidian.entity.TestBean;
import com.sdxxtop.zhidian.eventbus.MessageEvent;
import com.sdxxtop.zhidian.ui.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：${刘香龙} on 2018/4/28 0028 17:00
 * 类的描述：
 */
public class ExaminePendingFramgent extends BaseFragment {

    @BindView(R.id.lv_recycler_view)
    RecyclerView lvApplied;
    @BindView(R.id.tv_no_apply)
    TextView tvNoApply;
    @BindView(R.id.btn_no_apply)
    Button btnNoApply;

    private PersonnelListAdapter appliedAdapter;
    List<TestBean> beans;

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

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        beans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestBean bean = new TestBean("长军", "徐小白的漏打卡申请", "时间：3月13", "2018-4-27", "未审核", "");
            beans.add(bean);
        }

//        appliedAdapter = new PersonnelListAdapter(mContext, beans, true);
//        lvApplied.setAdapter(appliedAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showPlanSettingtContentViewEvent(MessageEvent.approvalSuccess event) {
        beans.clear();
        lvApplied.setVisibility(View.GONE);
        tvNoApply.setVisibility(View.VISIBLE);
        btnNoApply.setVisibility(View.VISIBLE);
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
