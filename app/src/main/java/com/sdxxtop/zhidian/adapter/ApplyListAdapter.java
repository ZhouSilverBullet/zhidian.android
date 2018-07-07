package com.sdxxtop.zhidian.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ApplyListBean;
import com.sdxxtop.zhidian.entity.DateBean;
import com.sdxxtop.zhidian.ui.activity.SubmissionActivity;
import com.sdxxtop.zhidian.ui.fragment.ApplyListFragment;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.LeaveUtil;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.widget.CircleTextImageView;

/**
 * Created by Administrator on 2018/5/8.
 */

public class ApplyListAdapter extends BaseQuickAdapter<ApplyListBean.DataBean.ApplyBean, BaseViewHolder> {

    public static final int TYPE_LEAVE = 1; //请假
    public static final int TYPE_LEAKAGE = 2; //漏打卡
    public static final int TYPE_LATE = 3; //消迟到
    public static final int TYPE_BUSINESS = 4; //出差
    public static final int YEAR_WORK_OVERTIME = 5; //加班
    public static final int YEAR_LEAVE_COLLECTIVE = 6; //集体请假
    public static final int TYPE_BUSINESS_COLLECTIVE = 7; //集体出差
    public static final int TYPE_IN_CLASS = 8; //调班
    public static final int YEAR_FIELD = 9; //外勤
    public static final int YEAR_UNBUNDING_PHONE = 10; //手机解绑

    private String path;
    private boolean isShowSelect;
    private int status;

    public void setPath(String path) {
        this.path = path;
    }

