package com.sdxxtop.zhidian.ui.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.TabLayoutPagerAdapter;
import com.sdxxtop.zhidian.entity.ReportIndexBean;
import com.sdxxtop.zhidian.eventbus.MineWorkEvent;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.MineWorkFragment;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.PagerSlidingTabStrip;
import com.sdxxtop.zhidian.widget.SubTitleView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MineWorkActivity extends BaseActivity {

    @BindView(R.id.mine_work_title_view)
    SubTitleView mTitleView;
    @BindView(R.id.mine_work_tab)
    PagerSlidingTabStrip mPagerTab;
    @BindView(R.id.mine_work_pager)
    ViewPager mViewPager;

    //我发送的
    public static final int TYPE_SEND = 10;
    //我批读的
    public static final int TYPE_READ = 11;
    //我抄送的
    public static final int TYPE_COPY = 12;
    private PopupWindow popupWindow;

    private int[] imgs = new int[]{R.drawable.daily_toast, R.drawable.weekly_toast, R.drawable.monthly_toast};
    private String[] names = new String[]{"日报", "周报", "月报"};
    private TabLayoutPagerAdapter mAdapter;
    private int current = 0;

    @Override
    protected int getActivityView() {
        return R.layout.activity_mine_work;
    }

    @Override
    protected void initView() {
        super.initView();
        mViewPager.setOffscreenPageLimit(3);
        setTabStrip(mPagerTab);
        registeredEvent();
    }

    private PagerSlidingTabStrip setTabStrip(PagerSlidingTabStrip pagerSlidingTabStrip) {
        pagerSlidingTabStrip.setTabPaddingLeftRight(ViewUtil.dp2px(this, 0));
        pagerSlidingTabStrip.setTextColor(Color.BLACK);
        pagerSlidingTabStrip.setMatchExpand(true);
        pagerSlidingTabStrip.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics()));
        pagerSlidingTabStrip.setTypeface(null, Typeface.NORMAL);
        pagerSlidingTabStrip.setIndicatorHeight(ViewUtil.dp2px(this, 2));
        pagerSlidingTabStrip.setIndicatorColor(getResources().getColor(R.color.lan));
        pagerSlidingTabStrip.setBackgroundColor(Color.WHITE);
        return pagerSlidingTabStrip;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mTitleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //应该是新建， 暂时用详情页 测试跳转
//                //TODO 类型等接口确定
//                MineWorkDetailActivity.startWorkDetailActivity(mContext, TYPE_SEND);
                showPopWindow();
            }
        });

        mPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    protected void initData() {
        super.initData();

        loadData();
    }

    public void loadData() {
        Params params = new Params();
        params.put("tp", 1);
        params.put("sp", 0);
        RequestUtils.createRequest().postReportIndex(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<ReportIndexBean>() {
            @Override
            public void onSuccess(ReportIndexBean reportIndexBean) {
                ReportIndexBean.DataBean data = reportIndexBean.getData();
                if (data != null) {
                    int reviewNum = data.getReview_num();
                    int sendNum = data.getSend_num();
                    List<String> titleList = new ArrayList<>();
                    titleList.add("我发送的");
                    if (reviewNum == 0) {
                        titleList.add("我批读的");
                    } else {
                        titleList.add("我批读的(" + reviewNum + ")");
                    }

                    if (sendNum == 0) {
                        titleList.add("抄送给我");
                    } else {
                        titleList.add("抄送给我(" + sendNum + ")");
                    }

                    List<Fragment> fragmentList = new ArrayList<>();
                    fragmentList.add(MineWorkFragment.newInstance(1));
                    fragmentList.add(MineWorkFragment.newInstance(2));
                    fragmentList.add(MineWorkFragment.newInstance(3));

                    mAdapter = new TabLayoutPagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
                    mViewPager.setAdapter(mAdapter);
                    mPagerTab.setViewPager(mViewPager);
                    mViewPager.setCurrentItem(current);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    /**
     * 展示日报，周报，月报
     */
    private void showPopWindow() {
        if (popupWindow == null) {
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_mine_work_layout, null);
            popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
            popupWindow.setOutsideTouchable(true);
            //设置点击事件
            RecyclerView workRecycler = (RecyclerView) contentView.findViewById(R.id.pop_mine_work_recycler);
            workRecycler.setLayoutManager(new LinearLayoutManager(this));
            workRecycler.addItemDecoration(new ItemDivider().setLastLineNotDraw(true));
            List<PopBean> list = new ArrayList<>();
            for (int i = 0; i < imgs.length; i++) {
                list.add(new PopBean(imgs[i], names[i]));
            }
            PopAdapter popAdapter = new PopAdapter(R.layout.item_pop_mine_work_layout, list);
            workRecycler.setAdapter(popAdapter);

            popAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    popupWindow.dismiss();
                    darkenBackground(1f);
                    WorkWeeklyActivity.startWorkWeeklyActivity(mContext, position);
                }
            });

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    darkenBackground(1f);
                }
            });
        }
        int[] location = new int[2];
        mTitleView.getRightText().getLocationOnScreen(location);
        int height = mTitleView.getRightText().getHeight();
        popupWindow.showAtLocation(mTitleView.getRightText(), Gravity.NO_GRAVITY
                , location[0] - ViewUtil.dp2px(this, 95), location[1] + height + ViewUtil.dp2px(this, 2));
        darkenBackground(0.6f);
    }

    /**
     * 改变背景颜色
     */
    private void darkenBackground(Float bgcolor) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    class PopBean {
        public int icon;
        public String name;

        public PopBean(int icon, String name) {
            this.icon = icon;
            this.name = name;
        }
    }

    class PopAdapter extends BaseQuickAdapter<PopBean, BaseViewHolder> {
        public PopAdapter(int layoutResId, @Nullable List<PopBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, PopBean item) {
            ImageView img = helper.getView(R.id.item_pop_mine_work_icon);
            img.setImageResource(item.icon);

            helper.setText(R.id.item_pop_mine_work_text, item.name);
        }
    }

    //查看文章之后刷新
    @Subscribe
    public void workNotifyEvent(MineWorkEvent event) {
        loadData();
    }

}
