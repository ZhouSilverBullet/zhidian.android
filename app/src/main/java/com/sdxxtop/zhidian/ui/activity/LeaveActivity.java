package com.sdxxtop.zhidian.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ApproverIndexBean;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.ImageParams;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.utils.ApplyTimePicker;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import cn.addapp.pickers.picker.DateTimePicker;
import cn.qqtheme.framework.picker.SinglePicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * @author lxl
 * @date 2018/4/29  15:15
 * @desc
 */
public class LeaveActivity extends BaseApproverActivity implements View.OnClickListener {

    @BindView(R.id.ll_select_type)
    LinearLayout llSelectType;
    @BindView(R.id.horlv_photo)
    RecyclerView horListViewPhoto;
    @BindView(R.id.horlv_approving_officer)
    RecyclerView horApprovOfficer;
    @BindView(R.id.horlv_scribble)
    RecyclerView horHorlvScribble;
    @BindView(R.id.tv_select_type)
    TextView tvSelectType;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_leave_time)
    TextView tvLeaveTime;
    @BindView(R.id.btn_submission)
    Button btnSubmission;
    @BindView(R.id.leave_title_view)
    SubTitleView titleView;
    @BindView(R.id.item_apply_content_content)
    EditText contentEdit;
    @BindView(R.id.item_apply_content_change_text)
    TextView changeText;
    private SinglePicker<String> singlePicker;

    @Override
    protected int getActivityView() {
        return R.layout.activity_leave;
    }

    @Override
    protected void initView() {
        super.initView();

    }

    @Override
    protected void initChildData(ApproverIndexBean.DataBean data) {
        tvStartTime.setText(data.getStart_time());
        tvEndTime.setText(data.getEnd_time());

        //设置相片选择
        setPhotoRecycler(horListViewPhoto);

        //抄送人
        setCopyRecycler(horHorlvScribble);

        //审批人
        setApproverRecycler(horApprovOfficer);

        editTextCountControl(R.id.item_apply_content_content, contentEdit, changeText);
    }


    @Override
    protected void initEvent() {
        super.initEvent();
        llSelectType.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        btnSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submit();
//                Intent intentSubmission = new Intent(LeaveActivity.this, SubmissionActivity.class);
//                startActivity(intentSubmission);

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<String> strings = new ArrayList<>();
//                        strings.add("请假一天");
//                        strings.add("5月5号-5月6号");
//                        EventBus.getDefault().post(new MessageEvent.intentActivity(
//                                "请假",
//                                "事假",
//                                strings,
//                                "回家玩玩",
//                                photos,
//                                "李贵喜",
//                                "小白",
//                                "长军"));
//                    }
//                },1000);
            }
        });
    }

    private int selectorType = -1;

    private void submit() {
        String startTime = tvStartTime.getText().toString();
        String endTime = tvEndTime.getText().toString();
        String eidtContent = contentEdit.getText().toString().trim();


        if (selectorType == -1) {
            showToast("请选择请假类型");
            return;
        }

        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            showToast(getString(R.string.toast_apply_start_end_time));
            return;
        }

        long startLongTime = DateUtil.convertTimeToLong(startTime, "yyyy-MM-dd HH:mm");
        long endLongTime = DateUtil.convertTimeToLong(endTime, "yyyy-MM-dd HH:mm");

        if (startLongTime >= endLongTime) {
            showToast("结束时间必须大于开始时间");
            return;
        }

        if (endLongTime - startLongTime < 60000) {
            showToast("结束时间必须大于开始时间一分钟");
            return;
        }


        if (TextUtils.isEmpty(eidtContent)) {
            showToast("请填写请假理由");
            return;
        }


        if (data == null) {
            showToast("数据错误");
            return;
        }

        ImageParams params = new ImageParams();
        params.put("lt", selectorType);
        params.put("rs", eidtContent);
        params.put("ai", getApproverValue()); //获取审批人
        params.put("si", getCopyValue()); //抄送
        params.put("st", startTime);
        params.put("et", endTime);

        //设置相片
        params.addImagePathList("img[]", getImagePushPath());

        RequestUtils.createRequest().postApplyLeave("leave", params.getImgData()).enqueue(new RequestCallback<BaseModel>(new IRequestListener<BaseModel>() {
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
            case R.id.ll_select_type:
                onConstellationPicker();
                break;

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
        }
    }


    public void onConstellationPicker() {
        if (singlePicker == null) {
            singlePicker = new SinglePicker<>(this, new String[]{
                    "事假", "病假", "年假", "学习", "婚假", "产检假", "产假", "陪产假", "哺乳假", "工伤假", "丧假", "探亲假", "其他"});
            singlePicker.setTopBackgroundColor(0xFFEEEEEE);
            singlePicker.setTopHeight(52);
            singlePicker.setTopLineColor(0xFF33B5E5);
            singlePicker.setTopLineHeight(1);
            singlePicker.setTitleText("");
            singlePicker.setTitleTextColor(0xFF999999);
            singlePicker.setTitleTextSize(18);
            singlePicker.setCancelTextColor(0xFF999999);
            singlePicker.setCancelTextSize(18);
            singlePicker.setSubmitTextColor(0xFF33B5E5);
            singlePicker.setSubmitTextSize(18);
            WheelView.LineConfig config = new WheelView.LineConfig();
            config.setColor(0xFFD3D3D3);//线颜色
            config.setAlpha(120);//线透明度
//        config.setRatio(1);//线比率
            singlePicker.setLineConfig(config);
            singlePicker.setItemWidth(500);
            singlePicker.setTextSize(23);
            singlePicker.setBackgroundColor(0xFFffffff);
            //picker.setSelectedItem(isChinese ? "处女座" : "Virgo");
            singlePicker.setSelectedIndex(0);

            singlePicker.setOnItemPickListener(new SinglePicker.OnItemPickListener<String>() {
                @Override
                public void onItemPicked(int index, String item) {
                    selectorType = index + 1;
                    tvSelectType.setText(item);
                }
            });
        }

        singlePicker.show();

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
