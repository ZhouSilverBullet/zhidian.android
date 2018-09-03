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
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.ui.activity.ParentListActivity;
import com.sdxxtop.zhidian.utils.PartIconColorUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.BottomSelectorView;
import com.xuxin.entry.ParentUserBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/7/17.
 */

public class ParentRecyclerAdapter extends BaseQuickAdapter<ParentListBean.DataBeanX.DataBean, BaseViewHolder> implements ISelector {
    private int selectType;
    private int selectStatus;

    //保存typeId
    private HashSet<Integer> typeSet = new HashSet<>();
    //保存学生家长 以studentId为key来进行的
    private HashMap<String, ParentUserBean> parentMap = new HashMap<>();

    private HashSet<ParentListBean.DataBeanX.DataBean> cacheSet = new HashSet<>();

    //判断头部的面包挟 有几个，3个是班级，4个是学生，都是固定的显示
    private List<ParentListBean.DataBeanX.NavBean> navigationViewList;
    private BottomSelectorView bottomSelectorView;

    public ParentRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ParentListBean.DataBeanX.DataBean item) {
        CircleImageView circleImage = helper.getView(R.id.item_part_and_parent_circle_img);
        TextView shortNameText = helper.getView(R.id.item_part_and_parent_short_name);
        TextView userNameText = helper.getView(R.id.item_part_and_parent_user_name);
        final CheckBox checkBox = helper.getView(R.id.item_part_and_parent_checkbox);
        final LinearLayout checkBoxLayout = helper.getView(R.id.item_part_and_parent_checkbox_layout);
        final ImageView rightIcon = helper.getView(R.id.item_part_and_parent_right_icon);

        String name = item.getName();
        final int typeId = item.getType_id();
        String partName = item.getPart_name();
        String img = item.getImg();
        String student_name = item.getStudent_name();
        final int userid = item.getUserid();

        checkBoxLayout.setVisibility(View.VISIBLE);

        //如果没有获取图片就是用这种方式显示
        int position = helper.getAdapterPosition();
        final String color = PartIconColorUtil.getColor(position);
        if (TextUtils.isEmpty(img)) {
            ViewUtil.setPartItemView(color, name, shortNameText, circleImage);
            userNameText.setText(name);
        } else {
            ViewUtil.setColorItemView(img, name, shortNameText, circleImage);
            userNameText.setText(student_name + "家长(" + name + ")");
        }

        final boolean isPart = item.isPart();

        checkBox.setChecked(item.isCheck());
        final boolean check = checkBox.isChecked();
        checkBoxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                //只选择班级的情况下
                if (isSelectClass() && isGrade()) {
                    return;
                }

                boolean isAdd = !check;

                //设置partSet 和 userSet
                handleSelectSet(isPart, isAdd, typeId, userid, item.getUserStudentId());

                //设置cacheSet
                setCacheSet(item, typeId, isAdd, isPart, item.getUserStudentId());
                //是否选中
                item.setCheck(isAdd);

                //刷新recycler布局
                notifyDataSetChanged();
                //最后刷新底部的选择布局
                refreshBottomSelectorView(item, typeId, isAdd, color, isPart, item.getUserStudentId());

                //check回调
                if (checkListener != null) {
                    checkListener.onCheck();
                }
            }
        });

        //是选中的状态就不给点击事件了
        if (checkBox.isChecked() && isPart) {
            helper.getConvertView().setClickable(false);
            rightIconStatus(rightIcon, true);
        } else {
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectorClickListener != null) {
                        selectorClickListener.onSelectorClick(helper.getLayoutPosition() - getHeaderLayoutCount());
                    }
                }
            });
            rightIconStatus(rightIcon, false);
        }

        //重新布局的时候刷新一下item的选择状态
        checkBox.setChecked(item.isCheck());

