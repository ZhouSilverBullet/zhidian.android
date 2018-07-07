package com.sdxxtop.zhidian.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.ui.base.BaseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2018/5/7.
 */

public class ImageSelectorHelper {

    String[] readStoragePerms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] readCameraPerms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    public static final int SELECTOR_PHOTO = 100;
    public static final int TAKE_CAMERA = 101;
    private Activity activity;
    private ImageView targetImageView;

    private String imgPath;
    private File imgTakePhotoFile;
    //记住拍照的uri地址，回头判断然后去这个uri取值
    private Uri uri;
    private Dialog dialog;

    private Fragment fragment;

    //存取当前选择的类型 ，是SELECTOR_PHOTO 还是 TAKE_CAMERA
    private int tempType;

    public ImageSelectorHelper(Activity activity, ImageView targetImageView) {
        this.activity = activity;
        this.targetImageView = targetImageView;
        init();
    }

    public ImageSelectorHelper(Activity activity) {
        this.activity = activity;
        init();
    }

    public ImageSelectorHelper(Fragment fragment) {
        this.activity = fragment.getActivity();
        this.fragment = fragment;
        init();
    }

    public void setTargetImageView(ImageView targetImageView) {
        this.targetImageView = targetImageView;
    }

    private void init() {
        dialog = new Dialog(activity, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_pic_selector_layout, null);
        View takeCamera = view.findViewById(R.id.dialog_pic_take_camera);
        View selectPhoto = view.findViewById(R.id.dialog_pic_select_photo);
        takeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(activity, readCameraPerms)) {
                    takePhoto("img" + System.currentTimeMillis() + ".png");
                    dismiss();
                } else {
                    EasyPermissions.requestPermissions(activity, "请给予知点app读写权限才能打开相机", BaseActivity.REQUEST_PERMISSION_READ_STORAGE, readCameraPerms);
                }
            }
        });

        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(activity, readStoragePerms)) {
                    selectImage();
                    dismiss();
                } else {
                    EasyPermissions.requestPermissions(activity, "请给予知点app读写权限才能上传图片", BaseActivity.REQUEST_PERMISSION_READ_STORAGE, readStoragePerms);
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

    public int getTempType() {
        return tempType;
    }

    public void clearTempType() {
        tempType = 0;
    }

    /**
     * 获取图片地址
     *
     * @return
     */
    public String getImgPath() {
        return imgPath;
    }

    /**
     * 选择图片
     */
    public void selectImage() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_PICK); //过滤多个选择图片的app
        }
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        tempType = SELECTOR_PHOTO;
        if (fragment != null) {
            fragment.startActivityForResult(Intent.createChooser(intent, "选择图片"), SELECTOR_PHOTO);
        } else {
            activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), SELECTOR_PHOTO);
        }
    }

    /**
     * 相机拍照
     *
     * @param fileName 必须要个filename用于区分
     */
    public void takePhoto(String fileName /* 如：img1.png */) {
        File savePath = new File(activity.getExternalCacheDir() + "/xunxing_photo/");
        if (!savePath.exists()) {
            savePath.mkdirs();
        }
        imgTakePhotoFile = new File(savePath, fileName);
        //拍照路径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(activity, "com.sdxxtop.zhidian.FileProvider", imgTakePhotoFile);
        } else {
            uri = Uri.fromFile(imgTakePhotoFile);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        tempType = TAKE_CAMERA;
        if (fragment != null) {
            fragment.startActivityForResult(intent, TAKE_CAMERA);
        } else {
            activity.startActivityForResult(intent, TAKE_CAMERA);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECTOR_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri == null) {
                ToastUtil.show("获取图片失败");
                return;
            }
            Cursor cursor = activity.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    String path = cursor.getString(0);
                    if (!TextUtils.isEmpty(path)) {
                        savePath(path);
                    } else {
                        ToastUtil.show("获取图片失败");
                    }
                }
                cursor.close();
            }
        } else if (requestCode == TAKE_CAMERA && resultCode == Activity.RESULT_OK && uri != null) {
            Uri uri = getImageContentUri(imgTakePhotoFile);
            Cursor cursor = activity.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    String path = cursor.getString(0);
                    if (!TextUtils.isEmpty(path)) {
                        savePath(path);
                    }
                }
                cursor.close();
            } else {
                ToastUtil.show("获取图片失败");
            }
        }
    }

    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            cursor.close();
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DATA, filePath);
                return activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            } else {
                return uri;
            }
        }
    }

    private void savePath(String path) {
        try {
            File file = new File(path);
            FileInputStream is = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            if (bitmap != null) {
                if (targetImageView != null) {
                    targetImageView.setImageBitmap(bitmap);
                }
                imgPath = path;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
