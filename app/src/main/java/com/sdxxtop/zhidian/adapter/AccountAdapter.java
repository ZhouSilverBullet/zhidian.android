package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.AccountBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：CaiCM
 * 日期：2018/4/6  时间：09:54
 * 邮箱：15010104100@163.com
 * 描述：历史账号密码实体
 */

public class AccountAdapter extends BaseAdapter implements View.OnClickListener {

    public AccountAdapter(Context context, List<AccountBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    private Context context;
    private List<AccountBean> beans;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_account, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvAccount.setText(beans.get(position).getAccount());
        holder.ivDelect.setTag(position);
        holder.ivDelect.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        beans.remove((int) v.getTag());
        AppSession.getInstance().saveHistoryAccount(beans);
        notifyDataSetChanged();
        if (adapterClickListener != null) {
            adapterClickListener.adapterClick(getCount());
        }
    }


    static class ViewHolder {
        @BindView(R.id.tvAccount)
        TextView tvAccount;
        @BindView(R.id.ivDelect)
        ImageView ivDelect;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private AdapterClickListener adapterClickListener;

    public void setAdapterClickListener(AdapterClickListener adapterClickListener) {
        this.adapterClickListener = adapterClickListener;
    }

    public interface AdapterClickListener {
        void adapterClick(int count);
    }
}
