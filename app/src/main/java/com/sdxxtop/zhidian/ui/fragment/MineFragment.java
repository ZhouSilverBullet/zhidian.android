package com.sdxxtop.zhidian.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.PowerAdapter;
import com.sdxxtop.zhidian.entity.PowerBean;
import com.sdxxtop.zhidian.entity.UcenterIndexBean;
import com.sdxxtop.zhidian.eventbus.ChangeCompanyEvent;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.activity.CourseTableActivity;
import com.sdxxtop.zhidian.ui.activity.HomeworkActivity;
import com.sdxxtop.zhidian.ui.activity.MineAttendanceActivity;
import com.sdxxtop.zhidian.ui.activity.MineFieldActivity;
import com.sdxxtop.zhidian.ui.activity.MineWorkActivity;
import com.sdxxtop.zhidian.ui.activity.MyInfoActivity;
import com.sdxxtop.zhidian.ui.activity.MySettingActivity;
import com.sdxxtop.zhidian.ui.activity.Notice2Activity;
import com.sdxxtop.zhidian.ui.activity.SchedulingManageActivity;
import com.sdxxtop.zhidian.ui.activity.StatisticalActivity;
import com.sdxxtop.zhidian.ui.activity.StudentAttendanceActivity;
import com.sdxxtop.zhidian.ui.activity.VoteActivity;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：CaiCM
 * 日期：2018/3/23  时间：16:39
 * 邮箱：15010104100@163.com
 * 描述：我的界面
 */
public class MineFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.iv_img)
    CircleImageView ivImg;
    @BindView(R.id.tv_short_name)
    TextView tvShortName;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    //    @BindView(R.id.tv_notice)
//    TextView tvNotice;
//    @BindView(R.id.tv_attendance)
//    TextView tvAttendance;
//    @BindView(R.id.tv_field)
//    TextView tvField;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.tv_mine)
    TextView tvMine;
    @BindView(R.id.rl_middle)
    RelativeLayout rlMiddle;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.tv_manage)
    TextView tvManage;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.mine_gridView)
    GridView mineGridView;
    @BindView(R.id.mine_scroll_view)
    NestedScrollView mineNestedScrollView;


    String[] mineNames = {"公告", "我的考勤", "我的外勤", "工作汇报", "课程表"};
    int[] mineImgs = {R.mipmap.announcement, R.mipmap.my_attendance, R.mipmap.my_field, R.drawable.my_work, R.drawable.my_list};

    String[] names = {"排班", "统计", "民主评议", "学生出勤", "布置作业"};
    int[] imgs = {R.mipmap.classes, R.mipmap.statistics, R.mipmap.vote, R.drawable.my_student, R.drawable.my_homework};

    PowerBean powerBean = null;
    private int is_manager;
    private int is_class;
    private int is_out;
    private int is_stat;
    private PowerAdapter powerAdapter;
    private List<PowerBean> list;

    private boolean isFirstLayout;

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initVariables() {
        super.initVariables();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void initView() {
        statusBar(true);

        View topLayout = mRootView.findViewById(R.id.top_layout);
        topViewPadding(topLayout);

        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            statusBar(true);
            initData();
        }
    }

    @Override
    protected void initData() {
        postUcenterIndex();
    }

    @Override
    protected void initEvent() {
        gridView.setOnItemClickListener(this);
    }

    @OnClick({R.id.iv_img, R.id.tv_setting/*, R.id.tv_notice, R.id.tv_attendance, R.id.tv_field, R.id.tv_classes, R.id.tv_statistics, R.id.tv_vote*/})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_img:
                Intent intentMyInfo = new Intent(mContext, MyInfoActivity.class);
//                startActivity(intentMyInfo);
                startActivityForResult(intentMyInfo, 100);
                break;
            case R.id.tv_setting:
                Intent intentSetting = new Intent(mContext, MySettingActivity.class);
                startActivity(intentSetting);
                break;
