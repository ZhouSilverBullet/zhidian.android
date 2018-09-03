package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ApproverIndexBean;
import com.sdxxtop.zhidian.entity.ClassNameBean;
import com.sdxxtop.zhidian.entity.MainIndexBean;
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.ImageParams;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.popupwindow.SelectionDateWindow;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：${刘香龙} on 2018/5/4 0004 14:35
 * 类的描述：
 */
public class ChangeShiftActivity extends BaseApproverActivity {
    public static final int OTHER_PEOPLE = 20;
    public static final int MY_PEOPLE = 22;

    @BindView(R.id.horlv_photo)
    RecyclerView horListViewPhoto;
    @BindView(R.id.horlv_approving_officer)
    RecyclerView horApprovOfficer;
    @BindView(R.id.horlv_scribble)
    RecyclerView horHorlvScribble;
    @BindView(R.id.tv_change_start_time)
    TextView tvChangeStartTime;
    @BindView(R.id.tv_change_end_time)
    TextView tvChangeEndTime;
    @BindView(R.id.tv_other_change_start_time)
    TextView tvOtherChangeStartTime;
    @BindView(R.id.tv_other_change_end_time)
    TextView tvOtherChangeEndTime;
    @BindView(R.id.tv_change_shift)
    TextView tvChangeShift;
    @BindView(R.id.tv_my_shift)
    TextView tvMyShift;
    @BindView(R.id.btn_submission)
    Button btnSubmission;
    @BindView(R.id.horlv_approver_text)
    TextView approverText;
    @BindView(R.id.horlv_approver_line)
    View approverLine;
    @BindView(R.id.item_apply_content_content)
    EditText contentEdit;

    @BindView(R.id.tv_change_name_layout)
    LinearLayout nameLayout;
    @BindView(R.id.tv_change_name_text)
    TextView nameText;
    @BindView(R.id.item_apply_content_change_text)
    TextView changeText;

    private String class_id;
    private String class_name;
    private SelectBean selectBean;
    private String changeStartTime;

    @Override
    protected int getActivityView() {
        return R.layout.activity_changeshift;
    }

    @Override
    protected void initView() {
        super.initView();
        approverText.setVisibility(View.GONE);
        approverLine.setVisibility(View.GONE);
        horApprovOfficer.setVisibility(View.GONE);

        String dayOneTime = DateUtil.getDayOneTime();
        tvChangeStartTime.setText(dayOneTime);
        tvChangeEndTime.setText(dayOneTime);
        tvOtherChangeStartTime.setText(dayOneTime);
        tvOtherChangeEndTime.setText(dayOneTime);

        getPeopleClassTime(MY_PEOPLE, "", dayOneTime, dayOneTime);
    }


