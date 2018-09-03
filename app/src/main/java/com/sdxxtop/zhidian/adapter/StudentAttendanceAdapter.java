package com.sdxxtop.zhidian.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.StudentAttendanceBean;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.Arrays;
import java.util.List;

import zhangphil.iosdialog.widget.AlertDialog;

/**
 * Created by Administrator on 2018/7/23.
 */

public class StudentAttendanceAdapter extends BaseQuickAdapter<StudentAttendanceBean.DataBean.SignBean, BaseViewHolder> {
    public static final int SOLID_BLUE = 10;
    public static final int STROK_BLUE = 11;
    public static final int SOLID_GRAY = 12;


    //1:确定到校 2:通知家长 3:未到
    public static final int TYPE_CONFIRM_SCHOOL = 1;
    public static final int TYPE_NOTICE_PARENT = 2;
    public static final int TYPE_NOT_ARRIVE = 3;
    private String timeDate;


    public StudentAttendanceAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, StudentAttendanceBean.DataBean.SignBean item) {
        TextView nameText = helper.getView(R.id.item_student_atten_name);
        RecyclerView arriveSchoolRecyclerView = helper.getView(R.id.item_student_atten_get_school);
        RecyclerView leaveSchoolRecycler = helper.getView(R.id.item_student_atten_out_school);
        TextView modifyText = helper.getView(R.id.item_student_atten_modify);

        LinearLayout recyclerLayout = helper.getView(R.id.item_student_atten_recycler_layout);
        TextView isArriveText = helper.getView(R.id.tem_student_atten_is_arrive);

        LinearLayout modifyLayout = helper.getView(R.id.item_student_atten_operation_layout);
        TextView confirmSchool = helper.getView(R.id.item_student_atten_operation_confirm_school);
        TextView modifyParentText = helper.getView(R.id.item_student_atten_operation_notify_parent);

        final String studentName = item.getStudent_name();
        int isNotice = item.getIs_notice();
        int signId = item.getSign_id();
        int isArrive = item.getIs_arrive();
        String signArrive = item.getSign_arrive();
        String signLeave = item.getSign_leave();
        final int studentId = item.getStudent_id();

        nameText.setText(studentName);

        //3为请假
        if (isArrive == 3) {
            modifyText.setVisibility(View.GONE);
            modifyLayout.setVisibility(View.GONE);

            recyclerLayout.setVisibility(View.GONE);
            isArriveText.setVisibility(View.VISIBLE);
            isArriveText.setTextColor(getColor(R.color.lan));
            isArriveText.setText("请假");
        } else {
            //是否通知家长  1 已通知
            final boolean isNoticeParent = isNotice == 1;
            //到校时间为空，认为是未到校，需要通知家长
            boolean isArriveSchool = !TextUtils.isEmpty(signArrive);
            if (isArriveSchool) {
                //已经到达
                recyclerLayout.setVisibility(View.VISIBLE);
                isArriveText.setVisibility(View.GONE);
                modifyLayout.setVisibility(View.GONE);
                modifyText.setVisibility(View.VISIBLE);
                modifyText.setText("修改");
                modifyText.setTextColor(getColor(R.color.texthintcolor));
                modifyText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(studentName, studentId);
                    }
                });
            } else {
                recyclerLayout.setVisibility(View.GONE);
                isArriveText.setVisibility(View.VISIBLE);
                isArriveText.setTextColor(getColor(R.color.dot_red));
                isArriveText.setText("未到");

                modifyLayout.setVisibility(View.VISIBLE);
                modifyText.setVisibility(View.GONE);

                if (isNoticeParent) {
                    confirmSchool.setBackgroundResource(getStudentBg(SOLID_BLUE));
                    modifyParentText.setTextColor(getColor(R.color.texthintcolor));
                    modifyParentText.setBackgroundResource(getStudentBg(SOLID_GRAY));
                    modifyParentText.setText("已通知家长");
                } else {
                    confirmSchool.setBackgroundResource(getStudentBg(SOLID_BLUE));
                    modifyParentText.setTextColor(getColor(R.color.lan));
                    modifyParentText.setBackgroundResource(getStudentBg(STROK_BLUE));
                    modifyParentText.setText("通知家长");
                }

                confirmSchool.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmSchool(TYPE_CONFIRM_SCHOOL, studentId, timeDate);
                    }
                });

                modifyParentText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isNoticeParent) { //未通知的情况下通知
                            confirmSchool(TYPE_NOTICE_PARENT, studentId, timeDate);
                        }
                    }
                });

            }

            if (isArriveSchool) {
                String[] split = signArrive.split(",");
                List<String> stringList = Arrays.asList(split);
                arriveSchoolRecyclerView.setAdapter(new TextAdapter(R.layout.item_student_attendance_text_recycler, stringList));
                arriveSchoolRecyclerView.setVisibility(View.VISIBLE);
            } else {
                arriveSchoolRecyclerView.setVisibility(View.GONE);
            }
            //离校
            if (!TextUtils.isEmpty(signLeave)) {
                String[] split = signLeave.split(",");
                List<String> stringList = Arrays.asList(split);
                leaveSchoolRecycler.setAdapter(new TextAdapter(R.layout.item_student_attendance_text_recycler, stringList));
                leaveSchoolRecycler.setVisibility(View.VISIBLE);
            } else {
                leaveSchoolRecycler.setVisibility(View.GONE);
            }
        }
    }

    private void showDialog(String studentName, final int studentId) {
        new AlertDialog(mContext)
                .builder()
                .setTitle(studentName + "出勤")
                .setMsg("您确定该学生没有到校吗？")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmSchool(TYPE_NOT_ARRIVE, studentId, timeDate);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    private int getColor(int color) {
        return mContext.getResources().getColor(color);
    }

    private int getStudentBg(int type) {
        int drawable;
        switch (type) {
            case SOLID_BLUE:
                drawable = R.drawable.item_student_solid_blue_bg;
                break;
            case STROK_BLUE:
                drawable = R.drawable.item_student_stroke_blue_bg;
                break;
            default:
                drawable = R.drawable.item_student_solid_gray_bg;
                break;
        }
        return drawable;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }

    class TextAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public TextAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            TextView timeText = helper.getView(R.id.item_student_atten_time_text);
            if (!TextUtils.isEmpty(item) && item.length() > 15) {
                //2018-07-21 10:00:00
                item = item.substring(11, 16);
            }
            timeText.setText(item);
        }
    }

    private void confirmSchool(final int type, int studentId, String signDate) {
        Params params = new Params();
        //1:确定到校 2:通知家长 3:未到
        params.put("tp", type);
        params.put("si", studentId);
        params.put("sd", signDate);

        RequestUtils.createRequest().postStudentModify(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                if (type == 2) {
                    ToastUtil.show("通知成功");
                }
                if (confirmRequestSuccessCallback != null) {
                    confirmRequestSuccessCallback.onSuccess(type);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                ToastUtil.show(errorMsg);
            }
        }));
    }

    private ConfirmRequestSuccessCallback confirmRequestSuccessCallback;

    public void setConfirmRequestSuccessCallback(ConfirmRequestSuccessCallback confirmRequestSuccessCallback) {
        this.confirmRequestSuccessCallback = confirmRequestSuccessCallback;
    }

    public interface ConfirmRequestSuccessCallback {
        void onSuccess(int type);
    }
}
