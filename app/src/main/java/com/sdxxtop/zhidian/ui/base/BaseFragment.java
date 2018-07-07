package com.sdxxtop.zhidian.ui.base;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdxxtop.zhidian.App;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.utils.DialogUtil;
import com.sdxxtop.zhidian.utils.StatusBarUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：15:24
 * 邮箱：15010104100@163.com
 * 描述：视图控制基类
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private Unbinder mUnbinder;
    protected Dialog progressDialog;
    protected BaseActivity mContext;
    protected View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO: 2018/3/27  设置Fragment背景颜色?
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.bkcolor);
        getActivity().getWindow().setBackgroundDrawable(drawable);
        mRootView = inflater.inflate(getFragmentView(), container, false);
        mUnbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (BaseActivity) getActivity();
        initVariables();
        initView();
        initData();
        initEvent();
    }

    protected void initVariables() {
    }

    @Override
    public void onClick(View view) {

    }


    public void showToast(String value) {
        ToastUtil.show(value);
    }
    /**
     * 初始化界面
     */
    protected abstract int getFragmentView();

    /**
     * 初始化数据
     */
    protected void initView() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 设置监听及回掉
     */
    protected void initEvent() {
    }


    protected void showProgressDialog(String message) {
        if (null != mContext && !mContext.isFinishing()) {
            if (progressDialog == null) {
                progressDialog = DialogUtil.createLoadingDialog(getActivity(),
                        message, false);
            }
            progressDialog.show();
        }
    }

    protected void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }

        EventBus.getDefault().unregister(this);
    }

    public void topViewPadding(View view) {
        if (isVersionMoreKitkat()) {
            view.setPadding(0, ViewUtil.getStatusHeight(App.getAppContext()), 0, 0);
        }
    }

    public boolean isVersionMoreKitkat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return true;
        } else {
            return false;
        }
    }

    public void statusBar(boolean isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//Android6.0以上系统
            StatusBarUtil.setDarkStatusIcon(getActivity().getWindow(), isDark);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
