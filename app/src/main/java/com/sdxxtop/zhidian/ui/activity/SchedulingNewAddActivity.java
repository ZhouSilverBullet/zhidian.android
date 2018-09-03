package com.sdxxtop.zhidian.ui.activity;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.NewAddCreateEntry;
import com.sdxxtop.zhidian.entity.ReadClassBean;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;
import com.sdxxtop.zhidian.widget.TextAndTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.addapp.pickers.picker.TimePicker;
import cn.qqtheme.framework.picker.SinglePicker;
import cn.qqtheme.framework.widget.WheelView;

public class SchedulingNewAddActivity extends BaseActivity {

    public static final int LIST_ACTIVITY_IN = 10; //SchedulingListActivity

    @BindView(R.id.scheduling_new_root)
    View newRootLayout;
    @BindView(R.id.scheduling_add)
    View addView;
    @BindView(R.id.scheduling_up_down_text)
    TextAndTextView upDownText; //上下班打卡宽限
    @BindView(R.id.scheduling_up_down_can_text)
    TextAndTextView upCanText; //上下班前后允许打卡时间
    @BindView(R.id.scheduling_add_btn)
    Button addBtn;
    @BindView(R.id.new_add_title_view)
    SubTitleView titleView;

    @BindView(R.id.scheduling_add_edit)
    EditText addNameEdit; //班次名
    @BindView(R.id.scheduling_add_design_edit)
    EditText addShortEdit; //班次简称

    @BindView(R.id.item_recycler)
    RecyclerView recyclerView;
    private NewAddAdapter madapter;
    private SelectorTime[] kuanxianCard; //宽限时间
    private SelectorTime[] yuxuCard; //允许打卡时间
    private int schedulingListIntValue;
    private int classId;
    private String title;

    @Override
    protected int getActivityView() {
        return R.layout.activity_scheduling_new_add;
    }

    class SelectorTime {
        public SelectorTime(String time, int value) {
            this.time = time;
            this.value = value;
        }

        String time;
        int value;
    }

    @Override
    protected void initVariables() {
        super.initVariables();
        if (getIntent() != null) {
            schedulingListIntValue = getIntent().getIntExtra("scheduling_list", -1);
            classId = getIntent().getIntExtra("cli", -1);
            title = getIntent().getStringExtra("title");
        }
    }

    @Override
    protected void initView() {
        super.initView();
        if (!TextUtils.isEmpty(title)) {
            titleView.setTitleValue(title);
        }
        addShortEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
    }

    @Override
    protected void initData() {
        super.initData();
        kuanxianCard = new SelectorTime[]{
                new SelectorTime("无", 0)
                , new SelectorTime("1分钟", 1)
                , new SelectorTime("3分钟", 3)
                , new SelectorTime("5分钟", 5)
                , new SelectorTime("10分钟", 10)
                , new SelectorTime("15分钟", 15)
                , new SelectorTime("20分钟", 20)
                , new SelectorTime("30分钟", 30)
                , new SelectorTime("1小时", 60)
                , new SelectorTime("1.5小时", 90)
                , new SelectorTime("2小时", 120)
                , new SelectorTime("3.5小时", 210)
//                , new SelectorTime("6小时", 360)
        };
        yuxuCard = new SelectorTime[]{
                new SelectorTime("15分钟", 15),
                new SelectorTime("30分钟", 30),
                new SelectorTime("1小时", 60),
                new SelectorTime("2小时", 120),
                new SelectorTime("3小时", 180),
                new SelectorTime("4小时", 240),
        };

//        new SelectorTime("6小时", 360),
//        new SelectorTime("8小时", 480),
//        new SelectorTime("10小时", 600),

        madapter = new NewAddAdapter(R.layout.view_swipe_text_and_text);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemDivider());
        recyclerView.setAdapter(madapter);

        addDefaultValue();

        if (classId == -1) {
            return;
        }

