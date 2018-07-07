package com.sdxxtop.zhidian.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.eventbus.SchedulingFinishEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.widget.SubTitleView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 修改班次
 */
public class SchedulingChangeActivity extends BaseActivity implements View.OnTouchListener {

    @BindView(R.id.scheduling_change_view)
    SubTitleView titleView;
    @BindView(R.id.scheduling_change_text_name)
    TextView textName;
    @BindView(R.id.scheduling_change_text_right)
    EditText textRight;
    @BindView(R.id.scheduling_change_week_recycler)
    RecyclerView weekRecyclerView;
    @BindView(R.id.scheduling_change_holiday_recycler)
    RecyclerView holidayRecyclerView;
    @BindView(R.id.scheduling_change_btn)
    Button changeBtn;
    @BindView(R.id.scheduling_change_relate)
    RelativeLayout changeRelate;

    private String partValue;
    private String userValue;
    private String editValue;
    private String class_id;
    private ChangeAdapter weekAdapter;
    private ChangeAdapter holidayAdapter;
    private int managerSkip;
    private int rule_id;
    private String work_holiday;
    private String work_week_day;
    private String schedulingListValue;
    private String titleText;

    @Override
    protected int getActivityView() {
        return R.layout.activity_scheduling_change;
    }

    @Override
    protected void initVariables() {
        super.initVariables();
        if (getIntent() != null) {
            partValue = getIntent().getStringExtra("part_value");
            userValue = getIntent().getStringExtra("user_value");
            editValue = getIntent().getStringExtra("editValue");
            titleText = getIntent().getStringExtra("titleText");
            class_id = getIntent().getStringExtra("class_id");
            schedulingListValue = getIntent().getStringExtra("SchedulingList");

            managerSkip = getIntent().getIntExtra("managerSkip", -1);
            rule_id = getIntent().getIntExtra("rule_id", -1);
            work_holiday = getIntent().getStringExtra("work_holiday");
            work_week_day = getIntent().getStringExtra("work_week_day");
        }
    }

