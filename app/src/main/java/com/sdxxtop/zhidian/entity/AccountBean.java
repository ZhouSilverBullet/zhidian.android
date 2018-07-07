package com.sdxxtop.zhidian.entity;

/**
 * 作者: ZhangHD
 * 日期: 2018/3/24 时间: 9:22
 * 描述:账号密码实体类
 */

public class AccountBean {
    public AccountBean(String account, String password, String companyId, String userId) {
        this.account = account;
        this.password = password;
        this.companyId = companyId;
        this.userId = userId;
    }

    private String account;
    private String password;
    private String companyId;
    private String userId;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
