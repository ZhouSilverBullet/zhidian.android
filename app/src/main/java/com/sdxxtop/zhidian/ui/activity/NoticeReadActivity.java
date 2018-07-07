package com.sdxxtop.zhidian.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.TablayoutFragmentPagerAdapter;
import com.sdxxtop.zhidian.entity.NoticeShow2PeopleBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.NoticeReadFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NoticeReadActivity extends BaseActivity {

    @BindView(R.id.notice_read_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.notice_read_view_pager)
    ViewPager viewPager;
    private int notice_id;

    private List<String> titleString;
    private List<Fragment> fragmentList;

    @Override
    protected int getActivityView() {
        return R.layout.activity_notice_read;
    }

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            notice_id = getIntent().getIntExtra("ni", -1);
        }
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        Params params = new Params();
        params.put("ni", notice_id);
        RequestUtils.createRequest().postNoticeShowPeople(params.getData()).enqueue(new RequestCallback<NoticeShow2PeopleBean>(new IRequestListener<NoticeShow2PeopleBean>() {
            @Override
            public void onSuccess(NoticeShow2PeopleBean noticeShow2PeopleBean) {
                NoticeShow2PeopleBean.DataBean data = noticeShow2PeopleBean.getData();
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

    private void handleData(NoticeShow2PeopleBean.DataBean data) {
        List<NoticeShow2PeopleBean.DataBean.NotReadBean> not_read = data.getNot_read();
        List<NoticeShow2PeopleBean.DataBean.ReadBean> read = data.getRead();
        if (read == null || not_read == null) {
            return;
        }

        if (titleString == null) {
            titleString = new ArrayList<>();
        }

        titleString.add("未读人(" + not_read.size() + ")");
        titleString.add("已读人(" + read.size() + ")");

        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        }

        ArrayList<NoticeShow2PeopleBean.DataBean.ReadBean> notSame = new ArrayList<>();
        for (NoticeShow2PeopleBean.DataBean.NotReadBean notReadBean : not_read) {
            NoticeShow2PeopleBean.DataBean.ReadBean readBean = new NoticeShow2PeopleBean.DataBean.ReadBean();
            readBean.setImg(notReadBean.getImg());
            readBean.setName(notReadBean.getName());
            notSame.add(readBean);
        }

        fragmentList.add(NoticeReadFragment.newInstance(1, notSame));
        fragmentList.add(NoticeReadFragment.newInstance(2, read));

        viewPager.setAdapter(new TablayoutFragmentPagerAdapter(getSupportFragmentManager(),mContext, titleString, fragmentList));
        tabLayout.setupWithViewPager(viewPager);
    }
}