    @Override
    protected void initView() {
        if (managerSkip == SchedulingManageActivity.MANAGER_SKIP) {
            changeRelate.setVisibility(View.VISIBLE);
            textRight.setText(titleText);
            changeBtn.setText("完成");
            titleView.setTitleValue("修改工作日");
        } else {
            changeRelate.setVisibility(View.GONE);
        }

        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setAutoMeasureEnabled(true);
        weekRecyclerView.setHasFixedSize(true);
        weekRecyclerView.setNestedScrollingEnabled(false);
        weekRecyclerView.setLayoutManager(layout);
        weekRecyclerView.addItemDecoration(new ItemDivider());
        weekAdapter = new ChangeAdapter(R.layout.item_change_recycler);
        weekRecyclerView.setAdapter(weekAdapter);


        LinearLayoutManager layout1 = new LinearLayoutManager(this);
        layout1.setAutoMeasureEnabled(true);
        holidayRecyclerView.setHasFixedSize(true);
        holidayRecyclerView.setNestedScrollingEnabled(false);
        holidayRecyclerView.setLayoutManager(layout1);
        holidayRecyclerView.addItemDecoration(new ItemDivider());
        holidayAdapter = new ChangeAdapter(R.layout.item_change_recycler);
        holidayRecyclerView.setAdapter(holidayAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        ArrayList<WeekSelector> weekSelectors = new ArrayList<>();
        if (!TextUtils.isEmpty(work_week_day)) {
            String[] split = work_week_day.split(",");
            ArrayList<Integer> weekList = new ArrayList<>();
            for (String s : split) {
                if (!TextUtils.isEmpty(s)) {
                    weekList.add(Integer.parseInt(s));
                }
            }

            WeekSelector selector1 = new WeekSelector("星期一", "1", weekList.contains(1));
            WeekSelector selector2 = new WeekSelector("星期二", "2", weekList.contains(2));
            WeekSelector selector3 = new WeekSelector("星期三", "3", weekList.contains(3));
            WeekSelector selector4 = new WeekSelector("星期四", "4", weekList.contains(4));
            WeekSelector selector5 = new WeekSelector("星期五", "5", weekList.contains(5));
            WeekSelector selector6 = new WeekSelector("星期六", "6", weekList.contains(6));
            WeekSelector selector7 = new WeekSelector("星期日", "7", weekList.contains(7));

            weekSelectors.add(selector1);
            weekSelectors.add(selector2);
            weekSelectors.add(selector3);
            weekSelectors.add(selector4);
            weekSelectors.add(selector5);
            weekSelectors.add(selector6);
            weekSelectors.add(selector7);
        } else {
            WeekSelector selector1 = new WeekSelector("星期一", "1", true);
            WeekSelector selector2 = new WeekSelector("星期二", "2", true);
            WeekSelector selector3 = new WeekSelector("星期三", "3", true);
            WeekSelector selector4 = new WeekSelector("星期四", "4", true);
            WeekSelector selector5 = new WeekSelector("星期五", "5", true);
            WeekSelector selector6 = new WeekSelector("星期六", "6", false);
            WeekSelector selector7 = new WeekSelector("星期日", "7", false);

            weekSelectors.add(selector1);
            weekSelectors.add(selector2);
            weekSelectors.add(selector3);
            weekSelectors.add(selector4);
            weekSelectors.add(selector5);
            weekSelectors.add(selector6);
            weekSelectors.add(selector7);
        }
        weekAdapter.addData(weekSelectors);
        ArrayList<WeekSelector> holidaySelectors = new ArrayList<>();

        if (!TextUtils.isEmpty(work_holiday)) {
            String[] split = work_holiday.split(",");
            ArrayList<Integer> holidayList = new ArrayList<>();
            for (String s : split) {
                if (!TextUtils.isEmpty(s)) {
                    holidayList.add(Integer.parseInt(s));
                }
            }

            WeekSelector selector11 = new WeekSelector("元旦", "1", holidayList.contains(1));
            WeekSelector selector22 = new WeekSelector("春节", "2", holidayList.contains(2));
            WeekSelector selector33 = new WeekSelector("清明", "3", holidayList.contains(3));
            WeekSelector selector44 = new WeekSelector("劳动", "4", holidayList.contains(4));
            WeekSelector selector55 = new WeekSelector("端午", "5", holidayList.contains(5));
            WeekSelector selector66 = new WeekSelector("中秋", "6", holidayList.contains(6));
            WeekSelector selector77 = new WeekSelector("国庆", "7", holidayList.contains(7));
            holidaySelectors.add(selector11);
            holidaySelectors.add(selector22);
            holidaySelectors.add(selector33);
            holidaySelectors.add(selector44);
            holidaySelectors.add(selector55);
            holidaySelectors.add(selector66);
            holidaySelectors.add(selector77);

        } else {
            WeekSelector selector11 = new WeekSelector("元旦", "1", false);
            WeekSelector selector22 = new WeekSelector("春节", "2", false);
            WeekSelector selector33 = new WeekSelector("清明", "3", false);
            WeekSelector selector44 = new WeekSelector("劳动", "4", false);
            WeekSelector selector55 = new WeekSelector("端午", "5", false);
            WeekSelector selector66 = new WeekSelector("中秋", "6", false);
            WeekSelector selector77 = new WeekSelector("国庆", "7", false);
            holidaySelectors.add(selector11);
            holidaySelectors.add(selector22);
            holidaySelectors.add(selector33);
            holidaySelectors.add(selector44);
            holidaySelectors.add(selector55);
            holidaySelectors.add(selector66);
            holidaySelectors.add(selector77);
        }

        holidayAdapter.addData(holidaySelectors);

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(schedulingListValue)) {
                    postModify();
                } else {
                    pushData();
                }
            }
        });

        //设置touch事件
        textRight.setOnTouchListener(this);

    }

    //重写onTouch方法
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText控件，且当前EditText可以滚动，则将事件交给EditText处理；否则将事件交由其父类处理
        if ((view.getId() == R.id.scheduling_change_text_right && canVerticalScroll(textRight))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    /**
     * EditText竖直方向是否可以滚动
     * @param editText  需要判断的EditText
     * @return  true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        if(editText.canScrollVertically(-1) || editText.canScrollVertically(1)) {
            //垂直方向上可以滚动
            return true;
        }
        return false;
    }

    private void postModify() {
        String ruleName = textRight.getText().toString();
        if (TextUtils.isEmpty(ruleName)) {
            showToast("班次名不能为空");
            return;
        }

        Params params = new Params();
        params.put("ri", rule_id);
        params.put("cli", class_id);
        params.put("wd", getWeekValue());
        params.put("wh", getHolidayValue());
        params.put("rn", ruleName);
        params.put("tp", 3);
        RequestUtils.createRequest().postClassesModify(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                showToast(baseModel.msg);
                setResult(100);
                finish();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));

    }

    private void pushData() {
        Params params = new Params();
        params.put("rn", editValue);
        params.put("oi", params.getUserId());
        params.put("ui", userValue);
        params.put("pi", partValue);
        params.put("cli", class_id);
        params.put("wd", getWeekValue());
        params.put("wh", getHolidayValue());
        RequestUtils.createRequest().postClassAdd(params.getData()).enqueue(new RequestCallback<BaseModel>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                EventBus.getDefault().post(new SchedulingFinishEvent());
                setResult(100);
                finish();
                showToast(baseModel.msg);
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));

    }

    private String getWeekValue() {
        String value = "";
        List<WeekSelector> data = weekAdapter.getData();
        for (WeekSelector integer : data) {
            if (integer.isCheck) {
                value = value + integer.value + ",";
            }
        }
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    private String getHolidayValue() {
        String value = "";
        List<WeekSelector> data = holidayAdapter.getData();
        for (WeekSelector integer : data) {
            if (integer.isCheck) {
                value = value + integer.value + ",";
            }
        }
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    class WeekSelector {
        public WeekSelector(String name, String value, boolean isCheck) {
            this.name = name;
            this.value = value;
            this.isCheck = isCheck;
        }

        String name;
        String value;
        boolean isCheck;
    }

    private class ChangeAdapter extends BaseQuickAdapter<WeekSelector, BaseViewHolder> {

        public ChangeAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final WeekSelector item) {
            final TextView changeCheckText = helper.getView(R.id.item_change_check);
            TextView textNameText = helper.getView(R.id.item_change_text_name);
            setChecked(changeCheckText, item.isCheck);
            textNameText.setText(item.name);
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isCheck = item.isCheck;
                    setChecked(changeCheckText, !isCheck);
                    item.isCheck = !isCheck;
                    notifyDataSetChanged();
                }
            });
        }
    }

    private void setChecked(TextView textView, boolean isCheck) {
        if (isCheck) {
            textView.setBackgroundResource(R.drawable.selected);
        } else {
            textView.setBackgroundResource(R.drawable.unselected);
        }
    }
}
