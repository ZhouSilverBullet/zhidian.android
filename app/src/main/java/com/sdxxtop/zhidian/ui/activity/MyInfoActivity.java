package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ContactRemoveBean;
import com.sdxxtop.zhidian.entity.UcenterUserinfoBean;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.ImageParams;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.AccountPicHelper;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyInfoActivity extends BaseActivity {

    @BindView(R.id.my_info_title_view)
    SubTitleView titleView;
    @BindView(R.id.iv_img)
    CircleImageView ivImg;
    @BindView(R.id.tv_short_name)
    TextView tvShortName;
    @BindView(R.id.iv_into_img)
    ImageView ivIntoImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.btn_remove)
    Button btnRemove;

    private UcenterUserinfoBean ucenterUserinfoBean;
    private AccountPicHelper accountPicHelper;

    @Override
    protected int getActivityView() {
        return R.layout.activity_my_info;
    }

    @Override
    protected void initView() {
        accountPicHelper = new AccountPicHelper(this);
        titleView.getLeftText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishResult();
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        postUcenterUserinfo();
    }

    @OnClick({R.id.iv_img, R.id.iv_into_img, R.id.btn_remove})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_img:
                if (accountPicHelper != null) {
                    accountPicHelper.show();
                }
                break;
            case R.id.iv_into_img:
                break;
            case R.id.btn_remove:
                contactRemove();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finishResult();
        super.onBackPressed();
    }

    private void finishResult() {
        if (!isChange) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("img", ucenterUserinfoBean.getData().getImg());
        setResult(200, intent);
    }

    boolean isChange = false;//判断是否修改过头像

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (accountPicHelper != null) {
            accountPicHelper.handleResult(requestCode, ivImg, data, new Runnable() {
                @Override
                public void run() {
                    isChange = true;
                    postUcenterUserinfo();
                }
            });
        }
    }

    /**
     * 修改用户头像网络请求
     */
    private void postUcenterModImg(String imgPath) {
        ImageParams params = new ImageParams();
        String targetPath = mContext.getExternalCacheDir() + "/xunxing_photo/img" + System.currentTimeMillis() + ".png";
        params.addCompressImagePath("img", new File(imgPath), targetPath, 50);
        RequestUtils.createRequest().postUcenterModImg(params.getImgData()).enqueue(new RequestCallback<BaseModel>(mContext, new IRequestListener<BaseModel>() {

            @Override
            public void onSuccess(BaseModel baseModel) {
                String msg = baseModel.msg;
                ToastUtil.show(msg);
                postUcenterUserinfo();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                ToastUtil.show(errorMsg);
            }

        }));

    }

    /**
     * 账号信息网络请求
     */
    public void postUcenterUserinfo() {
        Params params = new Params();
        showProgressDialog("");
        RequestUtils.createRequest().postUcenterUserinfo(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<UcenterUserinfoBean>() {
            @Override
            public void onSuccess(UcenterUserinfoBean ucenterUserinfoBean) {
                closeProgressDialog();
                MyInfoActivity.this.ucenterUserinfoBean = ucenterUserinfoBean;
                UcenterUserinfoBean.DataEntity data = ucenterUserinfoBean.getData();
                ViewUtil.setColorItemView(data.getImg(), data.getName(), tvShortName, ivImg);
                tvName.setText(data.getName());

                tvPosition.setText(data.getPosition());
                String stringSex = "";
                int sex = data.getSex();
                if (sex == 1) {
                    stringSex = "男";
                } else if (sex == 2) {
                    stringSex = "女";
                } else {
                    stringSex = "未设置";
                }
                tvSex.setText(stringSex);
                tvCompany.setText(data.getCompany_name());
                tvMobile.setText(data.getMobile() + "");
                tvEmail.setText(data.getEmail());
                String device_name = data.getDevice_name();
                boolean driverNameIsEmpty = !TextUtils.isEmpty(device_name);
                if (driverNameIsEmpty) {
                    tvDeviceName.setText(device_name);
                    btnRemove.setClickable(true);
                    btnRemove.setBackgroundResource(R.drawable.btn_make_suer);
                } else {
                    tvDeviceName.setText("未设置");
                    btnRemove.setClickable(false);
                    btnRemove.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
                showToast(errorMsg);
            }
        }));
    }

    /**
     * 弹框调用解绑网络请求
     */
    private void contactRemove() {
        Map<String, String> map = new HashMap<>();
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        map.put("cui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postContactRemove(base64Data).enqueue(new Callback<ContactRemoveBean>() {
            @Override
            public void onResponse(Call<ContactRemoveBean> call, Response<ContactRemoveBean> response) {
                closeProgressDialog();
                ContactRemoveBean contactRemoveBean = response.body();
                if (contactRemoveBean.getCode() == 200) {
                    ToastUtil.show(response.body().getMsg());
                    tvDeviceName.setText("未设置");
                    btnRemove.setClickable(false);
                    btnRemove.setVisibility(View.INVISIBLE);
                } else {
                    ToastUtil.show(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<ContactRemoveBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }
}