    @Override
    protected void initChildData(ApproverIndexBean.DataBean data) {

        setPhotoRecycler(horListViewPhoto);

        setCopyRecycler(horHorlvScribble);

        editTextCountControl(R.id.item_apply_content_content, contentEdit, changeText);
        //审批人
//        setApproverRecycler(horApprovOfficer);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        nameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NoticeReciveRangeActivity.class);
                intent.putExtra("isPartSelect", NoticeReciveRangeActivity.PART_NOT_SELECTOR);
                intent.putExtra("singleSelect", NoticeReciveRangeActivity.SINGLE_SELECTOR);
                startActivityForResult(intent, 220);
            }
        });
        tvChangeStartTime.setOnClickListener(this);
        tvChangeEndTime.setOnClickListener(this);
        tvOtherChangeStartTime.setOnClickListener(this);
        tvOtherChangeEndTime.setOnClickListener(this);
        btnSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private String myName = ""; //我的班次
    private String myId = ""; //我的班次id

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 220 && resultCode == NoticeReciveRangeActivity.NOTICE_RESULT_OK && data != null) {
            List<SelectBean> userList = (List<SelectBean>) data.getSerializableExtra("selectData");
            if (userList != null && userList.size() >= 1) {
                selectBean = userList.get(0);
                nameText.setText(selectBean.getName());
                String startTime = tvOtherChangeStartTime.getText().toString();
                String endTime = tvOtherChangeEndTime.getText().toString();
                getPeopleClassTime(OTHER_PEOPLE, selectBean.getId() + "", startTime, endTime);
            }
        }
    }

    //获取其他人的班次
    private void getPeopleClassTime(final int type, String userId, String startTime, String endTime) {
        Long longStartTime = DateUtil.convertTimeToLong(startTime, "yyyy-MM-dd");
        Long longEndTime = DateUtil.convertTimeToLong(endTime, "yyyy-MM-dd");

        if (longStartTime > longEndTime) {
            //开始时间大于结束时间可能是正在切换日期
            return;
        }

        Params params = new Params();
        if (type == OTHER_PEOPLE) {
            params.put("ui", userId);
        }
        params.put("sd", startTime);
        params.put("ed", endTime);
        RequestUtils.createRequest().postClassName(params.getData())
                .enqueue(new RequestCallback<>(new IRequestListener<ClassNameBean>() {
                    @Override
                    public void onSuccess(ClassNameBean classNameBean) {
                        ClassNameBean.DataBean data = classNameBean.getData();
                        if (data != null) {
                            class_id = data.getClass_id();
                            class_name = data.getClass_name();
                            if (type == OTHER_PEOPLE) {
                                tvChangeShift.setText(class_name);
                            } else {
                                myName = class_name;
                                myId = class_id;
                                tvMyShift.setText(class_name);
                            }
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {

                    }
                }));


    }

    private void submit() {
        String changeStartTime = tvChangeStartTime.getText().toString().trim();
        String changeEndTime = tvChangeEndTime.getText().toString().trim();
        String otherChangeStartTime = tvOtherChangeStartTime.getText().toString().trim();
        String otherChangeEndTime = tvOtherChangeEndTime.getText().toString().trim();

        String eidtContent = contentEdit.getText().toString().trim();

        if (TextUtils.isEmpty(changeStartTime) || TextUtils.isEmpty(changeEndTime)) {
            showToast("请填写调班人开始时间或结束时间");
            return;
        }

        if (TextUtils.isEmpty(otherChangeStartTime) || TextUtils.isEmpty(otherChangeEndTime)) {
            showToast("请填写被调班人开始时间或结束时间");
            return;
        }


        Long changeLongStartTime = DateUtil.convertTimeToLong(changeStartTime, "yyyy-MM-dd");
        Long changeLongEndTime = DateUtil.convertTimeToLong(changeEndTime, "yyyy-MM-dd");
        Long otherChangeLongStartTime = DateUtil.convertTimeToLong(otherChangeStartTime, "yyyy-MM-dd");
        Long otherChangeLongEndTime = DateUtil.convertTimeToLong(otherChangeEndTime, "yyyy-MM-dd");

        if (changeLongStartTime < System.currentTimeMillis()) {
            showToast("申请日期必须大于今天");
            return;
        }

        if (otherChangeLongStartTime < System.currentTimeMillis()) {
            showToast("申请日期必须大于今天");
            return;
        }

        if (changeLongEndTime <
                changeLongStartTime) {
            showToast("开始时间不能大于结束时间");
            return;
        }

        if (otherChangeLongEndTime <
                otherChangeLongStartTime) {
            showToast("开始时间不能大于结束时间");
            return;
        }

        long otherCurrentTime = (long) otherChangeLongEndTime - (long) otherChangeLongStartTime;
        long currentTime = (long) changeLongEndTime - (long) changeLongStartTime;
        if (otherCurrentTime != currentTime) {
            showToast("两人调班天数需相等");
            return;
        }

        if (selectBean == null || selectBean.id == null) {
            showToast("请选择调班人员");
            return;
        }

        if (TextUtils.isEmpty(eidtContent)) {
            showToast("请填写调班理由");
            return;
        }

        if (TextUtils.isEmpty(class_id)) {
//            showToast("请选择被调班人");
//            return;
            class_id = "";
        }

        if (data == null) {
            showToast("数据错误");
            return;
        }

        ImageParams params = new ImageParams();
//        params.put("lt", selectorType);
        params.put("rs", eidtContent);

//        params.put("ai", getApproverValue()); //调班不需要审批人
        params.put("si", getCopyValue()); //抄送
//        params.put("st", startTime);
//        params.put("et", endTime);

        params.put("ei", selectBean.id);
        params.put("mci", myId);
        params.put("mcn", myName);
        params.put("msd", changeStartTime);
        params.put("med", changeEndTime);

        params.put("eci", class_id);
        params.put("ecn", class_name);
        params.put("esd", otherChangeStartTime);
        params.put("eed", otherChangeEndTime);


        //设置相片
        params.addImagePathList("img[]", getImagePushPath());

        RequestUtils.createRequest().postApplyLeave("exchange", params.getImgData()).enqueue(new RequestCallback<BaseModel>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                successSkip(baseModel);
//                ToastUtil.show(baseModel.msg);
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                ToastUtil.show(errorMsg);
            }
        }));
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        String changeEndTime;
        String changeStartTime;
        switch (view.getId()) {
            case R.id.tv_change_start_time:
                changeStartTime = tvChangeStartTime.getText().toString();
                showSelectDateWindow(1, getCalendar(changeStartTime));
                break;

            case R.id.tv_change_end_time:
                changeEndTime = tvChangeEndTime.getText().toString();
                showSelectDateWindow(2, getCalendar(changeEndTime));
                break;

            case R.id.tv_other_change_start_time:
                String otherChangeStartTime = tvOtherChangeStartTime.getText().toString();
                showSelectDateWindow(3, getCalendar(otherChangeStartTime));
                break;

            case R.id.tv_other_change_end_time:
                String otherChangeEndTime = tvOtherChangeEndTime.getText().toString();
                showSelectDateWindow(4, getCalendar(otherChangeEndTime));
                break;
//            case R.id.tv_change_shift:
//                List<String> strings = new ArrayList<>();
//                strings.add("春季时间");
//                strings.add("夏季时间");
//                strings.add("秋季时间");
//                strings.add("冬季时间");
//                showSelectShift(strings);
//                break;

            case R.id.btn_submission:

//                Intent intentSubmission = new Intent(this, SubmissionActivity.class);
//                startActivity(intentSubmission);

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<String> strings = new ArrayList<>();
//                        strings.add("调班日期");
//                        strings.add("5月5号-5月6号");
//
//                        EventBus.getDefault().post(new MessageEvent.intentActivity(
//                                "调班",
//                                "调班",
//                                strings,
//                                "处理紧急工作",
//                                photos,
//                                "张三",
//                                "长军",
//                                "洪峰",
//                                "与他调班",
//                                "张三 5月6号-5月7号"));
//                    }
//                }, 800);

                break;
        }
    }

    private Calendar getCalendar(String changeTime) {
        Long changeLongTime = DateUtil.convertTimeToLong(changeTime, "yyyy-MM-dd");
        Date date = new Date(changeLongTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private void showSelectDateWindow(final int textType, Calendar calendar) {
        final SelectionDateWindow selectionDateWindow = new SelectionDateWindow(this, false, at, calendar);
//        selectionDateWindow.setSelectedDate(calendar);
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = ViewUtil.dp2px(mContext, 420);
        selectionDateWindow.setWidth(weight);
        selectionDateWindow.setHeight(height);
        selectionDateWindow.setFocusable(true);
        selectionDateWindow.setTouchable(true);
        selectionDateWindow.setOutsideTouchable(true);
        selectionDateWindow.setAnimationStyle(R.style.AnimationRightBottom);

        selectionDateWindow.setSelectorDateListener(new SelectionDateWindow.SelectorDateListener() {
            @Override
            public void onSelector(String date, List<MainIndexBean.DataBean.SignLogBean> selectorList) {
                if (textType == 1) {
                    tvChangeStartTime.setText(selectionDateWindow.stringDate);
                } else if (textType == 2) {
                    tvChangeEndTime.setText(selectionDateWindow.stringDate);
                } else if (textType == 3) {
                    tvOtherChangeStartTime.setText(selectionDateWindow.stringDate);
                } else if (textType == 4) {
                    tvOtherChangeEndTime.setText(selectionDateWindow.stringDate);
                }
                String changeEndTime;
                if (textType <= 2) {
                    changeStartTime = tvChangeStartTime.getText().toString();
                    changeEndTime = tvChangeEndTime.getText().toString();
                    getPeopleClassTime(MY_PEOPLE, "", changeStartTime, changeEndTime);
                } else {
                    changeStartTime = tvOtherChangeStartTime.getText().toString();
                    changeEndTime = tvOtherChangeEndTime.getText().toString();
                    if (selectBean != null) {
                        getPeopleClassTime(OTHER_PEOPLE, selectBean.getId() + "", changeStartTime, changeEndTime);
                    }
                }
            }
        });
        selectionDateWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);


            }
        });

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        selectionDateWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_main, null), Gravity.BOTTOM, 0, 0);
    }
}
