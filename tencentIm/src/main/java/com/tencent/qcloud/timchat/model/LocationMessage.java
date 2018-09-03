package com.tencent.qcloud.timchat.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMImage;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMImageType;
import com.tencent.imsdk.TIMLocationElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.qcloud.timchat.R;
import com.tencent.qcloud.timchat.adapters.ChatAdapter;
import com.tencent.qcloud.timchat.utils.FileUtil;

import java.io.IOException;

/**
 * Created by Administrator on 2018/8/3.
 */

public class LocationMessage extends Message {

    public LocationMessage(TIMMessage message) {
        this.message = message;
    }

    @Override
    public void showMessage(final ChatAdapter.ViewHolder viewHolder, final Context context) {

        clearView(viewHolder);
        if (checkRevoke(viewHolder)) return;

        if (message.getElementCount() < 2) {
            return;
        }

        final View locationView = LayoutInflater.from(context).inflate(R.layout.message_loaction_view, null);
        final TIMLocationElem locationElem = (TIMLocationElem) message.getElement(0);
        TIMImageElem imageElem = (TIMImageElem) message.getElement(1);

        String desc = locationElem.getDesc();

        final ImageView locationImg = (ImageView) locationView.findViewById(R.id.message_location_view_img);
        TextView locationDec = (TextView) locationView.findViewById(R.id.message_location_view_dec);

        locationDec.setText(desc);
        switch (message.status()) {
            case Sending:
                locationImg.setImageBitmap(getThumb(imageElem.getPath()));
                clearView(viewHolder);
                getBubbleView(viewHolder).addView(locationView);
                break;
            case SendSucc:
                for (final TIMImage image : imageElem.getImageList()) {
                    if (image.getType() == TIMImageType.Thumb) {
                        final String uuid = image.getUuid();
                        if (FileUtil.isCacheFileExist(uuid)) {
                            showThumb(viewHolder, locationView, locationImg, uuid);
                        } else {
                            image.getImage(FileUtil.getCacheFilePath(uuid), new TIMCallBack() {
                                @Override
                                public void onError(int code, String desc) {//获取图片失败
                                    //错误码code和错误描述desc，可用于定位请求失败原因
                                    //错误码code含义请参见错误码表
                                    Log.e(TAG, "getImage failed. code: " + code + " errmsg: " + desc);
                                }

                                @Override
                                public void onSuccess() {//成功，参数为图片数据
                                    showThumb(viewHolder, locationView, locationImg, uuid);
                                }
                            });
                        }
                    }
                    if (image.getType() == TIMImageType.Original) {
                        final String uuid = image.getUuid();
//                        setImageEvent(viewHolder, uuid,context);
                        getBubbleView(viewHolder).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                navToImageview(image, context);
                                Intent intent = new Intent();
                                intent.setClassName("com.sdxxtop.zhidian", "com.sdxxtop.zhidian.ui.activity.AmapPoiActivity");
                                double latitude = locationElem.getLatitude();
                                double longitude = locationElem.getLongitude();
                                intent.putExtra("location_latitude", latitude);
                                intent.putExtra("location_longitude", longitude);
                                context.startActivity(intent);
                            }
                        });
                    }
                }
                break;
        }
        showStatus(viewHolder);

    }

    /**
     * 显示消息状态
     *
     * @param viewHolder 界面样式
     */
    public void showStatus(ChatAdapter.ViewHolder viewHolder) {
        switch (message.status()) {
            case Sending:
                viewHolder.error.setVisibility(View.GONE);
                viewHolder.sending.setVisibility(View.VISIBLE);
                break;
            case SendSucc:
                viewHolder.error.setVisibility(View.GONE);
                viewHolder.sending.setVisibility(View.GONE);
                break;
            case SendFail:
                viewHolder.error.setVisibility(View.VISIBLE);
                viewHolder.sending.setVisibility(View.GONE);
                viewHolder.leftPanel.setVisibility(View.GONE);
                break;
        }
    }

    private void showThumb(final ChatAdapter.ViewHolder viewHolder, View view, ImageView imageView, String filename) {
        Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.getCacheFilePath(filename));
        imageView.setImageBitmap(bitmap);
        getBubbleView(viewHolder).addView(view);
    }

    @Override
    public String getSummary() {
        String summary = getRevokeSummary();
        if (summary != null) return summary;
        return "[位置]";
    }

    @Override
    public void save() {

    }

    /**
     * 生成缩略图
     * 缩略图是将原图等比压缩，压缩后宽、高中较小的一个等于198像素
     * 详细信息参见文档
     */
    private Bitmap getThumb(String path) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int reqWidth, reqHeight, width = options.outWidth, height = options.outHeight;
        if (width > height) {
            reqWidth = 198;
            reqHeight = (reqWidth * height) / width;
        } else {
            reqHeight = 198;
            reqWidth = (width * reqHeight) / height;
        }
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        try {
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            Matrix mat = new Matrix();
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    mat.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    mat.postRotate(180);
                    break;
            }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
        } catch (IOException e) {
            return null;
        }
    }

    public static TIMMessage createMessage(String imgPath, double latitude, double longitude, String address) {
        TIMMessage msg = new TIMMessage();

        TIMLocationElem elem = new TIMLocationElem();
        elem.setLatitude(latitude);   //设置纬度
        elem.setLongitude(longitude);   //设置经度
        elem.setDesc(address);
        msg.addElement(elem);

        TIMImageElem imageElem = new TIMImageElem();
        imageElem.setPath(imgPath);
        msg.addElement(imageElem);

        if (msg.addElement(elem) != 0) {
            return null;
        }

        return msg;
    }
}
