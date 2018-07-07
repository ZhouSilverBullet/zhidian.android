package com.sdxxtop.zhidian.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.CreateCompanyBean;
import com.sdxxtop.zhidian.entity.CreateCompanyData;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.activity.CreateCompanyActivity;
import com.sdxxtop.zhidian.ui.activity.CreateCompanySuccActivity;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.TimePicker;
import cn.qqtheme.framework.widget.WheelView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：CaiCM
 * 日期：2018/3/23  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：创建公司的第三个Fragment
 */
public class CreateCompThirdFragment extends BaseFragment {

    @BindView(R.id.et_class_name)
    EditText etClassName;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_sign_time)
    TextView tvSignTime;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_sign_time_show)
    TextView tvSignTimeShow;
    @BindView(R.id.tv_start_time_show)
    TextView tvStartTimeShow;
    @BindView(R.id.tv_end_time_show)
    TextView tvEndTimeShow;

    private CreateCompanyData companyData;
    private String time;
    private List<TimeBean> timeBeanList;
    private List<String> timeStringList;
    private TimePicker picker;
    private TimePicker endPicker;
    private OptionPicker optionPicker;

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_create_comp_third;
    }

    class TimeBean {
        public TimeBean(String time, int minute) {
            this.time = time;
            this.minute = minute;
        }

        String time;
        int minute;
    }

    private int defaultTime = 60; //上班默认允许打开时间为1小时

    @Override
    protected void initData() {
        timeBeanList = new ArrayList<>();
        timeBeanList.add(new TimeBean("15分钟", 15));
        timeBeanList.add(new TimeBean("30分钟", 30));
        timeBeanList.add(new TimeBean("1小时", 60));
        timeBeanList.add(new TimeBean("2小时", 120));
        timeBeanList.add(new TimeBean("3小时", 180));
        timeBeanList.add(new TimeBean("4小时", 240));
//        timeBeanList.add(new TimeBean("6小时", 360));
//        timeBeanList.add(new TimeBean("10小时", 600));

        timeStringList = new ArrayList<>();
        for (TimeBean timeBean : timeBeanList) {
            timeStringList.add(timeBean.time);
        }
    }


    @OnClick({R.id.tv_start_time, R.id.tv_end_time, R.id.tv_sign_time, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start_time:
                onStartTimePicker();
                break;
            case R.id.tv_end_time:
                onEndTimePicker();
                break;
            case R.id.tv_sign_time:
                onOptionPicker();
                break;
            case R.id.btn_next:

                if (etClassName.getText().toString().length() == 0) {
                    ToastUtil.show("班次名称不能为空");
                    return;
                }
                if (tvStartTime.getText().toString().length() == 0) {
                    ToastUtil.show("上班时间不能为空");
                    return;
                }
                if (tvEndTime.getText().toString().length() == 0) {
                    ToastUtil.show("下班时间不能为空");
                    return;
                }
                if (tvSignTimeShow.getText().toString().length() == 0) {
                    ToastUtil.show("上下班允许打卡时间不能为空");
                    return;
                }

                companyData = ((CreateCompanyActivity) getActivity()).companyData;
                companyData.cln = etClassName.getText().toString();
                companyData.st = tvStartTimeShow.getText().toString();
                companyData.et = tvEndTimeShow.getText().toString();
                companyData.sgt = defaultTime + "";

                postCreateCompany();
                ((CreateCompanyActivity) getActivity()).companyData.count = 4;
                break;
        }
    }

    /**
     * 网络请求创建公司
     */
    private void postCreateCompany() {
        Map<String, String> map = new HashMap<>();
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        map.put("cn", StringUtil.stringNotNull(companyData.cn));
        map.put("stn", StringUtil.stringNotNull(companyData.stn));
        map.put("ar", StringUtil.stringNotNull(companyData.ar));
        map.put("ad", StringUtil.stringNotNull(companyData.ad));
        map.put("pn", StringUtil.stringNotNull(companyData.pn));
        map.put("lt", StringUtil.stringNotNull(companyData.lt));
        map.put("wn", StringUtil.stringNotNull(companyData.wn));
        map.put("bi", StringUtil.stringNotNull(companyData.bi));
        map.put("gn", StringUtil.stringNotNull(companyData.gn));
        map.put("sr", StringUtil.stringNotNull(companyData.sr));
        map.put("slt", StringUtil.stringNotNull(companyData.slt));
        map.put("sa", StringUtil.stringNotNull(companyData.sa));
        map.put("cln", StringUtil.stringNotNull(companyData.cln));
        map.put("st", StringUtil.stringNotNull(companyData.st));
        map.put("et", StringUtil.stringNotNull(companyData.et));
        map.put("sgt", StringUtil.stringNotNull(companyData.sgt));
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postCreateCompany(base64Data).enqueue(new Callback<CreateCompanyBean>() {
            @Override
            public void onResponse(Call<CreateCompanyBean> call, Response<CreateCompanyBean> response) {
                closeProgressDialog();
                CreateCompanyBean companyBean = response.body();
                if (companyBean.getCode() == 200) {
                    AppSession.getInstance().setCompanyId(companyBean.getData().getCompany_id());
//                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.COMPANY_ID, companyBean.getData().getCompany_id());
                    Intent intent = new Intent(mContext, CreateCompanySuccActivity.class);
                    startActivity(intent);
                    ((CreateCompanyActivity) getActivity()).finish();
                } else {
                    ToastUtil.show(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<CreateCompanyBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }


    /**
     * 上班时间选择
     */
    public void onStartTimePicker() {
        if (picker == null) {
            picker = new TimePicker(mContext, TimePicker.HOUR_24);
            //picker.setUseWeight(false);
            picker.setCycleDisable(false);
            picker.setRangeStart(0, 0);//00:00
            picker.setRangeEnd(23, 59);//23:59
            int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
            picker.setSelectedItem(currentHour, currentMinute);
            picker.setTopLineVisible(false);
            //picker.setTextPadding(ConvertUtils.toPx(mContext, 15));
            picker.setTextSize(20);
            picker.setCanceledOnTouchOutside(true);
            picker.setTextColor(Color.rgb(50, 150, 250));
            picker.setDividerColor(Color.rgb(153, 153, 153));
            picker.setCancelTextColor(Color.rgb(153, 153, 153));
            picker.setSubmitTextColor(Color.rgb(50, 150, 250));
            picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                @Override
                public void onTimePicked(String hour, String minute) {
                    String time = hour + ":" + minute;
                    tvStartTimeShow.setText(time);
                }

            });
        }

        picker.show();
    }


    /**
     * 下班时间选择
     */
    public void onEndTimePicker() {
        //picker.setUseWeight(false);
        if (endPicker == null) {
            endPicker = new TimePicker(mContext, TimePicker.HOUR_24);
            endPicker.setCycleDisable(false);
            endPicker.setRangeStart(0, 0);//00:00
            endPicker.setRangeEnd(23, 59);//23:59
            int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
            endPicker.setSelectedItem(currentHour, currentMinute);
            endPicker.setTopLineVisible(false);
            //picker.setTextPadding(ConvertUtils.toPx(mContext, 15));
            endPicker.setTextSize(20);
            endPicker.setCanceledOnTouchOutside(true);
            endPicker.setTextColor(Color.rgb(50, 150, 250));
            endPicker.setDividerColor(Color.rgb(153, 153, 153));
            endPicker.setCancelTextColor(Color.rgb(153, 153, 153));
            endPicker.setSubmitTextColor(Color.rgb(50, 150, 250));
            endPicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                @Override
                public void onTimePicked(String hour, String minute) {
                    String time = hour + ":" + minute;
                    tvEndTimeShow.setText(time);
                }

            });
        }
        endPicker.show();
    }

    /**
     * 允许上下班时间差选择器
     */
    public void onOptionPicker() {
        if (optionPicker == null) {
            optionPicker = new OptionPicker(mContext, timeStringList);
            optionPicker.setCanceledOnTouchOutside(false);
            optionPicker.setDividerRatio(WheelView.DividerConfig.FILL);
            optionPicker.setSelectedIndex(2);
            optionPicker.setCycleDisable(true);
            optionPicker.setTextSize(20);
            optionPicker.setCanceledOnTouchOutside(true);
            optionPicker.setTextColor(Color.rgb(50, 150, 250));
            optionPicker.setDividerColor(Color.rgb(153, 153, 153));
            optionPicker.setCancelTextColor(Color.rgb(153, 153, 153));
            optionPicker.setSubmitTextColor(Color.rgb(50, 150, 250));
            optionPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                @Override
                public void onOptionPicked(int index, String item) {
                    tvSignTimeShow.setText(timeBeanList.get(index).time);
                    defaultTime = timeBeanList.get(index).minute;
                }
            });
        }

        optionPicker.show();
    }

}
