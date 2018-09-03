package com.tencent.qcloud.ui;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 发送语音提示控件
 */
public class VoiceSendingView extends RelativeLayout {


    private final TextView mecropText;
    private AnimationDrawable frameAnimation;
    private final ImageView img;
    private final ImageView img2;

    public VoiceSendingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.voice_sending, this);
        img = (ImageView) findViewById(R.id.microphone);
        img2 = (ImageView) findViewById(R.id.microphone2);
        mecropText = (TextView) findViewById(R.id.microphone_text);
        img.setBackgroundResource(R.drawable.animation_voice);
        frameAnimation = (AnimationDrawable) img.getBackground();
        img2.setImageResource(R.drawable.voice_cancel);
        img2.setVisibility(GONE);
    }

    public void showRecording() {
        frameAnimation.start();
    }

    public void showCancel(boolean isCancel) {
        if (img != null) {
            if (isCancel) {
                img.setVisibility(GONE);
                img2.setVisibility(VISIBLE);
                mecropText.setText("松开手指，取消发送");
                mecropText.setBackgroundColor(getResources().getColor(R.color.btn_red));
            } else {
                img.setVisibility(VISIBLE);
                img2.setVisibility(GONE);
                mecropText.setBackgroundColor(getResources().getColor(R.color.transparent));
                mecropText.setText("手指上滑，取消发送");
            }
        }
    }

    public void release() {
        frameAnimation.stop();
    }
}