//            case R.id.tv_notice:
//                Intent intentNotice = new Intent(mContext, Notice2Activity.class);
//                startActivity(intentNotice);
//                break;
//            case R.id.tv_attendance:
//                Intent intent = new Intent(mContext, MineAttendanceActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.tv_field:
//                Intent intents = new Intent(mContext, MineFieldActivity.class);
//                startActivity(intents);
//                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        ToastUtil.show(data.getStringExtra("img"));
        if (requestCode == 100 && resultCode == 200 && data != null) {
            String img = data.getStringExtra("img");
            if (!TextUtils.isEmpty(img)) {
                tvShortName.setText("");
                Glide.with(mContext).load(img).into(ivImg);
            }
        }
    }

    /**
     * 我的模块主页网络请求
     */
    public void postUcenterIndex() {
        Map<String, String> map = new HashMap<>();
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postUcenterIndex(base64Data).enqueue(new Callback<UcenterIndexBean>() {
            @Override
            public void onResponse(Call<UcenterIndexBean> call, Response<UcenterIndexBean> response) {
                closeProgressDialog();
                UcenterIndexBean ucenterIndexBean = response.body();
                if (ucenterIndexBean.getCode() == 200) {
                    if (ucenterIndexBean.getData().getUserinfo().getImg().startsWith("#")) {
                        ivImg.setImageDrawable(null);
                        ivImg.setImageDrawable(new ColorDrawable(Color.parseColor(ucenterIndexBean.getData().getUserinfo().getImg())));
                    } else {
                        Glide.with(mContext).load(ucenterIndexBean.getData().getUserinfo().getImg()).into(ivImg);
                    }
                    tvName.setText(ucenterIndexBean.getData().getUserinfo().getName());
                    if (ucenterIndexBean.getData().getUserinfo().getImg().startsWith("#")) {
                        //图片上展示的两个字
                        if (ucenterIndexBean.getData().getUserinfo().getName().length() >= 2) {
                            tvShortName.setText(ucenterIndexBean.getData().getUserinfo().getName().substring(ucenterIndexBean.getData().getUserinfo().getName().length() - 2, ucenterIndexBean.getData().getUserinfo().getName().length()));
                        } else {
                            tvShortName.setText(ucenterIndexBean.getData().getUserinfo().getName());
                        }
                    } else {
                        tvShortName.setText("");
                    }
                    tvPosition.setText(ucenterIndexBean.getData().getUserinfo().getPosition());
                    tvPhone.setText(ucenterIndexBean.getData().getUserinfo().getMobile() + "");

//                    is_manager = ucenterIndexBean.getData().getIs_manager();//是否是高级管理员
                    is_class = ucenterIndexBean.getData().getIs_class();//是否有排班权限
                    is_out = ucenterIndexBean.getData().getIs_out();//是否有外勤权限
                    is_stat = ucenterIndexBean.getData().getIs_stat();//是否有统计权限

                    rlMiddle.setVisibility(View.VISIBLE);

                    list = new ArrayList<>();
                    for (int i = 0; i < imgs.length; i++) {
                        powerBean = new PowerBean();
                        powerBean.setId(i);
                        powerBean.setImg(imgs[i]);
                        powerBean.setName(names[i]);
                        list.add(powerBean);
                    }
                    if (is_stat == 2) {//排班
                        list.remove(1);
                    }
                    if (is_class == 2) {//统计
                        list.remove(0);
                    }

                    //我的 数据
                    powerAdapter = new PowerAdapter(mContext, list);
                    gridView.setAdapter(powerAdapter);

                    final List<PowerBean> powerBeanList = new ArrayList<>();
                    for (int i = 0; i < mineImgs.length; i++) {
                        powerBean = new PowerBean();
                        powerBean.setId(i);
                        powerBean.setImg(mineImgs[i]);
                        powerBean.setName(mineNames[i]);
                        powerBeanList.add(powerBean);
                    }

                    if (is_out != 1) { //去除外勤
                        powerBeanList.remove(2);
                    }

                    PowerAdapter adapter = new PowerAdapter(mContext, powerBeanList);
                    mineGridView.setAdapter(adapter);
                    mineGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                    mineGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            PowerBean powerBean = powerBeanList.get(position);
                            switch (powerBean.getId()) {
                                case 0:
                                    Intent intentNotice = new Intent(mContext, Notice2Activity.class);
                                    startActivity(intentNotice);
                                    break;
                                case 1:
                                    Intent intent = new Intent(mContext, MineAttendanceActivity.class);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    Intent intents = new Intent(mContext, MineFieldActivity.class);
                                    startActivity(intents);
                                    break;
                                case 3:
                                    Intent workIntent = new Intent(mContext, MineWorkActivity.class);
                                    startActivity(workIntent);
                                    break;
                                case 4:
                                    Intent coureTableIntent = new Intent(mContext, CourseTableActivity.class);
                                    startActivity(coureTableIntent);
                                    break;
                            }
                        }
                    });

                    if (!isFirstLayout) {
                        mineNestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
                        isFirstLayout = true;
                    }
                } else {
                    ToastUtil.show(ucenterIndexBean.getMsg());
                }
            }

            @Override
            public void onFailure(Call<UcenterIndexBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch (list.get(position).getId()) {
            case 0:
//                ToastUtil.show("排班");
                startActivity(new Intent(mContext, SchedulingManageActivity.class));
                break;
            case 1:
                startActivity(new Intent(mContext, StatisticalActivity.class));
                break;
            case 2:
                Intent intentVote = new Intent(mContext, VoteActivity.class);
                startActivity(intentVote);
                break;
            case 3:
//                intent = new Intent(mContext, VoteActivity.class);
//                startActivity(intent);
                StudentAttendanceActivity.startStudentAttendanceActivity(mContext);
                break;
            case 4:
                HomeworkActivity.startHomeworkActivity(mContext);
                break;
        }
    }

    /**
     * 切换公司成功后
     *
     * @param event
     */
    @Subscribe
    public void onRefreshConpanyChangeSuccess(ChangeCompanyEvent event) {
        initData();
    }
}
