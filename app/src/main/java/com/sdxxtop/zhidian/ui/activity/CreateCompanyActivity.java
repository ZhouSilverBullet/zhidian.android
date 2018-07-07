package com.sdxxtop.zhidian.ui.activity;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.CreateCompanyData;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.CreateCompFirstFragment;
import com.sdxxtop.zhidian.ui.fragment.CreateCompSecondFragment;
import com.sdxxtop.zhidian.ui.fragment.CreateCompThirdFragment;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.sdxxtop.zhidian.widget.SubTitleView;

import butterknife.BindView;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：创建公司界面
 */
public class CreateCompanyActivity extends BaseActivity {

    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    @BindView(R.id.create_company_title_view)
    SubTitleView titleView;

    public CreateCompFirstFragment createCompFirstFragment;
    public CreateCompSecondFragment createCompSecondFragment;
    public CreateCompThirdFragment createCompThirdFragment;

    public CreateCompanyData companyData = new CreateCompanyData();

    @Override
    protected int getActivityView() {
        return R.layout.activity_create_company;
    }

    @Override
    protected void initView() {
        createCompFirstFragment = new CreateCompFirstFragment();
        createCompSecondFragment = new CreateCompSecondFragment();
        createCompThirdFragment = new CreateCompThirdFragment();
        if (companyData.count == 1) {
            replaceFragment("first", createCompFirstFragment);
        }

        titleView.getLeftText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClicked();
            }
        });
    }

    @Override
    public void onBackPressed() {
        onViewClicked();
    }

    public void onViewClicked() {
        companyData.count--;
        LogUtils.e("log", companyData.count + "");
        switch (companyData.count) {
            case 0:
                finish();
                break;
            case 1:
                replaceFragment("first", createCompFirstFragment);
                break;
            case 2:
                replaceFragment("second", createCompSecondFragment);
                break;
            case 3:
                replaceFragment("third", createCompThirdFragment);
                break;
        }
    }

    private void replaceFragment(String name, Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.framelayout, fragment)
                .commit();
    }
}
