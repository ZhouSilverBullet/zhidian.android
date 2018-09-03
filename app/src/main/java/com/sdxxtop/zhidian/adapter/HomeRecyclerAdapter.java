package com.sdxxtop.zhidian.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.MainIndexBean;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.ImageParams;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.activity.PhotoViewActivity;
import com.sdxxtop.zhidian.ui.activity.PunchCardActivity;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.ImageSelectorHelper;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.utils.WorkStatusUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/5/4.
 */

public class HomeRecyclerAdapter extends BaseQuickAdapter<MainIndexBean.DataBean.SignLogBean, BaseViewHolder> {

    private MainIndexBean.DataBean.ApplyBean applyBean;
    private int is_rest;
    private final ImageSelectorHelper imageSelectorHelper;
    //当点击的时候得到cardId
    private String cardId;
    private String selectorDate;
    private int isNeed;
    private boolean isNotHomeData;
    private String[] signDateString;

    public HomeRecyclerAdapter(int layoutResId, Fragment currentFragment) {
        super(layoutResId);
        imageSelectorHelper = new ImageSelectorHelper(currentFragment);
    }

    public HomeRecyclerAdapter(int layoutResId, Activity activity) {
        super(layoutResId);
        imageSelectorHelper = new ImageSelectorHelper(activity);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MainIndexBean.DataBean.SignLogBean item) {
        View view = helper.getView(R.id.item_home_recycler_left_divider);
        //最后一个item隐藏左边的线
        if (helper.getAdapterPosition() == getItemCount() - 1) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }

        TextView timeText = helper.getView(R.id.item_home_recycler_time);
        TextView workTitle = helper.getView(R.id.item_home_recycler_work_title);
        TextView workTime = helper.getView(R.id.item_home_recycler_work_time);
        TextView workDescription = helper.getView(R.id.item_home_recycler_work_description);
        TextView workLeak = helper.getView(R.id.item_home_recycler_work_leak);

        TextView xiuxiTitle = helper.getView(R.id.item_home_recycler_work_xiuxi);

        //外勤打卡
        FrameLayout fieldLayout = helper.getView(R.id.item_home_recycler_work_field_layout);
        TextView fieldGpsText = helper.getView(R.id.item_home_recycler_field_gps_text);
        final ImageView fieldIcon = helper.getView(R.id.item_home_recycler_work_field_icon);
        View fieldDelete = helper.getView(R.id.item_home_recycler_work_field_delete);
        ImageView fieldTakePhoto = helper.getView(R.id.item_home_recycler_field_take_photo);
        View fieldLine = helper.getView(R.id.item_home_recycler_field_line);
        View timeLine = helper.getView(R.id.item_home_recycler_work_time_line);

