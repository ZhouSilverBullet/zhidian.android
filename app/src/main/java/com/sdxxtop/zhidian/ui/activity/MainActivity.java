package com.sdxxtop.zhidian.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.permissions.RxPermissions;
import com.sdxxtop.zhidian.App;
import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.alipush.LocationService;
import com.sdxxtop.zhidian.entity.AppInitBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.im.ConversationFragment;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.ApplyFragment;
import com.sdxxtop.zhidian.ui.fragment.ContactFragment;
import com.sdxxtop.zhidian.ui.fragment.HomeFragment;
import com.sdxxtop.zhidian.ui.fragment.MineFragment;
import com.sdxxtop.zhidian.utils.DeviceUtil;
import com.sdxxtop.zhidian.utils.DownloadDialog;
import com.sdxxtop.zhidian.utils.FDLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：进入首页，默认展示打卡界面
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    public static final String MAIN_SKIP = "main_skip";

    @BindView(R.id.fl_home)
    FrameLayout flHome;
    @BindView(R.id.rb_main)
    RadioButton rbMain;
    @BindView(R.id.rb_apply)
    RadioButton rbApply;
    @BindView(R.id.rb_book)
    RadioButton rbBook;
    @BindView(R.id.rb_mine)
    RadioButton rbMine;
    @BindView(R.id.rg_home)
    RadioGroup rgHome;
    @BindView(R.id.rb_conversation_red_count)
    TextView redCountText;

    private HomeFragment homeFragment;
    private ApplyFragment applyFragment;
    private ContactFragment contactFragment;
    public MineFragment mineFragment;
    private boolean isExit;
    private int mainSkip;
    private boolean isRestart;
    private LocalServiceConnection conn;
    private LocationService.LocationBind iBinder;
    private Timer tExit;
    private ConversationFragment conversationFragment;
    private RxPermissions rxPermissions;

    @Override
    protected int getActivityView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        homeTakeCardNotify(intent);
    }

    private void homeTakeCardNotify(Intent intent) {
        if (intent != null) {
            mainSkip = intent.getIntExtra(MAIN_SKIP, -1);
        }

        if (homeFragment != null && mainSkip == 1) {
            rbMain.setChecked(true);
            replaceFragment(homeFragment);
        }
    }

    @Override
    protected void initVariables() {
        super.initVariables();

        rxPermissions = new RxPermissions(this);

        homeTakeCardNotify(getIntent());
    }

    @Override
    protected void initView() {
        rgHome.setOnCheckedChangeListener(this);
        if (isRestart) {
            isRestart = false;
        } else {
            rbMain.setChecked(true);
        }

//        if (homeFragment == null) {
//            homeFragment = new HomeFragment();
//        }
//
//        if (applyFragment == null) {
//            applyFragment = new ApplyFragment();
//        }
//
//        if (contactFragment == null) {
//            contactFragment = new ContactFragment();
//        }
//
//        if (mineFragment == null) {
//            mineFragment = new MineFragment();
//        }
//        getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, homeFragment).commitAllowingStateLoss();
//        replaceFragment(homeFragment);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        Params params = new Params();
        params.put("pi", ConstantValue.pi);
        RequestUtils.createRequest().postAppInit(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<AppInitBean>() {
            @Override
            public void onSuccess(AppInitBean appInitBean) {
                AppInitBean.DataEntity data = appInitBean.getData();
                if (data != null) {
                    DownloadDialog dialog = new DownloadDialog(mContext, data, rxPermissions);
                    dialog.show();
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
            }
        }));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_main:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                replaceFragment(homeFragment);
//                getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, homeFragment).commitAllowingStateLoss();
                break;
            case R.id.rb_conversation:
                if (conversationFragment == null) {
                    conversationFragment = new ConversationFragment();
                }
                replaceFragment(conversationFragment);
                break;
            case R.id.rb_book:
                if (contactFragment == null) {
                    contactFragment = new ContactFragment();
                }
                replaceFragment(contactFragment);
//                getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, contactFragment).commitAllowingStateLoss();
                break;

            case R.id.rb_apply:
                if (applyFragment == null) {
                    applyFragment = new ApplyFragment();
                }

                replaceFragment(applyFragment);
//                getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, applyFragment).commitAllowingStateLoss();
                break;
            case R.id.rb_mine:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                }
                replaceFragment(mineFragment);
