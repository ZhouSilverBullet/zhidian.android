package com.sdxxtop.zhidian.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ReportColorTextBean;
import com.sdxxtop.zhidian.entity.StateIndexBean;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.activity.StatReportActivity;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.ItemDivider;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/18.
 */

public class StatReportFragment extends BaseFragment {
    public static final int TYPE_ALL = 10;
    public static final int TYPE_PART = 11;
    public static final int TYPE_USER = 12;
    @BindView(R.id.fragment_stat_report_part)
    TextView partTitleText;
    @BindView(R.id.fragment_stat_report_part_recycler)
    RecyclerView partRecyclerView;
    @BindView(R.id.fragment_stat_report_user)
    TextView userTitleText;
    @BindView(R.id.fragment_stat_report_user_recycler)
    RecyclerView userRecyclerView;
    @BindView(R.id.fragment_stat_report_10divider)
    View dividerView;
    private int type;
    private ReportAdapter partAdapter;
    private Report2Adapter userAdapter;
    private List<ReportColorTextBean.DataBean.PartListBean> part_list;
    private List<ReportColorTextBean.DataBean.UserListBean> user_list;

    public static StatReportFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        StatReportFragment fragment = new StatReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initVariables() {
        if (getArguments() != null) {
            type = getArguments().getInt("type", -1);
        }
    }

    @Override
    protected void initView() {

        if (type == TYPE_ALL) {
            setRecycler(partRecyclerView);
            setRecycler(userRecyclerView);

            partAdapter = new ReportAdapter(R.layout.item_stat_report_recycler);
            partRecyclerView.setAdapter(partAdapter);

            userAdapter = new Report2Adapter(R.layout.item_stat_report_recycler);
            userRecyclerView.setAdapter(userAdapter);
        } else if (type == TYPE_PART) {
            dividerView.setVisibility(View.GONE);
            userTitleText.setVisibility(View.GONE);
            userRecyclerView.setVisibility(View.GONE);
            setRecycler(partRecyclerView);

            partAdapter = new ReportAdapter(R.layout.item_stat_report_recycler);
            partRecyclerView.setAdapter(partAdapter);

        } else if (type == TYPE_USER) {
            dividerView.setVisibility(View.GONE);
            partTitleText.setVisibility(View.GONE);
            partRecyclerView.setVisibility(View.GONE);
            setRecycler(userRecyclerView);

            userAdapter = new Report2Adapter(R.layout.item_stat_report_recycler);
            userRecyclerView.setAdapter(userAdapter);
        }


    }

