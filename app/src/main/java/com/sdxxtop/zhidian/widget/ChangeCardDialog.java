package com.sdxxtop.zhidian.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sdxxtop.zhidian.R;

/**
 * Created by Administrator on 2018/5/5.
 */

public class ChangeCardDialog {

    public static final int GPS = 1;
    public static final int WIFI = 2;
    public static final int FACE = 3;
    public static final int FIELD = 4;


    private Context mContext;
    private Dialog dialog;

    public ChangeCardDialog(Context mContext) {
        this.mContext = mContext;
    }

    public void show() {
        dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_change_card_layout, null);
        dialog.setContentView(view);

        dialog.findViewById(R.id.dialog_change_card_gps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDialogClick(GPS);
                }
                dismiss();
            }
        });
        dialog.findViewById(R.id.dialog_change_card_wifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDialogClick(WIFI);
                }
                dismiss();
            }
        });
        dialog.findViewById(R.id.dialog_change_card_face).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDialogClick(FACE);
                }
                dismiss();
            }
        });
        dialog.findViewById(R.id.dialog_change_card_field).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDialogClick(FIELD);
                }
                dismiss();
            }
        });
        dialog.findViewById(R.id.dialog_change_card_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void setDialogClickListener(DialogClickListener listener) {
        this.listener = listener;
    }

    private DialogClickListener listener;

    public interface DialogClickListener {
        void onDialogClick(int position);
    }

}
