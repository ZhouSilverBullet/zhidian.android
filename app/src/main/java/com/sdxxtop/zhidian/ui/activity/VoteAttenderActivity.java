package com.sdxxtop.zhidian.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.VoteShowPeopleBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class VoteAttenderActivity extends BaseActivity {

    @BindView(R.id.vote_title_view)
    SubTitleView titleView;
    @BindView(R.id.vote_attender_grade)
    TextView gradeText;
    @BindView(R.id.vote_attender_three_text)
    TextView threeText;
    @BindView(R.id.vote_attender_recycler)
    RecyclerView recyclerView;
    private int vi;
    private VoteAttAdapter mAdapter;
    private int vote_type; //1投票 2.打分

    @Override
    protected int getActivityView() {
        return R.layout.activity_vote_attender;
    }

    @Override
    protected void initVariables() {
        super.initVariables();
        // 打分和投票
        if (getIntent() != null) {
            vi = getIntent().getIntExtra("vi", -1);
            vote_type = getIntent().getIntExtra("vote_type", -1);
        }
    }

    @Override
    protected void initView() {
        super.initView();

        if (vote_type == 1) { //1投票
            threeText.setText("投票选项");
            gradeText.setVisibility(View.GONE);
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemDivider());
        mAdapter = new VoteAttAdapter(R.layout.item_vote_attender_recycler);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        Params params = new Params();
        params.put("vi", vi);
        RequestUtils.createRequest().postVoteShowPeople(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<VoteShowPeopleBean>() {
            @Override
            public void onSuccess(VoteShowPeopleBean voteShowPeopleBean) {
                VoteShowPeopleBean.DataEntity data = voteShowPeopleBean.getData();
                if (data != null) {
                    handleData(data);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    private void handleData(VoteShowPeopleBean.DataEntity data) {
        List<VoteShowPeopleBean.DataEntity.UserinfoEntity> userinfo = data.getUserinfo();
        if (userinfo != null) {
            mAdapter.replaceData(userinfo);
        }
    }

    class VoteAttAdapter extends BaseQuickAdapter<VoteShowPeopleBean.DataEntity.UserinfoEntity, BaseViewHolder> {
        public VoteAttAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, VoteShowPeopleBean.DataEntity.UserinfoEntity item) {
            CircleImageView attImage = helper.getView(R.id.item_vote_att_img);
            TextView attImageName = helper.getView(R.id.item_vote_att_img_name);
            TextView attName = helper.getView(R.id.item_vote_att_name);
            TextView gradeUser = helper.getView(R.id.item_vote_att_grade_user);
            TextView grade = helper.getView(R.id.item_vote_att_grade);

            String img = item.getImg();
            String name = item.getName();
            String option_name = item.getOption_name();
            int score = item.getScore();

            ViewUtil.setColorItemView(img, name, attImageName, attImage);

            attName.setText(name);

            int is_hide = item.getIs_hide();
            boolean isHide = is_hide == 2;

            if (vote_type == 1) { //1投票
                gradeUser.setVisibility(View.GONE);
                if (isHide) {
                    grade.setText("**");
                } else {
                    grade.setText(option_name);
                }
            } else {
                gradeUser.setVisibility(View.VISIBLE);
                if (isHide) {
                    gradeUser.setText("**");
                    grade.setText("**分");
                } else {
                    gradeUser.setText(option_name);
                    grade.setText(score + "分");
                }
            }

        }
    }

}