//                getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, mineFragment).commitAllowingStateLoss();
                break;
        }
    }

    /**
     * 双击退出应用程序
     */
    private long mExitTime;

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //退出方法
    private void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            //用户退出处理
            finish();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }
        }
    }

    private HashMap<String, Fragment> map;
    private Fragment tempFragment;

    private void replaceFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        String name = fragment.getClass().getSimpleName();
        if (tempFragment == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, fragment).commitAllowingStateLoss();
            if (map == null) {
                map = new HashMap<>();
            }
            map.put(name, fragment);
        } else {
            String tempName = tempFragment.getClass().getSimpleName();
            if (tempName.equals(name)) {
                return;
            }
            Fragment mapFragment = map.get(name);
            if (mapFragment == null) {
                getSupportFragmentManager().beginTransaction().hide(tempFragment).add(R.id.fl_home, fragment).commitAllowingStateLoss();
                if (map == null) {
                    map = new HashMap<>();
                }
                map.put(name, fragment);
            } else {
                getSupportFragmentManager().beginTransaction().hide(tempFragment).show(fragment).commitAllowingStateLoss();
            }
        }
        tempFragment = fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getAppContext().setRun(true);

        if (EasyPermissions.hasPermissions(this, perms)) {
            DeviceUtil.getDeviceNo(this);
            FDLocation.getInstance().location();
        } else {
            EasyPermissions.requestPermissions(this, "请开启获取手机状态权限", PERMISSION_FOR_PHONE_STATE, perms);
        }
    }

    @Override
    protected void initByRestart(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isRestart = true;

            homeFragment = getFragment(savedInstanceState, HomeFragment.class);
            applyFragment = getFragment(savedInstanceState, ApplyFragment.class);
            contactFragment = getFragment(savedInstanceState, ContactFragment.class);
            mineFragment = getFragment(savedInstanceState, MineFragment.class);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getAppContext().setRun(false);
        AppSession.getInstance().clearSession();

        if (conn != null) {
            unbindService(conn);
        }
        if (tExit != null) {
            tExit.cancel();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        FDLocation.getInstance().location();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveFragment(outState, homeFragment);
        saveFragment(outState, applyFragment);
        saveFragment(outState, contactFragment);
        saveFragment(outState, mineFragment);
        super.onSaveInstanceState(outState);
    }

    public void startLocationService(List<String> signDateList,
                                     boolean isAllGoToWork, long currentRemindMin, long logLongDate) {
        LocationService.startLocationService(this, signDateList, isAllGoToWork, currentRemindMin, logLongDate);
        conn = new LocalServiceConnection();
        bindService(new Intent(this, LocationService.class), conn, BIND_AUTO_CREATE);
    }

    /**
     * 清除打卡的闹钟
     */
    public void clearAlarm() {
        if (iBinder != null) {
            iBinder.clearCache();
        }
    }

    /**
     * 设置未读tab显示
     */
    public void setMsgUnread(long unRead) {
        if (unRead <= 0) {
            redCountText.setVisibility(View.INVISIBLE);
        } else {
            redCountText.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unRead);
            if (unRead < 10) {
                redCountText.setBackgroundDrawable(mContext.getResources().getDrawable(com.tencent.qcloud.timchat.R.drawable.point1));
            } else {
                if (unRead > 99) {
                    redCountText.setBackgroundDrawable(mContext.getResources().getDrawable(com.tencent.qcloud.timchat.R.drawable.point2));
                    unReadStr = mContext.getResources().getString(com.tencent.qcloud.timchat.R.string.time_more);
                }
            }
            if (unRead > 99) {
                unReadStr = mContext.getResources().getString(com.tencent.qcloud.timchat.R.string.time_more);
            }
            redCountText.setText(unReadStr);
        }
    }

    private class LocalServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBinder = (LocationService.LocationBind) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
