package com.sdxxtop.zhidian.ui.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;

import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.ui.FaceLivenessActivity;
import com.sdxxtop.zhidian.eventbus.FaceEvent;
import com.sdxxtop.zhidian.eventbus.FaceRegEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.ImageParams;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.utils.DialogUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class FaceLivenessExpActivity extends FaceLivenessActivity {

    private String bestImagePath;
    private int face;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            face = getIntent().getIntExtra("face", -1);
        }
    }

    @Override
    public void onLivenessCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onLivenessCompletion(status, message, base64ImageMap);
        if (status == FaceStatusEnum.OK && mIsCompletion) {
            saveImage(base64ImageMap);
            final Dialog progressDialog = DialogUtil.createLoadingDialog(this, message, false);
            progressDialog.show();
            ImageParams params = new ImageParams();
            params.addCompressImagePath("img", new File(bestImagePath), getCacheDir() + "/img.png", 80);
            if (face == 1) {
                RequestUtils.createRequest().postFaceVerify(params.getImgData()).enqueue(new RequestCallback<>(this, new IRequestListener<BaseModel>() {
                    @Override
                    public void onSuccess(BaseModel baseModel) {
                        FaceEvent event = new FaceEvent();
                        event.code = 100;
                        EventBus.getDefault().post(event);
                        finish();
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                recreate();
//                            }
//                        }, 2500);
                        finish();
                        ToastUtil.show(errorMsg);
                    }
                }));
            } else {
                RequestUtils.createRequest().postFaceReg(params.getImgData()).enqueue(new RequestCallback<>(this, new IRequestListener<BaseModel>() {
                    @Override
                    public void onSuccess(BaseModel baseModel) {
                        FaceRegEvent event = new FaceRegEvent();
                        EventBus.getDefault().post(event);
                        finish();
                        ToastUtil.show("注册成功");
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recreate();
                            }
                        }, 2500);
                        ToastUtil.show(errorMsg);
                    }
                }));
            }

        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
            ToastUtil.show("采集超时");
//            showMessageDialog("活体检测", "采集超时");
        }
    }

    private void saveImage(HashMap<String, String> base64ImageMap) {

        String bestimageBase64 = base64ImageMap.get("bestImage0");
        Bitmap bmp = base64ToBitmap(bestimageBase64);

        // 如果觉的在线校验慢，可以压缩图片的分辨率，目前没有压缩分辨率，压缩质量80%-100%，在neuxs5上大概30k
        try {
            File file = File.createTempFile(UUID.randomUUID().toString(), ".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            outputStream.close();

            bestImagePath = file.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

}
