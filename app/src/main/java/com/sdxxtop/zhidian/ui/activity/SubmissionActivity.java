package com.sdxxtop.zhidian.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.SubmissionPicRecyclerAdapter;
import com.sdxxtop.zhidian.entity.SubmissionBean;
import com.sdxxtop.zhidian.eventbus.ChangeCompanyEvent;
import com.sdxxtop.zhidian.eventbus.MessageCenterEvent;
import com.sdxxtop.zhidian.eventbus.OneKeyEvent;
import com.sdxxtop.zhidian.eventbus.PostSuccessEvent;
import com.sdxxtop.zhidian.eventbus.PushCenterEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.im.IMLoginHelper;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.ApplyListFragment;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.LeaveUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import zhangphil.iosdialog.widget.AlertDialog;

/**
 * 作者：${刘香龙} on 2018/5/4 0004 10:56
 * 类的描述：提交申请界面
 */
public class SubmissionActivity extends BaseActivity {

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.lv_list_event)
    ListView lvListEvent;
    @BindView(R.id.hor_reason)
    RecyclerView horReason;
    @BindView(R.id.tv_approver_name)
    TextView tvApproverName;
    @BindView(R.id.my_user_text)
    TextView tvMyName;

    @BindView(R.id.tv_approvaling)
    TextView tvApprovaling;

    @BindView(R.id.submiss_title_view)
    SubTitleView titleView;


    @BindView(R.id.submiss_me_circle_image) //圆形图
            CircleImageView meCircleImage;
    @BindView(R.id.tv_me) //圆形图
            TextView tvMeText;
    @BindView(R.id.submiss_me_time)
    TextView meTime;

    @BindView(R.id.submiss_approver_circle_image) //圆形图
            CircleImageView approverCircleImage;
    @BindView(R.id.tv_approver)
    TextView tvApproverText;
    @BindView(R.id.submiss_approver_time)
    TextView approverTime;

    //只在手机解绑的时候调用隐藏
    @BindView(R.id.lv_list_divider)
    View dividerView;
    @BindView(R.id.lv_list_linear)
    View listLinear;


    @BindView(R.id.tv_over_time_place)
    TextView overTimePlace;
    @BindView(R.id.tv_over_time_name)
    TextView overTimeName;
    @BindView(R.id.tv_place)
    TextView tvPlact;
    @BindView(R.id.tv_place_name)
    TextView tvPlaceName;
    //申请理由
    @BindView(R.id.submiss_reason)
    TextView submissReasonText;
    @BindView(R.id.tv_Reason)
    TextView tvReason;

    @BindView(R.id.tv_copy_text)
    TextView copyText;
    @BindView(R.id.tv_copy_person)
    TextView tvCopyPerson;


    @BindView(R.id.ll_withdraw)
    LinearLayout llWithdraw;
    @BindView(R.id.btn_withdraw)
    Button btnWithdraw;
    @BindView(R.id.btn_urge)
    Button btnUrge;

    @BindView(R.id.tv_agreed_liyou)
    TextView tvAgreedLiyou;
    @BindView(R.id.approver_rl)
    RelativeLayout tvApproverRl;
    @BindView(R.id.approver_msg)
    EditText approverMsgEdit;
    @BindView(R.id.btn_approver_unagreed)
    Button approverUnagreedBtn;
    @BindView(R.id.btn_approver_agreed)
    Button approverAgreedBtn;


    private int company_id; //推送过来的

    private int at;
    private String applyId;
    private SubmissionPicRecyclerAdapter submissionPicAdapter;
    private int applyListValue;
    private int approvalActivityValue;
    private int applyTypeValue;

    @Override
    protected int getActivityView() {
        return R.layout.activity_submission;
    }


    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            at = getIntent().getIntExtra("at", -1);
            applyId = getIntent().getStringExtra("apply_id");
            approvalActivityValue = getIntent().getIntExtra("approvalActivity", -1);
            applyListValue = getIntent().getIntExtra(ApplyListFragment.APPLY_LIST, -1);
            applyTypeValue = getIntent().getIntExtra(ApplyListFragment.APPLY_TYPE, -1);
            company_id = getIntent().getIntExtra("company_id", -1);
        }

        EventBus.getDefault().post(new PushCenterEvent());
    }

    @Override
    protected void initView() {
        if (applyListValue == ApplyListFragment.MY_APPLY || approvalActivityValue == 100) { //我申请的
            tvApproverRl.setVisibility(View.GONE);
            llWithdraw.setVisibility(View.VISIBLE);
        } else { //我审批的
            if (applyTypeValue == 2) {  //我审批的 抄送，不进行审核操作显示
                tvApproverRl.setVisibility(View.GONE);
                llWithdraw.setVisibility(View.GONE);
            } else {
                tvApproverRl.setVisibility(View.VISIBLE);
                llWithdraw.setVisibility(View.GONE);
            }
        }

        initHeader(at);

        horReason.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        submissionPicAdapter = new SubmissionPicRecyclerAdapter(R.layout.item_submission_pic_recycler);
        horReason.setAdapter(submissionPicAdapter);
    }

    private void initHeader(int at) {
        String value = "";
        switch (at) {
            case 1:
                value = "请假";
                break;
            case 2:
                copyText.setText("证明人");
                value = "漏打卡";
                break;
            case 3:
                copyText.setText("证明人");
                value = "消迟到/早退";
                break;
            case 4:
                value = "出差";
                break;
            case 5:
                value = "加班";
                break;
            case 6:
                value = "集体请假";
                break;
            case 7:
                value = "集体出差";
                break;
            case 8:
                value = "调班";
                break;
            case 9:
                value = "外勤";
                break;
            case 10:
                value = "手机解绑";
                break;
            case 21:
                value = "请假";
                break;
            case 22:
                value = "拜访";
                break;
        }
        titleView.setTitleValue(value);
    }

    @Override
    protected void initData() {

        Params params = new Params();
        if (company_id != -1) {
            params.put("ci", company_id);
        }
        params.put("ai", applyId);
        String name = "";
        if (at < 21) {
            name = "read";
        } else {
            name = "readParent";
        }
        RequestUtils.createRequest().postApplyRead(name, params.getData()).enqueue(new RequestCallback<SubmissionBean>(new IRequestListener<SubmissionBean>() {
            @Override
            public void onSuccess(SubmissionBean submissionBean) {
                SubmissionBean.DataBean data = submissionBean.getData();
                if (data != null) {
                    loadData(data);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                ToastUtil.show(errorMsg);
            }
        }));

        if (company_id == -1) {
            return;
        }

        String stringParam = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID);
        final String companyId = company_id + "";
        if (!companyId.equals(stringParam)) {
            new AlertDialog(mContext).builder().setTitle("提示")
                    .setMsg("消息非当前公司，是否切换到该公司")
                    .setPositiveButton("", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            toChangeIm(companyId);
//                            AppSession.getInstance().setCompanyId(companyId);
//                            //刷新首页数据
//                            EventBus.getDefault().post(new MessageCenterEvent());
//                            //刷新我的
//                            EventBus.getDefault().post(new ChangeCompanyEvent());
//                            EventBus.getDefault().post(new PostSuccessEvent());
//                            PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.COMPANY_ID, companyId);
                        }
                    }).setNegativeButton("", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSplashActivity(mContext);
                    finish();
                }
            }).show();
        }
    }

    private void toChangeIm(final String company_id) {
        IMLoginHelper.getInstance().changeUserSignature(mContext, company_id + "", new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                AppSession.getInstance().setCompanyId(company_id);
                //刷新首页数据
                EventBus.getDefault().post(new MessageCenterEvent());
                //刷新我的
                EventBus.getDefault().post(new ChangeCompanyEvent());
                EventBus.getDefault().post(new PostSuccessEvent());
            }

            @Override
            public void onFailure(int code, String errorMsg) {
            }
        });
    }

    protected void loadData(SubmissionBean.DataBean data) {
        initHeader(data.getApply_type());
        String start_time = data.getStart_time(); //申请开始时间
        String end_time = data.getEnd_time(); //申请结束时间
        String add_time = data.getAdd_time(); //申请开始时间
        String opinion = data.getOpinion();
        if (!TextUtils.isEmpty(opinion)) {
            tvAgreedLiyou.setVisibility(View.VISIBLE);
            tvAgreedLiyou.setText("理由：" + opinion);
        } else {
            tvAgreedLiyou.setVisibility(View.GONE);
        }

        submissReasonText.setText("理由");
        //理由应该每个都有
        tvReason.setText(data.getReason());
        //图片
        String img = data.getImg();
        if (!TextUtils.isEmpty(img)) {
            String[] split = img.split(",");
            submissionPicAdapter.replaceData(Arrays.asList(split));
        } else {
            horReason.setVisibility(View.GONE);
        }

        if (at == 2 || at == 3) { //证明人
            String send_name = data.getWitness_name();
            if (TextUtils.isEmpty(send_name)) {  //没有值就应该隐藏
                copyText.setVisibility(View.GONE);
                tvCopyPerson.setVisibility(View.GONE);
            } else {
                tvCopyPerson.setText(send_name);
                tvCopyPerson.setVisibility(View.VISIBLE);
            }
        } else {
            //抄送人
            String send_name = data.getSend_name();
            if (TextUtils.isEmpty(send_name)) { //没有值就应该隐藏
                copyText.setVisibility(View.GONE);
                tvCopyPerson.setVisibility(View.GONE);
            } else {
                tvCopyPerson.setText(send_name);
                tvCopyPerson.setVisibility(View.VISIBLE);
            }
        }

        int status = data.getStatus();
        String uId = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID);
        //审批人item
        SubmissionBean.DataBean.ApproverNameBean approver_name = data.getApprover_name();

        if (status == 3) {
            meTime.setText(DateUtil.getReplaceLineTime(add_time));
            SubmissionBean.DataBean.UserinfoBean userinfo = data.getUserinfo();
            ViewUtil.setColorItemView(userinfo.getImg(), userinfo.getName(), tvApproverText, approverCircleImage);

            if ((userinfo.getUserid() + "").equals(uId)) {
                tvApproverName.setText("我");
            } else {
                tvApproverName.setText(userinfo.getName());
            }
        } else {
            ViewUtil.setColorItemView(approver_name.getImg(), approver_name.getName(), tvApproverText, approverCircleImage);
            //拿当前用户id来对比

            if ((approver_name.getUserid() + "").equals(uId)) {
                tvApproverName.setText("我");
            } else {
                tvApproverName.setText(approver_name.getName());
            }
        }

        //自己的item
        meTime.setText(DateUtil.getReplaceLineTime(add_time));
        SubmissionBean.DataBean.UserinfoBean userinfo = data.getUserinfo();
        ViewUtil.setColorItemView(userinfo.getImg(), userinfo.getName(), tvMeText, meCircleImage);

        if ((userinfo.getUserid() + "").equals(uId)) {
            tvMyName.setText("我");
        } else {
            if (data.getApply_type() == 21 || data.getApply_type() == 22) {
                tvMyName.setText(data.getStudent_name() + "家长\n(" + userinfo.getName() + ")");
            } else {
                tvMyName.setText(userinfo.getName());
            }
        }

