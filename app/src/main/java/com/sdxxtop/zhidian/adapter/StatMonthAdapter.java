package com.sdxxtop.zhidian.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.StatMonthBean;
import com.sdxxtop.zhidian.utils.GridDividerItemDecoration;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/5/17.
 */

public class StatMonthAdapter extends BaseQuickAdapter<StatMonthBean.DataBean.StatBean, BaseViewHolder> {
    private int numSort = 0; //正常的顺序为0 ，修改排序后的为1
    private int aveSort = 0; //正常的顺序为0 ，修改排序后的为1
    private int type;

    public StatMonthAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, StatMonthBean.DataBean.StatBean item) {
        CircleImageView statImg = helper.getView(R.id.item_stat_img);
        TextView sortText = helper.getView(R.id.item_stat_sort_text);
        TextView statShortName = helper.getView(R.id.item_stat_short_name);
        TextView statName = helper.getView(R.id.item_stat_name);
        TextView statContent = helper.getView(R.id.item_stat_content);
        TextView statHintText = helper.getView(R.id.item_stat_hint_text);
        TextView statMunit = helper.getView(R.id.item_stat_hint_muint);
        RecyclerView muintRecycler = helper.getView(R.id.item_stat_hint_muint_recycler);

        String img = item.getImg();
        String name = item.getName();
        ViewUtil.setColorItemView(img, name, statShortName, statImg);
        statName.setText(StringUtil.stringNotNull(name));
        statContent.setText(StringUtil.stringNotNull(item.getPart_name()));
        String ave = item.getAve() == null ? "0" : item.getAve();
        String num = item.getNum() == null ? "0" : item.getNum();


        sortText.setText((helper.getAdapterPosition() + 1) + "");


        switch (type) {
            case 1:
                statHintText.setVisibility(View.GONE);
                statMunit.setVisibility(View.VISIBLE);
                statMunit.setText(StringUtil.stringNotNull(num + "分钟"));
                break;
            case 2:
                statHintText.setVisibility(View.GONE);
                statMunit.setVisibility(View.VISIBLE);
                statMunit.setTextColor(mContext.getResources().getColor(R.color.dot_red));
                statMunit.setText(StringUtil.stringNotNull("旷工"));
                break;
            case 3:
                statHintText.setVisibility(View.GONE);
                statMunit.setVisibility(View.VISIBLE);
                statMunit.setText(StringUtil.stringNotNull(num + "次"));
                break;
            case 4: //这种情况不在这个处理，换种方式启动  今日打卡记录
//                statHintText.setVisibility(View.GONE);
//                statMunit.setVisibility(View.VISIBLE);
//                statMunit.setText(StringUtil.stringNotNull(num + "次"));
                statHintText.setVisibility(View.GONE);
                statMunit.setVisibility(View.GONE);
                muintRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
                if (muintRecycler.getAdapter() == null) {
                    muintRecycler.addItemDecoration(new GridDividerItemDecoration(ViewUtil.dp2px(mContext, 5),
                            mContext.getResources().getColor(R.color.translate)));
                }
                muintRecycler.setVisibility(View.VISIBLE);
                String sign_time = item.getSign_time();
                if (!TextUtils.isEmpty(sign_time)) {
                    String[] split = sign_time.split(",");
                    List<String> stringList = Arrays.asList(split);
                    muintRecycler.setAdapter(new GridRecAdapter(R.layout.item_attendance_detail_grid_recycler, stringList));
                }
                break;
            case 5:
                statHintText.setVisibility(View.VISIBLE);
                statMunit.setVisibility(View.VISIBLE);
                statHintText.setText(StringUtil.stringNotNull(ave + "次"));
                statMunit.setText(StringUtil.stringNotNull(num + "分钟"));
                break;
            case 6:
                statHintText.setVisibility(View.GONE);
                statMunit.setVisibility(View.VISIBLE);
                statMunit.setText(StringUtil.stringNotNull(num + "次"));
                break;
            case 7:
                statHintText.setVisibility(View.VISIBLE);
                statMunit.setVisibility(View.VISIBLE);
                statHintText.setText(StringUtil.stringNotNull(ave + "小时"));
                statMunit.setText(StringUtil.stringNotNull(num + "小时"));
                break;
            case 8:
                statHintText.setVisibility(View.VISIBLE);
                statMunit.setVisibility(View.VISIBLE);
                statHintText.setText(StringUtil.stringNotNull(ave + "天"));
                statMunit.setText(StringUtil.stringNotNull(num + "天"));
                break;
            case 9:
                statHintText.setVisibility(View.GONE);
                statMunit.setVisibility(View.VISIBLE);
                statMunit.setText(StringUtil.stringNotNull(num + "次"));
                break;
            case 10:
                statHintText.setVisibility(View.GONE);
                statMunit.setVisibility(View.VISIBLE);
                statMunit.setText(StringUtil.stringNotNull(num + "次"));
                break;
        }

    }

    public int getNumSort() {
        return numSort;
    }

    public void setNumSort(int numSort) {
        this.numSort = numSort;
    }

    public int getAveSort() {
        return aveSort;
    }

    public void setAveSort(int aveSort) {
        this.aveSort = aveSort;
    }

    public void setType(int type) {
        this.type = type;
    }

    private class GridRecAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public GridRecAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            TextView view = helper.getView(R.id.item_attendance_detail_text);
            view.setText(item);
        }
    }
}
