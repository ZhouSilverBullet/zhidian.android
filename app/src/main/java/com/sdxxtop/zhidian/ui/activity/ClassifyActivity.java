package com.sdxxtop.zhidian.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ClassifyActivity extends BaseActivity {

    @BindView(R.id.tv_xueqian)
    TextView tvXueqian;
    @BindView(R.id.tv_chuzhong)
    TextView tvChuzhong;
    @BindView(R.id.tv_gaodeng)
    TextView tvGaodeng;
    @BindView(R.id.tv_peixun)
    TextView tvPeixun;
    @BindView(R.id.tv_other)
    TextView tvOther;

    @Override
    protected int getActivityView() {
        return R.layout.activity_classify;
    }

    @Override
    protected void initView() {
    }

    @OnClick({R.id.tv_xueqian, R.id.tv_chuzhong, R.id.tv_gaodeng, R.id.tv_peixun, R.id.tv_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_xueqian:

                break;
            case R.id.tv_chuzhong:

                break;
            case R.id.tv_gaodeng:

                break;
            case R.id.tv_peixun:

                break;
            case R.id.tv_other:

                break;
        }
    }
}
