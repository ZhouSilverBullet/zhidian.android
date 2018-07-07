package com.sdxxtop.zhidian.ui.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gelitenight.waveview.library.WaveView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.VoteIndexBean;
import com.sdxxtop.zhidian.eventbus.MineVoteFragEvent;
import com.sdxxtop.zhidian.eventbus.VoteRefreshEvent;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.activity.MineInitiateVoteActivity;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.TimeUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.utils.WaveHelper;
import com.sdxxtop.zhidian.widget.RatingBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineVoteFragment extends BaseFragment {

    public static final String MINE_INIT_TYPE = "mine_init_type";
    private int mineInitType; //默认变量

    public static final int type_vote_text = 10;  //投票文字
    public static final int type_vote_image = 11;  //投票图片


    public static final int type_vote_5_mark_no_oo = 12;  //总分为5分，无打分对象
    public static final int type_vote_5_mark_more_oo = 13; //总分为5分，有一个或多个打分对象
    public static final int type_vote_100_mark = 14; //总分大于5分，有一个或多个打分对象

    public static final int mine_init_type = 15;


    @BindView(R.id.fragment_mine_vote_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_mine_vote_smart_layout)
    SmartRefreshLayout refreshLayout;
    private VoteAdapter mAdapter;
    private int type;

    public MineVoteFragment() {
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_mine_vote;
    }

    public static MineVoteFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        MineVoteFragment fragment = new MineVoteFragment();
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
        super.initView();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        recyclerView.addItemDecoration(
//                new ItemDivider().setDividerWidth(ViewUtil.dp2px(mContext, 0)).setDividerColor(0x00000000));
    }

    @Override
    protected void initData() {
        super.initData();
        loadData(0);
    }

    private void loadData(final int page) {
        Params params = new Params();
        params.put("if", type);
        params.put("sp", page);

        RequestUtils.createRequest().postVoteIndex(params.getData())
                .enqueue(new RequestCallback<>(mContext, new IRequestListener<VoteIndexBean>() {
                    @Override
                    public void onSuccess(VoteIndexBean voteIndexBean) {
                        VoteIndexBean.DataEntity data = voteIndexBean.getData();
                        if (data != null) {
                            handleData(page, data);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        showToast(errorMsg);
                    }
                }));
    }

    private void handleData(int page, VoteIndexBean.DataEntity data) {
        List<VoteIndexBean.DataEntity.VoteEntity> vote = data.getVote();

        if (mAdapter == null) {
            mAdapter = new VoteAdapter(R.layout.item_mine_vote_recycler);
            recyclerView.setAdapter(mAdapter);
            //设置空布局
            setEmptyView();
            if (vote == null) { //显示个空的
                mAdapter.replaceData(new ArrayList<VoteIndexBean.DataEntity.VoteEntity>());
                return;
            }
            mAdapter.addData(vote);
        } else {
            if (vote == null) { //为空时不替换
                return;
            }
            if (page == 0) {
                mAdapter.replaceData(vote);
            } else {
                mAdapter.addData(vote);
            }
        }

        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
    }

    private void setEmptyView() {
        TextView textView = new TextView(mContext);
        textView.setTextColor(getResources().getColor(R.color.textcolor));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        textView.setText("暂无数据");
        mAdapter.setEmptyView(textView);
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParams);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (mAdapter != null) {
                    loadData(mAdapter.getData().size());
                }
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData(0);
            }
        });
    }

    class VoteAdapter extends BaseQuickAdapter<VoteIndexBean.DataEntity.VoteEntity, BaseViewHolder> {
        public static final int VOTE_VOTE = 1; //投票
        public static final int VOTE_SCORE = 2; //打分

        public VoteAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, VoteIndexBean.DataEntity.VoteEntity item) {

            TextView timeText = helper.getView(R.id.item_vote_time);
            TextView statusText = helper.getView(R.id.item_vote_status);
            TextView titleText = helper.getView(R.id.item_vote_title);
            //评议
            LinearLayout voteLayout = helper.getView(R.id.item_vote_vote_layout);
            LinearLayout voteWaveLayout1 = helper.getView(R.id.item_vote_vote_wave_layout1);
            LinearLayout voteWaveLayout2 = helper.getView(R.id.item_vote_vote_wave_layout2);
            WaveView voteWave1 = helper.getView(R.id.item_vote_vote_wave_1);
            WaveView voteWave2 = helper.getView(R.id.item_vote_vote_wave_2);
            TextView voteMore = helper.getView(R.id.item_vote_vote_more);
            TextView voteWaveText1 = helper.getView(R.id.item_vote_vote_wave_text1);
            TextView voteWaveText2 = helper.getView(R.id.item_vote_vote_wave_text2);

            //评分
            LinearLayout scoreLayout = helper.getView(R.id.item_score_layout);
            TextView scoreTitle = helper.getView(R.id.item_score_title);

            RatingBar scoreRatingBar = helper.getView(R.id.item_score_rating);

            RelativeLayout socreSeekBarLayout = helper.getView(R.id.item_score_seekbar_layout);
            SeekBar socreSeekBar = helper.getView(R.id.item_score_seekbar);
            TextView socreSeekBarValue = helper.getView(R.id.item_score_seekbar_value);


            titleText.setText(item.getTitle());
            //判断是否为今天
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String systemDate = format.format(new Date()).substring(0, 10);//获取系统日期
            String add_time = item.getAdd_time();
            if (!TextUtils.isEmpty(add_time) && !TextUtils.isEmpty(systemDate)) {
                String addTime = add_time.substring(0, 10).trim();//获取评议时间
                if (systemDate.trim().equals(addTime)) {
                    timeText.setText("今天");
                } else {
                    Long aLong = DateUtil.convertTimeToLong(add_time, "yyyy-MM-dd hh:mm:ss");
                    Date date = new Date(aLong);
                    format = new SimpleDateFormat("MM月dd日");
                    String showDate = format.format(date);
                    timeText.setText(showDate);
                }
            }

            boolean isended = new TimeUtil().compareNowTime(item.getEnd_time());
            //判断评议是否结束
            if (isended) {
                statusText.setText("进行中");
                statusText.setTextColor(Color.rgb(84, 187, 91));
            } else {
                statusText.setText("已结束");
                statusText.setTextColor(Color.rgb(153, 153, 153));
            }

            final int vote_type = item.getVote_type();
            if (vote_type == VOTE_SCORE) { //打分
                voteLayout.setVisibility(View.GONE);
                scoreLayout.setVisibility(View.VISIBLE);
                int score = item.getScore();
                scoreTitle.setText("总分设置（" + score + "分）");
                if (score <= 5) {
                    scoreRatingBar.setVisibility(View.VISIBLE);
                    socreSeekBarLayout.setVisibility(View.GONE);
                } else { //大于 5分 都是seekbar的情况
                    scoreRatingBar.setVisibility(View.GONE);
                    socreSeekBarLayout.setVisibility(View.VISIBLE);
                    socreSeekBarValue.setText(score + "分");
                    socreSeekBar.setEnabled(false);
                }

            } else if (vote_type == VOTE_VOTE) { //投票
                voteLayout.setVisibility(View.VISIBLE);
                scoreLayout.setVisibility(View.GONE);
                List<String> option_name = item.getOption_name();
                if (option_name != null) {
                    int size = option_name.size();
                    if (size > 0) {
                        if (size == 1) {
                            voteWaveLayout1.setVisibility(View.VISIBLE);
                            voteWaveLayout2.setVisibility(View.INVISIBLE);
                            voteWave1.setWaterLevelRatio(0.5f);
                            voteWaveText1.setText(option_name.get(0));
                            setWaveView(voteWave1);
                            WaveHelper mWaveHelper = new WaveHelper(voteWave1);
                            mWaveHelper.start();
                        } else if (size >= 2) {
                            setWaveView(voteWave1);
                            setWaveView(voteWave2);
                            WaveHelper mWaveHelper = new WaveHelper(voteWave1);
                            mWaveHelper.start();
                            WaveHelper mWaveHelper2 = new WaveHelper(voteWave2);
                            mWaveHelper2.start();
                            voteWaveLayout1.setVisibility(View.VISIBLE);
                            voteWaveLayout2.setVisibility(View.VISIBLE);
                            voteWave1.setWaterLevelRatio(0.5f);
                            voteWave2.setWaterLevelRatio(0.5f);
                            voteWaveText1.setText(option_name.get(0));
                            voteWaveText2.setText(option_name.get(1));
                            if (size > 2) {
                                voteMore.setVisibility(View.VISIBLE);
                            } else {
                                voteMore.setVisibility(View.INVISIBLE);
                            }
                        }

                    } else {
                        voteLayout.setVisibility(View.GONE);
                    }
                } else {
                    voteLayout.setVisibility(View.GONE);
                }
            }

            final int vote_id = item.getVote_id();
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 1) {
                        Intent intent = new Intent(mContext, MineInitiateVoteActivity.class);
                        intent.putExtra("vi", vote_id);
                        intent.putExtra("type", 1); //1.从我发起 2.去评议
                        intent.putExtra("vote_type", vote_type);//1.投票 2.打分
                        startActivity(intent);
                    } else if (type == 2) {
                        Intent intent = new Intent(mContext, MineInitiateVoteActivity.class);
                        intent.putExtra("vi", vote_id);
                        intent.putExtra("type", 2); //1.从我发起 2.去评议
                        intent.putExtra("vote_type", vote_type); //1投票 2.打分
                        startActivity(intent);
                    }
                }
            });
        }

        private void setWaveView(WaveView waveView) {
            waveView.setWaveColor(
                    Color.parseColor("#50FF6600"),
                    Color.parseColor("#FFFF6600"));
            waveView.setBorder(ViewUtil.dp2px(mContext, 1), Color.parseColor("#FF6600"));
        }
    }

    @Subscribe
    public void notifyMineVoteFragEvent(MineVoteFragEvent event) {
        if (event.type == type) {
            loadData(0);
        }
    }

    @Subscribe
    public void notifyMineVoteEvent(VoteRefreshEvent event) {
        loadData(0);
    }
}
