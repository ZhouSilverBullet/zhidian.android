package com.sdxxtop.zhidian.ui.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.UcenterNoticeIndexBean;
import com.sdxxtop.zhidian.entity.UcenterNoticeModifyBean;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.addapp.pickers.picker.TimePicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：CaiCM
 * 日期：2018/4/18  时间：11:39
 * 邮箱：15010104100@163.com
 * 描述：消息提醒界面
 */
public class MessageNoticeActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.cb_message_notice)
    CheckBox cbMessageNotice;
    @BindView(R.id.cb_rejection_notice)
    CheckBox cbRejectionNotice;
    @BindView(R.id.tv_stat_time_show)
    TextView tvStatTimeShow;
    @BindView(R.id.rl_start_time)
    RelativeLayout rlStartTime;
    @BindView(R.id.tv_end_time_show)
    TextView tvEndTimeShow;
    @BindView(R.id.rl_end_time)
    RelativeLayout rlEndTime;

    private String noticeIsChecked;
    private String rejectionIsChecked;
    private String noticeTag;
    private String rejectionTag;
    private TimePicker picker;

    @Override
    protected int getActivityView() {
        return R.layout.activity_message_notice;
    }

    @Override
    protected void initView() {
//        String noticeIsChecked = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.NOTICEISCHECKED);
//        cbMessageNotice.setChecked(noticeIsChecked.equals("false") ? false : true);
//        String rejectionIsChecked = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.REJECTIONISCHECKED);
//        cbRejectionNotice.setChecked(rejectionIsChecked.equals("true") ? true : false);
//        rlStartTime.setVisibility(rejectionIsChecked.equals("true") ? View.VISIBLE : View.INVISIBLE);
//        rlEndTime.setVisibility(rejectionIsChecked.equals("true") ? View.VISIBLE : View.INVISIBLE);
//        String begin_time = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.BEGIN_TIME);
//        tvStatTimeShow.setText(begin_time.equals("") ? "23:00" : begin_time);
//        String end_time = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.END_TIME);
//        tvEndTimeShow.setText(end_time.equals("") ? "06:00" : end_time);
    }

    @Override
    protected void initData() {
        postUcenterNoticeIndex();
    }

    @Override
    protected void initEvent() {
        cbMessageNotice.setOnCheckedChangeListener(this);
        cbRejectionNotice.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.tv_stat_time_show, R.id.tv_end_time_show})
    public void onViewClicked(View view) {
        Calendar instance = Calendar.getInstance();
        switch (view.getId()) {
            case R.id.tv_stat_time_show:
                String startTimeShow = tvStatTimeShow.getText().toString().trim();
                if (!TextUtils.isEmpty(startTimeShow)) {
                    Long aLong = DateUtil.convertTimeToLong(startTimeShow, "HH:mm");
                    instance.setTime(new Date(aLong));
                }
                onStartTimePicker(1, instance);
                break;
            case R.id.tv_end_time_show:
                String endTimeShow = tvEndTimeShow.getText().toString().trim();
                if (!TextUtils.isEmpty(endTimeShow)) {
                    Long aLong = DateUtil.convertTimeToLong(endTimeShow, "HH:mm");
                    instance.setTime(new Date(aLong));
                }
                onStartTimePicker(2, instance);
                break;
        }
    }

    /**
     * 开始时间选择
     */
    public void onStartTimePicker(final int type, Calendar calendar) {
        picker = new TimePicker(mContext, TimePicker.HOUR_24);
        picker.setLabel(":", "");
//        picker.setCycleDisable(false);

        picker.setRangeStart(0, 0);//00:00
        picker.setRangeEnd(23, 59);//23:59
        picker.setSelectedItem(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        picker.setTopLineVisible(false);
        picker.setTextSize(20);
        picker.setTitleText("请选择时间");
//        picker.setCanceledOnTouchOutside(true);
//        picker.setTextColor(Color.rgb(50, 150, 250));
//        picker.setDividerColor(Color.rgb(153, 153, 153));
        picker.setCancelTextColor(Color.rgb(153, 153, 153));
        picker.setSubmitTextColor(Color.rgb(50, 150, 250));
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                if (type == 1) {
                    String time = hour + ":" + minute;
                    tvStatTimeShow.setText(time);
                    postUcenterNoticeModifyNoticeRejection();
                } else {
                    String time = hour + ":" + minute;
                    tvEndTimeShow.setText(time);
                    postUcenterNoticeModifyNoticeRejection();
                }
            }
        });
        picker.show();
    }


    /**
     * 结束时间选择
     */
    public void onEndTimePicker() {
        TimePicker picker = new TimePicker(mContext, TimePicker.HOUR_24);
//        picker.setCycleDisable(false);
        picker.setRangeStart(0, 0);//00:00
        picker.setRangeEnd(23, 59);//23:59
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
        picker.setSelectedItem(currentHour, currentMinute);
        picker.setTopLineVisible(false);
        picker.setTextSize(20);
        picker.setTitleText("请选择时间");
//        picker.setCanceledOnTouchOutside(true);
//        picker.setTextColor(Color.rgb(50, 150, 250));
//        picker.setDividerColor(Color.rgb(153, 153, 153));
        picker.setCancelTextColor(Color.rgb(153, 153, 153));
        picker.setSubmitTextColor(Color.rgb(50, 150, 250));
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                String time = hour + ":" + minute;
                tvEndTimeShow.setText(time);
                postUcenterNoticeModifyNoticeRejection();
            }

        });
        picker.show();
    }


    /**
     * 信息通知主页网络请求
     */
    private void postUcenterNoticeIndex() {
        Map<String, String> map = new HashMap<>();
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postUcenterNoticeIndex(base64Data).enqueue(new Callback<UcenterNoticeIndexBean>() {
            @Override
            public void onResponse(Call<UcenterNoticeIndexBean> call, Response<UcenterNoticeIndexBean> response) {
                closeProgressDialog();
                UcenterNoticeIndexBean ucenterNoticeIndexBean = response.body();
                if (ucenterNoticeIndexBean.getCode() == 200) {
                    String is_push = ucenterNoticeIndexBean.getData().getIs_push() + "";
                    String is_disturb = ucenterNoticeIndexBean.getData().getIs_disturb() + "";
                    String begin_time = ucenterNoticeIndexBean.getData().getBegin_time();
                    String end_time = ucenterNoticeIndexBean.getData().getEnd_time();
                    cbMessageNotice.setChecked(is_push.equals("1") ? true : false);
                    cbRejectionNotice.setChecked(is_disturb.equals("1") ? true : false);
//                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.NOTICEISCHECKED, is_push.equals("1") ? "true" : "false");
//                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.REJECTIONISCHECKED, is_push.equals("1") ? "true" : "false");
                    tvStatTimeShow.setText(begin_time);
                    tvEndTimeShow.setText(end_time);
//                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.BEGIN_TIME, begin_time);
//                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.END_TIME, end_time);
                } else {
                    ToastUtil.show(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<UcenterNoticeIndexBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_message_notice:
                if (isChecked) {
//                    noticeIsChecked = "true";
                    noticeTag = "1";
                } else {
//                    noticeIsChecked = "false";
                    noticeTag = "2";
                }
                postUcenterNoticeModifyNotice();
                break;
            case R.id.cb_rejection_notice:
                if (isChecked) {
//                    rejectionIsChecked = "true";
                    rejectionTag = "1";
                    rlStartTime.setVisibility(View.VISIBLE);
                    rlEndTime.setVisibility(View.VISIBLE);
                } else {
//                    rejectionIsChecked = "false";
                    rejectionTag = "2";
                    rlStartTime.setVisibility(View.INVISIBLE);
                    rlEndTime.setVisibility(View.INVISIBLE);
                }
                postUcenterNoticeModifyNoticeRejection();
                break;
            default:
                break;
        }
    }

    /**
     * 修改信息通知网络请求（消息提醒）
     */
    private void postUcenterNoticeModifyNotice() {
//        String id = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.REJECTIONISCHECKED);
//        rejectionTag = (id.equals("true") ? "1" : "2");
        String id = "";
        id = cbRejectionNotice.isChecked() ? "1" : "2";
        Map<String, String> map = new HashMap<>();
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        map.put("ip", noticeTag);
        map.put("id", id);
        map.put("bt", tvStatTimeShow.getText().toString());
        map.put("et", tvEndTimeShow.getText().toString());
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postUcenterNoticeModify(base64Data).enqueue(new Callback<UcenterNoticeModifyBean>() {
            @Override
            public void onResponse(Call<UcenterNoticeModifyBean> call, Response<UcenterNoticeModifyBean> response) {
                closeProgressDialog();
                UcenterNoticeModifyBean ucenterNoticeModifyBean = response.body();
                if (ucenterNoticeModifyBean.getCode() == 200) {
//                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.NOTICEISCHECKED, noticeIsChecked);
//                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.REJECTIONISCHECKED, rejectionIsChecked);
                } else {
                    ToastUtil.show(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<UcenterNoticeModifyBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }

    /**
     * 修改信息通知网络请求（勿扰）
     */
    private void postUcenterNoticeModifyNoticeRejection() {
//        String id = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.NOTICEISCHECKED);
//        noticeTag = (id.equals("true") ? "1" : "2");
        String ip = "";
        ip = cbMessageNotice.isChecked() ? "1" : "2";
        Map<String, String> map = new HashMap<>();
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        map.put("ip", ip);
        map.put("id", rejectionTag);
        map.put("bt", tvStatTimeShow.getText().toString());
        map.put("et", tvEndTimeShow.getText().toString());
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postUcenterNoticeModify(base64Data).enqueue(new Callback<UcenterNoticeModifyBean>() {
            @Override
            public void onResponse(Call<UcenterNoticeModifyBean> call, Response<UcenterNoticeModifyBean> response) {
                closeProgressDialog();
                UcenterNoticeModifyBean ucenterNoticeModifyBean = response.body();
                if (ucenterNoticeModifyBean.getCode() == 200) {
//                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.NOTICEISCHECKED, noticeIsChecked);
//                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.REJECTIONISCHECKED, rejectionIsChecked);

                } else {
                    ToastUtil.show(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<UcenterNoticeModifyBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }

}
