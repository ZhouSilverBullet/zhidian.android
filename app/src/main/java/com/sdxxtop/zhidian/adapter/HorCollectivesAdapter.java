package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：${刘香龙} on 2018/5/4 0004 16:45
 * 类的描述：
 */
public class HorCollectivesAdapter  extends BaseAdapter implements View.OnClickListener {


    public HorCollectivesAdapter(Context context, List<String> strings) {
        this.context = context;
        this.strings = strings;
    }


    private Context context;
    private List<String> strings;


    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int position) {
        return strings.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HorCollectivesAdapter.ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hor_collectives, null);
            holder = new HorCollectivesAdapter.ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (HorCollectivesAdapter.ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(strings.get(position).toString());

        return convertView;
    }

    @Override
    public void onClick(View v) {

    }


    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
