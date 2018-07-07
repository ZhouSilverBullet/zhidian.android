package com.sdxxtop.zhidian.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.TestBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：${刘香龙} on 2018/4/28 0028 17:48
 * 类的描述：
 */
public class RaceSelectWindowAdapter extends BaseAdapter {

    public RaceSelectWindowAdapter(Context context, List<TestBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    private Context context;
    private List<TestBean> beans;

    @Override
    public int getCount() {
        return beans == null ? 0 : beans.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_race_select_gridview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (beans.get(position).isSelected()) {
            holder.tvAccount.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvAccount.setBackgroundResource(R.drawable.shape_view_blue_solid_bg);
        } else {
            holder.tvAccount.setTextColor(context.getResources().getColor(R.color.notice_contentcolor));
            holder.tvAccount.setBackgroundResource(R.drawable.shape_view_border_bg);
        }

        holder.tvAccount.setText(beans.get(position).getString1());

        holder.tvAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0 && !beans.get(position).isSelected()) {
                    for (int i = 1; i < beans.size(); i++) {
                        beans.get(i).setSelected(false);
                    }
                    beans.get(0).setSelected(!beans.get(0).isSelected());
                } else {
                    beans.get(0).setSelected(false);
                    beans.get(position).setSelected(!beans.get(position).isSelected());
                }

                if (getTemp(beans)) {
                    for (int i = 1; i < beans.size(); i++) {
                        beans.get(i).setSelected(false);
                    }
                    beans.get(0).setSelected(!beans.get(0).isSelected());
                }

                notifyDataSetChanged();
            }
        });

        return convertView;
    }


    /**
     * 全部选中就是 全部
     *
     * @param beans
     * @return
     */
    private boolean getTemp(List<TestBean> beans) {
        int temp = 0;
        for (TestBean bean : beans) {
            if (bean.isSelected()) {
                temp = temp + 1;
            }
        }
        return temp == 9 || temp == 0;
    }

    public List<TestBean> getBeans() {
        return beans;
    }

    static class ViewHolder {
        @BindView(R.id.tv_content)
        TextView tvAccount;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