    @Override
    protected void initData() {
        super.initData();
        InputStream open = null;
        try {
            open = mContext.getAssets().open("stat_report_file.txt");
            StringBuilder sb = new StringBuilder();
            int len = -1;
            byte[] bs = new byte[1024 * 4];
            while ((len = open.read(bs)) != -1) {
                sb.append(new String(bs, 0, len));
            }
            String jsonValue = sb.toString();
            ReportColorTextBean colorTextBean = new Gson().fromJson(jsonValue, ReportColorTextBean.class);
            ReportColorTextBean.DataBean data = colorTextBean.getData();
            if (partAdapter != null) {
                part_list = data.getPart_list();
                partAdapter.replaceData(part_list);
            }

            if (userAdapter != null) {
                user_list = data.getUser_list();
                userAdapter.replaceData(user_list);
            }

            if (type == TYPE_ALL) {
                ((StatReportActivity) mContext).loadData();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setRecycler(RecyclerView recyclerView) {
        recyclerView.setFocusable(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new ItemDivider());
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_stat_report;
    }

    public void setData(List<Integer> userList, List<Integer> partList) {
        if (type == TYPE_ALL) {
            handlePartList(partList);
            handleUserList(userList);
        } else if (type == TYPE_PART) {
            handlePartList(partList);
        } else if (type == TYPE_USER) {
            handleUserList(userList);
        }

        ((StatReportActivity) mContext).loadControlData();
    }

    private void handleUserList(List<Integer> userList) {
        if (user_list == null) {
            return;
        }
        List<ReportColorTextBean.DataBean.UserListBean> tempData = new ArrayList<>();
        for (Integer integer : userList) {
            tempData.add(user_list.get(integer - 1));
        }
        userAdapter.replaceData(tempData);
    }

    public void loadControlData(StateIndexBean.DataBean data) {
        String part_stat = data.getPart_stat();
        String user_stat = data.getUser_stat();
        if (!TextUtils.isEmpty(user_stat) && userAdapter != null) {
            String[] userStat = user_stat.split(",");
            List<ReportColorTextBean.DataBean.UserListBean> tempData = new ArrayList<>(userAdapter.getData());
            for (ReportColorTextBean.DataBean.UserListBean userDatum : tempData) {
                userDatum.setOpen(false);
            }
            for (String s : userStat) {
                if (!TextUtils.isEmpty(s)) {
                    for (ReportColorTextBean.DataBean.UserListBean tempDatum : tempData) {
                        if (s.equals(tempDatum.getType())) {
                            tempDatum.setOpen(true);
                            break;
                        }
                    }
                }
            }
            userAdapter.replaceData(tempData);
        }

        if (!TextUtils.isEmpty(part_stat) && partAdapter != null) {
            String[] partStat = part_stat.split(",");
            List<ReportColorTextBean.DataBean.PartListBean> tempData = new ArrayList<>(partAdapter.getData());
            for (ReportColorTextBean.DataBean.PartListBean tempDatum : tempData) {
                tempDatum.setOpen(false);
            }
            for (String s : partStat) {
                if (!TextUtils.isEmpty(s)) {
                    for (ReportColorTextBean.DataBean.PartListBean tempDatum : tempData) {
                        if (s.equals(tempDatum.getType())) {
                            tempDatum.setOpen(true);
                            break;
                        }
                    }
                }
            }
            partAdapter.replaceData(tempData);
        }
    }

    private void handlePartList(List<Integer> partList) {
        if (part_list == null) {
            return;
        }
        List<ReportColorTextBean.DataBean.PartListBean> tempData = new ArrayList<>();
        for (Integer integer : partList) {
            tempData.add(part_list.get(integer));
        }
        partAdapter.replaceData(tempData);
    }

    class ReportAdapter extends BaseQuickAdapter<ReportColorTextBean.DataBean.PartListBean, BaseViewHolder> {
        public ReportAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final ReportColorTextBean.DataBean.PartListBean item) {
            TextView reportBg = helper.getView(R.id.stat_report_bg);
            TextView reportTitle = helper.getView(R.id.stat_report_title);
            TextView reportContent = helper.getView(R.id.stat_report_content);
            final TextView reportStatusBtn = helper.getView(R.id.stat_report_status_btn);

            reportBg.setBackgroundColor(Color.parseColor(item.getBgColor()));
            reportContent.setText(item.getDec());
            reportTitle.setText(item.getTextValue());

            final boolean open = item.isOpen();
            isOpenChange(reportStatusBtn, open);
            reportStatusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //网络请求
                    Params params = new Params();
                    params.put("tp", 2);
                    params.put("st", !open ? 1 : 2);
                    params.put("si", item.getType());
                    showProgressDialog("");
                    RequestUtils.createRequest().postStatModify(params.getData()).enqueue(new RequestCallback<BaseModel>(new IRequestListener<BaseModel>() {
                        @Override
                        public void onSuccess(BaseModel baseModel) {
                            isOpenChange(reportStatusBtn, !open);
                            closeProgressDialog();
//                            showToast(baseModel.msg);
                            item.setOpen(!open);
                            notifyDataSetChanged();

                            ((StatReportActivity) mContext).loadControlData();
                        }

                        @Override
                        public void onFailure(int code, String errorMsg) {
                            closeProgressDialog();
                            showToast(errorMsg);
                        }
                    }));
                }
            });

        }
    }

    private void isOpenChange(TextView reportStatusBtn, boolean isOpen) {
        if (isOpen) {
            reportStatusBtn.setBackgroundResource(R.drawable.item_stat_report_red_bg);
            reportStatusBtn.setText("取消");
            reportStatusBtn.setTextColor(getResources().getColor(R.color.dot_red));
        } else {
            reportStatusBtn.setBackgroundResource(R.drawable.item_stat_report_blue_bg);
            reportStatusBtn.setText("开启");
            reportStatusBtn.setTextColor(getResources().getColor(R.color.blue));
        }
    }

    class Report2Adapter extends BaseQuickAdapter<ReportColorTextBean.DataBean.UserListBean, BaseViewHolder> {
        public Report2Adapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final ReportColorTextBean.DataBean.UserListBean item) {
            TextView reportBg = helper.getView(R.id.stat_report_bg);
            TextView reportTitle = helper.getView(R.id.stat_report_title);
            TextView reportContent = helper.getView(R.id.stat_report_content);
            final TextView reportStatusBtn = helper.getView(R.id.stat_report_status_btn);

            reportBg.setBackgroundColor(Color.parseColor(item.getBgColor()));
            reportContent.setText(item.getDec());
            reportTitle.setText(item.getTextValue());

            final boolean open = item.isOpen();
            isOpenChange(reportStatusBtn, open);
            reportStatusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //网络请求
                    Params params = new Params();
                    params.put("tp", 1);
                    params.put("st", !open ? 1 : 2);
                    params.put("si", item.getType());
                    showProgressDialog("");
                    RequestUtils.createRequest().postStatModify(params.getData()).enqueue(new RequestCallback<BaseModel>(new IRequestListener<BaseModel>() {
                        @Override
                        public void onSuccess(BaseModel baseModel) {
                            isOpenChange(reportStatusBtn, !open);
                            closeProgressDialog();
//                            showToast(baseModel.msg);
                            item.setOpen(!open);
                            notifyDataSetChanged();

                            ((StatReportActivity) mContext).loadControlData();
                        }

                        @Override
                        public void onFailure(int code, String errorMsg) {
                            closeProgressDialog();
                            showToast(errorMsg);
                        }
                    }));
                }
            });

        }
    }

}
