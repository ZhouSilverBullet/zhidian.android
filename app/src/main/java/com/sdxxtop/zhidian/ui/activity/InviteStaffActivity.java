package com.sdxxtop.zhidian.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingmei2.library.encode.QRCodeEncoder;
import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.CompanyShowInfoBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;

import butterknife.OnClick;

/**
 * 作者：CaiCM
 * 日期：2018/3/23 时间：16:24
 * 邮箱：15010104100@163.com
 * 描述：通过手机号邀请员工界面
 */
public class InviteStaffActivity extends BaseActivity {

    @Override
    protected int getActivityView() {
        return R.layout.activity_invite_staff;
    }

    @OnClick({R.id.tv_phone_add, R.id.tv_pilinag_add, R.id.tv_qr_cord_show})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_phone_add:
                Intent intentPhone = new Intent(mContext, InviteOfPhoneActivity.class);
                startActivity(intentPhone);
                break;
            case R.id.tv_pilinag_add:
                Intent intentExcel = new Intent(mContext, InviteOfExcelActivity.class);
                startActivity(intentExcel);
                break;
            case R.id.tv_qr_cord_show:
                postCompanyShowInfo();
                break;
        }
    }

    private void showQrDialog(String shortName, String path) {
        Dialog dialog = new Dialog(this, R.style.dialog_style);
        dialog.show();

        dialog.setContentView(R.layout.dialog_inivite_qr_code);
        TextView qrText = (TextView) dialog.findViewById(R.id.dialog_qr_text);
        ImageView qrImg = (ImageView) dialog.findViewById(R.id.dialog_qr_img);

        qrText.setText(shortName);
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(this);  //初始化
        qrCodeEncoder.createQrCode2ImageView(path, qrImg);//生成二维码
        closeProgressDialog();
    }

    /**
     * 公司信息展示网络请求
     */
    private void postCompanyShowInfo() {
        Params params = new Params();
        showProgressDialog("");
        RequestUtils.createRequest().postCompanyShowInfo(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<CompanyShowInfoBean>() {
            @Override
            public void onSuccess(CompanyShowInfoBean companyShowInfoBean) {
                CompanyShowInfoBean.DataEntity data = companyShowInfoBean.getData();
                if (data != null) {
                    CompanyShowInfoBean.DataEntity.CompanyEntity company = data.getCompany();
                    if (company != null) {
                        String shortCode = company.getShort_code();
                        String shortName = company.getCompany_name();
                        String path = String.format("http://wap.sdxxtop.com/admin/company/join/ui/%s/cs/%s", AppSession.getInstance().getUserId(), shortCode);

                        showQrDialog(shortName, path);
                        return;
                    }
                }
                closeProgressDialog();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
                showToast(errorMsg);
            }
        }));
    }
}
