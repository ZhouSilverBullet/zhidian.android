package com.sdxxtop.zhidian.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.widget.CustomDialog;



public class DialogUtil {
    private static AnimationDrawable AniDraw;

    public static void showInfoDialog(Context context, String message,
                                      String titleStr, String positiveStr,
                                      DialogInterface.OnClickListener onClickListener) {
        CustomDialog.Builder localBuilder = new CustomDialog.Builder(context);
        localBuilder.setTitle(titleStr);
        localBuilder.setMessage(message);
        if (onClickListener == null)
            onClickListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            };
        localBuilder.setPositiveButton(positiveStr, onClickListener);
        localBuilder.create().show();
    }


    public static void showInfoDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {

        CustomDialog.Builder customBuilder = new CustomDialog.Builder(context);

        customBuilder.setTitle("温馨提示").setMessage(message)

                .setPositiveButton("确定", onClickListener);

        customBuilder.create().show();

    }

    public static void showInfoDialog(Context context, String title,
                                      String message) {

        CustomDialog.Builder customBuilder = new CustomDialog.Builder(context);
        if (null == title || "".equals(title)) {
            title = "温馨提示";
        }
        customBuilder.setTitle(title).setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        customBuilder.create().show();
    }

    /**
     * @param context  上下文
     * @param msg      提示消息
     * @param isCancel 是否可取消,true 可取消
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg, boolean isCancel) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
//		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v
                .findViewById(R.id.imgv_diaglog);
        TextView tipTextView = (TextView) v.findViewById(R.id.txt_diaglog);// 提示文字
        // 使用ImageView显示动画
        AniDraw = (AnimationDrawable) spaceshipImage.getBackground();
        AniDraw.start();
        //spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        if (TextUtils.isEmpty(msg)) {
            tipTextView.setText("正在加载");// 设置加载信息
        } else {
            tipTextView.setText(msg);// 设置加载信息
        }

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        if (NetWorkUtil.isNetConnected(context)) {
            loadingDialog.setCancelable(true);
        } else {
            loadingDialog.setCancelable(isCancel);
        }

        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
        return loadingDialog;
    }




}
