package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ShowCompanyBean;
import com.sdxxtop.zhidian.eventbus.ChangeCompanyEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.im.IMLoginHelper;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.activity.ChangeCompanyActivity;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/4/20  时间：16:28
 * 邮箱：15010104100@163.com
 * 描述：切换公司列表展示公司适配器
 */

public class ChangeCompanyAdapter extends BaseAdapter {

    private Context context;
    private List<ShowCompanyBean.DataEntityX.DataEntity> list;

    public ChangeCompanyAdapter(Context context, List<ShowCompanyBean.DataEntityX.DataEntity> list) {
        this.context = context;
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_show_company, null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        holder.tv_company_name.setText(list.get(position).getCompany_name());
        final boolean equals = (list.get(position).getCompany_id() + "").equals(PreferenceUtils.getInstance(context).getStringParam(ConstantValue.COMPANY_ID));
        holder.checkbox.setChecked(equals);
        holder.checkbox.setClickable(false);
        final MyViewHolder finalHolder = holder;
        holder.companyRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!equals) {
                    int company_id = list.get(position).getCompany_id();
                    toChangeIm(company_id);
//                    AppSession.getInstance().setCompanyId(company_id + "");
////                    PreferenceUtils.getInstance(context).saveParam(ConstantValue.COMPANY_ID, list.get(position).getCompany_id()+"");
//                    ToastUtil.show("切换成功");
//                    //切换成功发送消息
//                    EventBus.getDefault().post(new ChangeCompanyEvent());
//                    notifyDataSetChanged();
//                    ((ChangeCompanyActivity) context).finish();
                } else {
                    finalHolder.checkbox.setChecked(true);
                    ToastUtil.show("已经在" + /*+list.get(position).getCompany_id()+*/"这家公司");
                    notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }

    private void toChangeIm(final int company_id) {
        IMLoginHelper.getInstance().changeUserSignature(context,company_id + "", new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                AppSession.getInstance().setCompanyId(company_id + "");
//                    PreferenceUtils.getInstance(context).saveParam(ConstantValue.COMPANY_ID, list.get(position).getCompany_id()+"");
                ToastUtil.show("切换成功");
                //切换成功发送消息
                EventBus.getDefault().post(new ChangeCompanyEvent());
                notifyDataSetChanged();
                ((ChangeCompanyActivity) context).finish();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
            }
        });
    }

    static class MyViewHolder {
        TextView tv_company_name;
        CheckBox checkbox;
        RelativeLayout companyRl;

        public MyViewHolder(View view) {
            tv_company_name = (TextView) view.findViewById(R.id.tv_company_name);
            checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            companyRl = (RelativeLayout) view.findViewById(R.id.tv_company_rl);
        }
    }


}
