package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.UcenterOutIndexBean;
import com.sdxxtop.zhidian.entity.UcenterOutModifyBean;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：CaiCM
 * 日期：2018/5/3  时间：12:04
 * 邮箱：15010104100@163.com
 * 描述：外勤权限根据部门 展示人员信息适配器
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyViewHolder> {

    //    Fragment fragment;
    Context context;
    List<UcenterOutIndexBean.DataEntity.UserinfoEntity> list;

    public GridAdapter(Context context, List<UcenterOutIndexBean.DataEntity.UserinfoEntity> list, OnCallBackListener onCallBackListener) {
        this.context = context;
        this.list = list;
        mOnCallBackListener = onCallBackListener;
//        this.fragment = fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_outpower_all, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.iv_item_recy_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = list.get(position).getUserid() + "";
                if (list.get(position).getIs_out() == 1){
                    postUcenterOutModify(userid, "2", holder);
                }
                else {
                    postUcenterOutModify(userid, "1", holder);
                }
            }
        });

        holder.tv_item_recy_name.setText(list.get(position).getName());
        if (list.get(position).getImg().startsWith("#")) {
            holder.iv_item_recy_img.setColorFilter(Color.parseColor(list.get(position).getImg()));
        } else {
            Glide.with(context).load(list.get(position).getImg()).into(holder.iv_item_recy_img);
        }

        String name = list.get(position).getName();
        if (list.get(position).getImg().toString().startsWith("#")) {
            //图片上展示的两个字
            if (name.length() >= 2) {
                holder.tv_item_recy_short_name.setText(name.substring(name.length() - 2, name.length()));
            } else {
                holder.tv_item_recy_short_name.setText(name);
            }
        }
        holder.tv_item_recy_name.setText(name);
        holder.iv_item_power.setVisibility(list.get(position).getIs_out() == 1 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_item_power;
        CircleImageView iv_item_recy_img;
        TextView tv_item_recy_name, tv_item_recy_short_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_item_recy_img = (CircleImageView) itemView.findViewById(R.id.iv_item_recy_img);
            tv_item_recy_name = (TextView) itemView.findViewById(R.id.tv_item_recy_name);
            tv_item_recy_short_name = (TextView) itemView.findViewById(R.id.tv_item_recy_short_name);
            iv_item_power = (ImageView) itemView.findViewById(R.id.iv_item_power);
        }
    }

    /**
     * 修改外勤权限，网络请求
     */
    private void postUcenterOutModify(String userid, final String st, final MyViewHolder holder) {
        Map<String, String> map = new HashMap<>();
        map.put("ci", PreferenceUtils.getInstance(context).getStringParam(ConstantValue.COMPANY_ID));
        map.put("ui", userid);
        map.put("st", st);
        String base64Data = NetUtil.getBase64Data(map);
//        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postUcenterOutModify(base64Data).enqueue(new Callback<UcenterOutModifyBean>() {
            @Override
            public void onResponse(Call<UcenterOutModifyBean> call, Response<UcenterOutModifyBean> response) {
//                closeProgressDialog();
                UcenterOutModifyBean ucenterOutModifyBean = response.body();
                if (ucenterOutModifyBean.getCode() == 200) {
                    //接口回调记得判空   不然很容易空指针
                    if (mOnCallBackListener != null) {
                        mOnCallBackListener.onCallBack();
                    }
                } else {
                    ToastUtil.show(ucenterOutModifyBean.getMsg());
                }
            }

            @Override
            public void onFailure(Call<UcenterOutModifyBean> call, Throwable t) {
//                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }

    private OnCallBackListener mOnCallBackListener;

    public interface OnCallBackListener{
        void onCallBack();
    }

    public void setmOnCallBackListener(OnCallBackListener onCallBackListener){
        this.mOnCallBackListener = onCallBackListener;
    }
}
