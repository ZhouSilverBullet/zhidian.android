package com.sdxxtop.zhidian.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ContactTeacherBean;
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.entity.TeacherBean;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.BottomSelectorView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/8/1.
 */

public class TeacherRecyclerAdapter extends BaseQuickAdapter<TeacherBean, BaseViewHolder> implements ISelector {
    //分别进行缓存选中的用户或者部门的id
    private HashSet<Integer> partSet = new HashSet<>();
    private HashSet<Integer> userSet = new HashSet<>();
    private HashSet<TeacherBean> cacheSet = new HashSet<>();
    private BottomSelectorView bottomSelectorView;

    public TeacherRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final TeacherBean item) {
        View lineView = helper.getView(R.id.item_teacher_user_line_view);

        CircleImageView circleImage = helper.getView(R.id.item_teacher_circle_img);
        TextView shortNameText = helper.getView(R.id.item_teacher_short_name);
        TextView userNameText = helper.getView(R.id.item_teacher_user_name);
        final CheckBox checkBox = helper.getView(R.id.item_teacher_checkbox);
        final LinearLayout checkBoxLayout = helper.getView(R.id.item_teacher_checkbox_layout);
        final ImageView rightIcon = helper.getView(R.id.item_teacher_right_icon);
        final TextView teacherPosition = helper.getView(R.id.item_teacher_position);

        checkBoxLayout.setVisibility(View.VISIBLE);

        //职位
        String teacherWork = item.getPosition();
        if (TextUtils.isEmpty(teacherWork)) {
            teacherPosition.setVisibility(View.GONE);
        } else {
            teacherPosition.setVisibility(View.VISIBLE);
            teacherPosition.setText(teacherWork);
        }

        //设置显示
        final boolean isPart = item.getType() == TeacherBean.PART;
        if (isPart) {
            lineView.setVisibility(View.GONE);
            ViewUtil.setPartItemView(item.getImg(), item.getPart_name(), shortNameText, circleImage);
            userNameText.setText(item.getPart_name());
        } else {
            //user的第一个出现一个分割条
            if (item.isUserFirst()) {
                lineView.setVisibility(View.VISIBLE);
            } else {
                lineView.setVisibility(View.GONE);
            }
            ViewUtil.setColorItemView(item.getImg(), item.getName(), shortNameText, circleImage);
            userNameText.setText(item.getName());
        }

        final int partId = item.getPart_id();
        final int userid = item.getUserid();

        //判断是否选中
        final boolean check = item.isCheck();
        checkBox.setChecked(check);
        checkBoxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isAdd = !check;

                //设置partSet 和 userSet
                Integer id = handleSelectSet(isPart, isAdd, partId, userid);

                //设置cacheSet
                setCacheSet(item, id, isAdd, isPart);
                //是否选中
                item.setCheck(isAdd);

                //刷新recycler布局
                notifyDataSetChanged();
                //最后刷新底部的选择布局
                refreshBottomSelectorView(item, id, isAdd);

                if (checkListener != null) {
                    checkListener.onCheck();
                }
            }
        });

        if (checkBox.isChecked() && isPart) {
            helper.getConvertView().setClickable(false);
            rightIconStatus(rightIcon, true);
        } else {
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (teacherClickListener != null) {
                        teacherClickListener.onClick(item, isPart);
                    }
                }
            });
            rightIconStatus(rightIcon, false);
        }
    }

    private void rightIconStatus(View rightIcon, boolean checked) {
        if (checked) {
            rightIcon.setVisibility(View.GONE);
        } else {
            rightIcon.setVisibility(View.VISIBLE);
        }
    }

    private Integer handleSelectSet(boolean isPart, boolean isAdd, Integer partId, Integer userid) {
        Integer id;
        //反着的
        if (isPart) {
            id = partId;
            //part的
            if (isAdd) {
                partSet.add((Integer) partId);
            } else {
                partSet.remove((Integer) partId);
            }
        } else {
            id = userid;
            //user的
            if (isAdd) {
                userSet.add((Integer) userid);
            } else {
                userSet.remove((Integer) userid);
            }
        }
        return id;
    }

    private void setCacheSet(TeacherBean item, Integer id, boolean isAdd, boolean isPart) {
        TeacherBean tempTeacherBean = null;
        for (TeacherBean teacherBean : cacheSet) {
            if (isPart) {
                int partId = teacherBean.getPart_id();
                if (partId == id) {
                    tempTeacherBean = teacherBean;
                    break;
                }
            } else {
                int userid = teacherBean.getUserid();
                if (userid == id) {
                    tempTeacherBean = teacherBean;
                    break;
                }
            }
        }

        //清除
        if (tempTeacherBean != null && !isAdd) {
            cacheSet.remove(tempTeacherBean);
        }

        //添加进去
        if (tempTeacherBean == null && isAdd && item != null) {
            cacheSet.add(item);
        }
    }

    private void refreshBottomSelectorView(TeacherBean item, Integer itemId, boolean isAdd) {
        refreshBottomSelectorView(item, itemId, isAdd, true);
    }

    private void refreshBottomSelectorView(TeacherBean item, Integer itemId, boolean isAdd, boolean isNotify) {
        if (bottomSelectorView == null) {
            return;
        }

        List<SelectBean> bottomList = bottomSelectorView.getList();
        SelectBean bean;
        if (isAdd && item != null) {
            int type;
            Integer id;
            String name;

            if (item.getType() == TeacherBean.PART) {
                type = SelectBean.TYPE_PART;
                id = item.getPart_id();
                name = item.getPart_name();
            } else {
                type = SelectBean.TYPE_USER;
                id = item.getUserid();
                name = item.getName();
            }

            bean = SelectBean.createSelectBean(type, id, name, item.getImg());
            bean.isParent = false;
            bottomList.add(bean);
        } else {
            SelectBean tempSelectBean = null;
            for (SelectBean selectBean : bottomList) {
                if (selectBean.getId().equals(itemId)) {
                    tempSelectBean = selectBean;
                    break;
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

    @Override
    public void checkAll(boolean isAdd) {
        if (isAdd) {
            List<TeacherBean> data = getData();
            for (TeacherBean teacherBean : data) {
                teacherBean.setCheck(true);
                if (teacherBean.isPart()) {
                    int partId = teacherBean.getPart_id();
                    if (!partSet.contains((Integer) partId)) {
                        partSet.add((Integer) partId);

                        cacheSet.add(teacherBean);

                        refreshBottomSelectorView(teacherBean, partId, true, false);
                    }
                } else {
                    int userid = teacherBean.getUserid();
                    if (!userSet.contains((Integer) userid)) {
                        userSet.add((Integer) userid);

                        cacheSet.add(teacherBean);

                        refreshBottomSelectorView(teacherBean, userid, true, false);
                    }
                }
            }
            bottomSelectorView.refreshData();
        } else {
            List<TeacherBean> data = getData();
            List<SelectBean> list = bottomSelectorView.getList();
            List<SelectBean> tempList = new ArrayList<>(list);
            for (TeacherBean teacherBean : data) {
                teacherBean.setCheck(false);
                if (teacherBean.isPart()) {
                    int partId = teacherBean.getPart_id();
                    if (partSet.contains((Integer) partId)) {
                        partSet.remove((Integer) partId);
                        //清除cacheSet当前页面选中的值
                        for (TeacherBean bean : cacheSet) {
                            if (bean.getPart_id() == partId) {
                                cacheSet.remove(bean);
                                break;
                            }
                        }

                        for (SelectBean bean : tempList) {
                            if (partId == bean.getId() && !bean.isParent) {
                                tempList.remove(bean);
                                break;
                            }
                        }
                    }
                } else {
                    int userid = teacherBean.getUserid();
                    if (userSet.contains((Integer) userid)) {
                        userSet.remove((Integer) userid);
                        //清除cacheSet当前页面选中的值
                        for (TeacherBean bean : cacheSet) {
                            if (bean.getUserid() == userid) {
                                cacheSet.remove(bean);
                                break;
                            }
                        }
                        for (SelectBean bean : tempList) {
                            if (userid == bean.getId() && !bean.isParent) {
                                tempList.remove(bean);
                                break;
                            }
                        }
                    }
                }
            }
            bottomSelectorView.refreshData(tempList);
        }
        notifyDataSetChanged();
    }

    @Override
    public boolean iteratorAllValue() {
        boolean isCheck = true;
        List<TeacherBean> data = getData();
        //如果是0返回false
        if (data.size() == 0) {
            return false;
        }

        for (TeacherBean datum : data) {
            if (!datum.isCheck()) {
                isCheck = false;
                break;
            }
        }

        return isCheck;
    }

    public void replaceData(ContactTeacherBean.DataEntity dataEntity) {
        replaceData(dataEntity.getTeacherBean(partSet, userSet));
    }

    public void clearAll() {
        List<TeacherBean> teacherBeans = new ArrayList<>();
        replaceData(teacherBeans);
    }

    public void setBottomSelectorView(BottomSelectorView bottomSelectorView) {
        this.bottomSelectorView = bottomSelectorView;
        bottomSelectorView.setBottomRemoveClickListener(new BottomSelectorView.BottomRemoveClickListener() {
            @Override
            public void onRemoveClick(int type, Integer id) {
                boolean isPart = type == SelectBean.TYPE_PART;
                //点击只会删除
                setCacheSet(null, id, false, isPart);

                //这个id会在里面判断，所以两都传就可以了
                handleSelectSet(isPart, false, id, id);
                refreshBottomSelectorView(null, id, false);

                //最后刷新一下item状态
                notifyAdapterChanged(isPart, id);

                if (checkListener != null) {
                    checkListener.onCheck();
                }
            }
        });
    }

    private void notifyAdapterChanged(boolean isPart, Integer id) {
        for (TeacherBean teacherBean : getData()) {
            if (isPart) {
                if (teacherBean.getPart_id() == id) {
                    teacherBean.setCheck(false);
                    break;
                }
            } else {
                if (teacherBean.getUserid() == id) {
                    teacherBean.setCheck(false);
                    break;
                }
            }
        }

        //最后刷新一下
        notifyDataSetChanged();
    }

    public HashSet<TeacherBean> getCacheSet() {
        return cacheSet;
    }

    public HashSet<Integer> getPartSet() {
        return partSet;
    }

    public HashSet<Integer> getUserSet() {
        return userSet;
    }

    public interface TeacherClickListener {
        void onClick(TeacherBean item, boolean isPart);
    }

    TeacherClickListener teacherClickListener;

    public void setTeacherClickListener(TeacherClickListener teacherClickListener) {
        this.teacherClickListener = teacherClickListener;
    }

    private CheckListener checkListener;

    public void setCheckClickListener(CheckListener checkListener) {
        this.checkListener = checkListener;
    }

}
