package com.sdxxtop.zhidian.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.GridAdapter;
import com.sdxxtop.zhidian.entity.UcenterOutIndexBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.activity.OutPowerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：CaiCM
 * 日期：2018/4/27  时间：21:07
 * 邮箱：15010104100@163.com
 * 描述：外勤权限Fragment（已开启）
 */

public class OutPowerFragment extends BaseScrollFragment {

    @BindView(R.id.tv_item_partname)
    TextView tvItemPartname;
    @BindView(R.id.rv_grade)
    RecyclerView rvGrade;

    private List<List<UcenterOutIndexBean.DataEntity.UserinfoEntity>> list = new ArrayList<>();

    private int type;

    public static OutPowerFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        OutPowerFragment fragment = new OutPowerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_outpower_open;
    }

    @Override
    protected void initVariables() {
        if (getArguments() != null) {
            type = getArguments().getInt("type", -1);
        }
    }

    @Override
    protected void initView() {
        setRecyclerView(rvGrade);
    }

    @Override
    protected void initData() {
        postUcenterOutIndex();
    }

    @Override
    protected void initEvent() {
        showOutPowerAllAdapter.setmOnCallBackListener(new GridAdapter.OnCallBackListener() {
            @Override
            public void onCallBack() {
                if (mContext instanceof OutPowerActivity) {
                    ((OutPowerActivity) mContext).refreshTabData();
                }
//                ArrayList<Fragment> fragmentList = ((OutPowerActivity) getActivity()).fragmentList;
//                if (fragmentList != null && fragmentList.size() > 0) {
//                    ((OutPowerAllFragment) fragmentList.get(0)).postUcenterOutIndex(pi);
//                    ((OutPowerCloseFragment) fragmentList.get(2)).postUcenterOutIndex(pi);
//                }
            }
        });
    }

    /**
     * 外勤权限主页，网络请求
     */
    public void postUcenterOutIndex() {
        final Params params = new Params();
        params.put("pi", "");
        params.put("te", type);
        showProgressDialog("");
        RequestUtils.createRequest().postUcenterOutIndex(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<UcenterOutIndexBean>() {
            @Override
            public void onSuccess(UcenterOutIndexBean ucenterOutIndexBean) {
                closeProgressDialog();
                UcenterOutIndexBean.DataEntity data = ucenterOutIndexBean.getData();
                if (data != null) {
                    List<UcenterOutIndexBean.DataEntity.PartEntity> part = data.getPart();
                    List<List<UcenterOutIndexBean.DataEntity.UserinfoEntity>> userinfo = data.getUserinfo();
                    if (part == null || userinfo == null) {
                        return;
                    }
                    showOutPowerAllAdapter.setPartList(part);
                    List<UcenterOutIndexBean.DataEntity.PartEntity> partEntityList = new ArrayList<>();
                    List<List<UcenterOutIndexBean.DataEntity.UserinfoEntity>> userinfo1 = new ArrayList<>();

                    for (UcenterOutIndexBean.DataEntity.PartEntity partEntity : part) {
                        int partId = partEntity.getPart_id();
                        String part_name = partEntity.getPart_name();
                        for (List<UcenterOutIndexBean.DataEntity.UserinfoEntity> userinfoEntities : userinfo) {
                            UcenterOutIndexBean.DataEntity.UserinfoEntity userinfoEntity = userinfoEntities.get(0);
                            if (userinfoEntity.getPart_id() == partId) {
                                userinfo1.add(userinfoEntities);
                                partEntityList.add(new UcenterOutIndexBean.DataEntity.PartEntity(partId, part_name));
                            }
                        }
                    }

                    mPartEntityList = partEntityList;

                    if (userinfo != null) {
                        showOutPowerAllAdapter.replaceData(userinfo1);
                    }

                    if (type == 0) {
                        ((OutPowerActivity) mContext).refreshData(mPartEntityList);
                    }
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
                showToast(errorMsg);
            }
        }));
    }

    List<UcenterOutIndexBean.DataEntity.PartEntity> mPartEntityList;

    public List<UcenterOutIndexBean.DataEntity.PartEntity> getDataPart() {

        return mPartEntityList;
    }
}
