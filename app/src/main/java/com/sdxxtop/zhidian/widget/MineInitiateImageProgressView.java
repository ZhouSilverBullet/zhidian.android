package com.sdxxtop.zhidian.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.VoteReadBean;
import com.sdxxtop.zhidian.ui.activity.PhotoViewActivity;
import com.sdxxtop.zhidian.utils.GridDividerItemDecoration;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/5/21.
 */

public class MineInitiateImageProgressView extends LinearLayout {
    private RecyclerView recyclerView;
    private ImageProgressAdapter mAdapter;
    private boolean isSingle;
    private boolean type; //从我的过来就不用进行选择
    private int sum;
    private boolean isVote;

//    private RelativeLayout pingFenLayout;
//    private TextView pingFenText;
//    private TextView pingFenProgress;
//    private CheckBox pingCheckBox;
//    private ClipDrawable clipDrawable;

    public MineInitiateImageProgressView(Context context) {
        this(context, null);
    }

    public MineInitiateImageProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MineInitiateImageProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_mine_initiate_image_progress, this, true);
        setBackgroundColor(getResources().getColor(R.color.white));
        recyclerView = (RecyclerView) findViewById(R.id.view_initate_image_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridDividerItemDecoration(ViewUtil.dp2px(getContext(), 5),
                getResources().getColor(R.color.translate)));
        mAdapter = new ImageProgressAdapter(R.layout.item_view_mine_initiate_image_progress);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (type) { //如果是我的评论进来是没有checkBox的，所以下面点击没什么用
                    return;
                }

                if (isVote) { //给选中但是不给再次选中
                    return;
                }

                //用数据源来改变状态
                List<VoteReadBean.DataBean.OptionBean> data = mAdapter.getData();
                VoteReadBean.DataBean.OptionBean optionBean = data.get(position);
                boolean check = optionBean.isCheck();
                if (isSingle) {
                    for (VoteReadBean.DataBean.OptionBean datum : data) {
                        datum.setCheck(false);
                    }
                }
                optionBean.setCheck(!check);

                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void setSingle(boolean single) {
        isSingle = single;
    }

    private void toSingle(RecyclerView recyclerView) {
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = recyclerView.getChildAt(i);
            if (childAt instanceof SquareFrameLayout) {
                ((CheckBox) childAt.findViewById(R.id.item_mine_initiate_ping_check)).setChecked(false);
            }
        }
    }

    public void setOptionBeanList(int sum, List<VoteReadBean.DataBean.OptionBean> option) {
        if (sum == 0) {
            sum = 1;
        }
        this.sum = sum;
        mAdapter.replaceData(option);
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public void setIsVote(boolean isVote) {
        this.isVote = isVote;
    }

    public class ImageProgressAdapter extends BaseQuickAdapter<VoteReadBean.DataBean.OptionBean, BaseViewHolder> {

        public ImageProgressAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final VoteReadBean.DataBean.OptionBean item) {
            final ImageView pingFenImg = helper.getView(R.id.item_mine_initiate_ping_fen_img);
            Glide.with(mContext).load(StringUtil.stringNotNull(item.getImg())).into(pingFenImg);

            CheckBox pingCheck = helper.getView(R.id.item_mine_initiate_ping_check);
            TextView pingProgressText = helper.getView(R.id.item_mine_initiate_ping_fen_progress_value);
            TextView pingFenText = helper.getView(R.id.item_mine_initiate_ping_fen);

            String option_name = item.getOption_name();
            pingFenText.setText(option_name);

            int num = item.getNum();
            int percent = num * 100 / sum;
            pingProgressText.setText(num + "(" + percent + "%)");

            if (type) {
                pingCheck.setVisibility(GONE);
            } else {
                pingCheck.setVisibility(VISIBLE);
            }
            pingCheck.setClickable(false);

            if (item.isCheck()) {
                pingCheck.setChecked(true);
            } else {
                pingCheck.setChecked(false);
            }

            helper.addOnClickListener(R.id.item_mine_initiate_check_layout);

            helper.getConvertView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoViewActivity.start(mContext, StringUtil.stringNotNull(item.getImg()));
                }
            });
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public ImageProgressAdapter getAdapter() {
        return mAdapter;
    }

    //    public CheckBox getPingCheckBox() {
//        return pingCheckBox;
//    }
}
