package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ContactPartBean;
import com.sdxxtop.zhidian.entity.SchedulingManageBean;
import com.sdxxtop.zhidian.eventbus.SchedulingFinishEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.IRequestListener2;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestCallback2;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import zhangphil.iosdialog.widget.AlertDialog;

public class SchedulingManageActivity extends BaseActivity {

    public static final int MANAGER_SKIP = 100;

    @BindView(R.id.scheduling_title_view)
    SubTitleView titleView;
    @BindView(R.id.scheduling_recycler)
    RecyclerView recyclerView;
    private SchedulingAdapter mAdapter;

    @Override
    protected int getActivityView() {
        return R.layout.activity_scheduling_manage;
    }

    @Override
    protected void initView() {

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SchedulingAdapter(R.layout.item_scheduling_manage_recycler);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        loadData(0);
    }

    private void loadData(final int type) {
        Params params = new Params();
        RequestUtils.createRequest().postClassIndex(params.getData())
                .enqueue(new RequestCallback<>(new IRequestListener<SchedulingManageBean>() {
                    @Override
                    public void onSuccess(SchedulingManageBean schedulingManageBean) {
                        SchedulingManageBean.DataBean data = schedulingManageBean.getData();
                        if (data != null) {
                            handleData(data, type);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        showToast(errorMsg);
                    }
                }));
    }

    private void handleData(SchedulingManageBean.DataBean data, int type) {
        List<SchedulingManageBean.DataBean.RuleBean> rule = data.getRule();
        if (rule != null) {
            if (type == 1) {
                mAdapter.replaceData(rule);
            } else {
                mAdapter.addData(rule);
            }
        }
    }

    @Override
    protected void initEvent() {
        titleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SchedulingCreateActivity.class));
            }
        });
    }

    class SchedulingAdapter extends BaseQuickAdapter<SchedulingManageBean.DataBean.RuleBean, BaseViewHolder> {
        public SchedulingAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final SchedulingManageBean.DataBean.RuleBean item) {
            final TextView titleText = helper.getView(R.id.item_scheduling_title);
            TextView content1Text = helper.getView(R.id.item_scheduling_content1);
            TextView content2Text = helper.getView(R.id.item_scheduling_content2);
            TextView content3Text = helper.getView(R.id.item_scheduling_content3);
            TextView change1Text = helper.getView(R.id.item_scheduling_change1);
            TextView change2Text = helper.getView(R.id.item_scheduling_change2);
            TextView change3Text = helper.getView(R.id.item_scheduling_change3);
            TextView endMessageText = helper.getView(R.id.item_scheduling_end_message);
            TextView deleteText = helper.getView(R.id.item_scheduling_delete);

            //最后一条信息
            final int position = helper.getAdapterPosition();
            if (position == getItemCount() - 1) {
                endMessageText.setVisibility(View.VISIBLE);
            } else {
                endMessageText.setVisibility(View.GONE);
            }

            String rule_name = item.getRule_name();
            titleText.setText(StringUtil.stringNotNull(rule_name));

            String part_name = item.getPart_name();
            String user_name = item.getUser_name();
            String content1Value = "";
            if (!TextUtils.isEmpty(part_name) && !TextUtils.isEmpty(user_name)) {
                content1Value = part_name + "," + user_name;
            } else if (!TextUtils.isEmpty(part_name)) {
                content1Value = part_name;
            } else if (!TextUtils.isEmpty(user_name)) {
                content1Value = user_name;
            }
            content1Text.setText(content1Value);

            content2Text.setText(StringUtil.stringNotNull(item.getClass_name()));


            String week_day = item.getWeek_day();
            String holiday = item.getHoliday();
            String content3Value = "";
            if (!TextUtils.isEmpty(week_day) && !TextUtils.isEmpty(holiday)) {
                content3Value = week_day + "," + holiday;
            } else if (!TextUtils.isEmpty(week_day)) {
                content3Value = week_day;
            } else if (!TextUtils.isEmpty(holiday)) {
                content3Value = holiday;
            }

            content3Text.setText(StringUtil.stringNotNull(content3Value));

            if (item.getIs_default() == 1) {
                change1Text.setEnabled(false);
                change1Text.setTextColor(mContext.getResources().getColor(R.color.texthintcolor));
                deleteText.setVisibility(View.GONE);
                content1Text.setText("全公司");
            } else {
                deleteText.setVisibility(View.VISIBLE);
                change1Text.setEnabled(true);
                change1Text.setTextColor(mContext.getResources().getColor(R.color.blue));
                change1Text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleChangePeople(item);
                    }
                });

                deleteText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog(mContext).builder()
                                .setCancelable(true)
                                .setMsg("确定删除此排班？")
                                .setTitle("提示")
                                .setNegativeButton("", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).setPositiveButton("", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteItem(position, item);
                            }
                        }).show();
                    }
                });
            }

            final String class_name = item.getClass_name();
            final int class_id = item.getClass_id();
            final int rule_id = item.getRule_id();
            final String work_holiday = item.getWork_holiday();
            final String work_week_day = item.getWork_week_day();
            //修改工作日
            change3Text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SchedulingChangeActivity.class);
                    intent.putExtra("managerSkip", MANAGER_SKIP);
                    intent.putExtra("editValue", class_name);
                    intent.putExtra("titleText", titleText.getText().toString());
                    intent.putExtra("class_id", class_id + "");
                    intent.putExtra("rule_id", rule_id);
                    intent.putExtra("work_holiday", work_holiday);
                    intent.putExtra("work_week_day", work_week_day);
                    startActivityForResult(intent, 103);
                }
            });

            change2Text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SchedulingListActivity.class);
                    intent.putExtra("rule_id", rule_id);
                    startActivityForResult(intent, 102);
                }
            });


        }

        private void deleteItem(final int position, final SchedulingManageBean.DataBean.RuleBean item) {
            Params params = new Params();
            params.put("ri", item.getRule_id());
            RequestUtils.createRequest().postClassDel(params.getData()).enqueue(new RequestCallback<BaseModel>(new IRequestListener<BaseModel>() {
                @Override
                public void onSuccess(BaseModel baseModel) {
                    showToast(baseModel.msg);
                    List<SchedulingManageBean.DataBean.RuleBean> data = getData();
                    data.remove(item);
//                    if (position == getData().size()) {
                    notifyDataSetChanged();
//                    } else {
//                        notifyItemChanged(position - 1);
//                    }
                }

                @Override
                public void onFailure(int code, String errorMsg) {
                    showToast(errorMsg);
                }
            }));
        }
    }

    private void handleChangePeople(SchedulingManageBean.DataBean.RuleBean item) {
        final int rule_id = item.getRule_id();
        showProgressDialog("");
        Params params = new Params();
        params.put("ri", rule_id);
        RequestUtils.createRequest().postGetClassesUser(params.getData()).enqueue(new RequestCallback2<>(new IRequestListener2<ContactPartBean>() {
            @Override
            public void onSuccess(ContactPartBean readClassBean) {
                ContactPartBean.DataEntity data = readClassBean.getData();
                if (data != null) {
                    Intent intent = new Intent(mContext, NoticeReciveRangeActivity.class);
                    intent.putExtra(NoticeReciveRangeActivity.PART_SELECT_NOT_IN, NoticeReciveRangeActivity.PART_SELECTOR_NOT_CLICK);
                    intent.putExtra(NoticeReciveRangeActivity.NOTICE_TITLE, "通讯录");
                    intent.putExtra("DataEntity", data);
                    intent.putExtra("rule_id", rule_id);
                    startActivityForResult(intent, 101);
                }
                closeProgressDialog();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
            }
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (103 == requestCode && resultCode == 100) {  //修改工作日
            loadData(1);
        } else if (102 == requestCode && resultCode == 200) {  //修改班次
            loadData(1);
        } else if (101 == requestCode && resultCode == NoticeReciveRangeActivity.NOTICE_RESULT_OK) {
            loadData(1);
        }
    }

    @Subscribe
    public void schedulingManagerFinish(SchedulingFinishEvent event) {
//        finish();
        loadData(1);
    }
}
