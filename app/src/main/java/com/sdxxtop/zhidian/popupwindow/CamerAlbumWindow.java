package com.sdxxtop.zhidian.popupwindow;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.sdxxtop.zhidian.R;

/**
 * @author lxl
 * @date 2018/5/1  23:10
 * @desc
 */
public class CamerAlbumWindow extends PopupWindow {

    private LayoutInflater inflater;
    private FragmentActivity mActivity;

    Button bt_album;
    Button bt_camera;
    Button bt_cancle;
    //获取屏幕宽高

    public CamerAlbumWindow(FragmentActivity mActivity) {
        this.mActivity = mActivity;
        inflater = LayoutInflater.from(mActivity);
        initView();
        initData();
    }

    private void initView() {
        View view = inflater.inflate(R.layout.popupwindow_camera_pic, null);
        this.setContentView(view);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);

        bt_album = (Button) view.findViewById(R.id.btn_pop_album);
        bt_camera = (Button) view.findViewById(R.id.btn_pop_camera);
        bt_cancle = (Button) view.findViewById(R.id.btn_pop_cancel);

        int weight = mActivity.getResources().getDisplayMetrics().widthPixels;
        int height = mActivity.getResources().getDisplayMetrics().heightPixels * 1 / 3;
        setWidth(weight);
        setHeight(height);

        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                mActivity.startActivityForResult(intent, 222);

                dismiss();

            }
        });

    }

    private void initData() {
    }
}
