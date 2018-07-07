package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.VoteIndexBean;
import com.sdxxtop.zhidian.utils.TimeUtil;
import com.sdxxtop.zhidian.widget.RatingBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/3/30  时间：10:28
 * 邮箱：15010104100@163.com
 * 描述：我发起的评议和去评议适配器
 */

public class ShowVoteAdapter extends BaseAdapter {

    //定义样式常量，注意常量值要从0开始
    private final int typeVote = 0;
    private final int typeMark = 1;
    TimeUtil timeUtil = new TimeUtil();

    private Context context;
    private List<VoteIndexBean.DataEntity.VoteEntity> list;

    public ShowVoteAdapter(Context context, List<VoteIndexBean.DataEntity.VoteEntity> list) {
        this.context = context;
        this.list = list;
    }

    //手动重写的方法
    @Override
    public int getItemViewType(int position) {
        //获得类型  typeVote为投票类型  typeMark为打分类型
        return list.get(position).getVote_type() == 1 ? typeVote : typeMark;
    }

    //手动重写的方法
    @Override
    public int getViewTypeCount() {
        return 2;
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
        int type = getItemViewType(position);
        ShowVoteAdapter.MyVoteHolder voteHolder = null;
        ShowVoteAdapter.MyMarkHolder markHolder = null;
        if (convertView == null) {
            switch (type) {
                case typeVote:
                    convertView = View.inflate(context, R.layout.item_show_vote, null);
                    voteHolder = new ShowVoteAdapter.MyVoteHolder(convertView);
                    convertView.setTag(voteHolder);
                    break;
                case typeMark:
                    convertView = View.inflate(context, R.layout.item_show_mark, null);
                    markHolder = new ShowVoteAdapter.MyMarkHolder(convertView);
                    convertView.setTag(markHolder);
                    break;
                default:
                    break;
            }

        }


        switch (type) {
            case typeVote:
                voteHolder = (ShowVoteAdapter.MyVoteHolder) convertView.getTag();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String systemDate = format.format(new Date()).substring(0, 10);//获取系统日期

                String startTime = list.get(position).getAdd_time().toString().substring(0, 10);//获取评议时间
                //判断是否为今天
                if (startTime.equals(systemDate)) {
                    voteHolder.tv_item_vote_time.setText("今天");
                } else {
                    voteHolder.tv_item_vote_time.setText(startTime);
                }


                String endTime = list.get(position).getEnd_time().toString();//获取评议结束时间
                boolean isended = timeUtil.compareNowTime(endTime);
                //判断评议是否结束
                if (isended) {
                    voteHolder.tv_item_vote_state.setText("进行中");
                    voteHolder.tv_item_vote_state.setTextColor(Color.rgb(84, 187, 91));
                } else {
                    voteHolder.tv_item_vote_state.setText("已结束");
                    voteHolder.tv_item_vote_state.setTextColor(Color.rgb(153, 153, 153));
                }


                voteHolder.tv_item_vote_title.setText(list.get(position).getTitle());


                //把字符串截取成数组（写投票选项）
                String option_name = list.get(position).getOption_name().toString().replace("[", "").replace("]", "");
                String[] splits = option_name.split(",");
                if (splits.length > 2) {
                    voteHolder.tv_item_detail.setVisibility(View.VISIBLE);
                } else {
                    voteHolder.tv_item_detail.setVisibility(View.GONE);
                }
                voteHolder.tv_item_select_one.setText(splits[0].toString());
                voteHolder.tv_item_select_two.setText(splits[1].toString());
                break;

            case typeMark:
                markHolder = (ShowVoteAdapter.MyMarkHolder) convertView.getTag();
                markHolder.tv_item_mark_title.setText(list.get(position).getTitle());
                SimpleDateFormat formatMark = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String systemDateMark = formatMark.format(new Date()).substring(0, 10);//获取系统日期

                String startTimeMark = list.get(position).getAdd_time().toString().substring(0, 10);//获取评议时间
                //判断是否为今天
                if (startTimeMark.equals(systemDateMark)) {
                    markHolder.tv_item_mark_time.setText("今天");
                } else {
                    markHolder.tv_item_mark_time.setText(startTimeMark);
                }


                String endTimeMark = list.get(position).getEnd_time().toString();//获取评议结束时间
                boolean isendedMark = timeUtil.compareNowTime(endTimeMark);
                //判断评议是否结束
                if (isendedMark) {
                    markHolder.tv_item_mark_state.setText("进行中");
                    markHolder.tv_item_mark_state.setTextColor(Color.rgb(84, 187, 91));
                } else {
                    markHolder.tv_item_mark_state.setText("已结束");
                    markHolder.tv_item_mark_state.setTextColor(Color.rgb(153, 153, 153));
                }

                markHolder.tv_item_mark_score.setText("总分设置（" + list.get(position).getScore() + "分）");

                if (list.get(position).getScore() > 5) {
                    markHolder.seekbar_item.setVisibility(View.VISIBLE);
                    markHolder.tv_item_ratingbar.setVisibility(View.GONE);
                    markHolder.seekbar_item.setEnabled(false);
                } else {
                    markHolder.seekbar_item.setVisibility(View.GONE);
                    markHolder.tv_item_ratingbar.setVisibility(View.VISIBLE);
                    markHolder.seekbar_item.setEnabled(false);
                }
                break;
            default:
                break;
        }
        return convertView;
    }


    //投票
    static class MyVoteHolder {
        TextView tv_item_vote_time, tv_item_vote_title, tv_item_vote_state, tv_item_select_one, tv_item_select_two, tv_item_detail;

        public MyVoteHolder(View view) {

            tv_item_vote_time = (TextView) view.findViewById(R.id.tv_item_vote_time);
            tv_item_vote_title = (TextView) view.findViewById(R.id.tv_item_vote_title);
            tv_item_vote_state = (TextView) view.findViewById(R.id.tv_item_vote_state);
            tv_item_select_one = (TextView) view.findViewById(R.id.tv_item_select_one);
            tv_item_select_two = (TextView) view.findViewById(R.id.tv_item_select_two);
            tv_item_detail = (TextView) view.findViewById(R.id.tv_item_detail);
        }
    }

    //打分
    static class MyMarkHolder {
        TextView tv_item_mark_time, tv_item_mark_title, tv_item_mark_state, tv_item_mark_score, tv_item_score;
        RatingBar tv_item_ratingbar;
        SeekBar seekbar_item;

        public MyMarkHolder(View view) {

            tv_item_mark_time = (TextView) view.findViewById(R.id.tv_item_mark_time);
            tv_item_mark_title = (TextView) view.findViewById(R.id.tv_item_mark_title);
            tv_item_mark_state = (TextView) view.findViewById(R.id.tv_item_mark_state);
            tv_item_mark_score = (TextView) view.findViewById(R.id.tv_item_mark_score);
            tv_item_ratingbar = (RatingBar) view.findViewById(R.id.tv_item_ratingbar);
            //tv_item_score = view.findViewById(R.id.tv_item_score);
            seekbar_item = (SeekBar) view.findViewById(R.id.seekbar_item);
        }
    }
}
