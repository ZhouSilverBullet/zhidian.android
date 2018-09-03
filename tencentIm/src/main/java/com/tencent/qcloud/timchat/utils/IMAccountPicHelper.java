package com.tencent.qcloud.timchat.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.qcloud.timchat.MyApplication;
import com.tencent.qcloud.timchat.R;
import com.xuxin.configure.UtilSession;
import com.xuxin.http.BaseModel;
import com.xuxin.http.IRequestListener;
import com.xuxin.http.ImageParams;
import com.xuxin.http.RequestCallback;
import com.xuxin.http.RequestExe;

import java.io.File;

import pub.devrel.easypermissions.EasyPermissions;

/**
 *
 */

public class IMAccountPicHelper {

    public static final String TAG = "AccountPicHelper";

    private static final int REQUEST_CODE_CROP = 0;
    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    private static final int REQUEST_CODE_SELECT_PHOTO = 2;
    private static final int PERMISSION_FOR_CAMERA = 0x0001;
    private static final int PERMISSION_FOR_PIC_SELECTOR = 0x0002;
    private String type;
    private String identify;

    private String tmpFileSaveDir;
    private File cropSaveFile;
    private Uri uri;
    private Activity activity;
    private File takePhotoPath;
    private Dialog dialog;

    public IMAccountPicHelper(Activity activity, String identify, String type) {
        this.activity = activity;
        this.identify = identify;
        this.type = type;
        init();
        try {
            tmpFileSaveDir = activity.getExternalCacheDir().getAbsolutePath() + "/xuxing/ad/";
        } catch (Exception e) {
            tmpFileSaveDir = "";
        }
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setCropSaveFile(File cropSaveFile) {
        this.cropSaveFile = cropSaveFile;
    }

    public File getCropSaveFile() {
        return cropSaveFile;
    }

    public void handleResult(int requestCode, final ImageView userIcon, Intent imageReturnIntent, Runnable runnable) {
        switch (requestCode) {
            case REQUEST_CODE_TAKE_PHOTO:
                if (uri == null) {
//                    LogUtil.errorLog("AccountPicHelper REQUEST_CODE_TAKE_PHOTO:null");
                    return;
                }
                if (!takePhotoPath.exists()) {
//                    Toast.makeText(MyApplication.getContext(), "选择图片错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActionCrop(REQUEST_CODE_TAKE_PHOTO);
                break;
            case REQUEST_CODE_SELECT_PHOTO:
                if (imageReturnIntent == null) {
                    return;
                }
                uri = imageReturnIntent.getData();
                if (uri == null) {
//                    LogUtil.errorLog("AccountPicHelper REQUEST_CODE_SELECT_PHOTO:null");
                    return;
                }

                File saveDir = new File(tmpFileSaveDir);
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }
                if (takePhotoPath == null) {  //选择相片回来的路径
                    takePhotoPath = new File(tmpFileSaveDir, "account1.jpg");
                }
                startActionCrop(REQUEST_CODE_SELECT_PHOTO);// 选图后裁剪
                break;
            case REQUEST_CODE_CROP:
                if (cropSaveFile == null) {
                    return;
                }

                if (!cropSaveFile.exists()) {
                    Toast.makeText(MyApplication.getContext(), "出错,图片文件不存在", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (takePhotoPath.exists()) {
                    takePhotoPath.delete();
                }

                postUcenterModImg(cropSaveFile.getPath(), userIcon, runnable);

                break;
        }
    }

    private void init() {
        dialog = new Dialog(activity, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_pic_selector_layout, null);
        View takeCamera = view.findViewById(R.id.dialog_pic_take_camera);
        View selectPhoto = view.findViewById(R.id.dialog_pic_select_photo);
        takeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (EasyPermissions.hasPermissions(activity, permissions)) {
                    takePhoto();
                    dismiss();
                } else {
                    EasyPermissions.requestPermissions(activity, "请给予知点app读写权限才能打开相机", 100, permissions);
                }
            }
        });

        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (EasyPermissions.hasPermissions(activity, permissions)) {
                    selectPhoto();
                    dismiss();
                } else {
                    EasyPermissions.requestPermissions(activity, "请给予知点app读写权限才能上传图片", 101, permissions);
                }
            }
        });

        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void selectPhoto() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        intent.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(intent, "选择图片"),
                REQUEST_CODE_SELECT_PHOTO);
    }

    private void takePhoto() {
        File saveDir = new File(tmpFileSaveDir);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        takePhotoPath = new File(tmpFileSaveDir, "account1.jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(activity, "com.sdxxtop.zhidian.FileProvider", takePhotoPath);
        } else {
            uri = Uri.fromFile(takePhotoPath);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent,
                REQUEST_CODE_TAKE_PHOTO);
    }

    private void startActionCrop(int status) {
        cropSaveFile = new File(tmpFileSaveDir, "account2.jpg");
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            switch (status) {
                case REQUEST_CODE_TAKE_PHOTO:
                    intent.setDataAndType(getImageContentUri(takePhotoPath), "image/*");
                    intent.putExtra("output", Uri.fromFile(cropSaveFile));
                    break;
                case REQUEST_CODE_SELECT_PHOTO:
                    intent.setDataAndType(uri, "image/*");
                    intent.putExtra("output", getUploadTempFile(uri));
                    break;
            }
        } else {
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("output", getUploadTempFile(uri));
        }
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);// 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        activity.startActivityForResult(intent,
                REQUEST_CODE_CROP);
        uri = null;
    }

    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = activity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return activity.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return uri;
            }
        }
    }

    private Uri getUploadTempFile(Uri uri) {
        File saveDir = new File(tmpFileSaveDir);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        String thePath = getAbsolutePathFromNoStandardUri(uri);

        if (TextUtils.isEmpty(thePath)) {
            thePath = getAbsoluteImagePath(uri);
        }

        String ext = getFileFormat(thePath);
        ext = TextUtils.isEmpty(ext) ? "jpg" : ext;
        String cropFileName = "account." + ext;
        cropSaveFile = new File(tmpFileSaveDir, cropFileName);

        Uri notifyUri = Uri.fromFile(cropSaveFile);
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, notifyUri));
        return notifyUri;
    }

    private String getAbsolutePathFromNoStandardUri(Uri mUri) {
        String filePath = null;

        String mUriString = mUri.toString();
        mUriString = Uri.decode(mUriString);

        String pre1 = "file://" + "/sdcard" + File.separator;
        String pre2 = "file://" + "/mnt/sdcard" + File.separator;

        if (mUriString.startsWith(pre1)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + mUriString.substring(pre1.length());
        } else if (mUriString.startsWith(pre2)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + mUriString.substring(pre2.length());
        }
        return filePath;
    }

    private String getAbsoluteImagePath(Uri uri) {
        String imagePath = "";
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(uri, proj, // Which columns to
                // return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                imagePath = cursor.getString(column_index);
            }
        }

        return imagePath;
    }

    private String getFileFormat(String fileName) {
        if (TextUtils.isEmpty(fileName))
            return "";

        int point = fileName.lastIndexOf('.');
        return fileName.substring(point + 1);
    }

    /**
     * 修改用户头像网络请求
     */
    private void postUcenterModImg(final String imgPath, final ImageView imageView, final Runnable runnable) {

        ImageParams params = new ImageParams();
        params.put("gi", identify);
        params.addImagePath("img", new File(imgPath));

        RequestExe.createRequest().postTimModifyGroupImg(params.getImgData()).enqueue(new RequestCallback<BaseModel>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                Toast.makeText(UtilSession.getInstance().getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                if (runnable != null) {
                    runnable.run();
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {

            }
        }));

//        TIMGroupManagerExt.ModifyGroupInfoParam createGroupParam = new TIMGroupManagerExt.ModifyGroupInfoParam(identify);
//        createGroupParam.setFaceUrl(imgPath);
//
//        TIMGroupManagerExt.getInstance().modifyGroupInfo(createGroupParam, new TIMCallBack() {
//            @Override
//            public void onError(int i, String s) {
//                Log.e(TAG, "code = " + i + " msg = " + s);
//            }
//
//            @Override
//            public void onSuccess() {
//                if (runnable != null) {
//                    runnable.run();
//                }
//            }
//        });

    }
}
