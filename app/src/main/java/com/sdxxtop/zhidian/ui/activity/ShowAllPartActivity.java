package com.sdxxtop.zhidian.ui.activity;

import android.widget.ListView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ShowContactPartAdapter;
import com.sdxxtop.zhidian.entity.ContactPartBean;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.widget.LinkManView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：17:24
 * 邮箱：15010104100@163.com
 * 描述：点击全公司之后回到此页面展示所有部门和不属于以上部门的人员
 */
public class ShowAllPartActivity extends BaseActivity implements ShowContactPartAdapter.OnClickObsever, LinkManView.TextSwitchObsever {

    @BindView(R.id.linkView)
    LinkManView linkView;
    @BindView(R.id.lvShow)
    ListView lvShow;

    private ShowContactPartAdapter showContactPartAdapter;
    private ContactPartBean.DataEntity dataBean = new ContactPartBean.DataEntity();

    @Override
    protected int getActivityView() {
        return R.layout.activity_search_allt;
    }

    @Override
    protected void initView() {
        linkView.setListener(this);
        showContactPartAdapter = new ShowContactPartAdapter(mContext, dataBean, this);
        lvShow.setAdapter(showContactPartAdapter);
    }

    @Override
    protected void initData() {
        postOrganize();
    }

    @Override
    public void onClickPartCallBack(int position) {
        postPartInfo(dataBean.getPart().get(position).getPart_id() + "");
    }

    @Override
    public void onClickUserCallBack(int position) {
        ContactDetailActivity.startContactDetailActivityTeacher(mContext, dataBean.getUserinfo().get(position).getUserid() + "");
    }


    @Override
    public void onTextSwitchCallback(int position) {
        if (position == 0) {
            postOrganize();
        } else {
            postPartInfo(dataBean.getNav().get(position - 1).getPart_id() + "");
        }
    }

    /**
     * 组织架构网络请求
     */
    private void postOrganize() {
        showProgressDialog("正在加载");
        Map<String, String> map = new HashMap<>();
        //PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID);
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        RequestUtils.getInstance().buildRequest().postContactOrganize(NetUtil.getBase64Data(map)).enqueue(new Callback<ContactPartBean>() {
            @Override
            public void onResponse(Call<ContactPartBean> call, Response<ContactPartBean> response) {
                closeProgressDialog();
                ContactPartBean organizeBean = response.body();
                if (organizeBean.getCode() != 200) {
                    ToastUtil.show(organizeBean.getMsg());
                    return;
                }
                dataBean.setPart(organizeBean.getData().getPart());
                dataBean.setUserinfo(organizeBean.getData().getUserinfo());

                showContactPartAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ContactPartBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }


    /**
     * 查看部门信息网络请求
     */
    private void postPartInfo(String partId) {
        showProgressDialog("正在加载");
        Map<String, String> map = new HashMap<>();
        //PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID)
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        map.put("pi", partId);
        RequestUtils.getInstance().buildRequest().postContactPart(NetUtil.getBase64Data(map)).enqueue(new Callback<ContactPartBean>() {
            @Override
            public void onResponse(Call<ContactPartBean> call, Response<ContactPartBean> response) {
                closeProgressDialog();
                ContactPartBean organizeBean = response.body();
                if (organizeBean.getCode() != 200) {
                    ToastUtil.show(organizeBean.getMsg());
                    return;
                }
                dataBean.setPart(organizeBean.getData().getPart());
                dataBean.setUserinfo(organizeBean.getData().getUserinfo());
                dataBean.setNav(organizeBean.getData().getNav());
                showContactPartAdapter.notifyDataSetChanged();
                if (organizeBean.getData().getNav().size() != 0) {
                    linkView.initView(organizeBean.getData().getNav());
                }
            }

            @Override
            public void onFailure(Call<ContactPartBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }

}

