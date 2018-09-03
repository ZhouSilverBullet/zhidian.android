package com.sdxxtop.zhidian.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.SelectionSubjectAdapter;
import com.sdxxtop.zhidian.entity.ParentSelectBean;
import com.sdxxtop.zhidian.entity.UserCourseBean;
import com.sdxxtop.zhidian.eventbus.CourseTableRefreshEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.widget.TextAndTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import zhangphil.iosdialog.widget.AlertDialog;

public class CourseUpdateActivity extends BaseActivity {

    @BindView(R.id.course_update_select_class_text)
    TextAndTextView mSelectClassText;
    @BindView(R.id.course_update_select_subject_text)
    TextAndTextView mSelectSubjectText;
    @BindView(R.id.course_update_clear_btn)
    Button mSubmitClearBtn;
    @BindView(R.id.course_update_no_clear)
    Button mSubmitnotClearBtn;
    @BindView(R.id.course_update_right_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.course_update_right_drawer_cancel)
    Button mDrawerCancel;
    @BindView(R.id.course_update_right_drawer_confirm)
    Button mDrawerConfirm;
    @BindView(R.id.course_update_drawer)
    DrawerLayout mDrawerLayout;
    private SelectionSubjectAdapter selectionAdapter;
    private ArrayList<ParentSelectBean> parentBeanSet;
    private String courseName;
    private int courseId;

    @Override
    protected int getActivityView() {
        return R.layout.activity_course_update;
    }

    @Override
    protected void initView() {
        //授课课程缓存
        courseName = PreferenceUtils.getInstance(mContext).getStringParam(getSelectorKey() + ConstantValue.COURSE_SELECTOR_NAME);
        courseId = PreferenceUtils.getInstance(mContext).getIntParam(getSelectorKey() + ConstantValue.COURSE_SELECTOR_ID, -1);
        if (!TextUtils.isEmpty(courseName)) {
            mSelectSubjectText.getTextRightText().setText(courseName);
            mSelectSubjectText.setTag(courseId);
        }
        //授课班级

        String gradeName = PreferenceUtils.getInstance(mContext).getStringParam(getSelectorKey() + ConstantValue.HOMEWORK_SELECTOR_USER_OR_GRADE_NAME);
        if (!TextUtils.isEmpty(gradeName)) {
            mSelectClassText.getTextRightText().setText(gradeName);
        }
    }