        String sys_date = item.getSys_date();
        if (!TextUtils.isEmpty(sys_date)) {
            sys_date = sys_date.substring(0, sys_date.length() - 3);
        }
        if (item.isLeakCard()) { //是否是空，未打卡
            if (is_rest != 0 || isNeed == 2) {  //休息状态不一样
                xiuxiTitle.setVisibility(View.VISIBLE);
                xiuxiTitle.setText("打卡时间");
                String sign_time = item.getSign_time();
                if (TextUtils.isEmpty(sign_time)) {
                    workDescription.setText("---");
                } else {
                    workDescription.setText(sign_time);
                }
                workDescription.setTextColor(mContext.getResources().getColor(R.color.recycler_text_stroke_color));
                workTime.setVisibility(View.INVISIBLE);
                timeText.setVisibility(View.VISIBLE);
                fieldLine.setVisibility(View.GONE);
                timeLine.setVisibility(View.VISIBLE);
                fieldLayout.setVisibility(View.GONE);
                fieldTakePhoto.setVisibility(View.GONE);
                workLeak.setVisibility(View.INVISIBLE);
                fieldGpsText.setVisibility(View.GONE);
                workTitle.setVisibility(View.INVISIBLE);
            } else {
                xiuxiTitle.setVisibility(View.GONE);
                timeText.setText(StringUtil.stringNotNull(sys_date));
                workTitle.setText(StringUtil.stringNotNull(item.getSign_name()) + "打卡");
                workTitle.setVisibility(View.VISIBLE);
                workTime.setText("---");
                workTime.setVisibility(View.VISIBLE);
                fieldLine.setVisibility(View.GONE);
                timeLine.setVisibility(View.VISIBLE);
                workDescription.setText("");
                fieldLayout.setVisibility(View.GONE);
                fieldTakePhoto.setVisibility(View.GONE);
                workLeak.setVisibility(View.INVISIBLE);
                fieldGpsText.setVisibility(View.GONE);
            }
        } else {

            if (is_rest != 0 || isNeed == 2) {  //休息状态不一样
                xiuxiTitle.setVisibility(View.VISIBLE);
                xiuxiTitle.setText("打卡时间");
                String sign_time = item.getSign_time();
                if (!TextUtils.isEmpty(sign_time)) {
                    String[] split = sign_time.split(" ");
                    if (split.length == 2) {
                        String time = split[1].substring(0, split[1].lastIndexOf(":"));
                        workDescription.setText(StringUtil.stringNotNull(time));
                    } else {
                        workDescription.setText("---");
                    }
                } else {
                    workDescription.setText("---");
                }
                workDescription.setTextColor(mContext.getResources().getColor(R.color.recycler_text_stroke_color));
                workTime.setVisibility(View.INVISIBLE);
                timeText.setVisibility(View.VISIBLE);
                fieldLine.setVisibility(View.GONE);
                timeLine.setVisibility(View.VISIBLE);
                fieldLayout.setVisibility(View.GONE);
                fieldTakePhoto.setVisibility(View.GONE);
                workLeak.setVisibility(View.INVISIBLE);
                fieldGpsText.setVisibility(View.GONE);
                //标题隐藏
                workTitle.setVisibility(View.INVISIBLE);
            } else {
                workTime.setVisibility(View.VISIBLE);
                xiuxiTitle.setVisibility(View.GONE);
                workTitle.setVisibility(View.VISIBLE);
                timeText.setText(StringUtil.stringNotNull(sys_date));
                workTitle.setText(StringUtil.stringNotNull(item.getSign_name()) + "打卡");
                String sign_time = item.getSign_time();
                if (!TextUtils.isEmpty(sign_time)) {
                    String[] split = sign_time.split(" ");
                    if (split.length == 2) {
                        String time = split[1].substring(0, split[1].lastIndexOf(":"));
                        workTime.setText(StringUtil.stringNotNull(time));
                    } else {
                        workTime.setText("---");
                    }
                } else {
                    workTime.setText("---");
                }

                int status = item.getStatus();
                String statusValue = "";
                switch (status) {
                    //(1:正常 2:迟到减免 3:早退减免4消迟到早退 5:迟到 6:早退 7:旷工 8:漏打卡)
                    case WorkStatusUtil.WORK_NORMAL:
                        statusValue = "正常";
                        workDescription.setTextColor(mContext.getResources().getColor(R.color.recycler_text_stroke_color));
                        workLeak.setVisibility(View.INVISIBLE);
                        break;
                    case WorkStatusUtil.WORK_LATE_BREAKS:
//                        statusValue = "迟到减免";
                        statusValue = "正常";
                        workDescription.setTextColor(mContext.getResources().getColor(R.color.recycler_text_stroke_color));
                        workLeak.setVisibility(View.VISIBLE);
                        workLeak.setText("迟到减免");
                        break;
                    case WorkStatusUtil.WORK_LEAVE_EARLY_BREAKS:
//                        statusValue = "早退减免";
                        statusValue = "正常";
                        workDescription.setTextColor(mContext.getResources().getColor(R.color.recycler_text_stroke_color));
                        workLeak.setVisibility(View.VISIBLE);
                        workLeak.setText("早退减免");
                        break;
                    case WorkStatusUtil.WORK_LATE_ARRIVAL:
                        statusValue = "消迟到早退";
                        workDescription.setTextColor(mContext.getResources().getColor(R.color.recycler_text_stroke_color));
                        workLeak.setVisibility(View.INVISIBLE);
                        break;
                    case WorkStatusUtil.WORK_LATE:
                        statusValue = "消迟到";
                        workDescription.setTextColor(mContext.getResources().getColor(R.color.calendar_selector_color));
                        workLeak.setVisibility(View.VISIBLE);
                        workLeak.setText("迟到");
                        break;
                    case WorkStatusUtil.WORK_LEAVE_EARLY:
                        statusValue = "消早退";
                        workDescription.setTextColor(mContext.getResources().getColor(R.color.calendar_selector_color));
                        workLeak.setVisibility(View.VISIBLE);
                        workLeak.setText("早退");
                        break;
                    case WorkStatusUtil.WORK_ABSENTEEISM:
//                        statusValue = "旷工";
                        statusValue = "";
                        workDescription.setTextColor(mContext.getResources().getColor(R.color.calendar_selector_color));
                        workLeak.setVisibility(View.VISIBLE);
                        workLeak.setText("旷工");
                        break;
                    case WorkStatusUtil.WORK_CLOCK_IN_LEAKAGE:
                        statusValue = "去补卡";
                        workDescription.setTextColor(mContext.getResources().getColor(R.color.calendar_selector_color));
                        workLeak.setVisibility(View.VISIBLE);
                        workLeak.setText("漏打卡");
                        workTime.setText("---");
                        break;
                }
                workDescription.setText(statusValue);

                if (status == WorkStatusUtil.WORK_CLOCK_IN_LEAKAGE) {
                    workDescription.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, PunchCardActivity.class);
                            ArrayList<MainIndexBean.DataBean.SignLogBean> arrayList = new ArrayList<>();
                            arrayList.add(item);
                            intent.putExtra("sign_log", arrayList);
                            intent.putExtra("selector_date", selectorDate);
                            intent.putExtra("at", 2);
                            mContext.startActivity(intent);
                        }
                    });
                } else if (status == WorkStatusUtil.WORK_LEAVE_EARLY ||
                        status == WorkStatusUtil.WORK_LATE) {
                    workDescription.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, PunchCardActivity.class);
                            ArrayList<MainIndexBean.DataBean.SignLogBean> arrayList = new ArrayList<>();
                            arrayList.add(item);
                            intent.putExtra("sign_log", arrayList);
                            intent.putExtra("selector_date", selectorDate);
                            intent.putExtra("at", 3);
                            mContext.startActivity(intent);
                        }
                    });
                } else {
                    workDescription.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }

                //是否为外勤打卡 1:是 2:否
                if (item.getIs_out() == 1) {
//                    fieldTakePhoto.setVisibility(View.VISIBLE);
                    fieldGpsText.setVisibility(View.VISIBLE);
                    fieldLine.setVisibility(View.VISIBLE);
                    timeLine.setVisibility(View.GONE);
                    workTime.setVisibility(View.VISIBLE); //外勤打卡需要显示时间
                    final String img = item.getImg();
                    if (!TextUtils.isEmpty(img)) { //有图片显示图片展示
                        fieldTakePhoto.setVisibility(View.GONE);
                        fieldLayout.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(img).into(fieldIcon);
                    } else {
                        fieldTakePhoto.setVisibility(View.VISIBLE);
                        fieldLayout.setVisibility(View.GONE);
                    }
                    final int sign_id = item.getSign_id();
                    fieldGpsText.setText(StringUtil.stringNotNull(item.getSign_data()));
                    fieldTakePhoto.setOnClickListener(new View.OnClickListener() {  //跳入到相机或者图片选择
                        @Override
                        public void onClick(View v) {
                            cardId = sign_id + "";
                            if (imageSelectorHelper != null) {
                                imageSelectorHelper.show();
                            }
                        }
                    });

                    fieldIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PhotoViewActivity.start(mContext, img);
                        }
                    });

                    fieldDelete.setOnClickListener(new View.OnClickListener() { //删除图片
                        @Override
                        public void onClick(View v) {
                            logicDelete(sign_id + "");
                        }
                    });

                    if (isNotHomeData) {
                        fieldDelete.setVisibility(View.GONE);
                        fieldTakePhoto.setVisibility(View.GONE);
                    }

                } else {
                    fieldTakePhoto.setVisibility(View.GONE);
                    fieldLayout.setVisibility(View.GONE);
                    fieldGpsText.setVisibility(View.GONE);
                    workTime.setVisibility(View.VISIBLE); //出现时间一般不是外勤打卡，需要显示
                    fieldLine.setVisibility(View.GONE);
                    timeLine.setVisibility(View.VISIBLE);
                }
            }
        }

        //这个就是休息日后面就不管请假还是申请了
        if (is_rest != 0) {
            timeText.setText("休息");
        } else {
            if (applyBean != null) {
                String start_time = applyBean.getStart_time();
                String end_time = applyBean.getEnd_time();
                int apply_type = applyBean.getApply_type();
                long startLongTime = DateUtil.convertTimeToLong(start_time, "yyyy-MM-dd HH:mm:ss");
                long endLongTime = DateUtil.convertTimeToLong(end_time, "yyyy-MM-dd HH:mm:ss");
                //在这个时间之间就先是 申请类型(1:请假 4:出差 6:集体请假 7:集体出差)

                long sysDateLongTime = DateUtil.convertTimeToLong(signDateString[helper.getPosition() - 1], "yyyy-MM-dd HH:mm:ss");

                if (sysDateLongTime >= startLongTime && sysDateLongTime <= endLongTime) {
                    //代码一样////////////////////////////
                    String sign_time = item.getSign_time();
                    if (apply_type == 1 || apply_type == 6) {
                        timeText.setText("请假");
                        workLeak.setVisibility(View.INVISIBLE);
                    } else if (apply_type == 4 || apply_type == 7) {
                        timeText.setText("出差");
                        workLeak.setVisibility(View.INVISIBLE);
                    }

                    if (!item.isLeakCard()) {
                        if (!TextUtils.isEmpty(sign_time)) {
                            String[] split = sign_time.split(" ");
                            if (split.length == 2) {
                                String time = split[1].substring(0, split[1].lastIndexOf(":"));
//                                workDescription.setText(StringUtil.stringNotNull(time));
                                workDescription.setText("正常");
                            } else {
                                workDescription.setText("---");
                            }
                        } else {
                            workDescription.setText("---");
                        }
                    }
                }
            }
        }

        if (isNeed == 2) {
            timeText.setText("无勤");
        }
    }

    private void logicDelete(String cardId) {
        Params params = new Params();
        params.put("si", cardId);

        RequestUtils.createRequest().postMainDelImg(params.getData()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200) {
                    return;
                }
                //删除成功刷新界面
                if (listener != null) {
                    listener.onDeleteClick();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void addData(@NonNull Collection<? extends MainIndexBean.DataBean.SignLogBean> newData, MainIndexBean.DataBean.ApplyBean applyBean, int is_rest, String selectorDate) {
        this.applyBean = applyBean;
        this.is_rest = is_rest;
        this.selectorDate = selectorDate;
        super.replaceData(newData);
    }

    /**
     * 不是首页的数据，就不应该进行相机个删除图片操作
     *
     * @param isNotHomeData
     */
    public void setIsNotHomeData(boolean isNotHomeData) {
        this.isNotHomeData = isNotHomeData;
    }

    private FieldImageListener listener;

    public void setFieldImageListener(FieldImageListener listener) {
        this.listener = listener;
    }

    public void onResult(final int requestCode, int resultCode, Intent data) {
        if (TextUtils.isEmpty(cardId)) {
            ToastUtil.show("图片上传失败");
            return;
        }
        if (imageSelectorHelper != null) {
            imageSelectorHelper.onActivityResult(requestCode, resultCode, data);
            String imgPath = imageSelectorHelper.getImgPath();
            if (!TextUtils.isEmpty(imgPath)) {
                ImageParams params = new ImageParams();
                params.put("si", cardId);
                String targetPath = mContext.getExternalCacheDir() + "/xunxing_photo/img" + System.currentTimeMillis() + ".png";
                params.addCompressImagePath("img", new File(imgPath), targetPath, 50);
                RequestUtils.createRequest().postMainImg(params.getImgData()).enqueue(new RequestCallback<BaseModel>(mContext, new IRequestListener<BaseModel>() {

                    @Override
                    public void onSuccess(BaseModel baseModel) {
                        if (listener != null) {
                            listener.onAddPhotoClick();
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        ToastUtil.show(errorMsg);
                    }

                }));
            }
        }
    }

    public void setIsNeed(int isNeed) {
        this.isNeed = isNeed;
    }

    public void setSignDateString(String[] signDateString) {
        this.signDateString = signDateString;
    }

    public interface FieldImageListener {
        void onDeleteClick();

        void onAddPhotoClick();
    }

}