        Params params = new Params();
        params.put("cli", classId);
        RequestUtils.createRequest().postReadClass(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<ReadClassBean>() {
            @Override
            public void onSuccess(ReadClassBean baseModel) {
                ReadClassBean.DataBean data = baseModel.getData();
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

    private void handleData(ReadClassBean.DataBean data) {
        ReadClassBean.DataBean.ClassBean classX = data.getClassX();
        String name = classX.getName();
        String short_name = classX.getShort_name();
        addNameEdit.setText(name);
        addShortEdit.setText(short_name);

        String sign_name = classX.getSign_name();
        String sign_date = classX.getSign_date();
        String[] signName = sign_name.split(",");
        String[] signDate = sign_date.split(",");

        if (signName.length == signDate.length) {
            List<NewAddCreateEntry> baseModels = new ArrayList<>();
            for (int i = 0; i < signName.length; i++) {
                NewAddCreateEntry entry1 = new NewAddCreateEntry();
                entry1.name = signName[i] + "时间";
                entry1.time = signDate[i];
                baseModels.add(entry1);
            }
            madapter.replaceData(baseModels);
        }

        yuxuValue = classX.getSign_time();
        kuanxianValue = classX.getLevel_time();

        for (SelectorTime selectorTime : kuanxianCard) {
            if (selectorTime.value == kuanxianValue) {
                upDownText.getTextRightText().setText(selectorTime.time);
                break;
            }
        }

        for (SelectorTime selectorTime : yuxuCard) {
            if (selectorTime.value == yuxuValue) {
                upCanText.getTextRightText().setText(selectorTime.time);
                break;
            }
        }

    }

    private void addDefaultValue() {
        List<NewAddCreateEntry> baseModels = new ArrayList<>();
        NewAddCreateEntry entry1 = new NewAddCreateEntry();
        entry1.name = "打卡时间";
        entry1.time = "09:00";
        NewAddCreateEntry entry2 = new NewAddCreateEntry();
        entry2.name = "打卡时间";
        entry2.time = "18:00";

        baseModels.add(entry1);
        baseModels.add(entry2);
        List<NewAddCreateEntry> data = madapter.getData();
        if (data.size() <= 6) {
            madapter.addData(baseModels);
        } else if (data.size() == 7) {
            baseModels.remove(0);
            madapter.addData(baseModels);
        } else {
            showToast("每天最多设置8次打卡");
        }
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        newRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TimePicker timePicker = new TimePicker(mContext);
//                timePicker.setLabel("时", "分");
//                timePicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
//                    @Override
//                    public void onTimePicked(String hour, String minute) {
//                        showToast("hour:" + hour + ", minute " + minute);
//                    }
//                });
//                timePicker.show();
//                swipeMenuLayout.smoothClose();

            }
        });

        upDownText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> kuanXian = new ArrayList<>();
                for (int i = 0; i < kuanxianCard.length; i++) {
                    String time = kuanxianCard[i].time;
                    kuanXian.add(time);
                }
                onConstellationPicker(1, kuanXian);
            }
        });

        upCanText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> yuxu = new ArrayList<>();
                for (int i = 0; i < yuxuCard.length; i++) {
                    String time = yuxuCard[i].time;
                    yuxu.add(time);
                }
                onConstellationPicker(2, yuxu);
            }
        });

        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDefaultValue();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classId != -1) {
                    modifyClass();  //修改班次
                } else {
                    submitData(); //新增班次
                }
            }
        });
    }

    private void modifyClass() {
        String addNameEditValue = addNameEdit.getText().toString();
        String addShortEditValue = addShortEdit.getText().toString();

        if (StringUtil.isEmptyWithTrim(addNameEditValue)) {
            showToast("请输入班次名");
            return;
        }

        if (StringUtil.isEmptyWithTrim(addShortEditValue)) {
            showToast("请输入班次名");
            return;
        }

        List<NewAddCreateEntry> data = madapter.getData();
        String signDate = "";
        for (int i = 0; i < data.size(); i++) {
            NewAddCreateEntry entry = data.get(i);
            if (i == data.size() - 1) {
                signDate = signDate + entry.time;
            } else {
                signDate = signDate + entry.time + ",";
            }
        }

        Params params = new Params();
        params.put("nm", addNameEditValue);
        params.put("snm", addShortEditValue);
        params.put("cli", classId);
        params.put("sd", signDate);
        params.put("st", yuxuValue);
        params.put("lt", kuanxianValue);
        params.put("oi", params.getUserId());
        RequestUtils.createRequest().postClassModifyClass(params.getData())
                .enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
                    @Override
                    public void onSuccess(BaseModel baseModel) {
                        showToast(baseModel.msg);
                        setResult(200);
                        finish();
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        showToast(errorMsg);
                    }
                }));
    }

    private void submitData() {
        String addNameEditValue = addNameEdit.getText().toString();
        String addShortEditValue = addShortEdit.getText().toString();

        if (StringUtil.isEmptyWithTrim(addNameEditValue)) {
            showToast("请输入班次名");
            return;
        }

        if (StringUtil.isEmptyWithTrim(addShortEditValue)) {
            showToast("请输入班次名");
            return;
        }

        List<NewAddCreateEntry> data = madapter.getData();
        String signDate = "";
        for (int i = 0; i < data.size(); i++) {
            NewAddCreateEntry entry = data.get(i);
            if (i == data.size() - 1) {
                signDate = signDate + entry.time;
            } else {
                signDate = signDate + entry.time + ",";
            }
        }

        Params params = new Params();
        params.put("nm", addNameEditValue);
        params.put("snm", addShortEditValue);
        params.put("sd", signDate);
        params.put("st", yuxuValue);
        params.put("lt", kuanxianValue);
        params.put("oi", params.getUserId());
        RequestUtils.createRequest().postClassAddClass(params.getData())
                .enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
                    @Override
                    public void onSuccess(BaseModel baseModel) {
                        showToast(baseModel.msg);
                        setResult(200);
                        finish();
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        showToast(errorMsg);
                    }
                }));

    }

    private int kuanxianValue = 0;
    private int yuxuValue = 60;


    public void onConstellationPicker(final int type, List<String> strings) {
        final SinglePicker<String> picker = new SinglePicker<>(this, strings);
        picker.setTopBackgroundColor(0xFFEEEEEE);
//        picker.setHeight(801);
        picker.setTopHeight(52);
        picker.setTopLineColor(0xFF33B5E5);
        picker.setTopLineHeight(1);
        picker.setTitleText("");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(18);
        picker.setCancelTextColor(0xFF999999);
        picker.setCancelTextSize(18);
        picker.setSubmitTextColor(0xFF33B5E5);
        picker.setSubmitTextSize(18);
        /*picker.setSelectedTextColor(0xFF333333);
        picker.setUnSelectedTextColor(0xFF999999);
        picker.setWheelModeEnable(false);*/
        WheelView.LineConfig config = new WheelView.LineConfig();
        config.setColor(0xFFD3D3D3);//线颜色
        config.setAlpha(120);//线透明度
//        config.setRatio(1);//线比率
        picker.setLineConfig(config);
        picker.setItemWidth(500);
        picker.setBackgroundColor(0xFFffffff);
        //picker.setSelectedItem(isChinese ? "处女座" : "Virgo");
        picker.setSelectedIndex(0);
        picker.show();

        picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                if (type == 1) {
                    upDownText.getTextRightText().setText(item);
                    kuanxianValue = kuanxianCard[index].value;
                } else {
                    upCanText.getTextRightText().setText(item);
                    yuxuValue = yuxuCard[index].value;
                }
            }
        });
    }


    class NewAddAdapter extends BaseQuickAdapter<NewAddCreateEntry, BaseViewHolder> {

        TimePicker timePicker;

        public NewAddAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final NewAddCreateEntry item) {
            final int position = helper.getAdapterPosition();
            helper.getView(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<NewAddCreateEntry> data = madapter.getData();
                    if (data.size() <= 1) {
                    } else {
                        data.remove(item);
                        notifyDataSetChanged();
                    }
                }
            });

            TextView textName = helper.getView(R.id.text_and_text_name);
            textName.setText(item.name);
            TextView textRight = helper.getView(R.id.text_and_text_right);
            textRight.setText(item.time);
            helper.getView(R.id.swipe_text_and_text_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date(DateUtil.convertTimeToLong(item.time, "HH:ss")));
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    timePicker = new TimePicker((Activity) mContext);
                    timePicker.setLabel("时", "分");
                    timePicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                        @Override
                        public void onTimePicked(String hour, String minute) {
//                            showToast("hour:" + hour + ", minute " + minute);
                            item.time = hour + ":" + minute;
                            notifyDataSetChanged();
                        }
                    });
                    timePicker.setSelectedItem(hour, minute);
                    timePicker.show();
                }
            });
        }
    }

}