    @Override
    protected void initEvent() {
        mSelectClassText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentListActivity.startParentListActivity(mContext,
                        ParentListActivity.TYPE_SINGLE_SELECT,
                        ParentListActivity.STATUS_SELECT_CLASS, "courseUpdate", 100);
            }
        });

        mDrawerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });

        mSelectSubjectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                toOpenCheck();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                for (UserCourseBean.DataBean.CourseBean courseBean : selectionAdapter.getData()) {
                    courseBean.setCheck(false);
                }
                toOpenCheck();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        mDrawerConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectionAdapter != null) {
                    boolean isCheck = false;
                    List<UserCourseBean.DataBean.CourseBean> data = selectionAdapter.getData();
                    for (UserCourseBean.DataBean.CourseBean datum : data) {
                        if (datum.isCheck()) {
                            isCheck = true;
                            String course_name = datum.getCourse_name();
                            int course_id = datum.getCourse_id();
                            mSelectSubjectText.getTextRightText().setText(course_name);
                            mSelectSubjectText.setTag(course_id);

                            //保存上次选择的记录
                            PreferenceUtils.getInstance(mContext).saveParam(getSelectorKey() + ConstantValue.COURSE_SELECTOR_NAME, course_name);
                            PreferenceUtils.getInstance(mContext).saveParam(getSelectorKey() + ConstantValue.COURSE_SELECTOR_ID, course_id);
                            break;
                        }
                    }

                    if (!isCheck) {
                        PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.COURSE_SELECTOR_NAME + getSelectorKey(), "");
                        PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.COURSE_SELECTOR_ID + getSelectorKey(), 0);
                        mSelectSubjectText.getTextRightText().setText("选择");
                        mSelectSubjectText.setTag(0);
                    }
                }
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });

        //清空原所有课程再添加新课程
        mSubmitClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(TYPE_CLEAR);
            }
        });

        //在原有基础上追加课程，当课程有冲突
        mSubmitnotClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkClassConflict();
            }
        });
    }

    private void toOpenCheck() {
        Object tag = mSelectSubjectText.getTag();
        int courseId = 0;
        if (tag instanceof Integer) {
            courseId = (int) tag;
        }
        if (courseId == 0) {
            for (UserCourseBean.DataBean.CourseBean courseBean : selectionAdapter.getData()) {
                courseBean.setCheck(false);
            }
        } else {
            for (UserCourseBean.DataBean.CourseBean courseBean : selectionAdapter.getData()) {
                if (courseBean.getCourse_id() == courseId) {
                    courseBean.setCheck(true);
                    break;
                }
            }
        }
        selectionAdapter.notifyDataSetChanged();
    }

    //这个可以不进行检测
    public static final int TYPE_CLEAR = 1;
    //这个需要进行检测
    public static final int TYPE_NO_CLEAR = 2;

    private void submit(int type) {

        String gradeId = PreferenceUtils.getInstance(mContext).getStringParam(getSelectorKey() + ConstantValue.HOMEWORK_SELECTOR_GRADE_ID);
//        String gradeId = getGradeId();
        if (TextUtils.isEmpty(gradeId)) {
            showToast("请选择授课班级");
            return;
        }

        String courseName = mSelectSubjectText.getTextRightText().getText().toString();
        int courseId = 0;
        Object tag = mSelectSubjectText.getTag();
        if (tag instanceof Integer) {
            courseId = (int) tag;
        }
        if (TextUtils.isEmpty(courseName) || courseId == 0) {
            showToast("请选择授课课程");
            return;
        }

        Params params = new Params();
        //班级id
        params.put("cli", gradeId);
        //科目id
        params.put("cei", courseId);
        params.put("ic", type);
        RequestUtils.createRequest().postTimeTableAdd(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                //刷新外面的课程表
                EventBus.getDefault().post(new CourseTableRefreshEvent());
                showToast("添加课程成功");
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    private void checkClassConflict() {

        if (!toCheckParamsSafe()) {
            return;
        }

        int courseId = (int) mSelectSubjectText.getTag();
        Params params = new Params();
        //班级id
        String gradeId = PreferenceUtils.getInstance(mContext).getStringParam(getSelectorKey() + ConstantValue.HOMEWORK_SELECTOR_GRADE_ID);
        params.put("cli", gradeId);
        //科目id
        params.put("cei", courseId);
        RequestUtils.createRequest().postTimeTableCheckRepeat(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                submit(TYPE_NO_CLEAR);
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                if (code == 390) {
                    if (TextUtils.isEmpty(errorMsg)) {
                        errorMsg = "课程时间有冲突，是否覆盖?";
                    }
                    new AlertDialog(mContext)
                            .builder()
                            .setTitle("提示")
                            .setMsg(errorMsg)
                            .setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    submit(TYPE_NO_CLEAR);
                                }
                            })
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                }
            }
        }));

    }


    public boolean toCheckParamsSafe() {
        String gradeId = PreferenceUtils.getInstance(mContext).getStringParam(getSelectorKey() + ConstantValue.HOMEWORK_SELECTOR_GRADE_ID);
        if (TextUtils.isEmpty(gradeId)) {
            showToast("请选择授课班级");
            return false;
        }

        String courseName = mSelectSubjectText.getTextRightText().getText().toString();
        int courseId = 0;
        Object tag = mSelectSubjectText.getTag();
        if (tag instanceof Integer) {
            courseId = (int) tag;
        }

        if (TextUtils.isEmpty(courseName) || courseId == 0) {
            showToast("请选择授课课程");
            return false;
        }
        return true;
    }

    @Override
    protected void initData() {
        loadCourse();
    }

    private void loadCourse() {
        Params params = new Params();
        RequestUtils.createRequest().postUserCourse(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<UserCourseBean>() {
            @Override
            public void onSuccess(UserCourseBean userCourseBean) {
                UserCourseBean.DataBean data = userCourseBean.getData();
                if (data != null) {
                    List<UserCourseBean.DataBean.CourseBean> course = data.getCourse();
                    if (course != null) {
                        selectionAdapter = new SelectionSubjectAdapter(R.layout.item_stat_selector_recycler, course);
                        mRecyclerView.addItemDecoration(new ItemDivider());
                        mRecyclerView.setAdapter(selectionAdapter);

                        Object tag = mSelectSubjectText.getTag();
                        int courseId = 0;
                        if (tag instanceof Integer) {
                            courseId = (int) tag;
                            for (UserCourseBean.DataBean.CourseBean courseBean : selectionAdapter.getData()) {
                                if (courseBean.getCourse_id() == courseId) {
                                    courseBean.setCheck(true);
                                    break;
                                }
                            }
                            selectionAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == ParentListActivity.CUSTOMER_RESULT_OK && data != null) {
            parentBeanSet = ((ArrayList<ParentSelectBean>) data.getSerializableExtra(ParentListActivity.SELECT_SET_DATA));
            String gradeName = "";
            for (ParentSelectBean selectBean : parentBeanSet) {
                if (selectBean.type == ParentSelectBean.TYPE_GRADE) {
                    gradeName = gradeName + " " + selectBean.value;
                }
            }

            mSelectClassText.getTextRightText().setText(gradeName);
            PreferenceUtils.getInstance(mContext).saveParam(getSelectorKey() + ConstantValue.HOMEWORK_SELECTOR_USER_OR_GRADE_NAME, gradeName);
            PreferenceUtils.getInstance(mContext).saveParam(getSelectorKey() + ConstantValue.HOMEWORK_SELECTOR_GRADE_ID, getGradeId());
        }
    }

    public String getGradeId() {
        String temp = "";
        if (parentBeanSet == null) {
            return temp;
        }

        for (ParentSelectBean selectBean : parentBeanSet) {
            if (selectBean.type == ParentSelectBean.TYPE_GRADE) {
                temp = temp + selectBean.gradeId + ",";
            }
        }
        if (temp.length() > 0) {
            temp = temp.substring(0, temp.length() - 1);
        }

        return temp;
    }

    public String getSelectorKey() {
        return AppSession.getInstance().getUserId() + AppSession.getInstance().getCompanyId();
    }

    public static void startCourseUpdateActivity(Context context) {
        Intent intent = new Intent(context, CourseUpdateActivity.class);
        context.startActivity(intent);
    }
}
