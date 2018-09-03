package com.sdxxtop.zhidian.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ApplyHorCopyRecyclerAdapter;
import com.sdxxtop.zhidian.entity.ReportReadBean;
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.Date2Util;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;
import com.sdxxtop.zhidian.widget.TextAndTextView;
import com.tencent.qcloud.timchat.model.ChatBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.DateTimePicker;

public class WorkWeeklyActivity extends BaseActivity {

    private static final int IS_COPY = 1; //抄送人
    private static final int IS_REVIEWER = 2; //批读人

    private static final int START_DATE = 1; //开始时间
    private static final int END_DATE = 2; //结束时间


    @BindView(R.id.work_weekly_title_view)
    SubTitleView mTitleView;
    @BindView(R.id.text_and_text_daily_view)
    TextAndTextView mDailyView;
    @BindView(R.id.work_weekly_left_btn)
    Button mLeftBtn;
    @BindView(R.id.work_weekly_right_btn)
    Button mRightBtn;
    @BindView(R.id.work_weekly_layout)
    LinearLayout mWeeklyLayout;
    @BindView(R.id.work_weekly_title_1)
    TextView mTitle1;
    @BindView(R.id.work_weekly_content_1)
    EditText mContent1;
    @BindView(R.id.work_weekly_title_2)
    TextView mTitle2;
    @BindView(R.id.work_weekly_content_2)
    EditText mContent2;
    @BindView(R.id.work_weekly_title_3)
    TextView mTitle3;
    @BindView(R.id.work_weekly_content_3)
    EditText mContent3;
    @BindView(R.id.item_work_content_view_read_people_title)
    TextView mReadPeopleTitle;
    @BindView(R.id.item_work_content_view_read_people_recycler)
    RecyclerView mReadPeopleRecycler;
    @BindView(R.id.item_work_content_view_copy_title)
    TextView mCopyTitle;
    @BindView(R.id.item_work_content_view_copy_recycler)
    RecyclerView mCopyRecycler;
    @BindView(R.id.btn_submission)
    Button mBtnSubmit;
    private int mPosition;
    private int mType;

    private HashSet<Integer> selectUserSetCopy;
    private List<SelectBean> selectDataCopy;
    private HashSet<Integer> selectUserSetReviewer;
    private List<SelectBean> selectDataReviewer;
    private DatePicker mMonthAndDayPicker;

    protected ApplyHorCopyRecyclerAdapter copyAdapter;
    protected ApplyHorCopyRecyclerAdapter reviewerAdapter;

    private int chatActivityValue;

    @Override
    protected int getActivityView() {
        return R.layout.activity_work_weekly;
    }

    @Override
    protected void initVariables() {
        super.initVariables();
        if (getIntent() != null) {
            mPosition = getIntent().getIntExtra("position", -1);
            mType = mPosition + 1;

            chatActivityValue = getIntent().getIntExtra("chat_activity", -1);
        }
    }

    @Override
    protected void initView() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String stringMonth;

        if (month < 10) {
            stringMonth = "0" + month;
        } else {
            stringMonth = "" + month;
        }

