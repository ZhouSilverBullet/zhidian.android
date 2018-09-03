package com.sdxxtop.zhidian.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ParentListBean;
import com.sdxxtop.zhidian.entity.ParentSelectBean;
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.ui.activity.ParentListActivity;
import com.sdxxtop.zhidian.utils.PartIconColorUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.BottomSelectorView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/7/17.
 */

public class ParentListAdapter extends BaseQuickAdapter<ParentListBean.DataBeanX.DataBean, BaseViewHolder> implements ISelector {
    private boolean isParent;
    private int selectType;
    private int selectStatus;
    //判断头部的面包挟 有几个，3个是班级，4个是学生，都是固定的显示
    private List<ParentListBean.DataBeanX.NavBean> navigationViewList;
    private BottomSelectorView bottomSelectorView;
    private ArrayList<ParentSelectBean> cacheSelectSet = new ArrayList<>();

    public ParentListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ParentListBean.DataBeanX.DataBean item) {
        CircleImageView circleImage = helper.getView(R.id.item_parent_circle_img);
        TextView shortNameText = helper.getView(R.id.item_parent_short_name);
        TextView userNameText = helper.getView(R.id.item_parent_user_name);
        final CheckBox checkBox = helper.getView(R.id.item_parent_checkbox);
        final LinearLayout checkBoxLayout = helper.getView(R.id.item_parent_checkbox_layout);
        final ImageView rightIcon = helper.getView(R.id.item_parent_right_icon);
        helper.getConvertView().setBackgroundResource(R.drawable.recycler_item_selector);

        String name = item.getName();
        int partId = item.getPart_id();
        final int type_id = item.getType_id();
        String partName = item.getPart_name();
        String img = item.getImg();
        String student_name = item.getStudent_name();

        //如果没有获取图片就是用这种方式显示
        if (TextUtils.isEmpty(img)) {
            int position = helper.getAdapterPosition();
            ViewUtil.setPartItemView(PartIconColorUtil.getColor(position), name, shortNameText, circleImage);
            userNameText.setText(name);
        } else {
            ViewUtil.setColorItemView(img, name, shortNameText, circleImage);
            userNameText.setText(student_name + "家长(" + name + ")");
        }

        //重新布局的时候刷新一下item的选择状态
        boolean check = item.isCheck();
        checkBox.setChecked(check);

        if (check && item.isPart()) {
            rightIconStatus(rightIcon, true);
            helper.getConvertView().setClickable(false);
        } else {
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked() && isGrade()) {
                        return;
                    }

                    //只选择班级的情况下
                    if (isSelectClass() && isGrade()) {
                        return;
                    }

                    if (selectorClickListener != null) {
                        selectorClickListener.onSelectorClick(helper.getLayoutPosition() - getHeaderLayoutCount());
                    }
                }
            });
            if (isSelectClass() && isGrade()) {
                rightIcon.setVisibility(View.GONE);
                helper.getConvertView().setBackgroundColor(mContext.getResources().getColor(R.color.white));
            } else {
                rightIconStatus(rightIcon, false);
            }
        }
