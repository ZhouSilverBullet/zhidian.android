package com.tencent.qcloud.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.qcloud.bean.InputBean;
import com.tencent.qcloud.ui.R;

import java.util.List;

/**
 * Created by Administrator on 2018/7/30.
 */

public class ChatGridAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<InputBean> list;

    public ChatGridAdapter(Context context, List<InputBean> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_chat_input, null);
            viewHolder.chatImg = (ImageView) convertView.findViewById(R.id.item_chat_img);
            viewHolder.chatText = (TextView) convertView.findViewById(R.id.item_chat_text);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        InputBean inputBean = list.get(position);
        viewHolder.chatImg.setImageResource(inputBean.drawableRes);
        viewHolder.chatText.setText(inputBean.bottomName);

        return convertView;
    }

    class ViewHolder {
        ImageView chatImg;
        TextView chatText;
    }
}