        switch (mPosition) {
            case 0: //日报
                mTitleView.setTitleValue("日报");
                mTitle1.setText("今日工作总结");
                mContent1.setHint("请输入今日工作总结");

                mTitle2.setText("明日工作计划");
                mContent2.setHint("请输入明日工作计划");

                mWeeklyLayout.setVisibility(View.GONE);
                mDailyView.setVisibility(View.VISIBLE);

                //默认当天

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String stringDay;
                if (day < 10) {
                    stringDay = "0" + day;
                } else {
                    stringDay = "" + day;
                }
                mDailyView.getTextRightText().setText(year + "年" + stringMonth + "月" + stringDay + "日");
                mReportDate = year + "-" + stringMonth + "-" + stringDay;

                break;
            case 1: //周报
                mTitleView.setTitleValue("周报");
                mTitle1.setText("本周工作总结");
                mContent1.setHint("请输入本周工作总结");

                mTitle2.setText("下周工作计划");
                mContent2.setHint("请输入下周工作计划");

                mWeeklyLayout.setVisibility(View.VISIBLE);
                mDailyView.setVisibility(View.GONE);

                //默认这一周
                Date mondayOfThisWeek = Date2Util.getMondayOfThisWeek();
                mStartDate = new SimpleDateFormat("yyyy-MM-dd").format(mondayOfThisWeek);
                mLeftBtn.setText(mStartDate);

                Date lastDayOfMonth = Date2Util.getSundayOfThisWeek();
                mEndDate = new SimpleDateFormat("yyyy-MM-dd").format(lastDayOfMonth);
                mRightBtn.setText(mEndDate);

                break;
            default: //月报
                mTitleView.setTitleValue("月报");
                mTitle1.setText("本月工作总结");
                mContent1.setHint("请输入本月工作总结");

                mTitle2.setText("下月工作计划");
                mContent2.setHint("请输入下月工作计划");

                mWeeklyLayout.setVisibility(View.GONE);
                mDailyView.setVisibility(View.VISIBLE);

                //默认当月
                mReportMonthlyDate = year + "-" + stringMonth;
                mDailyView.getTextRightText().setText(year + "年" + stringMonth + "月");
                break;
        }

        setReviewerRecycler(mReadPeopleRecycler);
        setCopyRecycler(mCopyRecycler);

