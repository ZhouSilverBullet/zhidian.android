package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ViewPagerAdapter;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.NoReaderNoticeFragment;
import com.sdxxtop.zhidian.ui.fragment.ReaderNoticeFragment;

import java.util.ArrayList;

import butterknife.BindView;

public class NoticeReciveDetailActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.tv_retuen)
    TextView tvRetuen;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rbUnReader)
    RadioButton rbUnReader;
    @BindView(R.id.rbReader)
    RadioButton rbReader;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;
    @BindView(R.id.vp_group)
    ViewPager vpGroup;
    @BindView(R.id.ll_notread)
    LinearLayout llNotread;
    @BindView(R.id.ll_read)
    LinearLayout llRead;

    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private String noticeid;
    private String not_num;
    private String read_num;
    private NoReaderNoticeFragment noReaderNoticeFragment;
    private ReaderNoticeFragment readerNoticeFragment;

    @Override
    protected int getActivityView() {
        return R.layout.activity_notice_recive_detail;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        noticeid = intent.getStringExtra("noticeid");
        not_num = intent.getStringExtra("not_num");
        read_num = intent.getStringExtra("read_num");
        rbUnReader.setText("未读人（" + not_num + ")");
        rbReader.setText("已读人（" +read_num + ")");
        tvTitle.setText("公告接收详情");
        noReaderNoticeFragment = new NoReaderNoticeFragment(noticeid);
        readerNoticeFragment = new ReaderNoticeFragment(noticeid);
        fragmentList.add(noReaderNoticeFragment);
        fragmentList.add(readerNoticeFragment);
        vpGroup.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentList));
    }


    @Override
    protected void initEvent() {
        tvRetuen.setOnClickListener(this);
        rbUnReader.setOnClickListener(this);
        rbReader.setOnClickListener(this);
        vpGroup.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_retuen:
                finish();
                break;
            case R.id.rbUnReader:
                vpGroup.setCurrentItem(0);
                break;
            case R.id.rbReader:
                vpGroup.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        rbReader.setChecked(position == 0 ? false : true);
        rbUnReader.setChecked(position == 0 ? true : false);
        llRead.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        llNotread.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
