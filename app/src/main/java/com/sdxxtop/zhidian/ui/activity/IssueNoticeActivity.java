package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ApproverIndexBean;
import com.sdxxtop.zhidian.entity.NoticeAddBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.ImageParams;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者: CaiCM
 * 日期: 2018/3/27 时间: 16:17
 * 邮箱：15010104100@163.com
 * 描述: 发布公告界面
 */

public class IssueNoticeActivity extends BaseApproverActivity {

    @BindView(R.id.et_notice_title)
    EditText etNoticeTitle;
    @BindView(R.id.et_adder_name)
    EditText etAdderName;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.et_notice_content)
    EditText etNoticeContent;
    @BindView(R.id.btn_issue)
    Button btnIssue;
    @BindView(R.id.tv_show_recive_range)
    TextView tvShowReciveRange;
    @BindView(R.id.horlv_photo)
    RecyclerView horlvPhoto;

    @Override
    protected int getActivityView() {
        return R.layout.activity_issue_notice;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initChildData(ApproverIndexBean.DataBean data) {
        //设置相片选择
        setPhotoRecycler(horlvPhoto);
    }

    @OnClick({R.id.tv_show_recive_range, R.id.iv_camera, R.id.btn_issue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_show_recive_range:
                Intent intent = new Intent(mContext, NoticeReciveRangeActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.iv_camera:

                break;
            case R.id.btn_issue:
                if (tvShowReciveRange.getText().toString().length() == 0) {
                    ToastUtil.show("请选择公告接收范围");
                    return;
                }
                if (etNoticeTitle.getText().toString().length() == 0) {
                    ToastUtil.show("请选输入公告标题");
                    return;
                }
                if (etAdderName.getText().toString().length() == 0) {
                    ToastUtil.show("请选输入发布人姓名");
                    return;
                }
                if (etNoticeContent.getText().toString().length() == 0) {
                    ToastUtil.show("请选输入公告内容");
                    return;
                }
                postNoticeAdd();
                break;
        }
    }


    /**
     * 添加公告网络请求
     */
    private void postNoticeAdd() {



        ImageParams params = new ImageParams();
        //设置相片
        params.addImagePathList("img[]", getImagePushPath());
        params.put("pi", "18, 19, 20");
        params.put("te", etNoticeTitle.getText().toString());
        params.put("un", etAdderName.getText().toString());
        params.put("ct", etNoticeContent.getText().toString());
        params.put("oi", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));

        RequestUtils.createRequest().postNoticeAdd(params.getImgData()).enqueue(new RequestCallback<NoticeAddBean>(mContext, new IRequestListener<NoticeAddBean>() {
            @Override
            public void onSuccess(NoticeAddBean noticeAddBean) {
                String msg = noticeAddBean.msg;
                if (noticeAddBean.code == 200) {
                    Intent intent = new Intent(mContext, NoticeDetailActivity.class);
                    intent.putExtra("notice_id", noticeAddBean.getData().getNotice_id() + "");
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtil.show(msg);
                }

            }

            @Override
            public void onFailure(int code, String errorMsg) {
                ToastUtil.show(errorMsg);
            }
        }));
    }


}
