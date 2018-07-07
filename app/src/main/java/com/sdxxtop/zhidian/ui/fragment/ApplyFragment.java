package com.sdxxtop.zhidian.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ApplyIndexBean;
import com.sdxxtop.zhidian.eventbus.ChangeCompanyEvent;
import com.sdxxtop.zhidian.eventbus.OneKeyEvent;
import com.sdxxtop.zhidian.eventbus.PostSuccessEvent;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.activity.ChangeShiftActivity;
import com.sdxxtop.zhidian.ui.activity.CollectiveLeaveActivity;
import com.sdxxtop.zhidian.ui.activity.EvectionActivity;
import com.sdxxtop.zhidian.ui.activity.ExamineActivity;
import com.sdxxtop.zhidian.ui.activity.LeaveActivity;
import com.sdxxtop.zhidian.ui.activity.MobilePhoneActivity;
import com.sdxxtop.zhidian.ui.activity.MyAppliedActivity;
import com.sdxxtop.zhidian.ui.activity.OvertimeActivity;
import com.sdxxtop.zhidian.ui.activity.PunchCardActivity;
import com.sdxxtop.zhidian.ui.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * 作者：CaiCM
 * 日期：2018/3/23  时间：16:39
 * 邮箱：15010104100@163.com
 * 描述：申请界面
 */
