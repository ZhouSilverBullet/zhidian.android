package com.sdxxtop.zhidian.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ShowOutPowerDrawerLayoutAdapter;
import com.sdxxtop.zhidian.adapter.ViewPagerAdapter;
import com.sdxxtop.zhidian.entity.UcenterOutIndexBean;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.OutPowerFragment;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：CaiCM
 * 日期：2018/4/18  时间：11:39
 * 邮箱：15010104100@163.com
 * 描述：外勤权限界面
 */
public class OutPowerActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, AdapterView.OnItemClickListener {

    @BindView(R.id.out_power_title_view)
    SubTitleView titleView;
    @BindView(R.id.rb_all)
    RadioButton rbAll;
    @BindView(R.id.rb_open)
    RadioButton rbOpen;
    @BindView(R.id.rb_close)
    RadioButton rbClose;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;
    @BindView(R.id.ll_all)
    LinearLayout llAll;
    @BindView(R.id.ll_open)
    LinearLayout llOpen;
    @BindView(R.id.ll_close)
    LinearLayout llClose;
    @BindView(R.id.vp_group)
    ViewPager vpGroup;
    @BindView(R.id.right_listview)
    ListView rightListview;
    @BindView(R.id.right)
    RelativeLayout right;
    public DrawerLayout drawerLayout;

    public ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ShowOutPowerDrawerLayoutAdapter adapter;

    @Override
    protected int getActivityView() {
        return R.layout.activity_out_power;
    }

    @Override
    protected void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        fragmentList.add(OutPowerFragment.newInstance(0));
        fragmentList.add(OutPowerFragment.newInstance(1));
        fragmentList.add(OutPowerFragment.newInstance(2));
        vpGroup.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentList));
        vpGroup.setOffscreenPageLimit(3);//预加载多少页
        adapter = new ShowOutPowerDrawerLayoutAdapter(mContext);
        rightListview.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        getOutIndex();
    }

    @Override
    protected void initEvent() {
        rbAll.setOnClickListener(this);
        rbOpen.setOnClickListener(this);
        rbClose.setOnClickListener(this);
        vpGroup.addOnPageChangeListener(this);
        rightListview.setOnItemClickListener(this);

        titleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_all:
                vpGroup.setCurrentItem(0);
                break;
            case R.id.rb_open:
                vpGroup.setCurrentItem(1);
                break;
            case R.id.rb_close:
                vpGroup.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    /**
     * 外勤权限主页，网络请求
     */
    private void getOutIndex() {
//        Params params = new Params();
//        params.put("pi", "");
//        showProgressDialog("");
//        RequestUtils.createRequest().postUcenterOutIndex(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<UcenterOutIndexBean>() {
//            @Override
//            public void onSuccess(UcenterOutIndexBean ucenterOutIndexBean) {
//                closeProgressDialog();
//                UcenterOutIndexBean.DataEntity data = ucenterOutIndexBean.getData();
//                if (data != null) {
//                    adapter.replaceData(data.getPart());
//                }
//            }
//
//            @Override
//            public void onFailure(int code, String errorMsg) {
//                closeProgressDialog();
//                showToast(errorMsg);
//            }
//        }));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        rbAll.setChecked(position == 0 ? true : false);
        rbOpen.setChecked(position == 1 ? true : false);
        rbClose.setChecked(position == 2 ? true : false);
        llAll.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        llOpen.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
        llClose.setVisibility(position == 2 ? View.VISIBLE : View.GONE);

        refreshData(position);
    }

    public void refreshData(int position) {
        List<UcenterOutIndexBean.DataEntity.PartEntity> dataPart =
                ((OutPowerFragment) fragmentList.get(position)).getDataPart();
        if (dataPart != null) {
            adapter.replaceData(dataPart);
        }
    }

    boolean isRefresh = false;
    public void refreshData(List<UcenterOutIndexBean.DataEntity.PartEntity> dataPart) {
        if (adapter.getData().size() == 0 || isRefresh) {
            isRefresh = false;
            if (dataPart != null) {
                adapter.replaceData(dataPart);
            }
        }
    }

    public void refreshTabData() {
        isRefresh = true;
        ((OutPowerFragment) fragmentList.get(0)).postUcenterOutIndex();
        ((OutPowerFragment) fragmentList.get(1)).postUcenterOutIndex();
        ((OutPowerFragment) fragmentList.get(2)).postUcenterOutIndex();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((OutPowerFragment) fragmentList.get(vpGroup.getCurrentItem())).toMove(position);
        drawerLayout.closeDrawer(Gravity.RIGHT);

//        outPowerAllFragment.postUcenterOutIndex(.get(position).getPart_id() + "");
//        outPowerOpenFragment.postUcenterOutIndex(adapter.get.get(position).getPart_id() + "");
//        outPowerCloseFragment.postUcenterOutIndex(list.get(position).getPart_id() + "");
    }
}