//        if (approvalActivityValue == -1) {
        approverTextStatus(data, status);
//        }

        String sign_time = "";
        String sign_name = "";
        String apply_date = "";
        String applyData = "";
        SubmissionBean.ExtraBean extra = null;
        String[] signTime;
        String[] signName;
        ArrayAdapter arrayAdapter = null;
        ArrayList<String> stringList = new ArrayList<>();
        String leaveTime = data.getTime();
        String applyShowTime;

        //集体请假
        String partAndUser = "";
        String group_part;
        String group_user;
        int leave_type;
        switch (data.getApply_type()) {
            case 1: //请假
//                String getidmt = getidmt(start_time, end_time);
//                stringList.add(getidmt);
                if (!TextUtils.isEmpty(leaveTime)) {
                    stringList.add("请假" + leaveTime);
                }
                applyShowTime = DateUtil.getApplyShowTime(start_time, end_time);
                stringList.add(applyShowTime);
                arrayAdapter = new ArrayAdapter(this, R.layout.item_submiss_list, stringList);
                lvListEvent.setAdapter(arrayAdapter);
                leave_type = data.getLeave_type();
                String name = LeaveUtil.getName(leave_type);
                tvContent.setText(name);
                break;
            case 2:
                apply_date = data.getApply_date();
                applyData = DateUtil.getApplyData(apply_date);
                tvContent.setText(applyData);
                extra = data.getExtra();
                sign_time = extra.getSign_time();
                sign_name = extra.getSign_name();
                signTime = sign_time.split(",");
                signName = sign_name.split(",");
                for (int i = 0; i < signTime.length; i++) {
                    stringList.add(signName[i] + "漏打卡 " + signTime[i]);
                }
                arrayAdapter = new ArrayAdapter(this, R.layout.item_submiss_list, stringList);
                lvListEvent.setAdapter(arrayAdapter);
                break;
            case 3:
                apply_date = data.getApply_date();
                applyData = DateUtil.getApplyData(apply_date);
                tvContent.setText(applyData);
                extra = data.getExtra();
                sign_time = extra.getSign_time();
                sign_name = extra.getSign_name();
                signTime = sign_time.split(",");
                signName = sign_name.split(",");
                String sign_status = extra.getSign_status();
                String[] signStaus = sign_status.split(",");
                String late_time = extra.getLate_time();
                String[] lateTime = late_time.split(",");
                for (int i = 0; i < signTime.length; i++) {
                    String time = lateTime[i];
                    String signStau = signStaus[i];
                    if ("5".equals(signStau)) { //迟到
                        stringList.add(signTime[i] + "打卡(迟到 " + time + "分钟)");
                    } else if ("6".equals(signStau)) { //早退
                        stringList.add(signTime[i] + "打卡(早退 " + time + "分钟)");
                    }
                }
                arrayAdapter = new ArrayAdapter(this, R.layout.item_submiss_list, stringList);
                lvListEvent.setAdapter(arrayAdapter);
                break;
            case 4://代码跟外勤基本一样
                overTimePlace.setVisibility(View.VISIBLE);
                overTimePlace.setText("出差地点");
                overTimeName.setVisibility(View.VISIBLE);
                overTimeName.setText(data.getSpace());
                tvContent.setText("出差");
                if (!TextUtils.isEmpty(leaveTime)) {
                    stringList.add("出差" + leaveTime);
                }
                applyShowTime = DateUtil.getApplyShowTime(start_time, end_time);
                stringList.add(applyShowTime);
                arrayAdapter = new ArrayAdapter(this, R.layout.item_submiss_list, stringList);
                lvListEvent.setAdapter(arrayAdapter);
                break;
            case 5:
                tvContent.setText("加班");
//                if (!TextUtils.isEmpty(leaveTime)) {
//                    stringList.add("加班" + leaveTime);
//                }
                applyShowTime = DateUtil.getApplyShowTime(start_time, end_time);
                if (TextUtils.isEmpty(leaveTime)) {
                    stringList.add(LeaveUtil.getOverTimeType(data.getOvertime_type()));
                } else {
                    stringList.add(LeaveUtil.getOverTimeType(data.getOvertime_type()) + "：" + leaveTime);
                }
                stringList.add(applyShowTime);
                arrayAdapter = new ArrayAdapter(this, R.layout.item_submiss_list, stringList);
                lvListEvent.setAdapter(arrayAdapter);
                overTimePlace.setVisibility(View.VISIBLE);
                overTimePlace.setText("加班地点");
                overTimeName.setVisibility(View.VISIBLE);
                overTimeName.setText(LeaveUtil.getOverTimePosition(data.getOvertime_position()));
                break;
            case 6: //集体请假类型
                leave_type = data.getLeave_type();
                tvContent.setText(LeaveUtil.getName(leave_type));

                if (!TextUtils.isEmpty(leaveTime)) {
                    stringList.add("集体请假" + leaveTime);
                }

                overTimePlace.setVisibility(View.VISIBLE);
                overTimePlace.setText("请假人员");
                overTimeName.setVisibility(View.VISIBLE);
                group_part = data.getGroup_part();
                group_user = data.getGroup_user();
                if (!TextUtils.isEmpty(group_part) && !TextUtils.isEmpty(group_user)) {
                    partAndUser = "部门：" + group_part + "\n人员：" + group_user;
                } else if (!TextUtils.isEmpty(group_part)) {
                    partAndUser = "部门：" + group_part;
                } else if (!TextUtils.isEmpty(group_user)) {
                    partAndUser = "人员：" + group_user;
                }

                overTimeName.setText(partAndUser);
                int leave_type1 = data.getLeave_type();
                String name1 = LeaveUtil.getName(leave_type1);
                tvContent.setText(name1);
                applyShowTime = DateUtil.getApplyShowTime(start_time, end_time);
                stringList.add(applyShowTime);
                arrayAdapter = new ArrayAdapter(this, R.layout.item_submiss_list, stringList);
                lvListEvent.setAdapter(arrayAdapter);
                break;
            case 7:
                tvContent.setText("集体出差");
                if (!TextUtils.isEmpty(leaveTime)) {
                    stringList.add("出差" + leaveTime);
                }
                tvPlact.setVisibility(View.VISIBLE);
                tvPlact.setText("出差地点");
                tvPlaceName.setVisibility(View.VISIBLE);
                tvPlaceName.setText(data.getSpace());
                overTimePlace.setVisibility(View.VISIBLE);
                overTimePlace.setText("出差人员");
                overTimeName.setVisibility(View.VISIBLE);
                group_part = data.getGroup_part();
                group_user = data.getGroup_user();
                if (!TextUtils.isEmpty(group_part) && !TextUtils.isEmpty(group_user)) {
                    partAndUser = "部门：" + group_part + "\n人员：" + group_user;
                } else if (!TextUtils.isEmpty(group_part)) {
                    partAndUser = "部门：" + group_part;
                } else if (!TextUtils.isEmpty(group_user)) {
                    partAndUser = "人员：" + group_user;
                }
                overTimeName.setText(partAndUser);

                applyShowTime = DateUtil.getApplyShowTime(start_time, end_time);
                stringList.add(applyShowTime);
                arrayAdapter = new ArrayAdapter(this, R.layout.item_submiss_list, stringList);
                lvListEvent.setAdapter(arrayAdapter);
                break;
            case 8:
                tvContent.setText("调班");
                tvPlact.setVisibility(View.VISIBLE);
                tvPlact.setText("与他调班");
                tvPlaceName.setVisibility(View.VISIBLE);

                extra = data.getExtra();
                String ex_start_date = extra.getEx_start_date();
                String ex_end_date = extra.getEx_end_date();
                tvPlaceName.setText(approver_name.getName() + "\t\t\t" + DateUtil.getApplyChangeShiftTime(ex_start_date, ex_end_date));
                String my_start_date = extra.getMy_start_date();
                String my_end_date = extra.getMy_end_date();
                String applyChangeShiftTime = DateUtil.getApplyChangeShiftTime(my_start_date, my_end_date);
                stringList.add("调班日期");
                stringList.add(applyChangeShiftTime);
                arrayAdapter = new ArrayAdapter(this, R.layout.item_submiss_list, stringList);
                lvListEvent.setAdapter(arrayAdapter);

                overTimePlace.setVisibility(View.VISIBLE);
                overTimePlace.setText("调换班次");
                overTimeName.setVisibility(View.VISIBLE);
                overTimeName.setText(extra.getMy_class_name() + "\t\t更换成\t\t" + extra.getEx_class_name());

                break;
            case 9: //代码跟出差基本一样
                if (!TextUtils.isEmpty(leaveTime)) {
                    stringList.add("外勤" + leaveTime);
                }
                overTimePlace.setVisibility(View.VISIBLE);
                overTimePlace.setText("外勤地点");
                overTimeName.setVisibility(View.VISIBLE);
                overTimeName.setText(data.getSpace());
                tvContent.setText("外勤");
                applyShowTime = DateUtil.getApplyShowTime(start_time, end_time);
                stringList.add(applyShowTime);
                arrayAdapter = new ArrayAdapter(this, R.layout.item_submiss_list, stringList);
                lvListEvent.setAdapter(arrayAdapter);
                break;
            case 10:
                dividerView.setVisibility(View.GONE);
                listLinear.setVisibility(View.GONE);
                tvContent.setText("手机解绑");
                overTimePlace.setVisibility(View.VISIBLE);
                overTimePlace.setText("解绑手机");
                overTimeName.setVisibility(View.VISIBLE);
                overTimeName.setText(data.getExtra().getDevice_name());
                break;
            case 21: //家长请假
                if (!TextUtils.isEmpty(leaveTime)) {
                    stringList.add("请假" + leaveTime);
                }
                applyShowTime = DateUtil.getApplyShowTime(start_time, end_time);
                stringList.add(applyShowTime);
                arrayAdapter = new ArrayAdapter(this, R.layout.item_submiss_list, stringList);
                lvListEvent.setAdapter(arrayAdapter);
                leave_type = data.getLeave_type();
                String parentName = LeaveUtil.getParentName(leave_type);
                tvContent.setText(parentName);
                tvContent.setText(data.getStudent_name() + "家长\n(" + userinfo.getName() + ")" + parentName);
                break;
            case 22: //家长拜访
                String visit_time = data.getVisit_time();
                if (!TextUtils.isEmpty(visit_time) && visit_time.length() == 19) {
                    visit_time = visit_time.substring(0, 16);
                }
                stringList.add(visit_time);
                arrayAdapter = new ArrayAdapter(this, R.layout.item_submiss_list, stringList);
                lvListEvent.setAdapter(arrayAdapter);

                String parentUserName = userinfo.getName();
                String student_name = data.getStudent_name();
                tvContent.setText(student_name + "家长\n(" + parentUserName + ")拜访");
                break;
        }
    }

    private void approverTextStatus(SubmissionBean.DataBean data, int status) {
        // 0:待审核 1:审核通过 2:审核不通过 3:撤销
        String value = "";
        switch (status) {
            case 1:
                llWithdraw.setVisibility(View.GONE);
                tvApproverRl.setVisibility(View.GONE);
                value = "已同意";
                tvApprovaling.setTextColor(getResources().getColor(R.color.lan));
                approverTime.setText(DateUtil.getReplaceLineTime(data.getReview_time()));
                break;
            case 2:
                llWithdraw.setVisibility(View.GONE);
                tvApproverRl.setVisibility(View.GONE);
                value = "已驳回";
                tvApprovaling.setTextColor(getResources().getColor(R.color.dot_red));
                approverTime.setText(DateUtil.getReplaceLineTime(data.getReview_time()));
                break;
            case 3:
                llWithdraw.setVisibility(View.GONE);
                tvApproverRl.setVisibility(View.GONE);
                value = "已撤回";
                tvApprovaling.setTextColor(getResources().getColor(R.color.dot_red));
                approverTime.setText(DateUtil.getReplaceLineTime(data.getReview_time()));
                break;
            default:
                value = "审批中";
                if (applyListValue != ApplyListFragment.MY_APPLY) {
                    String approverIds = data.getApprover_id();
                    boolean isSkip = false;
                    if (!TextUtils.isEmpty(approverIds) && approvalActivityValue != 100) {
                        String[] split = approverIds.split(",");
                        for (String s : split) {
                            String userId = AppSession.getInstance().getUserId();
                            if (!TextUtils.isEmpty(s) && s.equals(userId)) {
                                llWithdraw.setVisibility(View.GONE);
                                tvApproverRl.setVisibility(View.VISIBLE);
                                isSkip = true;
                                break;
                            }
                        }
                    }
                    if (!isSkip) {
                        tvApproverRl.setVisibility(View.GONE);
                    }
                }

                approverTime.setText(DateUtil.getSubmissionData(data.getAdd_time()));
                break;
        }
        tvApprovaling.setText(value);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        //驳回
        approverUnagreedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unagreed();
            }
        });

        //同意
        approverAgreedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agreed();
            }
        });

        //测回
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        //催办
        btnUrge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urge();
            }
        });

        titleView.getLeftText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSplashActivity(mContext);
                finish();
            }
        });
    }


    private void showDialog() {
        new AlertDialog(mContext)
                .builder()
                .setTitle("撤回")
                .setMsg("你确定撤回本次申请吗？")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modifyApply(TYPE_UNDO);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    //不同意
    private void unagreed() {
        String value = approverMsgEdit.getText().toString();
        if (TextUtils.isEmpty(value)) {
            modifyApply(TYPE_REJECTED);
        } else {
            modifyApply(TYPE_REJECTED, value);
        }
    }

    //不同意
    private void agreed() {
        String value = approverMsgEdit.getText().toString();
        if (TextUtils.isEmpty(value)) {
            modifyApply(TYPE_AGREED);
        } else {
            modifyApply(TYPE_AGREED, value);
        }
    }

    //催办
    private void urge() {
        modifyApply(TYPE_CUIBAN);
    }


    public static final int TYPE_AGREED = 1;
    public static final int TYPE_REJECTED = 2;
    public static final int TYPE_UNDO = 3;
    public static final int TYPE_CUIBAN = 4;

    //	修改类型(1:同意 2:驳回 3:撤销 4:催办)
    //修改申请状态
    private void modifyApply(final int type, final String msg) {
        Params params = new Params();
        params.put("ai", applyId);
        params.put("tp", type);
        params.put("on", msg);
        String name = "";
        if (at < 21) {
            name = "modify";
        } else {
            name = "modifyParent";
        }
        RequestUtils.createRequest().postApplyModify(name, params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
//                showToast(baseModel.msg);
                //刷新外面的状态
                EventBus.getDefault().post(new OneKeyEvent());
                initData();
                if (TYPE_CUIBAN == type) {
                    showToast("催办成功");
                } else if (TYPE_UNDO == type) {
                    showToast("撤回成功");
                }
//                handleBottomStatus(type, msg);
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    private void handleBottomStatus(int type, String msg) {
        switch (type) {
            case TYPE_AGREED:
                tvApproverRl.setVisibility(View.GONE);
                tvApprovaling.setTextColor(getResources().getColor(R.color.lan));
                tvApprovaling.setText("已同意");
                break;
            case TYPE_REJECTED:
                tvApproverRl.setVisibility(View.GONE);
                tvApprovaling.setTextColor(getResources().getColor(R.color.lan));
                tvApprovaling.setText("已驳回");
                break;
            case TYPE_UNDO:
                llWithdraw.setVisibility(View.GONE);
                tvApprovaling.setTextColor(getResources().getColor(R.color.dot_red));
                tvApprovaling.setText("已撤回");
                break;
            case TYPE_CUIBAN:
                showToast("催办成功");
                break;
        }

        approverTime.setText(DateUtil.getReplaceLineTime(System.currentTimeMillis()));

        if (!TextUtils.isEmpty(msg)) {
            tvAgreedLiyou.setVisibility(View.VISIBLE);
            tvAgreedLiyou.setText("理由：" + msg);
        } else {
            tvAgreedLiyou.setVisibility(View.GONE);
        }
    }

    private void modifyApply(int type) {
        modifyApply(type, "");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openSplashActivity(mContext);
        finish();
    }
}
