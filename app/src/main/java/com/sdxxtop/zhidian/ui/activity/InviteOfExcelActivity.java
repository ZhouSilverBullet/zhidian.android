package com.sdxxtop.zhidian.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.ui.base.BaseActivity;

import butterknife.BindView;
import zhangphil.iosdialog.widget.AlertDialog;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：后台批量导入员工展示界面
 */
public class InviteOfExcelActivity extends BaseActivity {

    @BindView(R.id.tv_net2)
    TextView tvNet2;

    @Override
    protected void initView() {
        super.initView();

        tvNet2.setText("电脑端访问官网http://admin.sdxxtop.com/可以Excel批量导入员工并设置排班，管理设备等功能");
        SpannableStringBuilder builder = new SpannableStringBuilder(tvNet2.getText().toString());

        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#3296FA"));
        ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
        ForegroundColorSpan blackSpan2 = new ForegroundColorSpan(Color.parseColor("#333333"));


        builder.setSpan(blackSpan, 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(blueSpan, 8, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(blackSpan2, 32, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvNet2.setText(builder);

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        tvNet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog(mContext)
                        .builder()
                        .setTitle("拷贝提示")
                        .setMsg("是否拷贝官网链接？")
                        .setPositiveButton("拷贝", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                // 将文本内容放到系统剪贴板里。
                                if (cm != null) {
                                    cm.setText("http://admin.sdxxtop.com/");
                                }
                            }
                        })
                        .setNegativeButton("", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
            }
        });
    }

    @Override
    protected int getActivityView() {
        return R.layout.activity_invite_of_excel;
    }
}
