package com.sdxxtop.zhidian.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.utils.PreferenceUtils;

import java.util.ArrayList;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

public class GuideActivity extends SplashActivity implements ViewPager.OnPageChangeListener {
    private ArrayList<ImageView> cacheView;
    private int[] imgs = {R.drawable.guide_one,
            R.drawable.guide_two,
            R.drawable.guide_three,
    };

    @BindView(R.id.guide_pager)
    ViewPager viewPager;
    @BindView(R.id.guide_btn)
    Button btn;
    @BindView(R.id.guide_skip)
    ImageView skipImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

        PreferenceUtils.getInstance(this).saveParam(ConstantValue.GUIDE_IS_SHOW, true);
        PreferenceUtils.getInstance(this).saveParam(ConstantValue.GUIDE_SHOW_VERSION, getVersionCode());

        String[] perm = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        if (!EasyPermissions.hasPermissions(this, perm)) {
            EasyPermissions.requestPermissions(this, "请允许开启手机定位权限", REQUEST_PERMISSION_LOACTION, perm);
        }
    }

    @Override
    protected int getActivityView() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new GuidePagerAdapter());
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        viewPager.addOnPageChangeListener(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipToActivity();
            }
        });

        skipImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipToActivity();
            }
        });
    }

    private void skipToActivity() {
        totalTime = System.currentTimeMillis();
        int timeTemp = PreferenceUtils.getInstance(mContext).getIntParam(ConstantValue.LOGIN_TIME_TEMP);
        if (timeTemp != -1 && timeTemp < System.currentTimeMillis()) {
            //测试用暂关此功能
            postAutoLogin();
        } else { //没有自动登陆去登陆界面
            Intent intent = new Intent(mContext, NormalLoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 2) {
            btn.setVisibility(View.VISIBLE);
        } else {
            btn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class GuidePagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image;
            if (cacheView != null && cacheView.size() > 0) {
                image = cacheView.get(0);
                cacheView.remove(0);
            } else {
                image = new ImageView(getBaseContext());
                image.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            Glide.with(mContext).load(imgs[position]).into(image);
//            image.setImageResource(imgs[position]);
            container.addView(image, 0);
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (cacheView == null) {
                cacheView = new ArrayList<>();
            }
            cacheView.add((ImageView) object);
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cacheView != null) {
            cacheView.clear();
        }
    }
}