public class ApplyFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.gridv_apply)
    GridView gridViewApply;
    @BindView(R.id.image_my_apply)
    View imageMyApplied;
    @BindView(R.id.image_examine_layout)
    View imageExammine;
    @BindView(R.id.fragment_apply_count)
    TextView applyCountText;

    int[] imageRes = new int[]{R.mipmap.leave, R.mipmap.forget_punch_card, R.mipmap.late, R.mipmap.evection, R.mipmap.overtime,
            R.mipmap.collective_leave, R.mipmap.change_shift, R.mipmap.field_personnel, R.mipmap.mobile_phone};

    int[] name = new int[]{R.string.apply_grid_leave_text, R.string.apply_grid_forget_punch_card_text, R.string.apply_grid_late_text,
            R.string.apply_grid_evection_text, R.string.apply_grid_overtime_text, R.string.apply_grid_collective_leave_text,
            R.string.apply_grid_change_shift_text, R.string.apply_grid_field_personnel_text, R.string.apply_grid_mobile_phone_text};

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_apply;
    }

    @Override
    protected void initVariables() {
        super.initVariables();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void initView() {
        statusBar(true);

        View topView = mRootView.findViewById(R.id.top_title);
        topViewPadding(topView);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            statusBar(true);
            initData();
        }
    }

    @Override
    protected void initData() {
        super.initData();

        Params params = new Params();
        RequestUtils.createRequest().postApplyIndex(params.getData()).enqueue(
                new RequestCallback<>(new IRequestListener<ApplyIndexBean>() {
                    @Override
                    public void onSuccess(ApplyIndexBean mainIndexBean) {
                        handleData(mainIndexBean.getData());
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        showToast(errorMsg);
                    }
                }));
    }

    boolean fieldIsShow = false;

    private void handleData(ApplyIndexBean.DataBean data) {
        if (data != null) {
            int is_out = data.getIs_out();
            int num = data.getNum();
            if (is_out == 1) {
                fieldIsShow = true;
            } else {
                fieldIsShow = false;
            }
            if (num == 0) { //零的时候隐藏
                applyCountText.setVisibility(View.GONE);
            } else {
                applyCountText.setVisibility(View.VISIBLE);
                if (num >= 99) {
                    applyCountText.setText(99 + "+");
                } else {
                    applyCountText.setText("" + num);
                }
            }
        } else {
            applyCountText.setVisibility(View.GONE);
        }

        ArrayList<HashMap<String, Object>> listItemArrayList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < imageRes.length; i++) {
            if (!fieldIsShow && i == imageRes.length - 2) {  //外勤一般不显示
                continue;
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", imageRes[i]);
            map.put("itemText", getResources().getString(name[i]));
            listItemArrayList.add(map);
        }

        SimpleAdapter saImageItems = new SimpleAdapter(getActivity(),
                listItemArrayList,
                R.layout.item_apply_catalog_grid,
                new String[]{"itemImage", "itemText"},
                new int[]{R.id.image_conten_lcon, R.id.tv_conten_title});

        gridViewApply.setAdapter(saImageItems);
        gridViewApply.setSelector(new ColorDrawable(Color.TRANSPARENT));

        gridViewApply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("", "onItemClick: " + position);
                switch (position) {
                    case 0:
                        Intent intentLeaveActivity = new Intent(mContext, LeaveActivity.class);
                        intentLeaveActivity.putExtra("at", position + 1);
                        startActivity(intentLeaveActivity);
                        break;
                    case 1:
                        Intent intentPunchCardActivity = new Intent(mContext, PunchCardActivity.class);
                        intentPunchCardActivity.putExtra("at", position + 1);
                        startActivity(intentPunchCardActivity);
                        break;
                    case 2:
                        Intent intentLateActivity = new Intent(mContext, PunchCardActivity.class);
                        intentLateActivity.putExtra("at", position + 1);
                        startActivity(intentLateActivity);
                        break;
                    case 3:
                        Intent intentEvectionActivity = new Intent(mContext, EvectionActivity.class);
                        intentEvectionActivity.putExtra("at", position + 1);
                        startActivity(intentEvectionActivity);
                        break;
                    case 4:
                        Intent intentOvertimeActivity = new Intent(mContext, OvertimeActivity.class);
                        intentOvertimeActivity.putExtra("at", position + 1);
                        startActivity(intentOvertimeActivity);
                        break;
                    case 5:
                        Intent intentCollectiveLeaveActivity = new Intent(mContext, CollectiveLeaveActivity.class);
                        intentCollectiveLeaveActivity.putExtra("at", position + 1);
                        startActivity(intentCollectiveLeaveActivity);
                        break;
                    case 6:
                        Intent intentChangeShiftActivity = new Intent(mContext, ChangeShiftActivity.class);
                        intentChangeShiftActivity.putExtra("at", position + 2);
                        startActivity(intentChangeShiftActivity);
                        break;
                    case 7:
                        if (fieldIsShow) {
                            Intent intentEvectionActivity1 = new Intent(mContext, EvectionActivity.class);
                            intentEvectionActivity1.putExtra("at", position + 2);
                            startActivity(intentEvectionActivity1);
                        } else {
                            Intent intentMobilePhoneActivity = new Intent(mContext, MobilePhoneActivity.class);
                            intentMobilePhoneActivity.putExtra("at", position + 3);
                            startActivity(intentMobilePhoneActivity);
                        }
                        break;
                    case 8:
                        Intent intentMobilePhoneActivity = new Intent(mContext, MobilePhoneActivity.class);
                        intentMobilePhoneActivity.putExtra("at", position + 2);
                        startActivity(intentMobilePhoneActivity);
                        break;
                }
            }
        });
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        imageMyApplied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentApply = new Intent(mContext, MyAppliedActivity.class);
                startActivity(intentApply);
            }
        });
        imageExammine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentexamine = new Intent(mContext, ExamineActivity.class);
                startActivity(intentexamine);
            }
        });
    }

    /**
     * 提交 申请等 成功后
     *
     * @param event
     */
    @Subscribe
    public void onRefreshPostSuccess(PostSuccessEvent event) {
        initData();
    }

    /**
     * 切换公司成功后
     *
     * @param event
     */
    @Subscribe
    public void onRefreshConpanyChangeSuccess(ChangeCompanyEvent event) {
        initData();
    }

    /**
     * 审批点击同意或者驳回刷新页面
     *
     * @param event
     */
    @Subscribe
    public void onRefreshOneKeyEvent(OneKeyEvent event) {
        initData();
    }
}
