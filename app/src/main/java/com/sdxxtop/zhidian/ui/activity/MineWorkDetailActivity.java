package com.sdxxtop.zhidian.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ReportReadBean;
import com.sdxxtop.zhidian.eventbus.ChangeCompanyEvent;
import com.sdxxtop.zhidian.eventbus.MessageCenterEvent;
import com.sdxxtop.zhidian.eventbus.MineWorkEvent;
import com.sdxxtop.zhidian.eventbus.PostSuccessEvent;
import com.sdxxtop.zhidian.eventbus.PushCenterEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.SendEditLayout;
import com.sdxxtop.zhidian.widget.SubTitleView;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import zhangphil.iosdialog.widget.AlertDialog;

public class MineWorkDetailActivity extends BaseActivity {

    @BindView(R.id.mine_work_detail_title_view)
    SubTitleView mTitleView;
    @BindView(R.id.mine_work_detail_first_title)
    TextView mFirstTitle;
    @BindView(R.id.mine_work_detail_first_time)
    TextView mFirstTime;
    @BindView(R.id.mine_work_detail_response_text)
    TextView mResponseText;
    @BindView(R.id.mine_work_detail_content_recycler)
    RecyclerView mContentRecycler;
    @BindView(R.id.mine_work_detail_response_recycler)
    RecyclerView mResponseRecycler;

    @BindView(R.id.mine_work_detail_send_edit_layout)
    SendEditLayout mEditLayout;
//    @BindView(R.id.mine_work_detail_send_btn)
//    Button mSendBtn;
//    @BindView(R.id.mine_work_detail_send_line)
//    View sendLine;
//    @BindView(R.id.mine_work_detail_send_layout)
//    View sendLayout;

    private static String WORK_DETAIL_TYPE = "work_detail_type";
    private static String REPORT_ID = "report_id";
    private static String COMPANY_ID = "company_id";

    private WorkResponseAdapter mResponseAdapter;
    private WorkContentAdapter mContentAdapter;

    // 如果是抄送的就把回复隐藏
    private int workDetailType;
    private int reportId;
    private int companyId;


    @Override
    protected int getActivityView() {
        return R.layout.activity_mine_work_detail;
    }

    @Override
    protected void initVariables() {
        super.initVariables();
        if (getIntent() != null) {
            workDetailType = getIntent().getIntExtra(WORK_DETAIL_TYPE, -1);
            reportId = getIntent().getIntExtra(REPORT_ID, -1);
            companyId = getIntent().getIntExtra(COMPANY_ID, -1);
        }

        EventBus.getDefault().post(new PushCenterEvent());
    }

    @Override
    protected void initView() {
        super.initView();

        configContentRecycler(mContentRecycler);
        configResponseRecycler(mResponseRecycler);
    }

    private void configResponseRecycler(RecyclerView responseRecycler) {
        responseRecycler.setLayoutManager(new LinearLayoutManager(this));
        responseRecycler.setNestedScrollingEnabled(false);
        responseRecycler.addItemDecoration(new ItemDivider().setDividerLeftOffset(ViewUtil.dp2px(mContext, 75)));
        mResponseAdapter = new WorkResponseAdapter(R.layout.item_work_detail_response_recycler);
        responseRecycler.setAdapter(mResponseAdapter);
    }

    private void configContentRecycler(RecyclerView contentRecyclerView) {
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contentRecyclerView.setNestedScrollingEnabled(false);
        mContentAdapter = new WorkContentAdapter(R.layout.item_work_detail_content_recycler);
        contentRecyclerView.setAdapter(mContentAdapter);
    }

    @Override
    protected void initEvent() {
        super.initEvent();

        mEditLayout.setEditSendClickListener(new SendEditLayout.EditSendClickListener() {
            @Override
            public void sendClick(int userId, int parentId, String content) {
                submit(userId + "", parentId + "", content);
            }
        });

        mTitleView.getLeftText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSplashActivity(mContext);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openSplashActivity(this);
    }

