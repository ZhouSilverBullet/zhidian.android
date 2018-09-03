package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.NoticeReadBean;
import com.sdxxtop.zhidian.eventbus.ChangeCompanyEvent;
import com.sdxxtop.zhidian.eventbus.MessageCenterEvent;
import com.sdxxtop.zhidian.eventbus.NoticeReadEvent;
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
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import zhangphil.iosdialog.widget.AlertDialog;

public class NoticeDetail2Activity extends BaseActivity {

    @BindView(R.id.notice2_detail_title_view)
    SubTitleView titleView;
    //    @BindView(R.id.notice2_detail_title)
//    TextView titleText;
//    @BindView(R.id.notice2_detail_recycler)
//    RecyclerView textRecyclerView;
//    @BindView(R.id.notice2_detail_name)
//    TextView nameText;
//    @BindView(R.id.notice2_detail_time)
//    TextView timeText;
    @BindView(R.id.notice2_detail_image_recycler)
    RecyclerView imageRecyclerView;
    //    @BindView(R.id.notice2_detail_content)
//    TextView contentText;
//    @BindView(R.id.notice2_detail_read)
//    TextView readText;
    private int notice_id;
    private ImageAdapter imageAdapter;
    private int company_id;

    @Override
    protected int getActivityView() {
        return R.layout.activity_notice_detail2;
    }

    @Override
    protected void initVariables() {
        super.initVariables();
        if (getIntent() != null) {
            notice_id = getIntent().getIntExtra("ni", -1);
            company_id = getIntent().getIntExtra("company_id", -1);
        }

        EventBus.getDefault().post(new PushCenterEvent());
    }

    @Override
    protected void initView() {
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        imageRecyclerView.addItemDecoration(new ItemDivider().setDividerColor(0x00000000).setDividerWidth(ViewUtil.dp2px(mContext, 10)));
        imageAdapter = new ImageAdapter(R.layout.item_notice2_img_recycler);
    }

    @Override
    protected void initEvent() {
        super.initEvent();

        titleView.getLeftText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new NoticeReadEvent());
                openSplashActivity(mContext);
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        Params params = new Params();
        params.put("ni", notice_id);
        RequestUtils.createRequest().postNoticeRead(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<NoticeReadBean>() {
            @Override
            public void onSuccess(NoticeReadBean noticeReadBean) {
                NoticeReadBean.DataEntity data = noticeReadBean.getData();
                if (data != null) {
                    handleData(data);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
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

    private void handleData(NoticeReadBean.DataEntity data) {

        NoticeReadBean.DataEntity.NoticeEntity notice = data.getNotice();
        if (notice == null) {
            return;
        }

        String img = notice.getImg();
        if (!TextUtils.isEmpty(img)) {
            String[] split = img.split(",");
            List<String> imgList = Arrays.asList(split);
            imageAdapter.replaceData(imgList);
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.notice_detail_recycler_header_view, null);
        handleHeaderView(data, view);
        imageAdapter.addHeaderView(view);
        View footerView = LayoutInflater.from(mContext).inflate(R.layout.notice_detail_recycler_footer_view, null);
        TextView contentText = (TextView) footerView.findViewById(R.id.notice2_detail_content);
        contentText.setText(notice.getContent());
        imageAdapter.setFooterView(footerView);
        imageRecyclerView.setAdapter(imageAdapter);

    }

    private void handleHeaderView(NoticeReadBean.DataEntity data, View view) {
        TextView titleText = (TextView) view.findViewById(R.id.notice2_detail_title);
        RecyclerView textRecyclerView = (RecyclerView) view.findViewById(R.id.notice2_detail_recycler);
        TextView nameText = (TextView) view.findViewById(R.id.notice2_detail_name);
        TextView timeText = (TextView) view.findViewById(R.id.notice2_detail_time);
        TextView readText = (TextView) view.findViewById(R.id.notice2_detail_read);

        textRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        textRecyclerView.setHasFixedSize(true);
        textRecyclerView.setNestedScrollingEnabled(false);

        int read_num = data.getRead_num();
        int not_num = data.getNot_num();
        readText.setText("已读：" + read_num + "人   未读：" + not_num + "人");

        NoticeReadBean.DataEntity.NoticeEntity notice = data.getNotice();
        if (notice == null) {
            return;
        }

        String operator_id = notice.getOperator_id();
        String stringParam = PreferenceUtils.getInstance(this).getStringParam(ConstantValue.USER_ID);
        if (!TextUtils.isEmpty(stringParam) && stringParam.equals(operator_id)) {
            titleView.getRightText().setText("撤回");
            titleView.getRightText().setVisibility(View.VISIBLE);
            //撤回
            titleView.getRightText().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    undo();
                }
            });
        }

        String title = notice.getTitle();
        titleText.setText(title);
        titleView.setTitleValue(title);

        String userName = notice.getName();
        String addTime = notice.getAdd_time();
        nameText.setText(userName);

        if (!TextUtils.isEmpty(addTime)) {
            String[] split = addTime.split(" ");
            timeText.setText(split[0]);
        } else {
            timeText.setText(addTime);
        }

        List<String> partAndNameList = new ArrayList<>();
        List<String> name = data.getName();
        List<String> part_name = data.getPart_name();
        if (part_name != null) {
            partAndNameList.addAll(part_name);
        }
        if (name != null) {
            partAndNameList.addAll(name);
        }

        if (partAndNameList.size() == 0) {
            partAndNameList.add("全公司");
        }

        textRecyclerView.setAdapter(new TextAdapter(R.layout.item_notice2_text_recycler, partAndNameList));

        //读取人的页面
        readText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NoticeReadActivity.class);
                intent.putExtra("ni", notice_id);
                startActivity(intent);
            }
        });
    }

    class TextAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public TextAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            TextView textText = helper.getView(R.id.item_notice2_text_text);
            textText.setText(item);
        }
    }

    class ImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public ImageAdapter(int layoutResId) {
            super(layoutResId);
        }

        public ImageAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final String item) {
            ImageView img = helper.getView(R.id.item_notice2_img);
            if (!TextUtils.isEmpty(item)) {
                Glide.with(mContext).load(item).into(img);
            }
        }
    }


    private void undo() {
        new AlertDialog(mContext)
                .builder()
                .setTitle("提示")
                .setMsg("确定撤回本次公告吗？")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postNoticeRevoke();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    /**
     * 撤回公告网络请求
     */
    private void postNoticeRevoke() {
        Params params = new Params();
        params.put("ni", notice_id);
        RequestUtils.createRequest().postRevokeNotice(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                showToast("撤回成功");
                refreshReadFragment();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    private void refreshReadFragment() {
        EventBus.getDefault().post(new NoticeReadEvent());
        finish();
    }

    @Override
    public void onBackPressed() {
        openSplashActivity(this);
        super.onBackPressed();
        EventBus.getDefault().post(new NoticeReadEvent());
    }
}
