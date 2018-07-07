package com.sdxxtop.zhidian.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.sdxxtop.zhidian.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：${刘香龙} on 2018/5/3 0003 20:17
 * 类的描述：
 */
public class SelectTypeWindow extends PopupWindow {


    private LayoutInflater inflater;
    private Context mContext;
    List<String> strings;

    private ListView listView;

    public String stringType = "";

    private List<Map<String, Object>> list_map = new ArrayList<Map<String, Object>>();

    public SelectTypeWindow(Context mContext, List<String> strings) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.strings = strings;
        initView();
        initData();
    }

    private void initView() {
        View view = inflater.inflate(R.layout.item_select_type_window, null);
        this.setContentView(view);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(new ColorDrawable());
        listView = (ListView) view.findViewById(R.id.lv_conten);

        view.findViewById(R.id.btn_dissmis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initData() {

        for (int i = 0; i < strings.size(); i++) {
            Map<String, Object> items = new HashMap<String, Object>();
            items.put("name", strings.get(i));
            list_map.add(items);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                mContext,
                list_map,
                R.layout.item_select_type_window_listview,
                new String[]{"name"},
                new int[]{R.id.tv_content});

        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stringType = strings.get(position);
                dismiss();
            }
        });
    }
}