    @Override
    protected void initData() {
        super.initData();

        Params params = new Params();
        params.put("ri", reportId);
        if (companyId != -1) {
            params.removeKey("ci");
            params.put("ci", companyId);
        }

        RequestUtils.createRequest().postReportRead(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<ReportReadBean>() {
            @Override
            public void onSuccess(ReportReadBean reportReadBean) {
                closeProgressDialog();
                ReportReadBean.DataBean data = reportReadBean.getData();
                if (data != null) {
                    handleData(data);
                    EventBus.getDefault().post(new MineWorkEvent());
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
                showToast(errorMsg);
            }
        }));

        if (companyId == -1) {
            return;
        }

        String stringParam = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID);
        final String companyId = this.companyId + "";
        if (!companyId.equals(stringParam)) {
            new AlertDialog(mContext).builder().setTitle("提示")
                    .setMsg("消息非当前公司，是否切换到该公司")
                    .setPositiveButton("", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppSession.getInstance().setCompanyId(companyId);
                            //刷新首页数据
                            EventBus.getDefault().post(new MessageCenterEvent());
                            //刷新我的
                            EventBus.getDefault().post(new ChangeCompanyEvent());
                            EventBus.getDefault().post(new PostSuccessEvent());
//                            PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.COMPANY_ID, companyId);
                        }
                    }).setNegativeButton("", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSplashActivity(mContext);
                    finish();
                }
            }).show();
        }
    }

    private void handleData(ReportReadBean.DataBean data) {
        ReportReadBean.DataBean.ReportBean reportBean = data.getReport();
        if (data.getIs_comment() == 2) {
            workDetailType = MineWorkActivity.TYPE_COPY;
            mEditLayout.setVisibility(View.GONE);
        } else {
            workDetailType = MineWorkActivity.TYPE_READ;
            mEditLayout.setVisibility(View.VISIBLE);
        }
        mResponseText.setText("回复(" + data.getComment_num() + ")");
        if (reportBean != null) {
            int reportType = reportBean.getReport_type();
            //设置title
            handleTitle(reportType, reportBean.getName());

            Calendar calendar = Calendar.getInstance();
            switch (reportType) {
                case 1:
                    Date reportTimeDate = new Date(DateUtil.convertTimeToLong(reportBean.getReport_date(), "yyyy-MM-dd"));
                    calendar.setTime(reportTimeDate);
                    String day = getDay(calendar);
                    String month = getMonth(calendar);
                    mFirstTitle.setText(month + "月" + day + "日  日报");
                    break;
                case 2:
                    String startDate = reportBean.getStart_date();
                    String endDate = reportBean.getEnd_date();
                    Date startTimeDate = new Date(DateUtil.convertTimeToLong(startDate, "yyyy-MM-dd"));
                    Date endTimeDate = new Date(DateUtil.convertTimeToLong(endDate, "yyyy-MM-dd"));
                    calendar.setTime(startTimeDate);
                    Calendar endCalendar = Calendar.getInstance();
                    endCalendar.setTime(endTimeDate);

                    String showDate = showDate(calendar, endCalendar);
                    mFirstTitle.setText(showDate + "  周报");
                    break;
                default:
                    String reportMonth = reportBean.getReport_month();
                    Date reportMonthTimeDate = new Date(DateUtil.convertTimeToLong(reportMonth, "yyyy-MM"));
                    calendar.setTime(reportMonthTimeDate);
                    int monthMoth = calendar.get(Calendar.MONTH) + 1;
                    String stringMonth;
                    if (monthMoth < 10) {
                        stringMonth = "0" + monthMoth;
                    } else {
                        stringMonth = "" + monthMoth;
                    }
                    mFirstTitle.setText(stringMonth + "月  月报");
                    break;
            }
            mFirstTime.setText(reportBean.getAdd_time());

            List<WorkListBean> workListBeanList = new ArrayList<>();
            workListBeanList.add(new WorkListBean(reportType, WorkListBean.SUMMARY, reportBean.getSummary()));
            workListBeanList.add(new WorkListBean(reportType, WorkListBean.PLAN, reportBean.getPlan()));
            String problem = reportBean.getProblem();
            if (!TextUtils.isEmpty(problem)) {
                workListBeanList.add(new WorkListBean(reportType, WorkListBean.PROBLEM, problem));
            }
            String reviewer_name = reportBean.getReviewer_name();
            if (!TextUtils.isEmpty(reviewer_name)) {
                workListBeanList.add(new WorkListBean(reportType, WorkListBean.REVIEWER, reviewer_name));
            }
            String send_name = reportBean.getSend_name();
            if (!TextUtils.isEmpty(send_name)) {
                workListBeanList.add(new WorkListBean(reportType, WorkListBean.COPY, send_name));
            }

            mContentAdapter.replaceData(workListBeanList);
        }

        //评论内容
        List<ReportReadBean.DataBean.CommentBean> comment = data.getComment();
        if (comment != null) {
            mResponseAdapter.replaceData(comment);
        }
    }

    private void handleTitle(int reportType, String name) {
        switch (reportType) {
            case 1: //日报
                mTitleView.setTitleValue(name + "的日报");
                break;
            case 2: //周报
                mTitleView.setTitleValue(name + "的周报");
                break;
            default: //月报
                mTitleView.setTitleValue(name + "的月报");
                break;
        }
    }

    /**
     * @param userid   被评论人id
     * @param parentId 父级评论id
     * @param content  评论内容
     */
    private void submit(String userid, String parentId, String content) {
        showProgressDialog("");
        Params params = new Params();
        params.put("ri", reportId);
        params.put("pi", StringUtil.stringNotNull(parentId));
        params.put("cui", StringUtil.stringNotNull(userid));
        params.put("ct", StringUtil.stringNotNull(content));
        RequestUtils.createRequest().postReportComment(params.getData()).enqueue(new RequestCallback<BaseModel>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                mEditLayout.setEditSuccess();
                showToast("评论成功");
                initData();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
                closeProgressDialog();
            }
        }));
    }

    class WorkListBean {
        public int reportType;
        public static final int SUMMARY = 1;
        public static final int PLAN = 2;
        public static final int PROBLEM = 3;
        public static final int REVIEWER = 4; //批读人
        public static final int COPY = 5; //抄送人

        //总结，计划，问题
        public String workValue;
        public int type;

        public WorkListBean(int reportType, int type, String workValue) {
            this.reportType = reportType;
            this.workValue = workValue;
            this.type = type;
        }
    }

    class WorkResponseAdapter extends BaseQuickAdapter<ReportReadBean.DataBean.CommentBean, BaseViewHolder> {

        public WorkResponseAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, ReportReadBean.DataBean.CommentBean item) {
            CircleImageView imgView = helper.getView(R.id.item_work_detail_response_circle_img);
            TextView shortNameText = helper.getView(R.id.item_work_detail_response_short_name);
            TextView nameText = helper.getView(R.id.item_work_detail_response_name);
            TextView timeText = helper.getView(R.id.item_work_detail_response_time);
            TextView contentText = helper.getView(R.id.item_work_detail_response_content);
            TextView responseText = helper.getView(R.id.item_work_detail_response_text);

            //设置头像
            String name = item.getName();
            ViewUtil.setColorItemView(item.getImg(), name, shortNameText, imgView);

            nameText.setText(name);

            String comName = item.getCom_name();
            String comment = item.getComment();
            if (!TextUtils.isEmpty(comName)) {
                String tempName = "回复：@" + comName + " ";
                String text = tempName + comment;
//                SpannableStringBuilder builder = new SpannableStringBuilder(text);

                //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
//                ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#3296FA"));
//                ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.parseColor("#333333"));

//                builder.setSpan(blueSpan, 0, tempName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                if (!TextUtils.isEmpty(comment)) {
//                    builder.setSpan(blackSpan, tempName.length() + 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                }

                contentText.setText(text);
            } else {
                contentText.setText(comment);
            }


            timeText.setText(getShowTime(item.getAdd_time()));

            if (workDetailType == MineWorkActivity.TYPE_COPY) {
                responseText.setVisibility(View.GONE);
            } else {
                final String userName = item.getName();
                final int parentId = item.getParent_id();
                final int comUserid = item.getUserid();
                responseText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //回复
                        mEditLayout.sendKeyBoard(userName, comUserid, parentId);
                    }
                });
            }

        }
    }

    class WorkContentAdapter extends BaseQuickAdapter<WorkListBean, BaseViewHolder> {

        public WorkContentAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, WorkListBean item) {
            TextView titleText = helper.getView(R.id.item_work_detail_content_title);
            TextView contentText = helper.getView(R.id.item_work_detail_content_content);

            switch (item.reportType) {
                case 1:
                    if (item.type == WorkListBean.SUMMARY) {
                        titleText.setText("今日工作总结");
                    } else if (item.type == WorkListBean.PLAN) {
                        titleText.setText("明日工作计划");
                    } else if (item.type == WorkListBean.PROBLEM) {
                        titleText.setText("工作中的问题");
                    }
                    break;
                case 2:
                    if (item.type == WorkListBean.SUMMARY) {
                        titleText.setText("本周工作总结");
                    } else if (item.type == WorkListBean.PLAN) {
                        titleText.setText("下周工作计划");
                    } else if (item.type == WorkListBean.PROBLEM) {
                        titleText.setText("工作中的问题");
                    }
                    break;
                default:
                    if (item.type == WorkListBean.SUMMARY) {
                        titleText.setText("本月工作总结");
                    } else if (item.type == WorkListBean.PLAN) {
                        titleText.setText("下月工作计划");
                    } else if (item.type == WorkListBean.PROBLEM) {
                        titleText.setText("工作中的问题");
                    }
                    break;
            }

            if (item.type == WorkListBean.REVIEWER) {
                titleText.setText("批读人");
            } else if (item.type == WorkListBean.COPY) {
                titleText.setText("抄送人");
            }

            contentText.setText(item.workValue);
        }
    }

    private String showDate(Calendar startCalendar, Calendar endCalendar) {
        String startDay = getDay(startCalendar);
        String startMonth = getMonth(startCalendar);
        String endDay = getDay(endCalendar);
        String endMonth = getMonth(endCalendar);

        return startMonth + "月" + startDay + "日" + "-" + endMonth + "月" + endDay + "日";
    }

    private String getDay(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day < 10) {
            return "0" + day;
        } else {
            return "" + day;
        }
    }

    private String getMonth(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10) {
            return "0" + month;
        } else {
            return "" + month;
        }
    }

    public static String getShowTime(String time) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(date);
            Calendar nowDay = Calendar.getInstance();

            int month = currentDate.get(Calendar.MONTH) + 1;
            int day = currentDate.get(Calendar.DAY_OF_MONTH);

            int nowMonth = nowDay.get(Calendar.MONTH) + 1;
            int nowsDay = nowDay.get(Calendar.DAY_OF_MONTH);

            String format = sdf.format(date);
            if (month != nowMonth) {
                return format;
            }

            if (day != nowsDay) {
                return format;
            }

            String hourAnd = time.split(" ")[1];
            String substring = hourAnd.substring(0, hourAnd.lastIndexOf(":"));
            return substring;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void startWorkDetailActivity(Context context, int type, int reportId) {
        Intent intent = new Intent(context, MineWorkDetailActivity.class);
        intent.putExtra(WORK_DETAIL_TYPE, type);
        intent.putExtra(REPORT_ID, reportId);
        context.startActivity(intent);
    }

    public static void startWorkDetailActivity(Context context, int type, int reportId, int companyId) {
        Intent intent = new Intent(context, MineWorkDetailActivity.class);
        intent.putExtra(WORK_DETAIL_TYPE, type);
        intent.putExtra(COMPANY_ID, companyId);
        intent.putExtra(REPORT_ID, reportId);
        context.startActivity(intent);
    }
}
