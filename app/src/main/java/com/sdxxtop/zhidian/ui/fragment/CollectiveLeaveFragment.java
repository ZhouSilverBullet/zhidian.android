package com.sdxxtop.zhidian.ui.fragment;

import android.os.Bundle;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import cn.addapp.pickers.picker.DateTimePicker;
import cn.qqtheme.framework.picker.SinglePicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 作者：${刘香龙} on 2018/5/4 0004 12:24
 * 类的描述：
 */
public class CollectiveLeaveFragment extends BaseApproverFragment {

    @BindView(R.id.ll_select_type)
    LinearLayout llSelectType;
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
    @BindView(R.id.hor_collectives)
    RecyclerView horCollectives;
    @BindView(R.id.btn_submission)
    Button btnSubmission;
    @BindView(R.id.item_apply_content_content)
    EditText contentEdit;
    @BindView(R.id.ll_collect_leave_layout)
    LinearLayout busiLayout;
    @BindView(R.id.tv_select_type)
    TextView tvSelectType;
    @BindView(R.id.item_apply_content_change_text)
    TextView changeText;
    private DateTimePicker picker;
    private SinglePicker<String> singlePicker;


    public static CollectiveLeaveFragment newInstance(int at) {

        Bundle args = new Bundle();
        args.putInt("at", at);
        CollectiveLeaveFragment fragment = new CollectiveLeaveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_collectiveleave;
    }


    @Override
    protected void initChildData(ApproverIndexBean.DataBean data) {
        tvStartTime.setText(data.getStart_time());
        tvEndTime.setText(data.getEnd_time());

//        ViewGroup.LayoutParams params = horCollectives.getLayoutParams();
//
//        params.width = (int) getResources().getDimension(R.dimen.x83) * collectives.size();
//        params.height = (int) getResources().getDimension(R.dimen.y73);
//        horCollectives.setLayoutParams(params);

        //设置相片选择
        setPhotoRecycler(horListViewPhoto);

        //抄送人
        setCopyRecycler(horHorlvScribble);

        //审批人
        setApproverRecycler(horApprovOfficer);

        setCollectRecycler(horCollectives);

        editTextCountControl(R.id.item_apply_content_content, contentEdit, changeText);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        btnSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        llSelectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConstellationPicker();
            }
        });

        busiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectSkip();
            }
        });
    }

    int selectorType = -1;

    private void submit() {
        String startTime = tvStartTime.getText().toString();
        String endTime = tvEndTime.getText().toString();
        String eidtContent = contentEdit.getText().toString();

        if (selectorType == -1) {
            showToast("请选择请假类型");
            return;
        }

        String collectValue = getCollectValue();
        if (TextUtils.isEmpty(collectValue)) {
            showToast("请选择请假人员");
            return;
        }

        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            showToast(getString(R.string.toast_apply_start_end_time));
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
        params.put("gui", collectValue);
        params.put("gpi", getPartValue());
        params.put("at", at);

        //设置相片
        params.addImagePathList("img[]", getImagePushPath());

        RequestUtils.createRequest().postApplyLeave("group", params.getImgData()).enqueue(new RequestCallback<BaseModel>(new IRequestListener<BaseModel>() {
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


//                Intent intentSubmission = new Intent(getActivity(), SubmissionActivity.class);
//                startActivity(intentSubmission);

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<String> strings = new ArrayList<>();
//                        strings.add("出差1天");
//                        strings.add("5月5号-5月6号");
//
//                        EventBus.getDefault().post(new MessageEvent.intentActivity(
//                                "集体出差",
//                                "出差",
//                                strings,
//                                "组团出差",
//                                photos,
//                                "张三",
//                                "长军",
//                                "洪峰",
//                                "出差人员",
//                                "刘长军，小红，雄安，小倩"));
//                    }
//                }, 800);


        }
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

    public void onConstellationPicker() {
        if (singlePicker == null) {
            singlePicker = new SinglePicker<>(mContext, new String[]{
                    "事假", "病假", "年假", "学习", "婚假", "产检假", "产假", "陪产假", "哺乳假", "工伤假", "丧假", "探亲假", "其他"});
            singlePicker.setTopBackgroundColor(0xFFEEEEEE);
//        picker.setHeight(801);
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
        /*picker.setSelectedTextColor(0xFF333333);
        picker.setUnSelectedTextColor(0xFF999999);
        picker.setWheelModeEnable(false);*/
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
}
