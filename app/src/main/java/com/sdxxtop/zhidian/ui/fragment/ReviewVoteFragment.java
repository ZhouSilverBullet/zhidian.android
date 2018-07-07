package com.sdxxtop.zhidian.ui.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ShowVoteAdapter;
import com.sdxxtop.zhidian.entity.VoteIndexBean;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.widget.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：CaiCM
 * 日期：2018/4/9  时间：20:33
 * 邮箱：15010104100@163.com
 * 描述：去评议的Fragment
 */

public class ReviewVoteFragment extends BaseFragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener{
    @BindView(R.id.tv_noresult)
    TextView tvNoresult;
    @BindView(R.id.lv_vote)
    XListView lvVote;

    private List<VoteIndexBean.DataEntity.VoteEntity> listReviewVote = new ArrayList<>();
    private ShowVoteAdapter showVoteAdapter;
    private VoteIndexBean voteIndexBean;

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_vote_review;
    }

    @Override
    protected void initData() {
        showVoteAdapter = new ShowVoteAdapter(mContext, listReviewVote);
        lvVote.setEmptyView(tvNoresult);
        lvVote.setAdapter(showVoteAdapter);
    }

    @Override
    protected void initEvent() {
        lvVote.setPullRefreshEnable(true);
        lvVote.setPullLoadEnable(true);
        lvVote.setXListViewListener(this);
        lvVote.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        postVoteIndex(1);
    }


    /**
     * 民主评议主页网络请求（我发起的）
     */
    private void postVoteIndex(final int refreshType) {
//        if (voteIndexBean != null && voteIndexBean.getData().getNum() < 10) {
//            ToastUtil.show("暂无更多数据");
//            lvVote.stopRefresh();
//            lvVote.stopLoadMore();
//            return;
//        }
        Map<String, String> map = new HashMap<>();
        //PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID)
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        map.put("if", "2");
        map.put("sp", listReviewVote.size() + "");
        RequestUtils.getInstance().buildRequest().postVoteIndex(NetUtil.getBase64Data(map)).enqueue(new Callback<VoteIndexBean>() {
            @Override
            public void onResponse(Call<VoteIndexBean> call, Response<VoteIndexBean> response) {
//                voteIndexBean = response.body();
//                lvVote.stopRefresh();
//                lvVote.stopLoadMore();
//                if (voteIndexBean.getCode() != 200) {
//                    ToastUtil.show(voteIndexBean.getMsg());
//                    return;
//                }
////                if (voteIndexBean.getData().getIs_self() == 1) {
////                    mChangeObsver.onChangeCallBack(1);
////                }
//                if (refreshType == 1) {
//                    for (int i = 0; i < voteIndexBean.getData().getVote().size(); i++) {
//                        listReviewVote.add(i,voteIndexBean.getData().getVote().get(i));
//                    }
//                }else {
//                    listReviewVote.addAll(voteIndexBean.getData().getVote());
//                }
//                showVoteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<VoteIndexBean> call, Throwable t) {
                ToastUtil.show("网络错误");
                lvVote.stopRefresh();
                lvVote.stopLoadMore();
            }
        });
    }

    @Override
    public void onRefresh() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        lvVote.setRefreshTime(format.format(new Date()));
        postVoteIndex(1);
    }


    @Override
    public void onLoadMore() {
        postVoteIndex(2);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ToastUtil.show(position+"");
    }
}
