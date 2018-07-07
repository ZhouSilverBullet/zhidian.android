package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.MsgApprovalBean;
import com.sdxxtop.zhidian.eventbus.MessageCenterEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.ApplyListFragment;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.LeaveUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.widget.SubTitleView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MessageCenterApprovalActivity extends BaseActivity {

    private boolean isFirst;
    @BindView(R.id.message_center_approval_title_view)
    SubTitleView titleView;
    @BindView(R.id.message_center_approval_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.message_center_approval_refresh)
    SmartRefreshLayout refreshLayout;
    private MessageApprovalAdapter mAdapter;

    @Override
    protected int getActivityView() {
        return R.layout.activity_message_center_approval;
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initEvent() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (mAdapter != null) {
                    loadData(mAdapter.getData().size());
                }
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData(0);
            }
        });
    }

    @Override
    protected void initData() {
        loadData(0);
    }

    private void loadData(final int page) {
        Params params = new Params();
        params.put("sp", page);
        RequestUtils.createRequest().postMsgApply(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<MsgApprovalBean>() {
            @Override
            public void onSuccess(MsgApprovalBean msgNoticeBean) {

                if (!isFirst) { //通知主页刷新
                    isFirst = true;
                    EventBus.getDefault().post(new MessageCenterEvent());
                }
                MsgApprovalBean.DataBean data = msgNoticeBean.getData();
                if (data != null && data.getApply() != null) {
                    List<MsgApprovalBean.DataBean.ApplyBean> notice = data.getApply();
                    if (mAdapter == null) {
                        mAdapter = new MessageApprovalAdapter(R.layout.item_message_center_approval_recycler);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.addData(notice);
                    } else {
                        if (page == 0) {
                            mAdapter.replaceData(notice);
                        } else {
                            mAdapter.addData(notice);
                        }
                    }
                }
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    class MessageApprovalAdapter extends BaseQuickAdapter<MsgApprovalBean.DataBean.ApplyBean, BaseViewHolder> {
        public MessageApprovalAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final MsgApprovalBean.DataBean.ApplyBean item) {
            TextView time = helper.getView(R.id.item_message_center_approval_time);
            TextView title = helper.getView(R.id.item_message_center_approval_title);
            ListView contentListView = helper.getView(R.id.item_message_center_approval_content_list);
            contentListView.setEnabled(false);
            contentListView.setClickable(false);
            TextView opText = helper.getView(R.id.item_message_center_approval_op_text);
            LinearLayout agreeLayout = helper.getView(R.id.item_message_center_approval_gree_layout);
            Button unagreed = helper.getView(R.id.item_message_center_approval_unagreed);
            Button agreed = helper.getView(R.id.item_message_center_approval_agreed);

            title.setText(item.getTitle());
            time.setText(DateUtil.getShowTime(item.getAdd_time()));

            int status = item.getStatus();
            int reviewer_id = item.getReviewer_id();
            final int at = item.getApply_type();
            final int adapterPosition = helper.getAdapterPosition();

            switch (status) {
                case 1:
                    opText.setVisibility(View.VISIBLE);
                    agreeLayout.setVisibility(View.GONE);
//                    value = "已同意";
                    if (isSomeId(reviewer_id)) {
                        opText.setText("已审核 (通过)");
                    } else {
                        opText.setText("已被" + item.getReviewer_name() + "审核 (通过)");
                    }
                    opText.setTextColor(mContext.getResources().getColor(R.color.blue));
                    break;
                case 2:
                    if (isSomeId(reviewer_id)) {
                        opText.setText("已审核 (驳回)");
                    } else {
                        opText.setText("已被" + item.getReviewer_name() + "审核 (驳回)");
                    }
                    opText.setTextColor(mContext.getResources().getColor(R.color.dot_red));
                    opText.setVisibility(View.VISIBLE);
                    agreeLayout.setVisibility(View.GONE);
//                    value = "驳回";
                    break;
                case 3:
                    opText.setVisibility(View.VISIBLE);
                    agreeLayout.setVisibility(View.GONE);
                    opText.setTextColor(mContext.getResources().getColor(R.color.dot_red));
                    opText.setText("已撤回");
//                    value = "已撤回";
                    break;
                default:
//                    value = "审批中";
                    opText.setVisibility(View.GONE);
                    String approver_id = item.getApprover_id();
                    String stringParam = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID);
                    if (!TextUtils.isEmpty(approver_id)) {
                        agreeLayout.setVisibility(View.GONE);
                        String[] split = approver_id.split(",");
                        for (String s : split) {
                            if (!TextUtils.isEmpty(s) && s.equals(stringParam)) {
                                agreeLayout.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                    } else {
                        agreeLayout.setVisibility(View.GONE);
                    }
                    unagreed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            modifyApply(2, at, item, adapterPosition);
                        }
                    });

                    agreed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            modifyApply(1, at, item, adapterPosition);
                        }
                    });
                    break;
            }


            String start_time = item.getStart_time();
            String end_time = item.getEnd_time();

            String value = "";
            ArrayAdapter arrayAdapter;
            List<String> stringList;
            String applyShowTime;
            MsgApprovalBean.ExtraBean extra = item.getExtra();
            String apply_date;
            String sign_name;
            String sign_time;
            String[] signTime;
            String[] signName;

            String group_part;
            String group_user;
            String partAndUser = "";
            switch (at) {
                case 1:
                    // value = "请假";
                    stringList = new ArrayList<>();
                    applyShowTime = DateUtil.getApplyShowTime(start_time, end_time);
                    String name = LeaveUtil.getName(item.getLeave_type());
                    stringList.add("类型：" + name);
                    stringList.add("时间：" + applyShowTime);
                    stringList.add("时长：共" + item.getTime());
                    stringList.add("理由：" + item.getReason());
                    arrayAdapter = new ArrayAdapter(mContext, R.layout.item_submiss_list2, stringList);
                    contentListView.setAdapter(arrayAdapter);
                    break;
                case 2:
//                    value = "漏打卡";
                    stringList = new ArrayList<>();
                    apply_date = item.getApply_date();
                    stringList.add("时间：" + DateUtil.getApplyData0(apply_date));
                    stringList.add("漏打卡：" + extra.getSign_name());
                    stringList.add("理由：" + item.getReason());
                    arrayAdapter = new ArrayAdapter(mContext, R.layout.item_submiss_list2, stringList);
                    contentListView.setAdapter(arrayAdapter);
                    break;
                case 3:
//                    value = "迟到早退";
                    sign_time = extra.getSign_time();
                    sign_name = extra.getSign_name();
                    signTime = sign_time.split(",");
                    signName = sign_name.split(",");
                    String sign_status = extra.getSign_status();
                    String[] signStaus = sign_status.split(",");
                    String late_time = extra.getLate_time();
                    String[] lateTime = late_time.split(",");
                    stringList = new ArrayList<>();
                    apply_date = item.getApply_date();
                    stringList.add("时间：" + DateUtil.getApplyData0(apply_date));
                    for (int i = 0; i < signTime.length; i++) {
                        String times = lateTime[i];
                        String signStau = signStaus[i];
                        if ("5".equals(signStau)) { //迟到
                            stringList.add(/*signName[i] + */"迟到：" + times + "分钟");
                        } else if ("6".equals(signStau)) { //早退
                            stringList.add(/*signName[i] + */"早退：" + times + "分钟");
                        }
                    }
                    stringList.add("理由：" + item.getReason());
                    arrayAdapter = new ArrayAdapter(mContext, R.layout.item_submiss_list2, stringList);
                    contentListView.setAdapter(arrayAdapter);
                    break;
                case 4:
//                    value = "出差";
                    stringList = new ArrayList<>();
                    applyShowTime = DateUtil.getApplyShowTime(start_time, end_time);
                    stringList.add("地点：" + item.getSpace());
                    stringList.add("时间：" + applyShowTime);
//                    if (!TextUtils.isEmpty(item.getTime())) {
//                        stringList.add("时间：" + item.getTime());
//                    }
                    stringList.add("理由：" + item.getReason());
                    arrayAdapter = new ArrayAdapter(mContext, R.layout.item_submiss_list2, stringList);
                    contentListView.setAdapter(arrayAdapter);
                    break;
                case 5:
//                    value = "加班";
                    stringList = new ArrayList<>();
                    applyShowTime = DateUtil.getApplyShowTime(start_time, end_time);
                    stringList.add("类型：" + LeaveUtil.getOverTimeType(item.getOvertime_type()));
                    stringList.add("地方：" + LeaveUtil.getOverTimePosition(item.getOvertime_position()));
                    stringList.add("时间：" + applyShowTime);
//                    if (!TextUtils.isEmpty(item.getTime())) {
//                        stringList.add("时间：" + item.getTime());
//                    }
                    stringList.add("理由：" + item.getReason());
                    arrayAdapter = new ArrayAdapter(mContext, R.layout.item_submiss_list2, stringList);
                    contentListView.setAdapter(arrayAdapter);
                    break;
                case 6:
//                    value = "集体请假";
                    group_part = item.getGroup_part();
                    group_user = item.getGroup_user();
                    if (!TextUtils.isEmpty(group_part) && !TextUtils.isEmpty(group_user)) {
                        partAndUser = "部门:" + group_part + "\n人员：" + group_user;
                    } else if (!TextUtils.isEmpty(group_part)) {
                        partAndUser = "部门:" + group_part;
                    } else if (!TextUtils.isEmpty(group_user)) {
                        partAndUser = "人员：" + group_user;
                    }

                    int leave_type1 = item.getLeave_type();
                    String name1 = LeaveUtil.getName(leave_type1);
                    applyShowTime = DateUtil.getApplyShowTime(start_time, end_time);
                    stringList = new ArrayList<>();
                    stringList.add("类型：" + name1);
                    stringList.add("人员：" + partAndUser);
//                    if (!TextUtils.isEmpty(item.getTime())) {
//                        stringList.add("时间：" + item.getTime());
//                    }
                    stringList.add("时间：" + applyShowTime);
                    stringList.add("理由：" + item.getReason());
                    arrayAdapter = new ArrayAdapter(mContext, R.layout.item_submiss_list2, stringList);
                    contentListView.setAdapter(arrayAdapter);
                    break;
                case 7:
//                    value = "集体出差";

                    group_part = item.getGroup_part();
                    group_user = item.getGroup_user();
                    if (!TextUtils.isEmpty(group_part) && !TextUtils.isEmpty(group_user)) {
                        partAndUser = "部门:" + group_part + "\n人员：" + group_user;
                    } else if (!TextUtils.isEmpty(group_part)) {
                        partAndUser = "部门:" + group_part;
                    } else if (!TextUtils.isEmpty(group_user)) {
                        partAndUser = "人员：" + group_user;
                    }

                    applyShowTime = DateUtil.getApplyShowTime(start_time, end_time);
                    stringList = new ArrayList<>();
                    stringList.add("地点：" + item.getSpace());
                    stringList.add("人员：" + partAndUser);
//                    if (!TextUtils.isEmpty(item.getTime())) {
//                        stringList.add("时间：" + item.getTime());
//                    }
                    stringList.add("时间：" + applyShowTime);
                    stringList.add("理由：" + item.getReason());
                    arrayAdapter = new ArrayAdapter(mContext, R.layout.item_submiss_list2, stringList);
                    contentListView.setAdapter(arrayAdapter);
                    break;
                case 8:
//                    value = "调班";

                    String ex_start_date = extra.getEx_start_date();
                    String ex_end_date = extra.getEx_end_date();
                    String my_start_date = extra.getMy_start_date();
                    String my_end_date = extra.getMy_end_date();
                    String applyChangeShiftTime = DateUtil.getApplyChangeShiftTime(my_start_date, my_end_date);

                    stringList = new ArrayList<>();
                    stringList.add("日期：" + applyChangeShiftTime);
                    stringList.add("与他调换：" + item.getApprover_name() + "\t\t时间：" + DateUtil.getApplyChangeShiftTime(ex_start_date, ex_end_date));
                    stringList.add("调换班次：" + extra.getMy_class_name() + "\t\t更换成\t\t" + extra.getEx_class_name());
                    stringList.add("理由：" + item.getReason());
                    arrayAdapter = new ArrayAdapter(mContext, R.layout.item_submiss_list2, stringList);
                    contentListView.setAdapter(arrayAdapter);
                    break;
                case 9:
//                    value = "考勤";
                    applyShowTime = DateUtil.getApplyShowTime(start_time, end_time);
                    stringList = new ArrayList<>();
                    stringList.add("地点：" + item.getSpace());
//                    if (!TextUtils.isEmpty(item.getTime())) {
//                        stringList.add("时间：" + item.getTime());
//                    }
                    stringList.add("时间：" + applyShowTime);
                    stringList.add("理由：" + item.getReason());
                    arrayAdapter = new ArrayAdapter(mContext, R.layout.item_submiss_list2, stringList);
                    contentListView.setAdapter(arrayAdapter);
                    break;
                case 10:
//                    value = "手机解绑";
                    stringList = new ArrayList<>();
                    stringList.add("解绑手机：" + extra.getDevice_name());
                    stringList.add("理由：" + item.getReason());
                    arrayAdapter = new ArrayAdapter(mContext, R.layout.item_submiss_list2, stringList);
                    contentListView.setAdapter(arrayAdapter);
                    break;
            }

            final int apply_type = item.getApply_type();
            final int apply_id = item.getApply_id();
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SubmissionActivity.class);
                    intent.putExtra("at", apply_type);
                    intent.putExtra(ApplyListFragment.APPLY_LIST, ApplyListFragment.MY_APPROVAL);

                    //判断是不是抄送
                    String approver_id = item.getApprover_id();
                    String stringParam = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID);
                    boolean isApprover = false;
                    if (!TextUtils.isEmpty(approver_id)) {
                        String[] split = approver_id.split(",");
                        for (String s : split) {
                            if (!TextUtils.isEmpty(s) && s.equals(stringParam)) {
                                isApprover = true;
                                break;
                            }
                        }
                    }
                    if (!isApprover) { //抄送
                        intent.putExtra(ApplyListFragment.APPLY_TYPE, 2);
                    }

                    intent.putExtra("apply_id", apply_id + "");
                    startActivity(intent);
                }
            });
        }

        private void modifyApply(final int type, final int applyId, final MsgApprovalBean.DataBean.ApplyBean item, final int adapterPosition) {
            Params params = new Params();
            params.put("ai", item.getApply_id());
            params.put("tp", type);
//          params.put("on", "");
            RequestUtils.createRequest().postApplyModify(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
                @Override
                public void onSuccess(BaseModel baseModel) {
                    showToast(baseModel.msg);
                    if (type == 1) {
                        item.setStatus(1);
                    } else {
                        item.setStatus(2);
                    }
                    String ui = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID);
                    item.setReviewer_id(Integer.valueOf(ui));
                    notifyItemChanged(adapterPosition);
//                loadData();
                }

                @Override
                public void onFailure(int code, String errorMsg) {
                    showToast(errorMsg);
                }
            }));
        }
    }

    private boolean isSomeId(int reviewer_id) {
        if ((reviewer_id + "").equals(PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID))) {
            return true;
        }
        return false;
    }
}
