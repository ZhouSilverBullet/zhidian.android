package com.sdxxtop.zhidian.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
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
import com.sdxxtop.zhidian.adapter.PunchRecyclerAdapter;
import com.sdxxtop.zhidian.entity.ApproverIndexBean;
import com.sdxxtop.zhidian.entity.MainIndexBean;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.ImageParams;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.popupwindow.SelectionDateWindow;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * @author lxl
 * @date 2018/5/1  16:28
 * @desc
 */
public class PunchCardActivity extends BaseApproverActivity {

    public static final int type_punch_index = 1;
    public static final int type_punch_pop = 2;

    @BindView(R.id.horlv_photo)
    RecyclerView horListViewPhoto;
    @BindView(R.id.horlv_approving_officer)
    RecyclerView horApprovOfficer;
    @BindView(R.id.horlv_scribble)
    RecyclerView horHorlvScribble;
    @BindView(R.id.ll_select_time)
    LinearLayout llSelectTime;
    @BindView(R.id.tv_select_time)
    TextView tvSelect;
    @BindView(R.id.btn_submission)
    Button btnSubmission;
    @BindView(R.id.punch_recycler_view)
    RecyclerView dateRecyclerView;
    @BindView(R.id.item_apply_content_content)
    EditText contentEdit;
    @BindView(R.id.apply_content_second_title)
    TextView contentSecondTitle;

    @BindView(R.id.punch_title_view)
    SubTitleView titleView;
    @BindView(R.id.item_apply_content_change_text)
    TextView changeText;


    private PunchRecyclerAdapter adapter;
    private List<MainIndexBean.DataBean.SignLogBean> signLogList;
    private String selector_date;

    @Override
    protected int getActivityView() {
        return R.layout.activity_punch_card;
    }

    @Override
    protected void initView() {
        if (at == 3) {
            titleView.setTitleValue("消迟到/早退");
        } else {
            titleView.setTitleValue("漏打卡");
        }
        contentSecondTitle.setText("证明人");
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new PunchRecyclerAdapter(R.layout.item_punch_recycler, at);
        dateRecyclerView.setAdapter(adapter);

        if (signLogList != null && !TextUtils.isEmpty(selector_date)) {
            tvSelect.setText(selector_date);
            adapter.replaceData(signLogList);
            adapter.setType(type_punch_index);
            adapter.setStringDate(selector_date);
        }
    }

