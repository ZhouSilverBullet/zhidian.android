package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ColorTextBean;
import com.sdxxtop.zhidian.entity.StateIndexBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ColorTextUtil;
import com.sdxxtop.zhidian.utils.GridDividerItemDecoration;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.util.List;

import butterknife.BindView;

public class StatisticalActivity extends BaseActivity {

    @BindView(R.id.statistical_title_view)
    SubTitleView titleView;
    @BindView(R.id.statistical_department_recycler)
    RecyclerView departmentRecycler;
    @BindView(R.id.statistical_personal_recycler)
    RecyclerView personalRecycler;
    private StatisticalAdapter departmentAdapter;
    private StatisticalAdapter personalAdapter;


    @Override
    protected int getActivityView() {
        return R.layout.activity_statistical;
    }

    @Override
    protected void initView() {
        super.initView();
        departmentRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        departmentRecycler.setNestedScrollingEnabled(false);
        departmentRecycler.setHasFixedSize(true);
        departmentRecycler.addItemDecoration(new GridDividerItemDecoration(ViewUtil.dp2px(mContext, 5),
                getResources().getColor(R.color.translate)));
        departmentAdapter = new StatisticalAdapter(R.layout.item_statistical_recycler);
        departmentRecycler.setAdapter(departmentAdapter);

        personalRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        personalRecycler.setNestedScrollingEnabled(false);
        personalRecycler.setHasFixedSize(true);
        personalRecycler.addItemDecoration(new GridDividerItemDecoration(ViewUtil.dp2px(mContext, 5),
                getResources().getColor(R.color.translate)));
        personalAdapter = new StatisticalAdapter(R.layout.item_statistical_recycler);
        personalRecycler.setAdapter(personalAdapter);

    }

    @Override
    protected void initData() {
        super.initData();
        Params params = new Params();
        RequestUtils.createRequest().postStatIndex(params.getData())
                .enqueue(new RequestCallback<>(new IRequestListener<StateIndexBean>() {
                    @Override
                    public void onSuccess(StateIndexBean stateIndexBean) {
                        StateIndexBean.DataBean data = stateIndexBean.getData();
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

    private void handleData(StateIndexBean.DataBean data) {
        String part_stat = data.getPart_stat();
        String user_stat = data.getUser_stat();
        List<ColorTextBean> partValue = ColorTextUtil.getPartValue(part_stat);
        departmentAdapter.replaceData(partValue);

        List<ColorTextBean> userValue = ColorTextUtil.getUserValue(user_stat);
        personalAdapter.replaceData(userValue);

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        departmentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ColorTextBean item = departmentAdapter.getData().get(position);
                final int type = item.type;
                final int dayType;

                if (item.type == 0) {  //人员出勤
                    Intent intent = new Intent(mContext, StatPeopleActivity.class);
                    startActivity(intent);
                } else {
                    if (item.type <= 4) { //都属于今日
                        dayType = StatDayActivity.DAY_TYPE;
                    } else {
                        dayType = StatDayActivity.MONTH_TYPE;
                    }
                    Intent intent = new Intent(mContext, StatDayActivity.class);
                    intent.putExtra(StatDayActivity.STAT_LEAVE_TYPE, type);
                    intent.putExtra(StatDayActivity.STAT_DAY_TYPE, dayType);
                    startActivity(intent);
                }


            }
        });

        personalAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ColorTextBean item = personalAdapter.getData().get(position);
                final int type = item.type;
                final int dayType;
                if (item.type <= 4) { //都属于今日
                    dayType = StatMonthActivity.DAY_TYPE;
                } else {
                    dayType = StatMonthActivity.MONTH_TYPE;
                }
                Intent intent = new Intent(mContext, StatMonthActivity.class);
                intent.putExtra(StatMonthActivity.STAT_LEAVE_TYPE, type);
                intent.putExtra(StatMonthActivity.STAT_DAY_TYPE, dayType);
                startActivity(intent);
            }
        });

        titleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StatReportActivity.class);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 201) {
            initData();
        }
    }

    private class StatisticalAdapter extends BaseQuickAdapter<ColorTextBean, BaseViewHolder> {

        public StatisticalAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, ColorTextBean item) {
            helper.setText(R.id.item_statistical_text, item.textValue);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(ViewUtil.dp2px(mContext, 7));
            drawable.setColor(Color.parseColor(item.bgColor));
            helper.getConvertView().setBackgroundDrawable(drawable);
//            final int type = item.type;
//            final int dayType;
//            if (item.type <= 4) { //都属于今日
//                dayType = StatDayActivity.DAY_TYPE;
//            } else {
//                dayType = StatDayActivity.MONTH_TYPE;
//            }
            helper.addOnClickListener(R.id.item_statistical_layout);
//            helper.g.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }
    }
}
