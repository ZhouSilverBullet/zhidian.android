package com.sdxxtop.zhidian.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.NoticeReadAdapter;
import com.sdxxtop.zhidian.entity.NoticeDataBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.obsever.CommonChangeObsver;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者: CaiCM
 * 日期: 2018/3/27 时间: 20:23
 * 邮箱：15010104100@163.com
 * 描述:未读公告Fragment
 */

public class NoReadNoticeFragment extends BaseFragment {
    @BindView(R.id.lv_recycler_view)
    RecyclerView lvRecyclerView;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    private NoticeReadAdapter noticeReadAdapter;
    private List<NoticeDataBean.DataBean.NoticeBean> listNotRead = new ArrayList<>();
    private CommonChangeObsver mChangeObsver;
    private int status = 2;

    public void setChangeObsver(CommonChangeObsver mChangeObsver) {
        this.mChangeObsver = mChangeObsver;
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_notice;
    }

    @Override
    protected void initView() {
        lvRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        lvRecyclerView.addItemDecoration(new ItemDivider().setDividerColor(0xFFE5E5E5).setDividerLeftOffset(ViewUtil.dp2px(mContext, 0)));
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (noticeReadAdapter != null) {
                    loadData(noticeReadAdapter.getData().size());
                }
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData(0);
            }
        });
    }

    @Override
    protected void initData() {
        loadData(0);
    }

    private void loadData(final int page) {
        Params params = new Params();
        params.put("ir", status);
        params.put("sp", page);
        RequestUtils.createRequest().postNoticeIndex(params.getData()).enqueue(
                new RequestCallback<>(new IRequestListener<NoticeDataBean>() {
                    @Override
                    public void onSuccess(NoticeDataBean noticeDataBean) {
                        NoticeDataBean.DataBean data = noticeDataBean.getData();
                        if (data != null && data.getNotice() != null) {
                            if (data.getIs_add() == 1) {
                                if (mChangeObsver != null) {
                                    mChangeObsver.onChangeCallBack(1);
                                }
                            } else {
                            }
                            if (noticeReadAdapter == null) {
                                noticeReadAdapter = new NoticeReadAdapter(R.layout.item_show_notice);
                                lvRecyclerView.setAdapter(noticeReadAdapter);
                                noticeReadAdapter.addData(data.getNotice());
                            } else {
                                if (page == 0) {
                                    noticeReadAdapter.replaceData(data.getNotice());
                                } else {
                                    noticeReadAdapter.addData(data.getNotice());
                                }
                            }
                        }
                        finishRefreshLayout(page);

                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        showToast(errorMsg);
                        finishRefreshLayout(page);
                    }
                }));
    }

    private void finishRefreshLayout(int page) {
        if (page == 0) {
            smartRefreshLayout.finishRefresh(200);
        } else {
            smartRefreshLayout.finishLoadMore();
        }
    }
}
