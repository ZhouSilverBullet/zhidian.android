package com.sdxxtop.zhidian;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdxxtop.zhidian.entity.AccountBean;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 * <p>
 * TODO 用于缓存频繁读取 sp的内容缓存，和数据的内容缓存
 */

public class AppSession {
    private static AppSession session;
    private Context appContext;
    private String userId;
    private String companyId;
    private List<AccountBean> historyAccount;

    public static AppSession getInstance() {
        if (session == null) {
            synchronized (AppSession.class) {
                session = new AppSession();
            }
        }
        return session;
    }

    public Context getContext() {
        if (appContext == null) {
            appContext = App.getContext();
        }
        return appContext;
    }

    public String getUserId() {
        if (userId == null) {
            userId = PreferenceUtils.getInstance(getContext()).getStringParam(ConstantValue.USER_ID, "");
        }
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        if (companyId == null) {
            return getCompanyId(userId);
        }
        return companyId;
    }

    private String getCompanyId(String userId) {
        if (userId == null) {
            userId = getUserId();
        }

        List<AccountBean> historyAccount = getHistoryAccount();
        if (historyAccount != null && historyAccount.size() > 0) {
            for (AccountBean accountBean : historyAccount) {
                if (!TextUtils.isEmpty(userId) && accountBean != null && userId.equals(accountBean.getUserId())) {
                    companyId = accountBean.getCompanyId();
                    break;
                }
            }
        }

        saveHistoryAccount(historyAccount);

        if (companyId == null) {
            companyId = "";
        }

        return companyId;
    }

    //登陆的时候获取的本地的 公司id
    public String getCompanyIdForPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return "";
        }

        List<AccountBean> historyAccount = getHistoryAccount();
        if (historyAccount != null && historyAccount.size() > 0) {
            for (AccountBean accountBean : historyAccount) {
                if (accountBean != null && phone.equals(accountBean.getAccount())) {
                    companyId = accountBean.getCompanyId();
                    break;
                }
            }
        }

        if (companyId == null) {
            companyId = "";
        }

        return companyId;
    }

    //获取 AccountBean 的对象
    private List<AccountBean> getHistoryAccount() {
        if (historyAccount == null || historyAccount.size() == 0) {
            String historyAccountString = getHistoryAccountString();
            if (!TextUtils.isEmpty(historyAccountString)) {
                historyAccount = new Gson().fromJson(historyAccountString, new TypeToken<List<AccountBean>>() {
                }.getType());
            } else {
                historyAccount = new ArrayList<>();
            }
        }
        return historyAccount;
    }

    public void setCompanyId(String companyId) {
        //相同就不做更换
        if (!TextUtils.isEmpty(companyId) && companyId.equals(this.companyId)) {
            this.companyId = companyId;
            return;
        }

        // 更新当前的账号在本地切换公司的时候 companyId要进行修改
        List<AccountBean> historyAccount = getHistoryAccount();

        //这个方法不加数量，所以后面还得改一下，全部操作放着个里面来
        for (int i = 0; i < historyAccount.size(); i++) {
            AccountBean accountBean = historyAccount.get(i);
            if (!TextUtils.isEmpty(getUserId()) && accountBean != null && getUserId().equals(accountBean.getUserId())) {
                accountBean.setCompanyId(companyId);
                break;
            }
        }

        saveHistoryAccount(historyAccount);

        this.companyId = companyId;
    }

    /**
     * @param phoneNum  手机号码或者叫账户  格式（需要空格）：xxx xxxx xxxx
     * @param password  密码
     * @param companyId 公司id
     * @param userId    用户id
     */
    public void addOrRefreshAccountBean(String phoneNum, String password, String companyId, String userId) {
        List<AccountBean> historyAccount = getHistoryAccount();

        AccountBean accountBean = null;
        for (int i = 0; i < historyAccount.size(); i++) {
            AccountBean accountBean1 = historyAccount.get(i);
            if (phoneNum.equals(accountBean1.getAccount())) {
                accountBean = accountBean1;
            }
        }
        if (accountBean == null) {
            if (historyAccount.size() >= 10) { //只进行10个缓存
                historyAccount.remove(9);
            }
            historyAccount.add(0, new AccountBean(phoneNum, password, companyId, userId));
        } else {
            historyAccount.remove(accountBean);
            historyAccount.add(0, new AccountBean(phoneNum, password, companyId, userId));
        }

        this.companyId = companyId;
        //内存中存储了一份
        this.userId = userId;
        //sp中存储了一份
        PreferenceUtils.getInstance(getContext()).saveParam(ConstantValue.USER_ID, userId + "");
        //保存historyAccount
        AppSession.getInstance().saveHistoryAccount(historyAccount);
    }

    public synchronized void saveHistoryAccount(List<AccountBean> historyAccount) {
        if (historyAccount == null) {
            return;
        }
        PreferenceUtils.getInstance(getContext()).saveParam(ConstantValue.HISTORY_ACCOUNT, new Gson().toJson(historyAccount));
    }

    public synchronized String getHistoryAccountString() {
        return PreferenceUtils.getInstance(getContext()).getStringParam(ConstantValue.HISTORY_ACCOUNT);
    }

    public void clearSession() {
        session = null;
    }

    /**
     * 腾讯使用的
     *
     * @return
     */
    public String getIdentify() {
        return "t" + getCompanyId() + getUserId();
    }
}