        mBtnSubmit.setText("提交汇报");
        mTitle3.setText("工作中的问题");
        mContent3.setHint("请输入工作中的问题");
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        switch (mPosition) {
            case 0: //日报
                mDailyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDailySelectorWindow();
                    }
                });
                break;
            case 1: //周报
                mLeftBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showWeeklySelectorWindow(START_DATE);
                    }
                });

                mRightBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showWeeklySelectorWindow(END_DATE);
                    }
                });
                break;
            default: //月报
                mDailyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMonthSelectorWindow();
                    }
                });
                break;
        }

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    //日报的字符串缓存
    private String mReportDate;

    //周报的字符串缓存
    private String mStartDate;
    private String mEndDate;

    //月报
    private String mReportMonthlyDate;

    private void submit() {
        //总结
        final String summaryValue = mContent1.getText().toString();
        //计划
        final String planValue = mContent2.getText().toString();
        //问题 可以为空
        final String problemValue = mContent3.getText().toString();

        if (StringUtil.isEmptyWithTrim(summaryValue)) {
//            showToast("工作总结不能为空");
            showToast("请输入" + mTitle1.getText().toString());
            return;
        }

        if (StringUtil.isEmptyWithTrim(planValue)) {
//            showToast("工作计划不能为空");
            showToast("请输入" + mTitle2.getText().toString());
            return;
        }

        String reviewerValue = getReviewerValue();
        if (TextUtils.isEmpty(reviewerValue)) {
            showToast("请选择批读人");
            return;
        }

        Params params = new Params();
        params.put("rt", mType);
        params.put("sm", summaryValue);
        params.put("pn", planValue);
        params.put("pb", problemValue);
        params.put("ri", reviewerValue);
        params.put("si", getCopyValue());

        switch (mPosition) {
            case 0: //日报
                params.put("rd", mReportDate);
                break;
            case 1: //周报
                if (!isWeekDateSuccess()) {
                    showToast("周报结束时间必须大于开始时间");
                    return;
                }
                params.put("sd", mStartDate);
                params.put("ed", mEndDate);
                break;
            default: //月报
                params.put("rm", mReportMonthlyDate);
                break;
        }

        showProgressDialog("");

        RequestUtils.createRequest().postReportAdd(params.getData()).enqueue(new RequestCallback<BaseModel>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                showToast("提交成功");
                closeProgressDialog();

                Object data = baseModel.getData();
                String report_id = "";
                if (data != null) {
                    report_id = (String) ((LinkedTreeMap) data).get("report_id");
                    if (!TextUtils.isEmpty(report_id)) {
                        int reportInt = Integer.parseInt(report_id);
                        if (chatActivityValue == 0) { //chat的周报
                            toChatJson(reportInt, 0);
                        } else {
                            MineWorkDetailActivity.startWorkDetailActivity(mContext, MineWorkActivity.TYPE_SEND, reportInt);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
                showToast(errorMsg);
            }
        }));
    }

    private void toChatJson(final int reportInt, final int type) {

        Params params = new Params();
        params.put("ri", reportInt);

        RequestUtils.createRequest().postReportRead(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<ReportReadBean>() {
            @Override
            public void onSuccess(ReportReadBean reportReadBean) {
                closeProgressDialog();
                ReportReadBean.DataBean data = reportReadBean.getData();
                if (data != null) {
                    ReportReadBean.DataBean.ReportBean report = data.getReport();
                    ChatBean chatBean = new ChatBean();
                    int reportType = report.getReport_type();
                    chatBean.setType(reportType);
                    chatBean.setPlan(report.getPlan());
                    chatBean.setUserAction(1);
                    chatBean.setProblem(report.getProblem());
                    chatBean.setReport_id(reportInt);
                    chatBean.setSummary(report.getSummary());
                    chatBean.setReviewer_id(getReviewerValue());
                    String name = report.getName();
                    switch (reportType) {
                        case 1: //日报
                            chatBean.setTitle(name + "的日报");
                            break;
                        case 2: //周报
                            chatBean.setTitle(name + "的周报");
                            break;
                        default: //月报
                            chatBean.setTitle(name + "的月报");
                            break;
                    }

                    String s = new Gson().toJson(chatBean);
                    Intent intent = new Intent();
                    intent.putExtra("report_json", s);
                    intent.putExtra("report_type", reportType);
                    setResult(100, intent);
                    finish();
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
                showToast(errorMsg);
            }
        }));

    }

    private void showDailySelectorWindow() {
        Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(mReportDate)) {
            Date date = new Date(DateUtil.convertTimeToLong(mReportDate, "yyyy-MM-dd"));
            calendar.setTime(date);
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mMonthAndDayPicker = new DatePicker(this, DateTimePicker.YEAR_MONTH_DAY);
        mMonthAndDayPicker.setRangeStart(year - 100, 1, 1);
        mMonthAndDayPicker.setRangeEnd(year + 100, 12, 31);
        mMonthAndDayPicker.setLabel("年", "月", "日");
        mMonthAndDayPicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                mReportDate = year + "-" + month + "-" + day;
                mDailyView.getTextRightText().setText(year + "年" + month + "月" + day + "日");
            }
        });

        mMonthAndDayPicker.setSelectedItem(year, month, day);
        mMonthAndDayPicker.show();
    }

    private void showWeeklySelectorWindow(final int startDate) {
        Calendar calendar = Calendar.getInstance();
        if (startDate == START_DATE) {
            if (!TextUtils.isEmpty(mStartDate)) {
                Date date = new Date(DateUtil.convertTimeToLong(mStartDate, "yyyy-MM-dd"));
                calendar.setTime(date);
            }
        } else {
            if (!TextUtils.isEmpty(mEndDate)) {
                Date date = new Date(DateUtil.convertTimeToLong(mEndDate, "yyyy-MM-dd"));
                calendar.setTime(date);
            }
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mMonthAndDayPicker = new DatePicker(this, DateTimePicker.YEAR_MONTH_DAY);
        mMonthAndDayPicker.setRangeStart(year - 100, 1, 1);
        mMonthAndDayPicker.setRangeEnd(year + 100, 12, 31);
        mMonthAndDayPicker.setLabel("年", "月", "日");
        mMonthAndDayPicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String stringDate = year + "-" + month + "-" + day;
                switch (startDate) {
                    case START_DATE:
                        mStartDate = stringDate;
                        mLeftBtn.setText(mStartDate);
                        break;
                    case END_DATE:
                        mEndDate = stringDate;
                        mRightBtn.setText(mEndDate);
                        break;
                }
            }
        });

        mMonthAndDayPicker.setSelectedItem(year, month, day);
        mMonthAndDayPicker.show();
    }

    private void showMonthSelectorWindow() {
        Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(mReportMonthlyDate)) {
            Date date = new Date(DateUtil.convertTimeToLong(mReportMonthlyDate, "yyyy-MM"));
            calendar.setTime(date);
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        mMonthAndDayPicker = new DatePicker(this, DateTimePicker.YEAR_MONTH);
        mMonthAndDayPicker.setRangeStart(year - 100, 1);
        mMonthAndDayPicker.setRangeEnd(year + 100, 12);
        mMonthAndDayPicker.setLabel("年", "月", "日");
        mMonthAndDayPicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
            @Override
            public void onDatePicked(String year, String month) {
                mReportMonthlyDate = year + "-" + month;
                mDailyView.getTextRightText().setText(year + "年" + month + "月");
            }
        });

        mMonthAndDayPicker.setSelectedItem(year, month);
        mMonthAndDayPicker.show();
    }

    protected void setCopyRecycler(RecyclerView copyRecycler) {
        copyAdapter = new ApplyHorCopyRecyclerAdapter(R.layout.item_apply_hor_copy_start_recycler);
        copyRecycler.setAdapter(copyAdapter);
        copyRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        SelectBean selectBean = new SelectBean();
        copyAdapter.addData(selectBean);

        copyAdapter.setAddListener(new ApplyHorCopyRecyclerAdapter.AddListener() {
            @Override
            public void addClick() {
                Intent intent = new Intent(mContext, NoticeReciveRangeActivity.class);
                intent.putExtra("isPartSelect", NoticeReciveRangeActivity.PART_NOT_SELECTOR);
                startActivityForResult(intent, IS_COPY);
            }
        });

        copyAdapter.setDeleteListener(new ApplyHorCopyRecyclerAdapter.DeleteListener() {
            @Override
            public void onDelete(SelectBean item) {
                Integer id = item.getId();
                if (selectUserSetCopy != null) {
                    if (selectUserSetCopy.contains(id)) {
                        selectUserSetCopy.remove(id);
                    }
                }
            }
        });
    }

    protected void setReviewerRecycler(RecyclerView reviewerRecycler) {
        reviewerAdapter = new ApplyHorCopyRecyclerAdapter(R.layout.item_apply_hor_copy_start_recycler);
        reviewerRecycler.setAdapter(reviewerAdapter);
        reviewerRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        SelectBean selectBean = new SelectBean();
        reviewerAdapter.addData(selectBean);

        reviewerAdapter.setAddListener(new ApplyHorCopyRecyclerAdapter.AddListener() {
            @Override
            public void addClick() {
                Intent intent = new Intent(mContext, NoticeReciveRangeActivity.class);
                intent.putExtra("isPartSelect", NoticeReciveRangeActivity.PART_NOT_SELECTOR);
                startActivityForResult(intent, IS_REVIEWER);
            }
        });

        reviewerAdapter.setDeleteListener(new ApplyHorCopyRecyclerAdapter.DeleteListener() {
            @Override
            public void onDelete(SelectBean item) {
                Integer id = item.getId();
                if (selectUserSetReviewer != null) {
                    if (selectUserSetReviewer.contains(id)) {
                        selectUserSetReviewer.remove(id);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_COPY && resultCode == NoticeReciveRangeActivity.NOTICE_RESULT_OK && data != null) { //抄送人
            copyPeople(data, IS_COPY);
        } else if (requestCode == IS_REVIEWER && resultCode == NoticeReciveRangeActivity.NOTICE_RESULT_OK && data != null) { //抄送人
            copyPeople(data, IS_REVIEWER);
        }
    }

    private void copyPeople(Intent data, int type) {
        if (data == null) {
            return;
        }
        if (IS_COPY == type) {
            copyPeople(data);
//            selectUserSetCopy = (HashSet<Integer>) data.getSerializableExtra("userList");
//            selectDataCopy = (List<SelectBean>) data.getSerializableExtra("selectData");
//            selectDataCopy.add(new SelectBean());
//            copyAdapter.replaceData(selectDataCopy);
        } else {
            reviewerPeople(data);
//            selectUserSetReviewer = (HashSet<Integer>) data.getSerializableExtra("userList");
//            selectDataReviewer = (List<SelectBean>) data.getSerializableExtra("selectData");
//            selectDataReviewer.add(new SelectBean());
//            reviewerAdapter.replaceData(selectDataReviewer);
        }
    }


    private void copyPeople(Intent data) {
        if (data == null) {
            return;
        }

        if (selectUserSetCopy != null) {
            HashSet<Integer> tempSelectUserSet = selectUserSetCopy;
            selectUserSetCopy = (HashSet<Integer>) data.getSerializableExtra("userList");
            selectUserSetCopy.addAll(tempSelectUserSet);
        } else {
            selectUserSetCopy = (HashSet<Integer>) data.getSerializableExtra("userList");
        }

        if (selectDataCopy != null && selectDataCopy.size() > 0) {
            List<SelectBean> tempSelectData = selectDataCopy;
            tempSelectData.remove(tempSelectData.size() - 1);
            selectDataCopy = (List<SelectBean>) data.getSerializableExtra("selectData");
            List<SelectBean> centerSelectData = new ArrayList<>(selectDataCopy);
            for (SelectBean centerSelectDatum : centerSelectData) {
                for (SelectBean tempSelectDatum : tempSelectData) {
                    if (centerSelectDatum.getId().equals(tempSelectDatum.getId())) {
                        selectDataCopy.remove(centerSelectDatum);
                    }
                }
            }

            //删除相同的最后再加入集合中
            selectDataCopy.addAll(0, tempSelectData);
        } else {
            selectDataCopy = (List<SelectBean>) data.getSerializableExtra("selectData");
        }
        selectDataCopy.add(new SelectBean());
        copyAdapter.replaceData(selectDataCopy);
    }

    private void reviewerPeople(Intent data) {
        if (data == null) {
            return;
        }

        if (selectUserSetReviewer != null) {
            HashSet<Integer> tempSelectUserSet = selectUserSetReviewer;
            selectUserSetReviewer = (HashSet<Integer>) data.getSerializableExtra("userList");
            selectUserSetReviewer.addAll(tempSelectUserSet);
        } else {
            selectUserSetReviewer = (HashSet<Integer>) data.getSerializableExtra("userList");
        }

        if (selectDataReviewer != null && selectDataReviewer.size() > 0) {
            List<SelectBean> tempSelectData = selectDataReviewer;
            tempSelectData.remove(tempSelectData.size() - 1);
            selectDataReviewer = (List<SelectBean>) data.getSerializableExtra("selectData");
            List<SelectBean> centerSelectData = new ArrayList<>(selectDataReviewer);
            for (SelectBean centerSelectDatum : centerSelectData) {
                for (SelectBean tempSelectDatum : tempSelectData) {
                    if (centerSelectDatum.getId().equals(tempSelectDatum.getId())) {
                        selectDataReviewer.remove(centerSelectDatum);
                    }
                }
            }

            //删除相同的最后再加入集合中
            selectDataReviewer.addAll(0, tempSelectData);
        } else {
            selectDataReviewer = (List<SelectBean>) data.getSerializableExtra("selectData");
        }
        selectDataReviewer.add(new SelectBean());
        reviewerAdapter.replaceData(selectDataReviewer);
    }

    protected String getCopyValue() {
        String value = "";
        if (selectUserSetCopy == null) {
            return value;
        }
        for (Integer integer : selectUserSetCopy) {
            value = value + integer + ",";
        }
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    protected String getReviewerValue() {
        String value = "";
        if (selectUserSetReviewer == null) {
            return value;
        }
        for (Integer integer : selectUserSetReviewer) {
            value = value + integer + ",";
        }
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    private boolean isWeekDateSuccess() {
        Long start = DateUtil.convertTimeToLong(mStartDate, "yyyy-MM-dd");
        Long end = DateUtil.convertTimeToLong(mEndDate, "yyyy-MM-dd");
        if (start < end) {
            return true;
        }
        return false;
    }

    public static void startWorkWeeklyActivity(Context context, int position) {
        Intent intent = new Intent(context, WorkWeeklyActivity.class);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }
}
