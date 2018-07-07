package com.sdxxtop.zhidian.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ContactPartBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: ZhangHD
 * 日期: 2018/3/23 时间: 23:48
 * 描述:自定义联系人View
 */

public class LinkManView extends RelativeLayout {

    private List<ContactPartBean.DataEntity.NavEntity> dataList;
    private List<TextView> textViewList = new ArrayList<>();
    private TextSwitchObsever textSwitchObsever;
    private LinearLayout ll_layout;
    private Context context;

    public LinkManView(Context context) {
        super(context);
        initView(context);
    }

    public LinkManView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }


    private void initView(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_textswitchview, this, true);
        ll_layout = (LinearLayout) view.findViewById(R.id.ll_layout);
        TextView allCompany = new TextView(context);
        allCompany.setText("  全公司 ");
        allCompany.setTextSize(16);
        allCompany.setOnClickListener(onClickListener);
        allCompany.setTextColor(Color.parseColor("#999999"));
        allCompany.setTag(0);
        ll_layout.addView(allCompany);
        textViewList.add(allCompany);
    }

    public void setListener(TextSwitchObsever textSwitchObsever) {
        this.textSwitchObsever = textSwitchObsever;
    }

    /**
     * 外部调用，填充数据
     */
    public void initView(List<ContactPartBean.DataEntity.NavEntity> dataList) {
        this.dataList = dataList;
        ll_layout.removeAllViews();
        textViewList.clear();

        TextView allCompany = new TextView(context);
        allCompany.setText("  全公司 ");
        allCompany.setTextSize(16);
        allCompany.setOnClickListener(onClickListener);
        allCompany.setTextColor(Color.parseColor("#999999"));
        allCompany.setTag(0);
        ll_layout.addView(allCompany);
        textViewList.add(allCompany);

        for (int i = 0; i < dataList.size(); i++) {
            TextView textView = new TextView(context);
            textView.setText("> " + dataList.get(i).getPart_name() + " ");
            textView.setTextSize(16);
            textView.setTextColor(Color.parseColor("#3296FA"));
            textView.setOnClickListener(onClickListener);
            textView.setTag(i + 1);
            ll_layout.addView(textView);
            textViewList.add(textView);
        }
    }

    /**
     * 点击事件处理
     */
    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            for (int i = 0; i < textViewList.size(); i++) {
                if ((int) view.getTag() == i) {
                    if (null != textSwitchObsever) {
                        if (i == 0) {
                            ll_layout.removeAllViews();
                            textViewList.clear();

                            TextView allCompany = new TextView(context);
                            allCompany.setText("  全公司 ");
                            allCompany.setTextSize(16);
                            allCompany.setOnClickListener(onClickListener);
                            allCompany.setTextColor(Color.parseColor("#999999"));
                            allCompany.setTag(0);
                            ll_layout.addView(allCompany);
                            textViewList.add(allCompany);
                        }
                        textSwitchObsever.onTextSwitchCallback(i);
                    }
                }
            }
        }
    };

    /**
     * 文字变化的观察者
     */
    public interface TextSwitchObsever {
        void onTextSwitchCallback(int position);
    }
}
