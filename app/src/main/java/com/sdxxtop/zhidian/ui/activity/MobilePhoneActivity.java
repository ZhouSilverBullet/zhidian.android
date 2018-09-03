package com.sdxxtop.zhidian.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ApproverIndexBean;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.ImageParams;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：${刘香龙} on 2018/5/4 0004 16:14
 * 类的描述：
 */
public class MobilePhoneActivity extends BaseApproverActivity {

    @BindView(R.id.horlv_photo)
    RecyclerView horListViewPhoto;
    @BindView(R.id.horlv_approving_officer)
    RecyclerView horApprovOfficer;
    @BindView(R.id.btn_submission)
    Button btnSubmission;
    @BindView(R.id.mobile_phone_text)
    TextView phoneText;
    @BindView(R.id.mobile_phone_content)
    EditText phoneContent;
    @BindView(R.id.mobile_phone_content_change)
    TextView phoneContentChange;

    List<Bitmap> photos = new ArrayList<>();

    @Override
    protected int getActivityView() {
        return R.layout.activity_mobilephone;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initChildData(ApproverIndexBean.DataBean data) {
        phoneText.setText("解绑手机：" + data.getDevice_name());

        photos.add(BitmapFactory.decodeResource(getResources(), R.mipmap.upload_pictures));

        setPhotoRecycler(horListViewPhoto);

        //审批人
        setApproverRecycler(horApprovOfficer);

        editTextCountControl(R.id.mobile_phone_content, phoneContent, phoneContentChange);
    }


    @Override
    protected void initEvent() {
        super.initEvent();
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
//                        strings.add("手机解绑");
//                        strings.add("5月6号");
//
//                        EventBus.getDefault().post(new MessageEvent.intentActivity(
//                                "手机解绑",
//                                "手机解绑",
//                                strings,
//                                "这个号码不想要了",
//                                photos,
//                                "张三",
//                                "长军",
//                                "洪峰"));
//                    }
//                }, 800);
            }
        });

    }

    private void submit() {
        String phoneEditContent = phoneContent.getText().toString().trim();

        if (TextUtils.isEmpty(phoneEditContent)) {
            showToast("请填写手机绑定理由");
            return;
        }

        if (data == null) {
            showToast("数据错误");
            return;
        }

        ImageParams params = new ImageParams();
        params.put("dn", data.getDevice_name());
        params.put("rs", phoneEditContent);

        params.put("ai", getApproverValue());
        params.put("si", getCopyValue()); //抄送

        params.addImagePathList("img[]", getImagePushPath());

        RequestUtils.createRequest().postApplyLeave("removeDevice", params.getImgData()).enqueue(new RequestCallback<BaseModel>(new IRequestListener<BaseModel>() {
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
}