    @Override
    protected void initChildData(ApproverIndexBean.DataBean data) {

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
        llSelectTime.setOnClickListener(this);
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
//                        strings.add("白班漏打卡 9:00");
//                        strings.add("白班漏打卡 12:00");
//                        strings.add("白班漏打卡 16:00");
//                        strings.add("晚班漏打卡 21:00");
//
//                        EventBus.getDefault().post(new MessageEvent.intentActivity(
//                                "漏打卡",
//                                "11月15日",
//                                strings,
//                                "忘记打卡",
//                                photos,
//                                "张三",
//                                "长军",
//                                "红枫"));
//                    }
//                }, 800);
            }
        });
    }

    private void submit() {

        if (adapter.getItemCount() == 0) {
            if (at == 2) {
                showToast("请选择要消除的漏打卡");
            } else {
                showToast("请选择要消除的迟到/早退打卡");
            }
            return;
        }

        String eidtContent = contentEdit.getText().toString();


        if (TextUtils.isEmpty(eidtContent)) {
            if (at == 2) {
                showToast("请填写漏打卡理由");
            } else {
                showToast("请填写迟到早退理由");
            }
            return;
        }


        if (data == null) {
            showToast("数据错误");
            return;
        }

        String cardIds = "";
        String cardNames = "";
        String cardTimes = "";
        String cardStatus = "";
        String cardMinute = "";
        List<MainIndexBean.DataBean.SignLogBean> beanList = adapter.getData();
        for (int i = 0; i < beanList.size(); i++) {
            MainIndexBean.DataBean.SignLogBean signLogBean = beanList.get(i);
            int status = signLogBean.getStatus();
            int minute = signLogBean.getMinute();
            String sysDate = signLogBean.getSys_date();
            if (i == beanList.size() - 1) {
                cardIds = cardIds + signLogBean.getSign_id();
                cardNames = cardNames + signLogBean.getSign_name();
                cardTimes = cardTimes + sysDate;
                cardStatus = cardStatus + status;
                cardMinute = cardMinute + minute;
            } else {
                cardIds = cardIds + signLogBean.getSign_id() + ",";
                cardNames = cardNames + signLogBean.getSign_name() + ",";
                cardTimes = cardTimes + sysDate + ",";
                cardStatus = cardStatus + status + ",";
                cardMinute = cardMinute + minute + ",";
            }
        }

        ImageParams params = new ImageParams();
        params.put("sgi", cardIds);
        params.put("sgn", cardNames);
        params.put("sgt", cardTimes);
        params.put("ad", adapter.getStringDate());
        params.put("rs", eidtContent);
        params.put("ai", getApproverValue());
        params.put("wi", getCopyValue()); //证明人

        params.addImagePathList("img[]", getImagePushPath());

        String name = "";
        if (at == 2) {
            name = "leak";
        } else { //迟到早退多了两个参数
            name = "late";
            params.put("sgs", cardStatus);
            params.put("lt", cardMinute);
        }
        RequestUtils.createRequest().postApplyLeave(name, params.getImgData()).enqueue(new RequestCallback<BaseModel>(new IRequestListener<BaseModel>() {
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

    private String getSubString(String sysDate) {
        if (!TextUtils.isEmpty(sysDate) && sysDate.length() > 0) {
            sysDate = sysDate.substring(0, sysDate.length() - 3);
        }
        return sysDate;
    }

    @Override
    protected void initVariables() {
        super.initVariables();
        if (getIntent() != null) {
            signLogList = (List<MainIndexBean.DataBean.SignLogBean>) getIntent().getSerializableExtra("sign_log");
            if (signLogList != null && signLogList.size() > 0) {
                for (MainIndexBean.DataBean.SignLogBean signLogBean : signLogList) {
                    signLogBean.setSys_date(getSubString(signLogBean.getSys_date()));
                }
            }
            selector_date = getIntent().getStringExtra("selector_date");
        }
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_select_time:
                showSelectDateWindow();
                break;
        }
    }

    private void showSelectDateWindow() {
        String stringDate = adapter.getStringDate();
        Calendar instance;
        if (stringDate == null) {
            instance = Calendar.getInstance();
        } else {
            Date date = new Date(DateUtil.convertTimeToLong(stringDate, "yyyy-MM-dd"));
            instance = Calendar.getInstance();
            instance.setTime(date);
        }

        final SelectionDateWindow selectionDateWindow = new SelectionDateWindow(this, true, at, instance);
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels * 8 / 9;

//        selectionDateWindow.setSelectedDate(instance);
        selectionDateWindow.setWidth(weight);
        selectionDateWindow.setHeight(height);
        selectionDateWindow.setFocusable(true);
        selectionDateWindow.setTouchable(true);
        selectionDateWindow.setOutsideTouchable(true);
        selectionDateWindow.setAnimationStyle(R.style.AnimationRightBottom);

        selectionDateWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });

        selectionDateWindow.setSelectorDateListener(new SelectionDateWindow.SelectorDateListener() {
            @Override
            public void onSelector(String date, List<MainIndexBean.DataBean.SignLogBean> selectorList) {
                tvSelect.setText(date);
                adapter.setStringDate(date);
                adapter.setType(type_punch_pop);
                adapter.replaceData(selectorList);
            }
        });

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        selectionDateWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_main, null), Gravity.BOTTOM, 0, 0);
    }
}
