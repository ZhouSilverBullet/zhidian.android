package com.sdxxtop.zhidian.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ContactPartBean;
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.ui.activity.NoticeReciveRangeActivity;
import com.sdxxtop.zhidian.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者：CaiCM
 * 日期：2018/3/27  时间：10:28
 * 邮箱：15010104100@163.com
 * 描述：发布公告选择接受范围也可用
 */

public class ShowReciveRangeAdapter extends BaseAdapter {

    private int partSelectNotIn;
    private int isPartSelect;
    String[] partColors = {"#3296fa", "#9396fa", "#9d81ff", "#81a4f9", "#d9d6ec", "#4e76e4", "#ad8ea3"};

    public HashSet<Integer> selectUserSet = new HashSet<>();
    public HashSet<Integer> selectPartSet = new HashSet<>();
    public SparseArray<SelectBean> selectUserMap = new SparseArray<>();
    public SparseArray<SelectBean> selectPartMap = new SparseArray<>();

    private Context context;
    private ContactPartBean.DataEntity dataBean;
    private OnClickObsever obsever;

    private boolean singleSelector;

    public ShowReciveRangeAdapter(Context context, ContactPartBean.DataEntity dataBean, OnClickObsever obsever, int isPartSelect, int partSelectNotIn) {
        this.context = context;
        this.dataBean = dataBean;
        this.obsever = obsever;
        this.isPartSelect = isPartSelect;
        this.partSelectNotIn = partSelectNotIn;
    }

    public void setSingSelector(boolean singleSelector) {
        this.singleSelector = singleSelector;
    }


    @Override
    public int getCount() {
        if (dataBean.getPart() == null && dataBean.getUserinfo() == null) {
            return 0;
        } else if (dataBean.getPart() == null && dataBean.getUserinfo() != null) {
            return dataBean.getUserinfo().size();
        } else if (dataBean.getPart() != null && dataBean.getUserinfo() == null) {
            return dataBean.getPart().size();
        } else {
            return dataBean.getPart().size() + dataBean.getUserinfo().size();
        }
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final MyViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_show_user2, null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        String name = "";

        if (position < dataBean.getPart().size() && dataBean.getPart().size() != 0) {
            holder.line.setVisibility(View.GONE);
            holder.tv_item_right_icon_layout.setVisibility(View.VISIBLE);
            final ContactPartBean.DataEntity.PartEntity partEntity = dataBean.getPart().get(position);
            name = partEntity.getPart_name();

            //图片上展示的两个字
            if (name.length() >= 2) {
                holder.tv_item_short_name.setText(name.substring(0, 2));
            } else {
                holder.tv_item_short_name.setText(name);
            }
            final int partId = partEntity.getPart_id();
            final String img = partColors[position % 7];
            holder.iv_item_user_head.setImageDrawable(new ColorDrawable(Color.parseColor(img)));

            if (isPartSelect == NoticeReciveRangeActivity.PART_NOT_SELECTOR) {
                holder.checkbox.setVisibility(View.GONE);
            } else {
                holder.checkbox.setVisibility(View.VISIBLE);
                final boolean isCheck;
                if (selectPartSet.contains((Integer) partId)) {
                    isCheck = true;
                    partEntity.setCheck(true);
                    setTextChecked(holder.checkbox, true);
                } else {
                    isCheck = false;
                    partEntity.setCheck(false);
                    setTextChecked(holder.checkbox, false);
                }

                final String finalName = name;
                final MyViewHolder finalHolder = holder;

                if (!isCheck) {
                    holder.tv_item_right_icon_layout.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_item_right_icon_layout.setVisibility(View.GONE);
                }

                holder.rl_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<ContactPartBean.DataEntity.PartEntity> part = dataBean.getPart();
                        if (position < part.size()) {
                            if (!isCheck) {
                                selectPartSet.add((Integer) partId);
                                SelectBean selectBean = SelectBean.createSelectBean(SelectBean.TYPE_PART, (Integer) partId, finalName, img);
                                selectPartMap.put(partId, selectBean);
                                partEntity.setCheck(true);
                                holder.tv_item_right_icon_layout.setVisibility(View.GONE);
                            } else {
                                selectPartSet.remove((Integer) partId);
                                selectPartMap.remove(partId);
                                partEntity.setCheck(false);
                                holder.tv_item_right_icon_layout.setVisibility(View.VISIBLE);
                            }
                            setTextChecked(finalHolder.checkbox, !isCheck);
                            if (listener != null) {
                                listener.selectAll();
                            }
                            notifyDataSetChanged();
                        }
                    }
                });
            }

            holder.tv_item_right_icon_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (obsever != null) {
                        obsever.onClickPartCallBack(position);
                    }
                }
            });

            holder.tv_item_user_job.setVisibility(View.GONE);
        } else if ((position - dataBean.getPart().size()) < dataBean.getUserinfo().size() && dataBean.getUserinfo().size() != 0) {
            holder.tv_item_right_icon_layout.setVisibility(View.GONE);
            final ContactPartBean.DataEntity.UserinfoEntity userinfoEntity = dataBean.getUserinfo().get(position - dataBean.getPart().size());
            if (position - dataBean.getPart().size() == 0) {
                holder.line.setVisibility(View.VISIBLE);
//                holder.checkbox.setChecked(userinfoEntity.isCheck());
            } else {
                holder.line.setVisibility(View.GONE);
            }
            final int userid = userinfoEntity.getUserid();

            final boolean isCheck;
            if (selectUserSet.contains((Integer) userid)) {
                isCheck = true;
                setTextChecked(holder.checkbox, true);
                userinfoEntity.setCheck(true);
            } else {
                isCheck = false;
                userinfoEntity.setCheck(false);
                setTextChecked(holder.checkbox, false);
            }

            name = dataBean.getUserinfo().get(position - dataBean.getPart().size()).getName();
            if (dataBean.getUserinfo().get(position - dataBean.getPart().size()).getImg().startsWith("#")) {
                //图片上展示的两个字
                if (name.length() >= 2) {
                    holder.tv_item_short_name.setText(name.substring(name.length() - 2, name.length()));
                } else {
                    holder.tv_item_short_name.setText(name);
                }
            }

            final String img = dataBean.getUserinfo().get(position - dataBean.getPart().size()).getImg();
            if (img.startsWith("#")) {
                holder.iv_item_user_head.setImageDrawable(new ColorDrawable(Color.parseColor(img)));
            } else {
                holder.tv_item_short_name.setText("");
                Glide.with(context).load(img).into(holder.iv_item_user_head);
            }

            final String finalName = name;
            final MyViewHolder finalHolder = holder;
            holder.checkbox.setVisibility(View.VISIBLE);

            holder.tv_item_user_job.setVisibility(View.VISIBLE);
            holder.tv_item_user_job.setText(StringUtil.stringNotNull(userinfoEntity.getPosition()));

            holder.rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isCheck) {
                        if (singleSelector) {
                            selectUserSet.clear();
                            selectUserSet.add((Integer) userid);
                            selectUserMap.clear();
                            selectUserMap.put(userid, SelectBean.createSelectBean(SelectBean.TYPE_USER, (Integer) userid, finalName, img));
                        } else {
                            selectUserSet.add((Integer) userid);
                            selectUserMap.put(userid, SelectBean.createSelectBean(SelectBean.TYPE_USER, (Integer) userid, finalName, img));
                        }
                        userinfoEntity.setCheck(true);
                    } else {
                        selectUserSet.remove((Integer) userid);
                        selectUserMap.remove(userid);
                        userinfoEntity.setCheck(false);
                    }
                    setTextChecked(finalHolder.checkbox, !isCheck);
                    if (listener != null) {
                        listener.selectAll();
                    }
                    notifyDataSetChanged();

