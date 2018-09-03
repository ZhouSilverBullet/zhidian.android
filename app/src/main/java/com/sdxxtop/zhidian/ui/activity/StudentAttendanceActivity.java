package com.sdxxtop.zhidian.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.PopStudentRecyclerAdapter;
import com.sdxxtop.zhidian.adapter.StudentAttendanceAdapter;
import com.sdxxtop.zhidian.entity.ParentSelectBean;
import com.sdxxtop.zhidian.entity.StudentAttendanceBean;
import com.sdxxtop.zhidian.entity.StudentClassBean;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.popupwindow.StatSelectionDateWindow;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

public class StudentAttendanceActivity extends BaseActivity {
    @BindView(R.id.student_attendance_sub_title_view)
    SubTitleView mTitleView;
    @BindView(R.id.student_attendance_recycler)
    RecyclerView mRecyclerView;
    private ArrayList<ParentSelectBean> parentBeanSet;
    private StudentAttendanceAdapter mAdapter;

    private PopStudentRecyclerAdapter mClassPopAdapter;
    private PopupWindow popupWindow;
    private RequestCache requestCache;

    private StatSelectionDateWindow selectionDateWindow;

    private class RequestCache {
        //xxxx年xx月xx日
        public String titleDate;
        //xx月xx日
        public String rightTitleDate;
        // xxxx-xx-xx
        public String requestDate;

        public int classId;

    }

    @Override
    protected int getActivityView() {
        return R.layout.activity_student_attendance;
    }

    @Override
    protected void initView() {

        requestCache = new RequestCache();

//        initTitle();

        mTitleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDateWindow();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new ItemDivider());
        mAdapter = new StudentAttendanceAdapter(R.layout.item_student_attendance_recycler);
        mRecyclerView.setAdapter(mAdapter);

