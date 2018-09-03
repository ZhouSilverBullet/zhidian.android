package com.sdxxtop.zhidian.ui.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.course.CornerTextView;
import com.sdxxtop.zhidian.course.CourseModel;
import com.sdxxtop.zhidian.entity.CourseIndexBean;
import com.sdxxtop.zhidian.eventbus.CourseTableRefreshEvent;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.Date2Util;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

public class CourseTableActivity extends BaseActivity {
    @BindView(R.id.course_table_title)
    SubTitleView mTitleView;

    @BindView(R.id.weekNames)
    LinearLayout weekNames;
    @BindView(R.id.sections)
    LinearLayout sections;

    private int itemHeight;
    private int maxSection = 9;

    @BindViews({R.id.weekPanel_1, R.id.weekPanel_2, R.id.weekPanel_3, R.id.weekPanel_4,
            R.id.weekPanel_5, R.id.weekPanel_6, R.id.weekPanel_7})
    List<LinearLayout> mWeekViews;

    @Override
    protected int getActivityView() {
        return R.layout.activity_course_table;
    }

    @Override
    protected void initView() {
        registeredEvent();

        itemHeight = getResources().getDimensionPixelSize(R.dimen.sectionHeight);
        initWeekNameView();
        initSectionView();
    }

    @Override
    protected void initData() {
        Params params = new Params();
        RequestUtils.createRequest().postTimeTableIndex(params.getData())
                .enqueue(new RequestCallback<>(new IRequestListener<CourseIndexBean>() {
                    @Override
                    public void onSuccess(CourseIndexBean courseIndexBean) {
                        CourseIndexBean.DataBean data = courseIndexBean.getData();
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

    @Override
    protected void initEvent() {
        super.initEvent();
        mTitleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseUpdateActivity.startCourseUpdateActivity(mContext);
            }
        });
    }

    /**
     * 顶部周一到周日的布局
     **/
    private void initWeekNameView() {
        //获取tab的 周几 和 xx日
        List<TopTabBean> topListBean = getTopListBean();

        for (int i = 0; i < mWeekViews.size() + 1; i++) {
            TextView tvWeekName = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.gravity = Gravity.CENTER /*| Gravity.CENTER_HORIZONTAL*/;
            if (i != 0) {
                lp.weight = 1;
                TopTabBean topTabBean = topListBean.get(i - 1);
                tvWeekName.setText(topTabBean.weekValue + "\n" + topTabBean.weekToDay);

                if (i == getWeekDay()) {
                    tvWeekName.setBackgroundColor(getResColor(R.color.course_tab_select_color));
                    tvWeekName.setTextColor(getResColor(R.color.course_tab_color));
                } else {
                    tvWeekName.setBackgroundColor(getResColor(R.color.course_tab_color));
                    tvWeekName.setTextColor(getResColor(R.color.course_tab_text_color));
                }
            } else {
                lp.weight = 0.8f;
                tvWeekName.setText(getMonth() + "\n月");
                tvWeekName.setBackgroundColor(getResColor(R.color.course_tab_color));
                tvWeekName.setTextColor(getResColor(R.color.course_tab_text_color));
            }
            tvWeekName.setGravity(Gravity.CENTER);
            tvWeekName.setLayoutParams(lp);
            weekNames.addView(tvWeekName);
        }
    }

    private void handleData(CourseIndexBean.DataBean data) {
        List<CourseIndexBean.DataBean.CourseBean> course = data.getCourse();
        if (course != null) {
            initWeekCourseView(course);
        }
    }

    /**
     * 初始化课程表
     */
    private void initWeekCourseView(List<CourseIndexBean.DataBean.CourseBean> course) {
        List<List<CourseModel>> listCourseModel = new ArrayList<>();
        List<CourseModel> weekCourseList1 = new ArrayList<>();
        List<CourseModel> weekCourseList2 = new ArrayList<>();
        List<CourseModel> weekCourseList3 = new ArrayList<>();
        List<CourseModel> weekCourseList4 = new ArrayList<>();
        List<CourseModel> weekCourseList5 = new ArrayList<>();
        List<CourseModel> weekCourseList6 = new ArrayList<>();
        List<CourseModel> weekCourseList7 = new ArrayList<>();

        //当天星期几
        int nowWeekDay = getWeekDay();

        for (CourseIndexBean.DataBean.CourseBean courseBean : course) {
            int week_day = courseBean.getWeek_day();
            //大于或者是今天的，就用颜色
            boolean hasColor = week_day >= nowWeekDay;
            switch (week_day) {
                case 1:
                    setCourseList(courseBean, weekCourseList1, hasColor);
                    break;
                case 2:
                    setCourseList(courseBean, weekCourseList2, hasColor);
                    break;
                case 3:
                    setCourseList(courseBean, weekCourseList3, hasColor);
                    break;
                case 4:
                    setCourseList(courseBean, weekCourseList4, hasColor);
                    break;
                case 5:
                    setCourseList(courseBean, weekCourseList5, hasColor);
                    break;
                case 6:
                    setCourseList(courseBean, weekCourseList6, hasColor);
                    break;
                default:
                    setCourseList(courseBean, weekCourseList7, hasColor);
                    break;
            }
        }

        listCourseModel.add(weekCourseList1);
        listCourseModel.add(weekCourseList2);
        listCourseModel.add(weekCourseList3);
        listCourseModel.add(weekCourseList4);
        listCourseModel.add(weekCourseList5);
        listCourseModel.add(weekCourseList6);
        listCourseModel.add(weekCourseList7);

        for (int i = 0; i < mWeekViews.size(); i++) {
            initWeekPanel(mWeekViews.get(i), listCourseModel.get(i));
        }
    }

    private void setCourseList(CourseIndexBean.DataBean.CourseBean courseBean, List<CourseModel> weekCourseList, boolean hasColor) {
        int classId = courseBean.getClass_id();
        String className = courseBean.getClass_name();
        String course_name = courseBean.getCourse_name();
        int section = courseBean.getBegin();
        // 6 - 6 + 7
        int sectionSpan = courseBean.getEnd() - section + 1;
        int week = courseBean.getWeek_day();
        String color;
        if (hasColor) {
            String beanColor = courseBean.getColor();
            if (TextUtils.isEmpty(beanColor)) {
                color = "#BFBFBF";
            } else {
                color = beanColor;
            }
        } else {
            color = "#BFBFBF";
        }
        weekCourseList.add(new CourseModel(classId, course_name, section,
                sectionSpan, week, className, color));
    }

    public void initWeekPanel(LinearLayout ll, List<CourseModel> data) {

        if (ll == null || data == null || data.size() < 1)
            return;
        CourseModel firstCourse = data.get(0);
        for (int i = 0; i < data.size(); i++) {
            final CourseModel courseModel = data.get(i);

            if (courseModel.getSection() == 0 || courseModel.getSectionSpan() == 0)
                return;
            FrameLayout frameLayout = new FrameLayout(this);

            CornerTextView tv = new CornerTextView(this,
                    Color.parseColor(courseModel.getCourseFlag()),
                    ViewUtil.dp2px(this, 5));
            LinearLayout.LayoutParams frameLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    itemHeight * courseModel.getSectionSpan());
            LinearLayout.LayoutParams tvLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            if (i == 0) {
                frameLp.setMargins(0, (courseModel.getSection() - 1) * itemHeight, 0, 0);
            } else {
                frameLp.setMargins(0, (courseModel.getSection() - (firstCourse.getSection() + firstCourse.getSectionSpan())) * itemHeight, 0, 0);
            }
            tv.setLayoutParams(tvLp);
            tv.setPadding(3, 3, 3, 3);
            tv.setGravity(Gravity.LEFT | Gravity.TOP);
            tv.setTextSize(12);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setText(courseModel.getCourseName() + "\n-" + courseModel.getClassName());

            frameLayout.setLayoutParams(frameLp);
            frameLayout.addView(tv);
            frameLayout.setPadding(2, 2, 2, 2 + ViewUtil.dp2px(mContext, 1));
            ll.addView(frameLayout);
            firstCourse = courseModel;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    showToast(courseModel.getCourseName());
                }
            });
        }
    }

    /**
     * 左边节次布局，设定每天最多12节课
     */
    private void initSectionView() {
        for (int i = 1; i <= maxSection; i++) {
            TextView tvSection = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.sectionHeight));
            lp.gravity = Gravity.CENTER;
            tvSection.setGravity(Gravity.CENTER);
            tvSection.setText(String.valueOf(i));
            tvSection.setBackgroundColor(getResColor(R.color.course_tab_color));
            tvSection.setTextColor(getResColor(R.color.course_tab_text_color));
            tvSection.setLayoutParams(lp);
            sections.addView(tvSection);
        }
    }


    /**
     * 当前星期
     */
    public int getWeekDay() {
        int w = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        if (w <= 0) {
            w = 7;
        }
        return w;
    }

    /**
     * 当前月份
     */
    public int getMonth() {
        int w = Calendar.getInstance().get(Calendar.MONTH) + 1;
        return w;
    }

    public List<TopTabBean> getTopListBean() {
        List<TopTabBean> tabList = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            TopTabBean tabBean = getTabBean(i);
            tabList.add(tabBean);
        }
        return tabList;
    }

    public TopTabBean getTabBean(int week) {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + week);
        int day = c.get(Calendar.DAY_OF_MONTH);
        TopTabBean topTabBean = new TopTabBean();


        int cMonth = c.get(Calendar.MONTH);
        //和当前时间对比
        Calendar nowTime = Calendar.getInstance();
        int nowMonth = nowTime.get(Calendar.MONTH);

        //如果是新的一个月的一号，就显示 xx月，后面接着还是显示 xx日
        if (day == 1) {
//            topTabBean.weekToDay = Date2Util.getZeroTime(cMonth + 1) + "月";
            topTabBean.weekToDay = (cMonth + 1) + "月";
        } else {
            topTabBean.weekToDay = Date2Util.getZeroTime(day) + "日";
        }

        String weekValue = "";
        switch (week) {
            case 1:
                weekValue = "周一";
                break;
            case 2:
                weekValue = "周二";
                break;
            case 3:
                weekValue = "周三";
                break;
            case 4:
                weekValue = "周四";
                break;
            case 5:
                weekValue = "周五";
                break;
            case 6:
                weekValue = "周六";
                break;
            default:
                weekValue = "周日";
                break;
        }

        topTabBean.weekValue = weekValue;

        return topTabBean;
    }


    class TopTabBean {
        //周二
        public String weekValue;
        //11日
        public String weekToDay;
    }

    public int getResColor(int color) {
        return getResources().getColor(color);
    }


    /////////////////////刷新相关的/////////////////////

    /**
     * 下拉刷新
     */
    private void setRefreshListener() {
//        mFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                clearChildView();
//                initWeekCourseView();
//                mFreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mFreshLayout.setRefreshing(false);
//                    }
//                }, 500);
//            }
//        });
    }

    @Subscribe
    public void couseRefresh(CourseTableRefreshEvent event) {
        clearChildView();
        initData();
    }

    /**
     * 每次刷新前清除每个LinearLayout上的课程view
     */
    private void clearChildView() {
        for (int i = 0; i < mWeekViews.size(); i++) {
            if (mWeekViews.get(i) != null)
                if (mWeekViews.get(i).getChildCount() > 0)
                    mWeekViews.get(i).removeAllViews();
        }
    }

    /////////////////////刷新相关的/////////////////////
}
