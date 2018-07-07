package com.sdxxtop.zhidian.ui.activity;

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
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.ImageParams;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.popupwindow.SelectTypeWindow;
import com.sdxxtop.zhidian.utils.ApplyTimePicker;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.addapp.pickers.picker.DateTimePicker;

/**
 * 作者：${刘香龙} on 2018/5/3 0003 19:46
 * 类的描述：
 */
public class OvertimeActivity extends BaseApproverActivity {

    @BindView(R.id.horlv_photo)
    RecyclerView horListViewPhoto;
    @BindView(R.id.horlv_approving_officer)
    RecyclerView horApprovOfficer;
    @BindView(R.id.horlv_scribble)
    RecyclerView horHorlvScribble;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_leave_time)
    TextView tvLeaveTime;
    @BindView(R.id.ll_select_type)
    LinearLayout llSelecType;
    @BindView(R.id.tv_select_type)
    TextView tvSelectType;
    @BindView(R.id.ll_select_place)
    LinearLayout llSelectPlace;
    @BindView(R.id.tv_select_place)
    TextView tvSelectPlace;
    @BindView(R.id.btn_submission)
    Button btnSubmission;
    @BindView(R.id.item_apply_content_content)
    EditText contentEdit;
    @BindView(R.id.item_apply_content_change_text)
    TextView changeText;

    private List<String> overTypeList;
    private List<String> overPlaceList;
    private SelectTypeWindow selectTypeWindow;

    @Override
    protected int getActivityView() {
        return R.layout.activity_overtime;
    }

    @Override
    protected void initView() {

        overTypeList = new ArrayList<>();
        overTypeList.add("正常加班");
        overTypeList.add("预加班");

        overPlaceList = new ArrayList<>();
        overPlaceList.add("在家加班");
        overPlaceList.add("出差加班");
        overPlaceList.add("外出加班");
        overPlaceList.add("办公室加班");

    }

    @Override
    protected void initChildData(ApproverIndexBean.DataBean data) {
        tvStartTime.setText(data.getStart_time());
        tvEndTime.setText(data.getEnd_time());

        setPhotoRecycler(horListViewPhoto);

        setCopyRecycler(horHorlvScribble);

        //审批人
        setApproverRecycler(horApprovOfficer);

        editTextCountControl(R.id.item_apply_content_content, contentEdit, changeText);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        llSelecType.setOnClickListener(this);
        llSelectPlace.setOnClickListener(this);
        btnSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
//                Intent intentSubmission = new Intent(this, SubmissionActivity.class);
//                startActivity(intentSubmission);
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<String> strings = new ArrayList<>();
//                        strings.add(tvSelectType.getText().toString() + "5小时");
//                        strings.add("5月5号（9:00-14:00）");
//
//                        EventBus.getDefault().post(new MessageEvent.intentActivity(
//                                "加班",
//                                "加班",
//                                strings,
//                                "处理紧急工作",
//                                photos,
//                                "张三",
//                                "长军",
//                                "洪峰",
//                                "加班地点",
//                                tvSelectPlace.getText().toString()));
//                    }
//                }, 800);
            }
        });

