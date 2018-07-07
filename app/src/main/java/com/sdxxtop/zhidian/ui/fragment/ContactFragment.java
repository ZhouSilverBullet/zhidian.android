package com.sdxxtop.zhidian.ui.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ShowCollectionUserAdapter;
import com.sdxxtop.zhidian.adapter.ShowMyUserAdapter;
import com.sdxxtop.zhidian.entity.ContactIndexBean;
import com.sdxxtop.zhidian.eventbus.ChangeCompanyEvent;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.activity.ContactDetailActivity;
import com.sdxxtop.zhidian.ui.activity.ContactSearchActivity;
import com.sdxxtop.zhidian.ui.activity.InviteStaffActivity;
import com.sdxxtop.zhidian.ui.activity.ShowAllPartActivity;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.widget.MyListView;
import com.sdxxtop.zhidian.widget.SubTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：CaiCM
 * 日期：2018/3/23  时间：16:39
 * 邮箱：15010104100@163.com
 * 描述：通讯录界面
 */
public class ContactFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.tv_all_comp)
    TextView tvAllComp;
    @BindView(R.id.lv_collection)
    MyListView lvCollection;
    @BindView(R.id.rl_collection)
    RelativeLayout rlCollection;
    @BindView(R.id.tv_my_bumen)
    TextView tvMyBumen;
    @BindView(R.id.lv_mine)
    MyListView lvMine;
    @BindView(R.id.rl_mine)
    RelativeLayout rlMine;
    @BindView(R.id.rl_serch)
    RelativeLayout rlSerch;
    @BindView(R.id.rl_zhong)
    RelativeLayout rlZhong;
    Unbinder unbinder;
    @BindView(R.id.rl_xin)
    RelativeLayout rlXin;

    private ShowCollectionUserAdapter collectionUserAdapter;
    private ShowMyUserAdapter myUserAdapter;

    private List<ContactIndexBean.DataEntity.ColloectUserEntity> listColl = new ArrayList<>();
    private List<ContactIndexBean.DataEntity.ColloectUserEntity> listClub = new ArrayList<>();


    private SubTitleView contactTitle;

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

        myUserAdapter = new ShowMyUserAdapter(getActivity(), listClub);
        lvMine.setAdapter(myUserAdapter);
        collectionUserAdapter = new ShowCollectionUserAdapter(getActivity(), listColl);
        lvCollection.setAdapter(collectionUserAdapter);
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
        lvCollection.setOnItemClickListener(this);
        lvMine.setOnItemClickListener(this);

        contactTitle.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInvite = new Intent(mContext, InviteStaffActivity.class);
                startActivity(intentInvite);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, ContactDetailActivity.class);
        switch (parent.getId()) {
            case R.id.lv_collection:
                intent.putExtra("userId", listColl.get(position).getUserid() + "");
                startActivity(intent);
                break;
            case R.id.lv_mine:
                intent.putExtra("userId", listClub.get(position).getUserid() + "");
                startActivity(intent);
                break;
        }
    }


    /**
     * 通讯录主页网络请求
     */
    private void postContactIndex() {
        Map<String, String> map = new HashMap<>();
        //1000035
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        String base64Data = NetUtil.getBase64Data(map);
        LogUtils.e("log", "base64encode:" + base64Data);
        //showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postContactIndex(base64Data).enqueue(new Callback<ContactIndexBean>() {
            @Override
            public void onResponse(Call<ContactIndexBean> call, Response<ContactIndexBean> response) {
                ContactIndexBean bean = response.body();
                // TODO: 2018/3/22 后期RxJava 链式处理数据
                if (bean.getCode() != 200) {
                    ToastUtil.show(response.body().getMsg());
                    return;
                }
                if (bean.getData().getColloect_user() != null) {
                    listColl.clear();
                    listColl.addAll(bean.getData().getColloect_user());
                    collectionUserAdapter.notifyDataSetChanged();
                }

                if (bean.getData().getPart_user() != null) {
                    if (bean.getData().getPart_name() != null) {
                        tvMyBumen.setText(bean.getData().getPart_name() + "（我的部门）");
                    }
                    listClub.clear();
                    listClub.addAll(bean.getData().getPart_user());
                    myUserAdapter.notifyDataSetChanged();
                }
                rlCollection.setVisibility(collectionUserAdapter.getCount() == 0 ? View.GONE : View.VISIBLE);
                rlMine.setVisibility(myUserAdapter.getCount() == 0 ? View.GONE : View.VISIBLE);
                contactTitle.getRightText().setVisibility(bean.getData().getIs_invite() == 1 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onFailure(Call<ContactIndexBean> call, Throwable t) {
                ToastUtil.show("网络错误");
            }
        });
    }

    @Subscribe
    public void onRefreshConpanyChangeSuccess(ChangeCompanyEvent event) {
        initData();
    }
}
