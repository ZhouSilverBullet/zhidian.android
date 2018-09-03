package com.sdxxtop.zhidian.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by Administrator on 2018/5/10.
 * 用于选中的对象
 */

public class SelectBean implements MultiItemEntity, Serializable {
    //部门
    public static final int TYPE_PART = 100;
    //用户
    public static final int TYPE_USER = 101;
    public Integer id;
    public String name;
    public int type;
    public String color;
    public String identify;
    public boolean isPart;

    //用于判断是否是parent的值,当删除的时候id相同的时候只会删除一个，从而解决问题
    public boolean isParent;


    public static SelectBean createSelectBean(int type, Integer id, String finalName, String color) {
        SelectBean selectBean = new SelectBean();
        selectBean.id = id;
        selectBean.type = type;
        selectBean.name = finalName;
        selectBean.color = color;
        return selectBean;
    }

    public static void removeSelectBean(Set<SelectBean> selectList, int type, Integer id) {
        if (selectList == null || selectList.size() == 0) {
            return;
        }
        try {
            for (SelectBean selectBean : selectList) {
                if (selectBean.id.equals(id)) {
                    selectList.remove(selectBean);
                }
            }
        } catch (Exception e) {
        }

    }

    @Override
    public int getItemType() {
        return type;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(id);
//        dest.writeString(name);
//        dest.writeInt(type);
//        dest.writeString(color);
//    }
//
//    public static final Parcelable.Creator<SelectBean> CREATOR = new Creator<SelectBean>() {
//
//        @Override
//        public SelectBean createFromParcel(Parcel source) {
//            // TODO Auto-generated method stub
//            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
//            SelectBean selectBean = new SelectBean();
//            selectBean.setColor(source.readString());
//            selectBean.setId(source.readInt());
//            selectBean.setName(source.readString());
//            selectBean.setType(source.readInt());
//            return selectBean;
//        }
//
//        @Override
//        public SelectBean[] newArray(int size) {
//            // TODO Auto-generated method stub
//            return new SelectBean[size];
//        }
//    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getIdentify() {
        return identify;
    }
}