//        rightIconStatus(rightIcon, checkBox.isChecked());
//        rightIconStatus(rightIcon, isSelectClass());

    }

    private void handleSelectSet(boolean isPart, boolean isAdd, Integer typeId, Integer userid, String userStudentId) {
        if (isPart) {
            //type类型的
            if (isAdd) {
                typeSet.add((Integer) typeId);
            } else {
                typeSet.remove((Integer) typeId);
            }
        } else {
            //student类型的
            if (isAdd) {
                ParentUserBean parentUserBean = new ParentUserBean();
                parentUserBean.typeId = typeId;
                parentUserBean.userId = userid;
                parentMap.put(userStudentId, parentUserBean);
            } else {
                if (!TextUtils.isEmpty(userStudentId)) {
                    parentMap.remove(userStudentId);
                }
            }
        }
    }

    private void setCacheSet(ParentListBean.DataBeanX.DataBean item, Integer id, boolean isAdd, boolean isPart, String userStudentId) {
        ParentListBean.DataBeanX.DataBean tempDataBean = null;
        for (ParentListBean.DataBeanX.DataBean dataBean : cacheSet) {
            //type类型
            if (isPart) {
                int type_id = dataBean.getType_id();
                if (type_id == id) {
                    tempDataBean = dataBean;
                    break;
                }
            } else {
                String studentId = dataBean.getUserStudentId();
                if (!TextUtils.isEmpty(studentId) && studentId.equals(userStudentId)) {
                    tempDataBean = dataBean;
                    break;
                }
            }
        }

        //清除
        if (tempDataBean != null && !isAdd) {
            cacheSet.remove(tempDataBean);
        }

        //添加进去
        if (tempDataBean == null && isAdd && item != null) {
            cacheSet.add(item);
        }
    }

    private void refreshBottomSelectorView(ParentListBean.DataBeanX.DataBean item, Integer itemId, boolean isAdd, String color, boolean isPart, String userStudentId) {
        refreshBottomSelectorView(item, itemId, isAdd, color, isPart, userStudentId, true);
    }

    private void refreshBottomSelectorView(ParentListBean.DataBeanX.DataBean item, Integer itemId, boolean isAdd, String color, boolean isPart, String userStudentId, boolean isNotify) {
        if (bottomSelectorView == null) {
            return;
        }

        List<SelectBean> bottomList = bottomSelectorView.getList();
        SelectBean bean;
        if (isAdd && item != null) {
            int type;
            Integer id;
            String name;

            if (item.isPart()) {
                id = item.getType_id();
            } else {
                id = item.getType_id();
            }
            name = item.getName();
            if (item.isPart()) {
                type = SelectBean.TYPE_PART;
                bean = SelectBean.createSelectBean(type, id, name, color);
            } else {
                type = SelectBean.TYPE_USER;
                bean = SelectBean.createSelectBean(type, id, name, item.getImg());
                bean.identify = item.getUserStudentId();
            }
            //用于判断是否是parent的值，
            bean.isParent = true;
            bean.isPart = item.isPart();
            bottomList.add(bean);
        } else {
            SelectBean tempSelectBean = null;
            for (SelectBean selectBean : bottomList) {
                if (isPart) {
                    if (selectBean.getId().equals(itemId)) {
                        tempSelectBean = selectBean;
                        break;
                    }
                } else {
                    String itemUserStudentId = selectBean.getIdentify();
                    if (!TextUtils.isEmpty(itemUserStudentId) && itemUserStudentId.equals(userStudentId)) {
                        tempSelectBean = selectBean;
                        break;
                    }
                }
            }

            if (tempSelectBean != null) {
                bottomList.remove(tempSelectBean);
            }
        }

        if (isNotify) {
            bottomSelectorView.refreshData();
        }
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
    //默认加1，是因为第一个item是全部，加入了全部这个item
    public boolean isGrade() {
        if (navigationViewList != null && navigationViewList.size() == 3 + 1) {
            return true;
        }
        return false;
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

    @Override
    public void checkAll(boolean isAdd) {
        if (isAdd) {
            List<ParentListBean.DataBeanX.DataBean> data = getData();
            for (ParentListBean.DataBeanX.DataBean datum : data) {
                datum.setCheck(true);
                int type_id = datum.getType_id();
                if (datum.isPart()) {
                    if (!typeSet.contains((Integer) type_id)) {
                        typeSet.add((Integer) type_id);
                        cacheSet.add(datum);

//                        //添加
//                        List<SelectBean> list = bottomSelectorView.getList();
//                        Integer id = datum.getType_id();
//                        String name = datum.getName();
//                        int type = SelectBean.TYPE_PART;
//                        SelectBean bean = SelectBean.createSelectBean(type, id, name, "");
//                        //用于判断是否是parent的值，
//                        bean.isParent = true;
//                        list.add(bean);

                        refreshBottomSelectorView(datum, type_id, true, "", true, datum.getUserStudentId(), false);
                    }
                } else {
                    if (!parentMap.containsKey(datum.getUserStudentId())) {
                        cacheSet.add(datum);

                        ParentUserBean parentUserBean = new ParentUserBean();
                        parentUserBean.typeId = type_id;
                        parentUserBean.userId = datum.getUserid();
                        parentMap.put(datum.getUserStudentId(), parentUserBean);

//                        //添加操作
//                        List<SelectBean> list = bottomSelectorView.getList();
//                        Integer id = datum.getType_id();
//                        String name = datum.getName();
//                        int type = SelectBean.TYPE_PART;
//                        SelectBean bean = SelectBean.createSelectBean(type, id, name, datum.getImg());
//                        //用于判断是否是parent的值，
//                        bean.isParent = true;
//                        list.add(bean);
                        refreshBottomSelectorView(datum, type_id, true, "", false, datum.getUserStudentId(), false);
                    }
                }
            }
            bottomSelectorView.refreshData();
        } else {
            List<ParentListBean.DataBeanX.DataBean> data = getData();
            List<SelectBean> list = bottomSelectorView.getList();
            List<SelectBean> tempList = new ArrayList<>(list);
            for (ParentListBean.DataBeanX.DataBean datum : data) {
                datum.setCheck(false);
                int type_id = datum.getType_id();
                if (datum.isPart()) {
                    if (typeSet.contains((Integer) type_id)) {
                        typeSet.remove((Integer) type_id);
                        //清除cacheSet当前页面选中的值
                        for (ParentListBean.DataBeanX.DataBean dataBean : cacheSet) {
                            if (dataBean.getType_id() == type_id) {
                                cacheSet.remove(dataBean);
                                break;
                            }
                        }
                    }
                } else {
                    if (parentMap.containsKey(datum.getUserStudentId())) {
                        parentMap.remove(datum.getUserStudentId());
                    }
                }
                for (SelectBean bean : tempList) {
                    if (type_id == bean.getId() && bean.isParent) {
                        tempList.remove(bean);
                        break;
                    }
                }
            }
            bottomSelectorView.refreshData(tempList);
        }
        notifyDataSetChanged();
    }

    public interface SelectorClickListener {
        void onSelectorClick(int position);
    }

    public void setNavigationViewList(List<ParentListBean.DataBeanX.NavBean> navigationViewList) {
        this.navigationViewList = navigationViewList;
    }

    public void setBottomSelectorView(BottomSelectorView bottomSelectorView) {
        this.bottomSelectorView = bottomSelectorView;
        bottomSelectorView.setBottomRemoveParentClickListener(new BottomSelectorView.BottomRemove2ClickListener() {
            @Override
            public void onRemoveClick(int type, Integer id, String userStudentId) {
                boolean isPart = type == SelectBean.TYPE_PART;
                //点击只会删除
                setCacheSet(null, id, false, isPart, userStudentId);

                //这个id会在里面判断，所以两都传就可以了
                handleSelectSet(isPart, false, id, id, userStudentId);
                refreshBottomSelectorView(null, id, false, "", isPart, userStudentId);

                //最后刷新一下item状态
                notifyAdapterChanged(isPart, id, userStudentId);

                //check回调检查全选按钮
                if (checkListener != null) {
                    checkListener.onCheck();
                }
            }
        });
    }

    private void notifyAdapterChanged(boolean isPart, Integer id, String userStudentId) {
        for (ParentListBean.DataBeanX.DataBean dataBean : getData()) {
            if (isPart) {
                if (dataBean.getType_id() == id) {
                    dataBean.setCheck(false);
                    break;
                }
            } else {
                String studentId = dataBean.getUserStudentId();
                if (!TextUtils.isEmpty(studentId) && studentId.equals(userStudentId)) {
                    dataBean.setCheck(false);
                    break;
                }
            }
        }

        notifyDataSetChanged();
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
        if (navigationViewList != null && navigationViewList.size() == 4 + 1) {
            for (ParentListBean.DataBeanX.DataBean datum : data) {
                datum.setType_id(datum.getStudent_id());
            }
        }
        notifyAdapterStatusChanged(data);
        super.replaceData(data);
    }

    private void notifyAdapterStatusChanged(Collection<? extends ParentListBean.DataBeanX.DataBean> data) {
        //刷新一下看到的布局
//        for (ParentListBean.DataBeanX.DataBean dataBean : data) {
//            for (ParentSelectBean selectBean : cacheSelectSet) {
//                if (isGrade()) {
//                    if (selectBean.gradeId == dataBean.getType_id()) {
//                        dataBean.setCheck(true);
//                    }
//                } else {
//                    if (selectBean.studentId == dataBean.getType_id()) {
//                        dataBean.setCheck(true);
//                    }
//                }
//            }
//        }
        for (ParentListBean.DataBeanX.DataBean datum : data) {
            for (ParentListBean.DataBeanX.DataBean dataBean : cacheSet) {
                if (dataBean.getType_id() == datum.getType_id()) {
                    datum.setCheck(true);
                }
            }
        }

    }

    /**
     * 清楚所有数据
     */
    public void clearAll() {
        List<ParentListBean.DataBeanX.DataBean> list = new ArrayList<>();
        replaceData(list);
    }

    public HashSet<Integer> getTypeSet() {
        return typeSet;
    }

    public HashMap<String, ParentUserBean> getParentMap() {
        return parentMap;
    }

    public HashSet<ParentListBean.DataBeanX.DataBean> getCacheSet() {
        return cacheSet;
    }


    private CheckListener checkListener;

    public void setCheckClickListener(CheckListener checkListener) {
        this.checkListener = checkListener;
    }
}