//        rightIconStatus(rightIcon, isSelectClass());

        //不进行选择
        if (selectType == -1) { //肯定是不进行选择的
            checkBoxLayout.setVisibility(View.GONE);
        } else {
            //只能在选择，且满足是班级或者人员的时候，不然不进行显示
            if (navigationViewList.size() >= 3) {
                checkBoxLayout.setVisibility(View.VISIBLE);
            } else {
                checkBoxLayout.setVisibility(View.GONE);
            }
            //多选，暂时都是多选
            if (selectType == ParentListActivity.TYPE_MULTI_SELECT ||
                    selectType == ParentListActivity.TYPE_SINGLE_SELECT) {

                checkBoxLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean checked = checkBox.isChecked();

                        if (selectType == ParentListActivity.TYPE_SINGLE_SELECT) {
                            //单选就遍历一次
                            for (ParentListBean.DataBeanX.DataBean dataBean : getData()) {
                                if (dataBean.isCheck()) {
                                    dataBean.setCheck(false);
                                    refreshBottomSelectorView(false, (Integer) dataBean.getType_id(), dataBean);
                                }
                            }
                            cacheSelectSet.clear();
                        }

                        item.setCheck(!checked);
                        checkBox.setChecked(!checked);
                        refreshCacheSet(!checked, item);
                        refreshBottomSelectorView(!checked, (Integer) type_id, item);

//                        rightIconStatus(rightIcon, checkBox.isChecked());
                        notifyDataSetChanged();

                        if (checkListener != null) {
                            checkListener.onCheck();
                        }
                    }
                });
            }
        }
    }

    //刷新本地缓存的一些ID等
    private void refreshCacheSet(boolean toCheck, ParentListBean.DataBeanX.DataBean item) {
        boolean grade = isGrade();
        int type_id = item.getType_id();
        ParentSelectBean tempDataBean = null;
        for (ParentSelectBean dataBean : cacheSelectSet) {
            if (grade) { //是班级
                if (dataBean.gradeId == type_id) {
                    tempDataBean = dataBean;
                    break;
                }
            } else {
                if (dataBean.studentId == type_id) {
                    tempDataBean = dataBean;
                    break;
                }
            }
        }

        if (tempDataBean == null && toCheck) {
            ParentSelectBean selectBean = new ParentSelectBean();
            if (grade) {
                selectBean.type = ParentSelectBean.TYPE_GRADE;
                selectBean.gradeId = type_id;
                selectBean.value = navigationViewList.get(2).getName() + item.getName();
            } else {
                selectBean.type = ParentSelectBean.TYPE_STUDENT;
                selectBean.studentId = type_id;
                selectBean.value = item.getStudent_name();
            }
            String img = item.getImg();
            if (TextUtils.isEmpty(img)) {
                selectBean.color = "#000000";
            } else {
                selectBean.color = img;
            }
            selectBean.name = item.getName();
            cacheSelectSet.add(selectBean);
        } else if (!toCheck) {
            cacheSelectSet.remove(tempDataBean);
        }
    }

    private void refreshBottomSelectorView(boolean isAdd, Integer type_id, ParentListBean.DataBeanX.DataBean item) {
        if (bottomSelectorView == null) {
            return;
        }

        if (isAdd && item != null) {
            List<SelectBean> list = bottomSelectorView.getList();
            SelectBean bean;
            int type;
            String name = item.getName();

            if (item.isPart()) {
                type = SelectBean.TYPE_PART;
            } else {
                type = SelectBean.TYPE_USER;
            }
            Integer id = item.getType_id();
            bean = SelectBean.createSelectBean(type, id, name, item.getImg());
            list.add(bean);
        } else {
            List<SelectBean> list = bottomSelectorView.getList();
            for (SelectBean bean : list) {
                if (bean.id.equals(type_id)) {
                    list.remove(bean);
                    break;
                }
            }
        }

        bottomSelectorView.refreshData();
    }

    public void removeCacheSetItem() {

    }


    @Override
    public void checkAll(boolean isAdd) {
        if (isAdd) {
            List<ParentListBean.DataBeanX.DataBean> data = getData();
            for (ParentListBean.DataBeanX.DataBean datum : data) {
                boolean isContains = false;
                int type_id = datum.getType_id();
                boolean part = datum.isPart();
                for (ParentSelectBean parentSelectBean : cacheSelectSet) {
                    if (part) {
                        if (parentSelectBean.gradeId == type_id) {
                            isContains = true;
                            break;
                        }
                    } else {
                        if (parentSelectBean.studentId == type_id) {
                            isContains = true;
                            break;
                        }
                    }
                }

                if (!isContains) {
                    datum.setCheck(true);
                    refreshCacheSet(true, datum);
                    refreshBottomSelectorView(true, type_id, datum);
                }
            }
        } else {
            List<ParentListBean.DataBeanX.DataBean> data = getData();
            for (ParentListBean.DataBeanX.DataBean datum : data) {
                int type_id = datum.getType_id();
                datum.setCheck(false);
                refreshCacheSet(false, datum);
                refreshBottomSelectorView(false, type_id, null);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public boolean iteratorAllValue() {
        boolean isCheck = true;
        List<ParentListBean.DataBeanX.DataBean> data = getData();
        //如果是0返回false
        if (data.size() == 0) {
            return false;
        }
        for (ParentListBean.DataBeanX.DataBean datum : data) {
            if (!datum.isCheck()) {
                isCheck = false;
                break;
            }
        }
        return isCheck;
    }

    private void rightIconStatus(View rightIcon, boolean checked) {
        if (checked) {
            rightIcon.setVisibility(View.GONE);
        } else {
            rightIcon.setVisibility(View.VISIBLE);
        }
    }

    public boolean isSelectClass() {
        return selectStatus == ParentListActivity.STATUS_SELECT_CLASS;
    }

    //当前是否是班级
    public boolean isGrade() {
        if (navigationViewList != null && navigationViewList.size() == 3) {
            return true;
        }
        return false;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public void setSelectType(int selectType) {
        this.selectType = selectType;
    }

    private SelectorClickListener selectorClickListener;

    public void setSelectorClickListener(SelectorClickListener selectorClickListener) {
        this.selectorClickListener = selectorClickListener;
    }

    public void setSelectStatus(int selectStatus) {
        this.selectStatus = selectStatus;
    }

    public interface SelectorClickListener {
        void onSelectorClick(int position);
    }

    public void setNavigationViewList(List<ParentListBean.DataBeanX.NavBean> navigationViewList) {
        this.navigationViewList = navigationViewList;
    }

    public void setBottomSelectorView(BottomSelectorView bottomSelectorView) {
        this.bottomSelectorView = bottomSelectorView;
        bottomSelectorView.setBottomRemoveClickListener(new BottomSelectorView.BottomRemoveClickListener() {
            @Override
            public void onRemoveClick(int type, Integer id) {
                ParentSelectBean tempSelectBean = null;
                int tempId = 0;
                boolean isTypeGrade = type == SelectBean.TYPE_PART;
                if (isTypeGrade) {
                    for (ParentSelectBean selectBean : cacheSelectSet) {
                        if (selectBean.gradeId == id) {
                            tempId = id;
                            tempSelectBean = selectBean;
                            break;
                        }
                    }
                } else {
                    for (ParentSelectBean selectBean : cacheSelectSet) {
                        if (selectBean.studentId == id) {
                            tempId = id;
                            tempSelectBean = selectBean;
                            break;
                        }
                    }
                }
                if (tempSelectBean != null) {
                    cacheSelectSet.remove(tempSelectBean);
                }

//                notifyDataSetChanged();
                notifyAdapterItemStatusChanged(tempId);
                refreshBottomSelectorView(false, id, null);

                if (checkListener != null) {
                    checkListener.onCheck();
                }
            }
        });
    }

    private void notifyAdapterItemStatusChanged(int tempId) {
        //刷新一下看到的布局
        for (ParentListBean.DataBeanX.DataBean dataBean : getData()) {
            if (tempId == dataBean.getType_id()) {
                dataBean.setCheck(false);
                break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void replaceData(@NonNull Collection<? extends ParentListBean.DataBeanX.DataBean> data) {
        //是学生
        if (navigationViewList != null && navigationViewList.size() == 4) {
            for (ParentListBean.DataBeanX.DataBean datum : data) {
                datum.setType_id(datum.getStudent_id());
            }
        }
        notifyAdapterStatusChanged(data);
        super.replaceData(data);
    }

    private void notifyAdapterStatusChanged(Collection<? extends ParentListBean.DataBeanX.DataBean> data) {
        //刷新一下看到的布局
        for (ParentListBean.DataBeanX.DataBean dataBean : data) {
            for (ParentSelectBean selectBean : cacheSelectSet) {
                if (isGrade()) {
                    if (selectBean.gradeId == dataBean.getType_id()) {
                        dataBean.setCheck(true);
                    }
                } else {
                    if (selectBean.studentId == dataBean.getType_id()) {
                        dataBean.setCheck(true);
                    }
                }
            }
        }
    }

    public ArrayList<ParentSelectBean> getCacheSelectSet() {
        return cacheSelectSet;
    }

    private CheckListener checkListener;

    public void setCheckListener(CheckListener checkListener) {
        this.checkListener = checkListener;
    }
}
