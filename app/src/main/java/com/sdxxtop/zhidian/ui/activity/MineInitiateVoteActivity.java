package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.VoteReadBean;
import com.sdxxtop.zhidian.eventbus.MineVoteFragEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.widget.CHAlertDialog;
import com.sdxxtop.zhidian.widget.MineInitiateImageProgressView;
import com.sdxxtop.zhidian.widget.MineInitiateMarkView;
import com.sdxxtop.zhidian.widget.MineInitiateProgressView;
import com.sdxxtop.zhidian.widget.MineInitiateSeekBarView;
import com.sdxxtop.zhidian.widget.SubTitleView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.addapp.pickers.picker.DateTimePicker;
import zhangphil.iosdialog.widget.AlertDialog;

public class MineInitiateVoteActivity extends BaseActivity {

    @BindView(R.id.title_view)
    SubTitleView titleView;
    @BindView(R.id.activity_mine_initiate_people_text)
    TextView peopleText;
    @BindView(R.id.activity_mine_initiate_people_layout)
    LinearLayout peopleLayout;
    @BindView(R.id.activity_mine_initiate_title)
    TextView titleText;
    @BindView(R.id.activity_mine_initiate_username)
    TextView userNameText;
    @BindView(R.id.activity_mine_initiate_time)
    TextView timeText;
    @BindView(R.id.activity_mine_initiate_second_layout)
    LinearLayout secondLayout;
    @BindView(R.id.activity_mine_initiate_canyu_people)
    TextView canyuPeopleText;
    @BindView(R.id.activity_mine_initiate_gongkai)
    TextView gongkaiText;
    @BindView(R.id.activity_mine_initiate_img)
    ImageView imgImageView;
    @BindView(R.id.activity_mine_initiate_dec)
    TextView decText;

    @BindView(R.id.activity_mine_initiate_submit_layout)
    LinearLayout addBtnLayout;//提交按钮Layout
    @BindView(R.id.activity_mine_initiate_add_btn)
    Button addBtn;//提交按钮
    @BindView(R.id.activity_mine_initiate_change_layout)
    LinearLayout changeLayout; //修改结束layout
    @BindView(R.id.activity_mine_initiate_change_time)
    TextView changeTime; //修改结束时间
    @BindView(R.id.activity_mine_initiate_delete)
    TextView changeDelete;  //删除投票
    @BindView(R.id.activity_mine_initiate_single_text)
    TextView singleText;  //

    @BindView(R.id.activity_mine_initiate_text_pingfen_layout)
    LinearLayout pinfenLayuout;
    @BindView(R.id.activity_mine_initiate_single_text_and_image_vote_layout)
    LinearLayout voteLayout; //投票的layout
    @BindView(R.id.activity_mine_initiate_single_mark)
    LinearLayout markLayout; //打分的layout


    @BindView(R.id.activity_mine_initiate_single_all_num)
    TextView markAllNumText; //（总分：100分，以下均为
    @BindView(R.id.activity_mine_initiate_single_last)
    TextView markLastText; // 最后得分
    @BindView(R.id.activity_mine_initiate_single_last_right)
    TextView markLastRightText; // 右括号


    @BindView(R.id.activity_mine_initiate_niming)
    CheckBox nimingCheck;
    @BindView(R.id.activity_mine_initiate_niming_toupiao)
    CheckBox nimingToupiaoCheck;
    @BindView(R.id.activity_mine_initiate_mark_layout)
    LinearLayout markAddLayout;
    @BindView(R.id.activity_mine_delete_show)
    TextView deleteShow;

    private int vi;
    private int type; //1.从我发起 2.去评议
    private int vote_type; //1投票 2.打分
    private boolean isSingle;
    private MineInitiateImageProgressView imageProgressView;
    private DateTimePicker picker;
    private boolean selectorSingle;

    @Override
    protected int getActivityView() {
        return R.layout.activity_mine_initiate_vote;
    }

    @Override
    protected void initVariables() {
        super.initVariables();
        if (getIntent() != null) {
            vi = getIntent().getIntExtra("vi", -1);
            type = getIntent().getIntExtra("type", -1);
            vote_type = getIntent().getIntExtra("vote_type", -1);
        }
    }

