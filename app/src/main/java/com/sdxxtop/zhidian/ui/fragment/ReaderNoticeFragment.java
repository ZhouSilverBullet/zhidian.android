package com.sdxxtop.zhidian.ui.fragment;

import android.annotation.SuppressLint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ShowNoticeReaderAdapter;
import com.sdxxtop.zhidian.entity.NoticeShowPeopleBean;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：CaiCM
 * 日期：2018/3/29  时间：18:07
 * 邮箱：15010104100@163.com
 * 描述：阅读过公告人的展示
 */

@SuppressLint("ValidFragment")
public class ReaderNoticeFragment extends BaseFragment {
    @BindView(R.id.rv_grade)
    RecyclerView rvGrade;

    private String noticeid;
    private List<NoticeShowPeopleBean.DataBean.ReadBean> list = new ArrayList<>();
    private ShowNoticeReaderAdapter showNoticeReaderAdapter;

    @SuppressLint("ValidFragment")
    public ReaderNoticeFragment(String noticeid) {
        this.noticeid = noticeid;
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_notice_reader;
    }

    @Override
    protected void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvGrade.setLayoutManager(gridLayoutManager);
        showNoticeReaderAdapter = new ShowNoticeReaderAdapter(mContext, list);
        rvGrade.setAdapter(showNoticeReaderAdapter);
    }

    @Override
    protected void initData() {
        postNoticeShowPeople();
    }

    /**
     * 公告阅读人详情网络请求
     */
    private void postNoticeShowPeople() {
        Map<String, String> map = new HashMap<>();
        LogUtils.e("log", "ReaderFragmentNoticeId:" + noticeid);
        map.put("ni", noticeid);
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postShowNoticePeople(base64Data).enqueue(new Callback<NoticeShowPeopleBean>() {
            @Override
            public void onResponse(Call<NoticeShowPeopleBean> call, Response<NoticeShowPeopleBean> response) {
                closeProgressDialog();
                NoticeShowPeopleBean noticeShowPeopleBean = response.body();
                if (noticeShowPeopleBean.getCode() == 200) {
                    list.clear();
                    list.addAll(noticeShowPeopleBean.getData().getRead());
                    showNoticeReaderAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show(noticeShowPeopleBean.getMsg().toString());
                }
            }

            @Override
            public void onFailure(Call<NoticeShowPeopleBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }
}