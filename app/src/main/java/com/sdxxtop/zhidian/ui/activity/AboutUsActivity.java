package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RelativeLayout;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.widget.SubTitleView;
import com.sdxxtop.zhidian.widget.TextAndTextView;

import butterknife.BindView;

/**
 * 作者：CaiCM
 * 日期：2018/4/18  时间：11:39
 * 邮箱：15010104100@163.com
 * 描述：关于我们界面
 */
public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.us_title_view)
    SubTitleView titleView;
    @BindView(R.id.text_and_text_phone)
    TextAndTextView textPhone;
    @BindView(R.id.text_and_text_mark)
    TextAndTextView textMark;

    @Override
    protected int getActivityView() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initView() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) textPhone.getTextRightText().getLayoutParams();
        layoutParams.rightMargin = 0;
    }

    @Override
    protected void initEvent() {
        textPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "18063162802"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        textMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipMarket();
            }
        });
    }

    private void skipMarket() {
//        MarketUtil.skipDialog(mContext);
    }
}
