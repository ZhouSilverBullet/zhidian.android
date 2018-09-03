package com.sdxxtop.zhidian.ui.fragment;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ContactRecyclerAdapter;
import com.sdxxtop.zhidian.entity.ContactIndexBean;
import com.sdxxtop.zhidian.eventbus.ChangeCompanyEvent;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.activity.ContactSearchActivity;
import com.sdxxtop.zhidian.ui.activity.InviteStaffActivity;
import com.sdxxtop.zhidian.ui.activity.ParentListActivity;
import com.sdxxtop.zhidian.ui.activity.ShowAllPartActivity;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 作者：CaiCM
 * 日期：2018/3/23  时间：16:39
 * 邮箱：15010104100@163.com
 * 描述：通讯录界面
 */
public class ContactFragment extends BaseFragment {

    @BindView(R.id.tv_all_comp)
    TextView tvAllComp;
    @BindView(R.id.rl_collection)
    RelativeLayout rlCollection;
    @BindView(R.id.tv_my_bumen)
    TextView tvMyBumen;
    @BindView(R.id.rl_mine)
    RelativeLayout rlMine;
    @BindView(R.id.rl_serch)
    RelativeLayout rlSerch;
    @BindView(R.id.rl_zhong)
    RelativeLayout rlZhong;
    Unbinder unbinder;
    @BindView(R.id.rl_xin)
    RelativeLayout rlXin;
    @BindView(R.id.rl_all_parent)
    RelativeLayout rlAllParent;

    @BindView(R.id.contact_collection_recycler)
    RecyclerView collectionRecycler;
    @BindView(R.id.contact_mine_recycler)
    RecyclerView mineRecycler;

    private SubTitleView contactTitle;
    private ContactRecyclerAdapter collectionAdapter;
    private ContactRecyclerAdapter mineAdapter;

    @Override
    public void onResume() {
        super.onResume();
        postContactIndex();
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initView() {
        statusBar(true);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        contactTitle = (SubTitleView) mRootView.findViewById(R.id.contact_title);
        topViewPadding(contactTitle);

        collectionAdapter = new ContactRecyclerAdapter(R.layout.item_contact_recycler);
        collectionAdapter.setType(ContactRecyclerAdapter.TYPE_COLLECT);
        mineAdapter = new ContactRecyclerAdapter(R.layout.item_contact_recycler);
        mineAdapter.setType(ContactRecyclerAdapter.TYPE_MINE);

        collectionRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        collectionRecycler.setNestedScrollingEnabled(false);
        collectionRecycler.setFocusable(false);
        collectionRecycler.addItemDecoration(new ItemDivider().setDividerLeftOffset(ViewUtil.dp2px(mContext,66)));
        collectionRecycler.setAdapter(collectionAdapter);

        mineRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mineRecycler.setNestedScrollingEnabled(false);
        mineRecycler.setFocusable(false);
        mineRecycler.addItemDecoration(new ItemDivider().setDividerLeftOffset(ViewUtil.dp2px(mContext,66)));
        mineRecycler.setAdapter(mineAdapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            statusBar(true);
            postContactIndex();
        }
    }

    @Override
    protected void initEvent() {
        contactTitle.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInvite = new Intent(mContext, InviteStaffActivity.class);
                startActivity(intentInvite);
            }
        });

        rlAllParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentListActivity.startParentListActivity(mContext);
            }
        });
    }


    @OnClick({R.id.rl_serch, R.id.rl_all_comp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_serch:
                Intent intentSearch = new Intent(mContext, ContactSearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.rl_all_comp:
                Intent intentAllPart = new Intent(mContext, ShowAllPartActivity.class);
                startActivity(intentAllPart);
                break;
        }
    }

    /**
     * 通讯录主页网络请求
     */
    private void postContactIndex() {
        Params params = new Params();
        RequestUtils.createRequest().postContactIndex(params.getData()).enqueue(new RequestCallback<ContactIndexBean>(new IRequestListener<ContactIndexBean>() {
            @Override
            public void onSuccess(ContactIndexBean bean) {
                ContactIndexBean.DataEntity data = bean.getData();
                if (data == null) {
                    return;
                }
                List<ContactIndexBean.DataEntity.ColloectUserEntity> colloectUser = data.getColloect_user();
                if (colloectUser != null) {
                    collectionAdapter.replaceData(colloectUser);
                }

                List<ContactIndexBean.DataEntity.ColloectUserEntity> partUser = data.getPart_user();
                if (partUser != null) {
                    if (data.getPart_name() != null) {
                        tvMyBumen.setText(data.getPart_name() + "（我的部门）");
                    }
                    mineAdapter.replaceData(partUser);
                }
                rlCollection.setVisibility(collectionAdapter.getData().size() == 0 ? View.GONE : View.VISIBLE);
                rlMine.setVisibility(mineAdapter.getData().size() == 0 ? View.GONE : View.VISIBLE);
                contactTitle.getRightText().setVisibility(data.getIs_invite() == 1 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    @Subscribe
    public void onRefreshConpanyChangeSuccess(ChangeCompanyEvent event) {
        initData();
    }
}
