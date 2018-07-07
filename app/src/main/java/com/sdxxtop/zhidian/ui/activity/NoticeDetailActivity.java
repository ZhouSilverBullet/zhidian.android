package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;
import zhangphil.iosdialog.widget.AlertDialog;

/**
 * 作者：CaiCM
 * 日期：2018/3/27  时间：22:22
 * 邮箱：15010104100@163.com
 * 描述：发布完公告之后的公告详情界面
 */
public class NoticeDetailActivity extends BaseActivity {

    @BindView(R.id.tv_retuen)
    TextView tvRetuen;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_Right)
    TextView tvRight;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.tv_notice_title)
    TextView tvNoticeTitle;
    @BindView(R.id.tv_recive_range)
    TextView tvReciveRange;
    @BindView(R.id.tv_notice_name)
    TextView tvNoticeName;
    @BindView(R.id.tv_notice_time)
    TextView tvNoticeTime;
    @BindView(R.id.tv_read)
    TextView tvRead;
    @BindView(R.id.tv_not_read)
    TextView tvNotRead;
    @BindView(R.id.rl_read_or_not)
    RelativeLayout rlReadOrNot;
    @BindView(R.id.iv_notice_img)
    ImageView ivNoticeImg;
    @BindView(R.id.tv_notice_content)
    TextView tvNoticeContent;
    private String notice_id;
    private String not_num;
    private String read_num;

    @Override
    protected int getActivityView() {
        return R.layout.activity_notice_detail;
    }

    @Override
    protected void initView() {
        tvRetuen.setText("公告");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("撤回");
        tvRight.setTextColor(Color.parseColor("#3296FA"));
        Intent intent = getIntent();
        notice_id = intent.getStringExtra("notice_id");
        LogUtils.e("log", "notice_id:"+ notice_id);
    }

    @Override
    protected void initData() {
        postNoticeRead();
    }

    @OnClick({R.id.tv_retuen, R.id.tv_Right, R.id.rl_read_or_not, R.id.iv_notice_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_retuen:
                finish();
                break;
            case R.id.tv_Right:
                new AlertDialog(mContext)
                        .builder()
                        .setTitle("确定撤回本次公告吗？")
//                        .setMsg("再连续登陆天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？")
                        .setPositiveButton("撤回", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                postNoticeRevoke();
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                break;
            case R.id.rl_read_or_not:
                Intent intent = new Intent(mContext, NoticeReciveDetailActivity.class);
                intent.putExtra("noticeid", notice_id);
                intent.putExtra("not_num", not_num);
                intent.putExtra("read_num", read_num);
                startActivity(intent);
                break;
            case R.id.iv_notice_img:
                // TODO: 2018/3/29 点击查看图片
                break;
        }
    }


    /**
     * 浏览公告网络请求
     */
    private void postNoticeRead() {
//        final Map<String, String> map = new HashMap<>();
//        map.put("ni", notice_id);
//        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
//        String base64Data = NetUtil.getBase64Data(map);
//        showProgressDialog("");
//        RequestUtils.getInstance().buildRequest().postNoticeRead(base64Data).enqueue(new Callback<NoticeReadBean>() {
//
//            @Override
//            public void onResponse(Call<NoticeReadBean> call, Response<NoticeReadBean> response) {
//                closeProgressDialog();
//                NoticeReadBean noticeReadBean = response.body();
//                if (noticeReadBean.getCode() == 200){
//                    not_num = noticeReadBean.getData().getNot_num() + "";
//                    read_num = noticeReadBean.getData().getRead_num()+"";
//                    List<String> nameList = noticeReadBean.getData().getName();
//                    List<String> part_nameList = noticeReadBean.getData().getPart_name();
//                    /**
//                     * is_bind         是否绑定用户 1:全公司 2:绑定用户
//                     * 1.展示范围为全公司，2.展示范围为nameList和part_nameList
//                     */
//                    String is_bind = noticeReadBean.getData().getNotice().getIs_bind() + "";
//                    String title = noticeReadBean.getData().getNotice().getTitle();
//                    String add_time = noticeReadBean.getData().getNotice().getAdd_time();
//                    String content = noticeReadBean.getData().getNotice().getContent();
//                    String name = noticeReadBean.getData().getNotice().getName();
//                    String notice_id = noticeReadBean.getData().getNotice().getNotice_id()+"";
//                    String notice_img = noticeReadBean.getData().getNotice().getImg().toString();
//                    tvTitle.setText(title);
//                    tvNoticeTitle.setText(title);
//                    tvNoticeName.setText(name);
//                    tvNoticeTime.setText(add_time);
//                    tvNotRead.setText(not_num);
//                    tvRead.setText(read_num);
//                    tvNoticeContent.setText(content);
//                    if (notice_img.toString().equals("")){
//                        ivNoticeImg.setImageResource(R.mipmap.girl);
//                    }
//                    else{
//                        String[] imgArr = notice_img.split(",");
//                        // TODO: 2018/3/30 把数组的第一个元素赋值给控件
//                        Glide.with(mContext).load(imgArr[0]).into(ivNoticeImg);
//                    }
//                }
//                else{
//                    ToastUtil.show(noticeReadBean.getMsg().toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NoticeReadBean> call, Throwable t) {
//                closeProgressDialog();
//                ToastUtil.show("网络错误");
//            }
//        });
    }


    /**
     * 撤回公告网络请求
     */
    private void postNoticeRevoke() {
//        Map<String, String> map = new HashMap<>();
//        map.put("ni", notice_id);
//        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
//        String base64Data = NetUtil.getBase64Data(map);
//        showProgressDialog("");
//        RequestUtils.getInstance().buildRequest().postRevokeNotice(base64Data).enqueue(new Callback<RevokeNoticeBean>() {
//            @Override
//            public void onResponse(Call<RevokeNoticeBean> call, Response<RevokeNoticeBean> response) {
//                closeProgressDialog();
//                RevokeNoticeBean revokeNoticeBean = response.body();
//                if (revokeNoticeBean.getCode() == 200) {
//                    ToastUtil.show("撤回成功");
//                } else {
//                    ToastUtil.show(response.body().getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RevokeNoticeBean> call, Throwable t) {
//                closeProgressDialog();
//                ToastUtil.show("网络错误");
//            }
//        });
    }
}
