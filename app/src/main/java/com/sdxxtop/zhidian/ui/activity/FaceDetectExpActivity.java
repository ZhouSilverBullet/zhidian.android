package com.sdxxtop.zhidian.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.ui.FaceDetectActivity;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.utils.DialogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class FaceDetectExpActivity extends FaceDetectActivity {

    private String bestImagePath;
    private int loginType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            loginType = intent.getIntExtra("type", -1);
        }
    }

    @Override
    public void onDetectCompletion(final FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onDetectCompletion(status, message, base64ImageMap);
        if (status == FaceStatusEnum.OK && mIsCompletion) {
            saveImage(base64ImageMap);
//            Intent intent = new Intent();
//            intent.putExtra("file_path", bestImagePath);
//            setResult(Activity.RESULT_OK, intent);
            Dialog progressDialog = DialogUtil.createLoadingDialog(this, message, false);
            Params params = new Params();

        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
//            showMessageDialog("人脸图像采集", "采集超时");
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


//    private void showMessageDialog(String title, String message) {
//        if (mDefaultDialog == null) {
//            DefaultDialog.Builder builder = new DefaultDialog.Builder(this);
//            builder.setTitle(title).
//                    setMessage(message).
//                    setNegativeButton("确认",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    mDefaultDialog.dismiss();
//                                    finish();
//                                }
//                            });
//            mDefaultDialog = builder.create();
//            mDefaultDialog.setCancelable(true);
//        }
//        mDefaultDialog.dismiss();
//        mDefaultDialog.show();
//    }

    @Override
    public void finish() {
        super.finish();
    }
}