//                        if (obsever != null) {
//                            obsever.onClickUserCallBack(position - part.size());
//                        }
                }
            });
        }

        holder.tv_item_user_name.setText(name);
        return convertView;
    }

    public List<SelectBean> getSelectData() {
        ArrayList<SelectBean> selectBeans = new ArrayList<>();

        for (int i = 0; i < selectPartMap.size(); i++) {
            SelectBean selectBean = selectPartMap.get(selectPartMap.keyAt(i));
            if (selectBean != null) {
                selectBeans.add(selectBean);
            }
        }

        for (int i = 0; i < selectUserMap.size(); i++) {
            SelectBean selectBean = selectUserMap.get(selectUserMap.keyAt(i));
            if (selectBean != null) {
                selectBeans.add(selectBean);
            }
        }

        return selectBeans;
    }

    private void setTextChecked(TextView view, boolean isCheck) {
        if (isCheck) {
            view.setBackgroundResource(R.drawable.selected);
        } else {
            view.setBackgroundResource(R.drawable.unselected);
        }
    }

    public ContactPartBean.DataEntity getDataBean() {
        return dataBean;
    }

    public void setDataBean(ContactPartBean.DataEntity dataBean) {
        this.dataBean = dataBean;
        notifyDataSetChanged();
    }

    public interface OnClickObsever {
        void onClickPartCallBack(int position);

        void onClickUserCallBack(int position);
    }

    public interface SelectAllListener {
        void selectAll();
    }

    private SelectAllListener listener;

    public void setSelectAllListener(SelectAllListener listener) {
        this.listener = listener;
    }


    static class MyViewHolder {
        TextView tv_item_user_name, tv_item_user_job, tv_item_short_name, line;
        CircleImageView iv_item_user_head;
        RelativeLayout rl_item;
        TextView checkbox;
        ImageView tv_item_right_icon;
        View tv_item_right_icon_layout;

        public MyViewHolder(View view) {
            rl_item = (RelativeLayout) view.findViewById(R.id.rl_item);
            iv_item_user_head = (CircleImageView) view.findViewById(R.id.iv_item_user_head);
            tv_item_user_name = (TextView) view.findViewById(R.id.tv_item_user_name);
            tv_item_user_job = (TextView) view.findViewById(R.id.tv_item_user_job);
            tv_item_short_name = (TextView) view.findViewById(R.id.tv_item_short_name);
            tv_item_right_icon = (ImageView) view.findViewById(R.id.tv_item_right_icon);
            tv_item_right_icon_layout = view.findViewById(R.id.tv_item_right_icon_layout);
            checkbox = (TextView) view.findViewById(R.id.checkbox);
            line = (TextView) view.findViewById(R.id.line);
        }
    }
}