    public void setIsShow(boolean isShowSelect) {
        this.isShowSelect = isShowSelect;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ApplyListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ApplyListBean.DataBean.ApplyBean item) {
//        TextView leftText = helper.getView(R.id.item_apply_list_fragment_recycler_left_text);
        final TextView oneKeySelect = helper.getView(R.id.item_one_key_select);
        TextView titleText = helper.getView(R.id.item_apply_list_fragment_recycler_title);
        TextView statusText = helper.getView(R.id.item_apply_list_fragment_recycler_status);
        TextView opinionText = helper.getView(R.id.item_apply_list_fragment_recycler_opinion);
        TextView timeText = helper.getView(R.id.item_apply_list_fragment_recycler_time);

        String name = item.getName();
        CircleTextImageView leftText = helper.getView(R.id.item_apply_list_fragment_recycler_left_text);
        leftText.setTextColor(mContext.getResources().getColor(R.color.white));
        leftText.setBorderOverlay(false);
        String img = item.getImg();
        if (!TextUtils.isEmpty(img)) {
            if (img.startsWith("#")) {
                leftText.setImageResource(0);
                leftText.setFillColor(Color.parseColor(img));
                String textString = StringUtil.stringSubName(name);
                leftText.setText(textString);
            } else {
                leftText.setText("");
                Glide.with(mContext).load(img).into(leftText);
            }
        }

        //一分钟之内叫刚刚
        String showTime = DateUtil.getShowTime(item.getAdd_time());
        timeText.setText(showTime);


        //(0:待审核 1:审核通过 2:审核不通过 3:撤销)
        final boolean isApproval = "approval".equals(path);
        if (isApproval) {  //我审批的
            opinionText.setVisibility(View.VISIBLE);
            int status = item.getStatus();
            switch (status) { //(0:待审核 1:审核通过 2:审核不通过 3:撤销)
                case 0:
                    opinionText.setText("未审核");
                    opinionText.setTextColor(mContext.getResources().getColor(R.color.blue));
                    opinionText.setBackgroundResource(R.drawable.item_apply_list_recycler_blue_bg);
                    break;
                case 1:
                    opinionText.setText("同意");
                    opinionText.setTextColor(mContext.getResources().getColor(R.color.notice_contentcolor));
                    opinionText.setBackgroundResource(R.drawable.item_apply_list_recycler_gray_bg);
                    break;
                case 2:
                    opinionText.setText("驳回");
                    opinionText.setTextColor(mContext.getResources().getColor(R.color.text_color_red));
                    opinionText.setBackgroundResource(R.drawable.item_apply_list_recycler_red_bg);
                    break;
                case 3:
                    opinionText.setTextColor(mContext.getResources().getColor(R.color.text_color_red));
                    opinionText.setBackgroundResource(R.drawable.item_apply_list_recycler_red_bg);
                    opinionText.setText("撤回");
                    break;
            }

            int applyType = item.getApply_type();
            String titleValue = "时间：";
            switch (applyType) {
                case TYPE_LEAVE:
                    String overTimeTypeValue = LeaveUtil.getName(item.getLeave_type());
                    handleApprovalTitleView(titleText, name, overTimeTypeValue);
                    handleStartAndEndDate(statusText, titleValue, item);
                    break;
                case TYPE_BUSINESS:
                    handleApprovalTitleView(titleText, name, "出差");
                    handleStartAndEndDate(statusText, titleValue, item);
                    break;
                case YEAR_WORK_OVERTIME:
                    handleApprovalTitleView(titleText, name, "加班");
                    handleStartAndEndDate(statusText, titleValue, item);
                    break;
                case YEAR_LEAVE_COLLECTIVE:
                    handleApprovalTitleView(titleText, name, "集体请假");
                    handleStartAndEndDate(statusText, titleValue, item);
                    break;
                case TYPE_BUSINESS_COLLECTIVE:
                    handleApprovalTitleView(titleText, name, "集体出差");
                    handleStartAndEndDate(statusText, titleValue, item);
                    break;
                case TYPE_IN_CLASS:
                    handleApprovalTitleView(titleText, name, "调班");
                    handleStartAndEndDate(statusText, titleValue, item);
                    break;
                case YEAR_FIELD:
                    handleApprovalTitleView(titleText, name, "外勤");
                    handleStartAndEndDate(statusText, titleValue, item);
                    break;
                case TYPE_LEAKAGE:
                    handleApprovalTitleView(titleText, name, "漏打卡");
                    statusText.setText("时间：" + DateUtil.getApplyListLeaveCardTime(item.getApply_date()));
//                    handleStartAndEndDate(statusText, titleValue, item);
                    break;
                case TYPE_LATE:
                    handleApprovalTitleView(titleText, name, "消迟到");
                    statusText.setText("时间：" + item.getTime() + "分钟");
                    break;
                case YEAR_UNBUNDING_PHONE:
                    handleApprovalTitleView(titleText, name, "手机解绑");
                    statusText.setText("解绑手机：" + item.getExtra());
                    break;
            }


        } else { //我申请的
            int status = item.getStatus();
            switch (status) { //(0:待审核 1:审核通过 2:审核不通过 3:撤销)
                case 0:
                    statusText.setText("审批中");
                    statusText.setTextColor(getColor(R.color.texthintcolor));
                    break;
                case 1:
                    statusText.setText(item.getName() + "审核通过");
                    statusText.setTextColor(getColor(R.color.lan));
                    break;
                case 2:
                    statusText.setText(item.getName() + "已驳回");
                    statusText.setTextColor(getColor(R.color.dot_red));
                    break;
                case 3:
                    statusText.setText("已撤回");
                    statusText.setTextColor(getColor(R.color.dot_red));
                    break;
            }


            int applyType = item.getApply_type();
            String titleValue = "";
            switch (applyType) {
                case TYPE_LEAVE:
                    titleValue = "请假：";
                    handleStartAndEndDate(titleText, titleValue, item);
                    break;
                case TYPE_BUSINESS:
                    titleValue = "出差：";
                    handleStartAndEndDate(titleText, titleValue, item);
                    break;
                case YEAR_WORK_OVERTIME:
                    titleValue = "加班：";
                    handleStartAndEndDate(titleText, titleValue, item);
                    break;
                case YEAR_LEAVE_COLLECTIVE:
                    titleValue = "集体请假：";
                    handleStartAndEndDate(titleText, titleValue, item);
                    break;
                case TYPE_BUSINESS_COLLECTIVE:
                    titleValue = "集体出差：";
                    handleStartAndEndDate(titleText, titleValue, item);
                    break;
                case TYPE_IN_CLASS:
                    titleValue = "调班：";
                    handleStartAndEndDate(titleText, titleValue, item);
                    break;
                case YEAR_FIELD:
                    titleValue = "外勤：";
                    handleStartAndEndDate(titleText, titleValue, item);
                    break;
                case TYPE_LEAKAGE:
                    titleText.setText("漏打卡：" + DateUtil.getApplyListLeaveCardTime(item.getApply_date()));
                    break;
                case TYPE_LATE:
                    titleText.setText("消迟到：" + item.getTime() + "分钟");
                    break;
                case YEAR_UNBUNDING_PHONE:
                    titleText.setText("手机解绑：" + item.getExtra());
                    break;
            }

            titleText.requestLayout();
        }

        final int apply_id = item.getApply_id();
        final int apply_type = item.getApply_type();
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SubmissionActivity.class);
                intent.putExtra("at", apply_type);
                if (isApproval) {
                    intent.putExtra(ApplyListFragment.APPLY_LIST, ApplyListFragment.MY_APPROVAL);
                    intent.putExtra(ApplyListFragment.APPLY_TYPE, status);
                } else {
                    intent.putExtra(ApplyListFragment.APPLY_LIST, ApplyListFragment.MY_APPLY);
                }
                intent.putExtra("apply_id", apply_id + "");
                mContext.startActivity(intent);
            }
        });


        if (!isShowSelect) { //是选择逻辑往下面走
            return;
        }
        //选择的逻辑
        oneKeySelect.setVisibility(View.VISIBLE);
        if (item.isCheck()) {
            oneKeySelect.setBackgroundResource(R.mipmap.select);
        } else {
            oneKeySelect.setBackgroundResource(R.mipmap.unselected);
        }

        oneKeySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = item.isCheck();
                setCheck(oneKeySelect, !check);
                item.setCheck(!check);
                notifyDataSetChanged();
                if (checkAllListener != null) {
                    checkAllListener.onCheckAll();
                }
            }
        });

    }

    private int getColor(int color) {
        return mContext.getResources().getColor(color);
    }

    private CheckAllListener checkAllListener;

    public void setCheckAllListener(CheckAllListener checkAllListener) {
        this.checkAllListener = checkAllListener;
    }

    //没选择的时候回调一次判断是否是全选
    public interface CheckAllListener {
        void onCheckAll();
    }

    private void setCheck(TextView textView, boolean isCheck) {
        textView.setBackgroundResource(isCheck ?
                R.mipmap.select : R.mipmap.unselected);
    }

    private void handleStartAndEndDate(TextView textView, String titleValue, ApplyListBean.DataBean.ApplyBean item) {
        DateBean startDate = DateUtil.getDateBean(item.getStart_time());
        DateBean endDate = DateUtil.getDateBean(item.getEnd_time());

        if (startDate != null && endDate != null) {
            if (startDate.year == endDate.year && startDate.month == endDate.month && startDate.day == endDate.day) {
                textView.setText(titleValue + startDate.month + "月" + startDate.day + "日");
            } else if (startDate.year == endDate.year && startDate.month == endDate.month) {
                textView.setText(titleValue + startDate.month + "月" + startDate.day + "日" + "-" + endDate.day + "日");
            } else if (startDate.year == endDate.year) {
                textView.setText(titleValue + startDate.month + "月" + startDate.day + "日" + "-" + endDate.month + "月" + endDate.day + "日");
            } else {
                textView.setText(titleValue + startDate.year + "年" + startDate.month + "月" + startDate.day + "日" + "-" + endDate.year + "年" + endDate.month + "月" + endDate.day + "日");
            }
        }

    }


    private void handleApprovalTitleView(TextView textView, String name, String titleValue) {
        textView.setText(name + "的" + titleValue + "申请");
        textView.requestLayout();
    }

}
