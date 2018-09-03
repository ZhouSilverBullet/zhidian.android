package com.sdxxtop.zhidian.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/1.
 */
public class TeacherBean implements Serializable {
    public static final int USER = 11;
    public static final int PART = 10;

    //区分是part还是user
    private int type;

    private int part_id;
    private String company_id;
    private String part_name;

    private int userid;
    //是否是第一个user
    private boolean isUserFirst;
    private String name;
    private String position;
    private String img;
    private int user_status;
    private boolean isCheck;

    public static int getUSER() {
        return USER;
    }

    public static int getPART() {
        return PART;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPart_id() {
        return part_id;
    }

    public void setPart_id(int part_id) {
        this.part_id = part_id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getPart_name() {
        return part_name;
    }

    public void setPart_name(String part_name) {
        this.part_name = part_name;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public boolean isUserFirst() {
        return isUserFirst;
    }

    public void setUserFirst(boolean userFirst) {
        isUserFirst = userFirst;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isPart() {
        return type == PART;
    }
}