//        horListViewPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.i("", "onItemClickposite: " + position);
//                if (position == photos.size() - 1) {
//                    showPopueWindow();
//                }
//            }
//        });
    }

    private void submit() {
        String startTime = tvStartTime.getText().toString();
        String endTime = tvEndTime.getText().toString();
        String eidtContent = contentEdit.getText().toString();

        String selectTypeValue = tvSelectType.getText().toString();
        String tvSelectPlaceValue = tvSelectPlace.getText().toString();

        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            showToast(getString(R.string.toast_apply_start_end_time));
            return;
        }

        if (TextUtils.isEmpty(eidtContent)) {
            showToast("请填写加班理由");
            return;
        }


        if (data == null) {
            showToast("数据错误");
            return;
        }

        ImageParams params = new ImageParams();
        params.put("rs", eidtContent);
        params.put("tp", overTypeList.indexOf(selectTypeValue) + 1);
        params.put("pt", overPlaceList.indexOf(tvSelectPlaceValue) + 1);
        String ai = "";
        List<ApproverIndexBean.DataBean.ApproverBean> approver = data.getApprover();
        if (approver != null) {
            for (int i = 0; i < approver.size(); i++) {
                if (i == approver.size() - 1) {
                    ai = ai + approver.get(i).getUserid();
                } else {
                    ai = ai + approver.get(i).getUserid() + ",";
                }
            }
        }

        params.put("ai", ai);
        params.put("si", getCopyValue()); //抄送
        params.put("st", startTime);
        params.put("et", endTime);

        params.addImagePathList("img[]", getImagePushPath());

        RequestUtils.createRequest().postApplyLeave("overtime", params.getImgData()).enqueue(new RequestCallback<BaseModel>(new IRequestListener<BaseModel>() {
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
        Calendar instance = Calendar.getInstance();
        switch (view.getId()) {

            case R.id.tv_start_time:
                String startTime = tvStartTime.getText().toString();
                if (!TextUtils.isEmpty(startTime)) {
                    long startLongTime = DateUtil.convertTimeToLong(startTime, "yyyy-MM-dd HH:mm");
                    Date date = new Date(startLongTime);
                    instance.setTime(date);
                }
                onYearMonthDayTimePicker(1, instance);
                break;

            case R.id.tv_end_time:
                String endTime = tvEndTime.getText().toString();
                if (!TextUtils.isEmpty(endTime)) {
                    long endLongTime = DateUtil.convertTimeToLong(endTime, "yyyy-MM-dd HH:mm");
                    Date date = new Date(endLongTime);
                    instance.setTime(date);
                }
                onYearMonthDayTimePicker(2, instance);
                break;

            case R.id.ll_select_type:
                showSelectType(overTypeList);
                break;

            case R.id.ll_select_place:
                showSelectType(overPlaceList);
                break;

        }
    }

    private void showSelectType(final List<String> strings) {

        final SelectTypeWindow selectTypeWindow = new SelectTypeWindow(this, strings);
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = (int) (getResources().getDimension(R.dimen.y145) + getResources().getDimension(R.dimen.y100) * strings.size());
        selectTypeWindow.setWidth(weight);
        selectTypeWindow.setHeight(height);
        selectTypeWindow.setFocusable(true);
        selectTypeWindow.setTouchable(true);
        selectTypeWindow.setOutsideTouchable(true);
        selectTypeWindow.setAnimationStyle(R.style.AnimationRightBottom);

        selectTypeWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
                if (strings.size() == 2 && !selectTypeWindow.stringType.equals("")) {
                    tvSelectType.setText(selectTypeWindow.stringType);
                } else if (strings.size() == 4 && !selectTypeWindow.stringType.equals("")) {
                    tvSelectPlace.setText(selectTypeWindow.stringType);
                }
            }
        });

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        selectTypeWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_main, null), Gravity.BOTTOM, 0, 0);
    }

    public void onYearMonthDayTimePicker(final int type, Calendar calendar) {
        ApplyTimePicker.timePicker(mContext, calendar, new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                if (type == 1) {
                    tvStartTime.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
                } else if (type == 2) {
                    tvEndTime.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
                    tvLeaveTime.setText(getidmt(tvStartTime.getText().toString(), tvEndTime.getText().toString()));
                }
            }
        });
    }


    public String getidmt(String startTime, String endTiem) {
        long days = 0, hours = 0, minutes = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date d1 = df.parse(endTiem);
            Date d2 = df.parse(startTime);
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            days = diff / (1000 * 60 * 60 * 24);
            hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            System.out.println("（请假时间共" + days + "天" + hours + "小时" + minutes + "分）");

        } catch (Exception e) {
        }
        return "（请假时间共" + days + "天" + hours + "小时" + minutes + "分）";
    }
}