    @Override
    protected void initView() {
        super.initView();
//        titleView.setTitleValue();
        if (type == 1) {  //我的投票过来的
            addBtnLayout.setVisibility(View.GONE);
            changeLayout.setVisibility(View.VISIBLE);

            nimingCheck.setVisibility(View.GONE); //匿名在评议的时候才能被看到
            nimingToupiaoCheck.setVisibility(View.GONE);
        } else if (type == 2) {
            addBtnLayout.setVisibility(View.VISIBLE);
            changeLayout.setVisibility(View.GONE);

            nimingCheck.setVisibility(View.VISIBLE);
            nimingToupiaoCheck.setVisibility(View.VISIBLE);
        }

        if (vote_type == 1) {
            titleView.setTitleValue("投票");
        } else {
            titleView.setTitleValue("打分");
            addBtn.setText("打分");
        }

//        pingFen1TextBackground = ((ClipDrawable) pingFen1Layout.getBackground());
//        pingFen2TextBackground = ((ClipDrawable) pingFen2Layout.getBackground());
    }

    @Override
    protected void initData() {
        super.initData();
        Params params = new Params();
        params.put("vi", vi);
        RequestUtils.createRequest().postVoteRead(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<VoteReadBean>() {
            @Override
            public void onSuccess(VoteReadBean voteReadBean) {
                VoteReadBean.DataBean data = voteReadBean.getData();
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

    private boolean isImageDafen; //是图片打分吗

    private void handleData(VoteReadBean.DataBean data) {
        int is_vote = data.getIs_vote();
        //是否公开
        final int is_hide = data.getIs_hide();
        if (is_hide == 1) { //1公开 2匿名
            gongkaiText.setText("公开");
        } else {
            gongkaiText.setText("匿名");
            nimingToupiaoCheck.setVisibility(View.INVISIBLE);
            nimingCheck.setVisibility(View.INVISIBLE);
        }

        //匿名的情况下不能进入评论列表
        peopleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_hide == 1) {
                    Intent intent = new Intent(mContext, VoteAttenderActivity.class);
                    intent.putExtra("vi", vi);
                    intent.putExtra("vote_type", vote_type);
                    startActivity(intent);
                } else {
                    showToast("本项目为匿名参与，不能查看投票人详情");
                }
            }
        });

        final boolean isVote = is_vote == 1; //(1:已投 2:未投)
        if (!isVote) {
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    send(is_hide == 2);
                }
            });
        } else {
            addBtn.setBackgroundResource(R.color.texthintcolor);
            if (vote_type == 1) {
                addBtn.setText("已投票");
            } else {
                addBtn.setText("已打分");
            }
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    showToast("您已经投过票了");
                }
            });
        }

        //参与人数
        int peopleNum = data.getPeople_num();
        peopleText.setText(peopleNum + "");

        //title
        String title = data.getTitle();
        titleText.setText(title);

        //用户发起人名
        String operator_name = data.getOperator_name();
        userNameText.setText(operator_name);

        String rest_time = data.getRest_time();
        timeText.setText("剩余" + rest_time);

        //参与人
        List<String> part_name = data.getPart_name();
        String value = "";
        if (part_name != null) {
            for (String s : part_name) {
                value = value + s + ",";
            }
        }
        List<String> name = data.getName();
        if (name != null) {
            for (String s : name) {
                value = value + s + ",";
            }
        }
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        } else {
            value = "全公司";
        }

        canyuPeopleText.setText("参与人：" + value);

        final String score_img = data.getScore_img();
        if (!TextUtils.isEmpty(score_img)) {
            imgImageView.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(score_img).into(imgImageView);
            imgImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoViewActivity.start(mContext, score_img);
                }
            });
        } else {
            imgImageView.setVisibility(View.GONE);
        }

        String vote_desc = data.getVote_desc();

        if (!TextUtils.isEmpty(vote_desc)) {
            decText.setText(vote_desc);
        } else {
            decText.setVisibility(View.GONE);
        }

        if (vote_type == 1) { //1投票 2.打分
            int sum = data.getSum();
//            pingFen1TextBackground.setLevel(100 * 50);
            voteLayout.setVisibility(View.VISIBLE);
            markLayout.setVisibility(View.GONE);

            int is_only = data.getIs_only();
            //是否是单选
            isSingle = is_only == 1;

            if (isSingle) {
                singleText.setText("单选");
            } else {
                singleText.setText("多选");
            }

            List<VoteReadBean.DataBean.OptionBean> option = data.getOption();
            if (option != null && option.size() > 0) {
                for (VoteReadBean.DataBean.OptionBean optionBean : option) {
                    int optionIsVote = optionBean.getIs_vote();
                    if (optionIsVote == 1) {
                        optionBean.setCheck(true);
                    }
                }
                String img = option.get(0).getImg();
                if (TextUtils.isEmpty(img)) {
                    isImageDafen = false;
                } else {
                    isImageDafen = true; //有图片评分
                }
                pinfenLayuout.removeAllViews();
                if (isImageDafen) {
                    imageProgressView = new MineInitiateImageProgressView(mContext);
                    imageProgressView.setSingle(isSingle);
                    imageProgressView.setIsVote(isVote);
                    imageProgressView.setType(type == 1);
                    imageProgressView.setOptionBeanList(sum, option);
                    pinfenLayuout.addView(imageProgressView);
                } else {
                    for (VoteReadBean.DataBean.OptionBean optionBean : option) {
                        final MineInitiateProgressView progressView = new MineInitiateProgressView(mContext);
                        progressView.setIsVote(isVote);
                        progressView.setOptionBean(sum, optionBean);
                        CheckBox pingCheckBox = progressView.getPingCheckBox();
                        if (type == 1) {
                            pingCheckBox.setVisibility(View.GONE);
                        } else {
                            pingCheckBox.setVisibility(View.VISIBLE);
                            progressView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (isVote) { //已经投票无法选中
                                        return;
                                    }
                                    CheckBox pingCheckBox = progressView.getPingCheckBox();
                                    boolean checked = pingCheckBox.isChecked();
                                    if (isSingle) {
                                        singleBtn();
                                    }
                                    pingCheckBox.setChecked(!checked);
                                }
                            });
                        }

                        pinfenLayuout.addView(progressView);
                    }
                }
            }
        } else {  //打分
            voteLayout.setVisibility(View.GONE);
            markLayout.setVisibility(View.VISIBLE);

            int score = data.getScore();
            if (vote_type == 1) { //投票
                nimingCheck.setVisibility(View.GONE);
                markLastRightText.setVisibility(View.VISIBLE);
                markAllNumText.setText("（总分：" + score + "分，以下均为");
                markLastText.setText("最后得分");

                //最后得分的界面
            } else {
                if (type != 1 && is_hide == 1) {
                    nimingCheck.setVisibility(View.VISIBLE);
                }
                markLastRightText.setVisibility(View.GONE);
                markAllNumText.setText("（总分：" + score + "分)");

                markLastText.setText("评分规则");

                //评分规则的界面
//                markLastText.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
            }

            markLastText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog(mContext).builder().setTitle("评分规则")
                            .setMsg("评分结果均为最后得分")
                            .setMsgGravity(Gravity.LEFT)
                            .setMsg2("去掉一个最高分，去掉一个最低分，取平均值，即为最后得分")
                            .setMsg2Gravity(Gravity.LEFT)
                            .setPositiveButton("我知道了", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }
            });

            List<VoteReadBean.DataBean.OptionBean> option = data.getOption();
            if (option != null && option.size() > 0) {
                selectorSingle = option.size() == 1;
                for (VoteReadBean.DataBean.OptionBean optionBean : option) {
                    int optionIsVote = optionBean.getIs_vote();
                    if (optionIsVote == 1) {
                        optionBean.setCheck(true);
                    }
                }
                markAddLayout.removeAllViews();
                if (score <= 5) {

                    if (type == 2 && selectorSingle) { //是去评论的前提下判断
                        addBtnLayout.setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < option.size(); i++) {
                        VoteReadBean.DataBean.OptionBean optionBean = option.get(i);
                        MineInitiateMarkView markView = new MineInitiateMarkView(mContext);
                        markView.setIsVote(isVote);
                        markView.setSelectorSingle(selectorSingle);
                        markView.setShowPinfenBtn(type == 2);
                        markView.setOptionBean(optionBean);
                        if (isDuiXiang) {
                            markView.setMarkListener(new MineInitiateMarkView.MarkListener() {
                                @Override
                                public void mark(int rating, VoteReadBean.DataBean.OptionBean optionBean) {
                                    if (rating == 0) {
                                        showToast("评分不能为0分");
                                        return;
                                    }
                                    option_id = optionBean.getOption_id();
                                    progressValue = rating;
                                    //进行网络请求
                                    send(is_hide == 2);
                                }
                            });
                        }
                        markAddLayout.addView(markView);
                    }
                } else {
                    //是否是一个打分
                    if (selectorSingle) {
                        if (type == 2) {
                            addBtnLayout.setVisibility(View.VISIBLE);
                        }
                        VoteReadBean.DataBean.OptionBean optionBean = option.get(0);
                        MineInitiateSeekBarView seekBarView = new MineInitiateSeekBarView(mContext);
                        //是否是我发起
                        seekBarView.setType(type == 1);
                        seekBarView.setIsVote(isVote);
                        seekBarView.setSelectorSingle(selectorSingle);
                        seekBarView.setData(score, optionBean);
                        seekBarView.setSubmitListener(new MineInitiateSeekBarView.SubmitListener() {

                            @Override
                            public void onSubmit(int progress, VoteReadBean.DataBean.OptionBean optionBean) {
                                if (progress == 0) {
                                    showToast("评分不能为0分");
                                    return;
                                }
                                if (optionBean != null) {
                                    option_id = optionBean.getOption_id();
                                    progressValue = progress;
                                    //进行网络请求
                                    send(is_hide == 2);
                                }
                            }
                        });
                        markAddLayout.addView(seekBarView);
                    } else {
                        addBtnLayout.setVisibility(View.GONE);
                        for (int i = 0; i < option.size(); i++) {
                            VoteReadBean.DataBean.OptionBean optionBean = option.get(i);
                            MineInitiateSeekBarView seekBarView = new MineInitiateSeekBarView(mContext);
                            //是否是我发起
                            seekBarView.setType(type == 1);
                            seekBarView.setIsVote(isVote);
                            seekBarView.setSelectorSingle(selectorSingle);
                            seekBarView.setData(score, optionBean);
                            seekBarView.setSubmitListener(new MineInitiateSeekBarView.SubmitListener() {

                                @Override
                                public void onSubmit(int progress, VoteReadBean.DataBean.OptionBean optionBean) {
                                    if (progress == 0) {
                                        showToast("评分不能为0分");
                                        return;
                                    }
                                    if (optionBean != null) {
                                        option_id = optionBean.getOption_id();
                                        progressValue = progress;
                                        //进行网络请求
                                        send(is_hide == 2);
                                    }
                                }
                            });
                            markAddLayout.addView(seekBarView);
                        }
                    }


                }
            }
        }
    }

    private void singleBtn() {
        if (isSingle) {
            int childCount = pinfenLayuout.getChildCount();
            if (childCount > 0) {
                for (int i = 0; i < pinfenLayuout.getChildCount(); i++) {
                    View childAt = pinfenLayuout.getChildAt(i);
                    if (childAt instanceof MineInitiateProgressView) {
                        ((MineInitiateProgressView) childAt).getPingCheckBox().setChecked(false);
                    }
                }
            }
        }
    }

    @Override
    protected void initEvent() {
        super.initEvent();

        changeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CHAlertDialog chAlertDialog = new CHAlertDialog(mContext, true, true);
                chAlertDialog.show();

                chAlertDialog.setContent("您确定删除本次项目吗？");
                chAlertDialog.setCancelButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chAlertDialog.dismiss();
                    }
                });
                chAlertDialog.setOkButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chAlertDialog.dismiss();
                        voteModfiy(2, "");
                    }
                });
            }
        });

        changeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onYearMonthDayTimePicker();
            }
        });

    }

    private int option_id = -1;  //用来打分
    private int progressValue = -1;
    private boolean isDuiXiang = false; // rating的评分是否有对象

    private void send(boolean isHide) {
        String optionId = getOptionId();
        if (option_id != -1) { //这个option是在 多人评分的时候调取
            optionId = option_id + "";
        }

//        if (!isDuiXiang) {
//            int childCount = markAddLayout.getChildCount();
//            if (childCount > 0) {
//                for (int i = 0; i < markAddLayout.getChildCount(); i++) {
//                    View childAt = markAddLayout.getChildAt(i);
//                    if (childAt instanceof MineInitiateMarkView) {
//                        progressValue = ((MineInitiateMarkView) childAt).getRatingCount();
//                        int optionId1 = ((MineInitiateMarkView) childAt).getOptionId();
//                        optionId = optionId1 + "";
//                    }
//                }
//            }
//        }

        //打分就一个
        if (selectorSingle) {
            int childCount = markAddLayout.getChildCount();
            if (childCount > 0) {
                for (int i = 0; i < markAddLayout.getChildCount(); i++) {
                    View childAt = markAddLayout.getChildAt(i);
                    if (childAt instanceof MineInitiateMarkView) {
                        progressValue = ((MineInitiateMarkView) childAt).getRatingCount();
                        int optionId1 = ((MineInitiateMarkView) childAt).getOptionId();
                        optionId = optionId1 + "";
                    }

                    if (childAt instanceof MineInitiateSeekBarView) {
                        progressValue = ((MineInitiateSeekBarView) childAt).getProgress();
                        int optionId1 = ((MineInitiateSeekBarView) childAt).getOptionId();
                        optionId = optionId1 + "";
                    }
                }
            }
        }

        if (vote_type == 2 && progressValue <= 0) {
            showToast("请打分后再提交");
            return;
        }

        Params params = new Params();
        params.put("vi", vi);
        params.put("vt", vote_type);
        params.put("oi", optionId);

        params.put("se", progressValue); //打分
        if (isHide) { //后台是匿名的时候
            params.put("ih", 2);
        } else {
            if (vote_type == 1) {
                params.put("ih", nimingToupiaoCheck.isChecked() ? 2 : 1); //1:公开 2:匿名
            } else {
                params.put("ih", nimingCheck.isChecked() ? 2 : 1); //1:公开 2:匿名
            }
        }

        RequestUtils.createRequest().postVoteMark(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                initData();
                showToast(baseModel.msg);
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    private String getOptionId() {
        String value = "";
        if (vote_type == 1) { // 投票的提交
            if (isImageDafen) {
                if (imageProgressView != null) {
                    MineInitiateImageProgressView.ImageProgressAdapter adapter = imageProgressView.getAdapter();
                    List<VoteReadBean.DataBean.OptionBean> data = adapter.getData();
                    for (VoteReadBean.DataBean.OptionBean datum : data) {
                        if (datum.isCheck()) {
                            value = value + datum.getOption_id() + ",";
                        }
                    }
                }
            } else {
                int childCount = pinfenLayuout.getChildCount();
                if (childCount > 0) {
                    for (int i = 0; i < pinfenLayuout.getChildCount(); i++) {
                        View childAt = pinfenLayuout.getChildAt(i);
                        if (childAt instanceof MineInitiateProgressView) {
                            if (((MineInitiateProgressView) childAt).getPingCheckBox().isChecked()) {
                                int option_id = ((MineInitiateProgressView) childAt).getOptionBean().getOption_id();
                                value = value + option_id + ",";
                            }
                        }
                    }
                }
            }
        } else { // 投票的提交

        }


        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    /**
     * @param type (1:修改 2:删除)
     * @param et   结束时间 修改
     */
    private void voteModfiy(final int type, String et) {
        Params params = new Params();
        params.put("vi", vi);
        params.put("mt", type);
        if (type == 1) {
            params.put("et", et);
        }
        RequestUtils.createRequest().postVoteModfiy(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                if (type == 2) {
//                    finish();
                    deleteShow.setVisibility(View.VISIBLE);
                    changeLayout.setVisibility(View.GONE);
                }
                showToast(baseModel.msg);
                MineVoteFragEvent mineVoteFragEvent = new MineVoteFragEvent();
                mineVoteFragEvent.type = MineInitiateVoteActivity.this.type;
                EventBus.getDefault().post(mineVoteFragEvent);
                initData();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    public void onYearMonthDayTimePicker() {
        if (picker == null) {
            Calendar calendar = Calendar.getInstance(); //获取当前时间
            calendar.add(Calendar.DATE, 7); //加一周的时间
            Date time = calendar.getTime();
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String endTime = sDateFormat.format(time);
            picker = new DateTimePicker(mContext, DateTimePicker.HOUR_24);
            picker.setActionButtonTop(true);
            picker.setDateRangeStart(2017, 1, 1);
            picker.setDateRangeEnd(2025, 11, 11);

            picker.setSelectedItem(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1),
                    calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
            picker.setTimeRangeStart(0, 0);
            picker.setTimeRangeEnd(23, 59);

            picker.setTopBackgroundColor(0xFFEEEEEE);
            int h = (int) getResources().getDimension(R.dimen.y601);
            picker.setHeight(h);
            picker.setTopHeight(52);
            picker.setTopLineColor(0xFF33B5E5);
            picker.setTopLineHeight(1);
            picker.setTitleText("");
            picker.setTitleTextColor(0xFF999999);
            picker.setTitleTextSize(18);
            picker.setCancelTextColor(0xFF999999);
            picker.setCancelTextSize(18);
            picker.setSubmitTextColor(0xFF3296FA);
            picker.setSubmitTextSize(18);

            picker.setTextSize(20);
            picker.setBackgroundColor(0xFFffffff);

            picker.setLabel("年", "月", "日", "时", "分");

            picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                @Override
                public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                    final String time = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":00";

                    new AlertDialog(mContext).builder().setTitle("提示")
                            .setMsg("确定修改时间为" + time).setPositiveButton("", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            voteModfiy(1, time);
                        }
                    }).setNegativeButton("", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                }
            });
        }


        picker.show();


//        picker.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                String year = picker.getSelectedYear();
//                String month = picker.getSelectedMonth();
//                String day = picker.getSelectedDay();
//                String hour = picker.getSelectedHour();
//                String minute = picker.getSelectedMinute();
//
//
//
//
//            }
//        });
    }
}