        //初始化
        initClassPop();
    }

    // type : 1 有数据 0 无数据
    private void initTitle(final int type) {
        if (type == 1) {
            Drawable drawable = getResources().getDrawable(R.mipmap.select_up);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            mTitleView.getTitleText().setCompoundDrawables(null, null, drawable, null);
            mTitleView.getTitleText().setCompoundDrawablePadding(ViewUtil.dp2px(mContext, 5));

            mTitleView.getTitleText().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showClassPop();
                }
            });
        } else {
            mTitleView.getTitleText().setText("请选择班级");

            mTitleView.getTitleText().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParentListActivity.startParentListActivity(mContext,
                            ParentListActivity.TYPE_SINGLE_SELECT,
                            ParentListActivity.STATUS_SELECT_CLASS, "studentAttendance", 100);
                }
            });
        }
    }

    @Override
    protected void initEvent() {

        mAdapter.setConfirmRequestSuccessCallback(new StudentAttendanceAdapter.ConfirmRequestSuccessCallback() {
            @Override
            public void onSuccess(int type) {
                loadIndex(requestCache.classId, requestCache.requestDate);
            }
        });
    }

    @Override
    protected void initData() {
        Calendar instance = Calendar.getInstance();
        CalendarDay from = CalendarDay.from(instance);
        requestCache.rightTitleDate = DateUtil.getChineseSelector2Date(from);
        mTitleView.getRightText().setText(requestCache.rightTitleDate);

        //通过加载title上的班级进行
        requestCache.requestDate = DateUtil.getSelectorDate(from);
        loadClass(requestCache.requestDate);

    }

    /**
     * 主页数据请求
     */
    private void loadIndex(int classId, final String date) {
        todayVisibility();

        Params params = new Params();
        params.put("cli", classId);
        params.put("sd", date);
        RequestUtils.createRequest().postStudentIndex(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<StudentAttendanceBean>() {
            @Override
            public void onSuccess(StudentAttendanceBean bean) {
                StudentAttendanceBean.DataBean data = bean.getData();
                if (data != null) {
                    handleData(data, date);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    private void todayVisibility() {
        Calendar instance = Calendar.getInstance();
        CalendarDay from = CalendarDay.from(instance);
        String dayChineseSelectorDate = DateUtil.getSelectorDate(from);
        if (dayChineseSelectorDate.equals(requestCache.requestDate)) {
            mTitleView.getRightText().setText("今天");
        }
    }

    private void handleData(StudentAttendanceBean.DataBean data, String date) {
        List<StudentAttendanceBean.DataBean.SignBean> sign = data.getSign();
        if (sign == null) {
            return;
        }

        mAdapter.setTimeDate(date);
        mAdapter.replaceData(sign);
    }

    private void loadClass(final String date) {
        Params params = new Params();
        RequestUtils.createRequest().postStudentShowClass(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<StudentClassBean>() {
            @Override
            public void onSuccess(StudentClassBean studentClassBean) {
                StudentClassBean.DataBean data = studentClassBean.getData();
                if (data != null) {
                    List<StudentClassBean.DataBean.ClassBean> classList = data.getClassX();
                    if (classList != null && classList.size() > 0) {
                        initTitle(1);

                        StudentClassBean.DataBean.ClassBean classBean = classList.get(0);
                        mTitleView.getTitleText().setText(classBean.getClass_name());
                        requestCache.classId = classBean.getClass_id();
                        loadIndex(requestCache.classId, date);
                        mClassPopAdapter.replaceData(classList);
                    } else {
                        initTitle(0);
                    }
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                initTitle(0);
                showToast(errorMsg);
            }
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == ParentListActivity.CUSTOMER_RESULT_OK && data != null) {
            parentBeanSet = ((ArrayList<ParentSelectBean>) data.getSerializableExtra(ParentListActivity.SELECT_SET_DATA));
            String gradeName = "";
            int gradeId = 0;
            if (parentBeanSet.size() > 0) {
                for (ParentSelectBean parentSelectBean : parentBeanSet) {
                    gradeId = parentSelectBean.gradeId;
                }
            }
            modifyClass(gradeId, 1);
//            mSelectClassText.getTextRightText().setText(gradeName);
        }
    }

    // 1添加，2删除
    private void modifyClass(int classId, final int type) {
        Params params = new Params();
        params.put("cli", classId);
        //1.添加 2.删除
        params.put("tp", type);
        RequestUtils.createRequest().postStudentModifyClass(params.getData()).enqueue(new RequestCallback<BaseModel>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                if (type == 1) {
                    showToast("添加成功");
                } else {
                    showToast("删除成功");
                }
                loadClass(requestCache.requestDate);
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    private void showSelectDateWindow() {
        if (selectionDateWindow == null) {
            selectionDateWindow = new StatSelectionDateWindow(this, false, true);
            int weight = getResources().getDisplayMetrics().widthPixels;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            selectionDateWindow.setWidth(weight);
            selectionDateWindow.setHeight(height);
            selectionDateWindow.setFocusable(true);
            selectionDateWindow.setTouchable(true);
            selectionDateWindow.setOutsideTouchable(true);
            selectionDateWindow.setAnimationStyle(R.style.AnimationRightBottom);

            selectionDateWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    darkenBackground(1f);
                }
            });

            selectionDateWindow.setSelectorDateListener(new StatSelectionDateWindow.SelectorDateListener() {
                @Override
                public void onSelector(String date, CalendarDay calendarDay) {
                    requestCache.requestDate = DateUtil.getSelectorDate(calendarDay);
                    requestCache.rightTitleDate = DateUtil.getChineseSelector2Date(calendarDay);
                    mTitleView.getRightText().setText(requestCache.rightTitleDate);
                    if (requestCache.classId != 0) {
                        loadIndex(requestCache.classId, requestCache.requestDate);
                    }
                    selectionDateWindow.dismiss();
                }
            });
        }

        darkenBackground(0.6f);
        selectionDateWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_main, null), Gravity.BOTTOM, 0, 0);
    }

    private void initClassPop() {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_student_attendance_class_view, null);
        popupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        View addText = inflate.findViewById(R.id.pop_student_add_text);
        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentListActivity.startParentListActivity(mContext,
                        ParentListActivity.TYPE_SINGLE_SELECT,
                        ParentListActivity.STATUS_SELECT_CLASS, "studentAttendance", 100);
                popupWindow.dismiss();
            }
        });

        RecyclerView popRecycler = (RecyclerView) inflate.findViewById(R.id.pop_student_recycler);
        popRecycler.setLayoutManager(new LinearLayoutManager(this));
        popRecycler.addItemDecoration(new ItemDivider());
        mClassPopAdapter = new PopStudentRecyclerAdapter(R.layout.item_pop_student_attendance_class_recycler);
        popRecycler.setAdapter(mClassPopAdapter);

        mClassPopAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                StudentClassBean.DataBean.ClassBean classBean = mClassPopAdapter.getData().get(position);
                int class_id = classBean.getClass_id();
                modifyClass(class_id, 2);
                popupWindow.dismiss();
            }
        });

        mClassPopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StudentClassBean.DataBean.ClassBean classBean = mClassPopAdapter.getData().get(position);
                int class_id = classBean.getClass_id();
                requestCache.classId = class_id;
                mTitleView.getTitleText().setText(classBean.getClass_name());
                loadIndex(class_id, requestCache.requestDate);
                popupWindow.dismiss();
            }
        });


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
                popupWindow.dismiss();
            }
        });
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

    //先是class的pop
    private void showClassPop() {
        if (popupWindow != null) {
            popupWindow.showAsDropDown(mTitleView);
            darkenBackground(0.6f);
        }
    }

    public static void startStudentAttendanceActivity(Context context) {
        Intent intent = new Intent(context, StudentAttendanceActivity.class);
        context.startActivity(intent);
    }
}
