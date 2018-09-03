package com.sdxxtop.zhidian.popupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.RaceSelectWindowAdapter;
import com.sdxxtop.zhidian.entity.TestBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：${刘香龙} on 2018/4/28 0028 17:33
 * 类的描述：
 */
public class RaceSelectionWindow extends PopupWindow {

    private List<TestBean> beans;
    GridView gridView;

    private LayoutInflater inflater;
    private Context mContext;

    private RaceSelectWindowAdapter adapter;
    private Button submitBtn;

    public RaceSelectionWindow(Context mContext, List<TestBean> beans) {
        this.mContext = mContext;
        this.beans = beans;
        inflater = LayoutInflater.from(mContext);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null && listener != null) {
                    List<TestBean> beans = adapter.getBeans();
                    listener.onRace(beans);
                }
                dismiss();
            }
        });
    }

    private void initView() {
        View view = inflater.inflate(R.layout.item_race_selection_window_view, null);
        this.setContentView(view);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setClippingEnabled(false);
        submitBtn = (Button) view.findViewById(R.id.gridv_btn);
        gridView = (GridView) view.findViewById(R.id.gridv_race_select);
    }

    private void initData() {
        if (beans == null || beans.size() <= 0) {
            beans = new ArrayList<>();
            TestBean bean1 = new TestBean("全部", true, 0);
            TestBean bean2 = new TestBean("请假", false, 1);
            TestBean bean3 = new TestBean("漏打卡", false, 2);
            TestBean bean4 = new TestBean("消迟到早退", false, 3);
            TestBean bean5 = new TestBean("出差", false, 4);
            TestBean bean6 = new TestBean("加班", false, 5);
            TestBean bean7 = new TestBean("集体申请", false, 6);
            TestBean bean8 = new TestBean("调班", false, 8);
            TestBean bean9 = new TestBean("外勤", false, 9);
            TestBean bean10 = new TestBean("手机解绑", false, 10);
            TestBean bean11 = new TestBean("家长请假", false, 21);
            TestBean bean12 = new TestBean("家长拜访", false, 22);

            beans.add(bean1);
            beans.add(bean2);
            beans.add(bean3);
            beans.add(bean4);
            beans.add(bean5);
            beans.add(bean6);
            beans.add(bean7);
            beans.add(bean8);
            beans.add(bean9);
            beans.add(bean10);
            beans.add(bean11);
            beans.add(bean12);
        }

        adapter = new RaceSelectWindowAdapter(mContext, beans);
        gridView.setAdapter(adapter);
    }

    RaceClickListener listener;

    public void setRaceClickListener(RaceClickListener listener) {
        this.listener = listener;
    }

    public interface RaceClickListener {
        void onRace(List<TestBean> beans);
    }
}
