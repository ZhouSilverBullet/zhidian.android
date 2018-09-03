package com.sdxxtop.zhidian.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.HomeworkDetailBean;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import zhangphil.iosdialog.widget.AlertDialog;

public class HomeworkDetailActivity extends BaseActivity {

    @BindView(R.id.home_detail_title_view)
    SubTitleView mTitleView;
    @BindView(R.id.home_detail_title)
    TextView mTitleText;
    @BindView(R.id.home_detail_content)
    TextView mContentText;
    @BindView(R.id.home_detail_content_text)
    TextView mDetailContentText;
    @BindView(R.id.home_detail_recycler)
    RecyclerView mRecyclerView;
    private String taskId;

    @Override
    protected int getActivityView() {
        return R.layout.activity_homework_detail;
    }

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            taskId = getIntent().getStringExtra("task_id");
        }
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new ItemDivider().setDividerWidth(ViewUtil.dp2px(mContext, 10)).setDividerColor(0x00000000));
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initEvent() {
        mTitleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recall();
            }
        });
    }

    @Override
    protected void initData() {
        Params params = new Params();
        params.put("ti", taskId);
        RequestUtils.createRequest().postUserReadTask(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<HomeworkDetailBean>() {
            @Override
            public void onSuccess(HomeworkDetailBean homeworkDetailBean) {
                HomeworkDetailBean.DataBean data = homeworkDetailBean.getData();
                if (data != null) {
                    HomeworkDetailBean.DataBean.TaskBean task = data.getTask();
                    if (task != null) {
                        handleData(task);
                    }
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }


    private void handleData(HomeworkDetailBean.DataBean.TaskBean data) {
        String addTime = data.getAdd_time();
        if (!TextUtils.isEmpty(addTime)) {
            addTime = addTime.substring(0, addTime.length() - 3);
        }
        mTitleText.setText(data.getTitle());
        mContentText.setText(addTime + "");
        mDetailContentText.setText(data.getContent());
        String img = data.getImg();
        if (!TextUtils.isEmpty(img)) {
            String[] split = img.split(",");
            List<String> stringList = Arrays.asList(split);
            HomeworkDetailAdapter homeworkDetailAdapter = new HomeworkDetailAdapter(R.layout.item_notice2_img_recycler, stringList);
            mRecyclerView.setAdapter(homeworkDetailAdapter);
        }
    }


    private void recall() {
        new AlertDialog(mContext)
                .builder()
                .setTitle("提示")
                .setMsg("您确定撤回本次作业吗？")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postHomeworkRevoke();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    private void postHomeworkRevoke() {
        Params params = new Params();
        params.put("ti", taskId);
        RequestUtils.createRequest().postUserRevokeTask(params.getData())
                .enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
                    @Override
                    public void onSuccess(BaseModel baseModel) {
                        showToast("撤回成功");
                        setResult(100);
                        finish();
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        showToast(errorMsg);
                    }
                }));
    }

    public static void startHomeworkDetailActivity(Activity context, String task_id) {
        Intent intent = new Intent(context, HomeworkDetailActivity.class);
        intent.putExtra("task_id", task_id);
        context.startActivityForResult(intent, 200);
    }

    private class HomeworkDetailAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public HomeworkDetailAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final String item) {
            final ImageView img = helper.getView(R.id.item_notice2_img);
            if (!TextUtils.isEmpty(item)) {
                Glide.with(mContext).load(item).into(img);
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhotoViewActivity.start(mContext, item);
                    }
                });
            }
        }
    }

}
