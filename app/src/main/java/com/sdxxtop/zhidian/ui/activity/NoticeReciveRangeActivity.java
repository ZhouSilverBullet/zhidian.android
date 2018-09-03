package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ShowReciveRangeAdapter;
import com.sdxxtop.zhidian.entity.ContactPartBean;
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.eventbus.ChangeCompanyEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.widget.BottomSelectorView;
import com.sdxxtop.zhidian.widget.LinkManView;
import com.sdxxtop.zhidian.widget.SubTitleView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zhangphil.iosdialog.widget.AlertDialog;

public class NoticeReciveRangeActivity extends BaseActivity implements ShowReciveRangeAdapter.OnClickObsever, LinkManView.TextSwitchObsever {
    String[] partColors = {"#3296fa", "#9396fa", "#9d81ff", "#81a4f9", "#d9d6ec", "#4e76e4", "#ad8ea3"};
    public static final String NOTICE_TITLE = "notice_title";  //头的名字
    //用户自己不进行选中
    public static final String SELF_NOT_SELECT = "self_not_select";

    //排班里面涉及的修改人员的地方使用到，要进行人员的验证
    //在选择班中，选中部门就不让进去选部门人
    public static final String PART_SELECT_NOT_IN = "partSelectNotIn";
    public static final int PART_SELECTOR_NOT_CLICK = 5;

    public static final int PART_SELECTOR = 2;  //可以选中
    public static final int PART_NOT_SELECTOR = 3;  //不进行选中
    public static final int SINGLE_SELECTOR = 4;  //当选


    public static final String NOTICE_TYPE = "notice_type";  //
    public static final int NOTICE_TYPE_CHECK_SAFE = 100;  // checkSafeActivity


    public static final int NOTICE_RESULT_OK = 200;  //结束回调


    @BindView(R.id.linkView)
    LinkManView linkView;
    @BindView(R.id.cb_check_all)
    TextView cbCheckAll;
    @BindView(R.id.cb_check_all_value)
    TextView checkAllValue;
    @BindView(R.id.cb_check_all_background)
    RelativeLayout checkLayout;
    @BindView(R.id.lvShow)
    ListView lvShow;
    //    @BindView(R.id.btn_make_sure)
//    Button btnMakeSure;
    @BindView(R.id.notice_recive_title_view)
    SubTitleView titleView;
    //    @BindView(R.id.notice_recive_recycler)
//    RecyclerView recyclerView;
    @BindView(R.id.bottom_select_view)
    BottomSelectorView bottomSelectorView;

    private HashMap<String, Boolean> clickAllMap = new HashMap<>();

    private ShowReciveRangeAdapter adapter;
    private ContactPartBean.DataEntity dataBean = new ContactPartBean.DataEntity();
    private int partIntId;
    //    private NoticeRecyclerAdapter recyclerAdapter;
    private int isPartSelect;  //部门是否可以选中
    private int singleSelect;  //单选
    private int partSelectNotIn; //part的select不能被点击
    private ContactPartBean.DataEntity schedulingDataEntity;

    //SchedulingManageActivity
    private int rule_id;

    private String noticeTitle; //获取的title
    private int noticeType;
    private boolean selfNotSelect;

    @Override
    protected int getActivityView() {
        return R.layout.activity_notice_recive_range;
    }

    @Override
    protected void initView() {
        linkView.setListener(this);
        titleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!TextUtils.isEmpty(noticeTitle)) {
            titleView.setTitleValue(noticeTitle);
        }

        adapter = new ShowReciveRangeAdapter(mContext, dataBean, this);
        //部门不被进行选中
        adapter.setIsPartSelect(isPartSelect);
        adapter.setPartSelectNotIn(partSelectNotIn);
        lvShow.setAdapter(adapter);
        adapter.setSingSelector(isOpenSingle());

        //SchedulingManageActivity 过来的，是否已经选中了值
        if (schedulingDataEntity != null) {
            List<ContactPartBean.DataEntity.PartEntity> part = schedulingDataEntity.getPart();
            List<ContactPartBean.DataEntity.UserinfoEntity> userinfo = schedulingDataEntity.getUserinfo();
            if (part != null) {
                for (int i = 0; i < part.size(); i++) {
                    ContactPartBean.DataEntity.PartEntity partEntity = part.get(i);
                    int partId = partEntity.getPart_id();
                    adapter.selectPartSet.add(partId);
                    adapter.selectPartMap.put(partEntity.getPart_id(), SelectBean.createSelectBean(SelectBean.TYPE_PART,
                            (Integer) partEntity.getPart_id(), partEntity.getPart_name(), partColors[i % 7]));
                }
            }

            if (userinfo != null) {
                for (int i = 0; i < userinfo.size(); i++) {
                    ContactPartBean.DataEntity.UserinfoEntity userinfoEntity = userinfo.get(i);
                    adapter.selectUserSet.add(userinfoEntity.getUserid());
                    adapter.selectUserMap.put(userinfoEntity.getUserid(), SelectBean.createSelectBean(SelectBean.TYPE_USER,
                            (Integer) userinfoEntity.getUserid(), userinfoEntity.getName(), userinfoEntity.getImg()));
                }
            }

            List<SelectBean> selectData = adapter.getSelectData();
            bottomRefreshData(selectData);
        }

        if (isOpenSingle()) {
            checkLayout.setVisibility(View.GONE);
            cbCheckAll.setVisibility(View.GONE);
            checkAllValue.setVisibility(View.GONE);
        }
    }

    private void bottomRefreshData(List<SelectBean> selectData) {
        bottomSelectorView.refreshData(selectData);
    }

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            noticeTitle = getIntent().getStringExtra(NOTICE_TITLE);
            noticeType = getIntent().getIntExtra(NOTICE_TYPE, -1);
            isPartSelect = getIntent().getIntExtra("isPartSelect", -1);
            singleSelect = getIntent().getIntExtra("singleSelect", -1);
            //排班的时候使用到
            partSelectNotIn = getIntent().getIntExtra(PART_SELECT_NOT_IN, -1);
            if (partSelectNotIn == PART_SELECTOR_NOT_CLICK) {
                schedulingDataEntity = (ContactPartBean.DataEntity) getIntent().getSerializableExtra("DataEntity");
                rule_id = getIntent().getIntExtra("rule_id", -1);
            }

            selfNotSelect = getIntent().getBooleanExtra(SELF_NOT_SELECT, false);
        }
    }

    //是否开启单选模式
    protected boolean isOpenSingle() {
        if (singleSelect == SINGLE_SELECTOR) {
            return true;
        }
        return false;
    }

    @Override
    protected void initData() {
        postOrganize("all");
    }

    @Override
    protected void initEvent() {

        checkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll();
            }
        });

        adapter.setSelectAllListener(new ShowReciveRangeAdapter.SelectAllListener() {
            @Override
            public void selectAll() {
                List<SelectBean> selectData = adapter.getSelectData();
                bottomRefreshData(selectData);
                List<ContactPartBean.DataEntity.UserinfoEntity> userinfo = adapter.getDataBean().getUserinfo();
                List<ContactPartBean.DataEntity.PartEntity> part = adapter.getDataBean().getPart();
                if (isPartSelect != PART_NOT_SELECTOR) { //部门是否选中
                    for (ContactPartBean.DataEntity.PartEntity partEntity : part) {
                        if (!partEntity.isCheck()) {
                            setChecked(partId, false);
                            return;
                        }
                    }
                }

                for (ContactPartBean.DataEntity.UserinfoEntity userinfoEntity : userinfo) {
                    if (!userinfoEntity.isCheck()) {
                        setChecked(partId, false);
                        return;
                    }
                }
                setChecked(partId, true);
            }
        });

        bottomSelectorView.getSubmitBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        bottomSelectorView.setBottomRemoveClickListener(new BottomSelectorView.BottomRemoveClickListener() {
            @Override
            public void onRemoveClick(int type, Integer id) {
                if (type == SelectBean.TYPE_PART) {
                    adapter.selectPartMap.remove(id);
                    adapter.selectPartSet.remove((Integer) id);
                } else if (type == SelectBean.TYPE_USER) {
                    adapter.selectUserMap.remove(id);
                    adapter.selectUserSet.remove((Integer) id);
                }
                adapter.notifyDataSetChanged();
                List<SelectBean> selectData = adapter.getSelectData();
                bottomRefreshData(selectData);
            }
        });
    }

    private void submit() {
        if (noticeType == NOTICE_TYPE_CHECK_SAFE) {
            checkSafe();
        } else if (partSelectNotIn == PART_SELECTOR_NOT_CLICK) {
            checkRepeat();
        } else {
            Intent intent = new Intent();
            intent.putExtra("range", "中心校");
            intent.putExtra("partList", adapter.selectPartSet);
            intent.putExtra("userList", adapter.selectUserSet);
            intent.putExtra("selectData", (ArrayList<SelectBean>) adapter.getSelectData());
            setResult(NOTICE_RESULT_OK, intent);
            finish();
        }
    }

    private void checkAll() {
        Boolean isChecked = clickAllMap.get(partId);
        clickAllMap.put(partId, !isChecked);
        List<ContactPartBean.DataEntity.PartEntity> part = adapter.getDataBean().getPart();
        List<ContactPartBean.DataEntity.UserinfoEntity> userinfo = adapter.getDataBean().getUserinfo();
        if (!isChecked) {
            cbCheckAll.setBackgroundResource(R.drawable.selected);
            if (isPartSelect != PART_NOT_SELECTOR) { //部门是否选中
                for (int i = 0; i < part.size(); i++) {
                    part.get(i).setCheck(true);
                    adapter.selectPartSet.add((Integer) part.get(i).getPart_id());
                    adapter.selectPartMap.put(part.get(i).getPart_id(), SelectBean.createSelectBean(SelectBean.TYPE_PART,
                            (Integer) part.get(i).getPart_id(), part.get(i).getPart_name(), partColors[i % 7]));
                }
            }


            for (int j = 0; j < userinfo.size(); j++) {
                ContactPartBean.DataEntity.UserinfoEntity userinfoEntity = userinfo.get(j);
                userinfoEntity.setCheck(true);
                adapter.selectUserSet.add((Integer) userinfoEntity.getUserid());
                adapter.selectUserMap.put(userinfoEntity.getUserid(), SelectBean.createSelectBean(SelectBean.TYPE_USER,
                        (Integer) userinfoEntity.getUserid(), userinfoEntity.getName(), userinfoEntity.getImg()));
            }

            adapter.notifyDataSetChanged();

            List<SelectBean> selectData = adapter.getSelectData();
            bottomRefreshData(selectData);
        } else {
            cbCheckAll.setBackgroundResource(R.drawable.unselected);
            if (isPartSelect != PART_NOT_SELECTOR) {
                for (int i = 0; i < part.size(); i++) {
                    part.get(i).setCheck(false);
                    adapter.selectPartSet.remove((Integer) part.get(i).getPart_id());
                    adapter.selectPartMap.remove((Integer) part.get(i).getPart_id());
                }
            }

            for (int j = 0; j < userinfo.size(); j++) {
                ContactPartBean.DataEntity.UserinfoEntity userinfoEntity = userinfo.get(j);
                userinfo.get(j).setCheck(false);
                adapter.selectUserSet.remove((Integer) userinfo.get(j).getUserid());
                adapter.selectUserMap.remove((Integer) userinfo.get(j).getUserid());
            }

            adapter.notifyDataSetChanged();

            List<SelectBean> selectData = adapter.getSelectData();
            bottomRefreshData(selectData);
        }
    }

    //移交管理员方法
    private void checkSafe() {
        String userValue = getUserValue();
        if (TextUtils.isEmpty(userValue)) {
            showToast("请选择人员");
            return;
        }
        Params params = new Params();
        params.put("ti", getUserValue());
        RequestUtils.createRequest().postCompanyTransfer(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                showToast(baseModel.msg);
                EventBus.getDefault().post(new ChangeCompanyEvent());
                setResult(200);
                finish();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    @Override
    public void onClickPartCallBack(int position) {
        partIntId = adapter.getDataBean().getPart().get(position).getPart_id();
        boolean check = adapter.getDataBean().getPart().get(position).isCheck();
        adapter.selectPartSet.remove((Integer) partIntId);
        postPartInfo(partIntId + "", check);
    }


    @Override
    public void onClickUserCallBack(int position) {

    }

    @Override
    public void onTextSwitchCallback(int position) {
        if (position == 0) {
            postOrganize("all");
        } else {
            postPartInfo(dataBean.getNav().get(position - 1).getPart_id() + "", false);
        }
    }

    /**
     * 组织架构网络请求
     */
    private void postOrganize(final String partId) {
        this.partId = partId;
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

                ContactPartBean.DataEntity data = organizeBean.getData();

                removeSelfData(data);

                if (clickAllMap.containsKey(partId)) {  //如果有个数就全部选中
                    Boolean aBoolean = clickAllMap.get(partId);
                    setChecked(partId, aBoolean);
                    if (aBoolean) {
                        cycle(data, partIntId, true);
                    }
                } else {
                    setChecked(partId, false);
                }
                dataBean.setPart(data.getPart());
                dataBean.setUserinfo(data.getUserinfo());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ContactPartBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }

    private void removeSelfData(ContactPartBean.DataEntity data) {
        //自己不进行选中，也就是剔除自己出去
        if (selfNotSelect && data.getUserinfo() != null) {
            for (ContactPartBean.DataEntity.UserinfoEntity userinfoEntity : data.getUserinfo()) {
                int userid = userinfoEntity.getUserid();
                String userId = AppSession.getInstance().getUserId();
                if (!TextUtils.isEmpty(userId) && userid == Integer.parseInt(userId)) {
                    data.getUserinfo().remove(userinfoEntity);
                    break;
                }
            }
        }
    }

    private void cycle(ContactPartBean.DataEntity dataBean, int partIntId, boolean isCheck) {
        if (dataBean == null) {
            return;
        }

        if (isPartSelect != PART_NOT_SELECTOR) { //部门是否选中
            for (int i = 0; i < dataBean.getPart().size(); i++) {
                ContactPartBean.DataEntity.PartEntity partEntity = dataBean.getPart().get(i);
                int partId = partEntity.getPart_id();
                if (partIntId == partId) {
                    partEntity.setCheck(false);
                    adapter.selectPartSet.remove((Integer) partIntId);
                    partIntId = -1;
                    setChecked("all", false);
                } else {
                    adapter.selectPartSet.add((Integer) partId);
                    partEntity.setCheck(isCheck);
                }
            }
        }


        for (int i = 0; i < dataBean.getUserinfo().size(); i++) {
            ContactPartBean.DataEntity.UserinfoEntity userinfoEntity = dataBean.getUserinfo().get(i);
            adapter.selectUserSet.add((Integer) userinfoEntity.getUserid());
            userinfoEntity.setCheck(isCheck);
        }
    }

    private void setChecked(String partId, boolean isCheck) {
        clickAllMap.put(partId, isCheck);
        if (isCheck) {
            cbCheckAll.setBackgroundResource(R.drawable.selected);
        } else {
            cbCheckAll.setBackgroundResource(R.drawable.unselected);
        }
    }

    String partId = "all";

    /**
     * 查看部门信息网络请求
     */
    private void postPartInfo(final String partId, final boolean isSelctor) {
        this.partId = partId;
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

                ContactPartBean.DataEntity data = organizeBean.getData();

                removeSelfData(data);

                if (isSelctor) {
                    setChecked(partId, true);
                    cycle(data, -1, true);
                } else {
                    if (clickAllMap.containsKey(partId)) {
                        Boolean aBoolean = clickAllMap.get(partId);
                        setChecked(partId, aBoolean);
                        if (aBoolean) {
                            cycle(data, partIntId, true);
                        }
                    } else {
                        setChecked(partId, false);
                    }
                }

                dataBean.setPart(data.getPart());
                dataBean.setUserinfo(data.getUserinfo());
                dataBean.setNav(data.getNav());
                adapter.notifyDataSetChanged();
                if (data.getNav().size() != 0) {
                    linkView.initView(data.getNav());
                }
            }

            @Override
            public void onFailure(Call<ContactPartBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }

    private void checkRepeat() {
        Params params = new Params();
        params.put("ui", getUserValue());
        params.put("pi", getPartValue());
        RequestUtils.createRequest().postCheckRepeat(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                if (rule_id != -1) {
                    postModify();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("range", "中心校");
                intent.putExtra("partList", adapter.selectPartSet);
                intent.putExtra("userList", adapter.selectUserSet);
                intent.putExtra("selectData", (ArrayList<SelectBean>) adapter.getSelectData());
                setResult(NOTICE_RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                if (code == 372) {
                    if (!TextUtils.isEmpty(errorMsg)) {
                        errorMsg = errorMsg.split("\\(")[0];
                    }
                    new AlertDialog(NoticeReciveRangeActivity.this)
                            .builder()
                            .setTitle("提示")
                            .setMsg(errorMsg)
                            .setPositiveButton("", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (rule_id != -1) {
                                        postModify();
                                        return;
                                    }
                                    Intent intent = new Intent();
                                    intent.putExtra("range", "中心校");
                                    intent.putExtra("partList", adapter.selectPartSet);
                                    intent.putExtra("userList", adapter.selectUserSet);
                                    intent.putExtra("selectData", (ArrayList<SelectBean>) adapter.getSelectData());
                                    setResult(NOTICE_RESULT_OK, intent);
                                    finish();
                                }
                            }).setNegativeButton("", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                } else {
                    showToast(errorMsg);
                }
            }
        }));
    }

    private void postModify() {
        Params params = new Params();
        params.put("ri", rule_id);
        params.put("ui", getUserValue());
        params.put("pi", getPartValue());
        params.put("tp", 1);
        RequestUtils.createRequest().postClassesModify(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                showToast(baseModel.msg);
                setResult(200);
                finish();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));

    }

    private String getUserValue() {
        String value = "";
        if (adapter.selectUserSet == null) {
            return value;
        }
        for (Integer integer : adapter.selectUserSet) {
            value = value + integer + ",";
        }
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    private String getPartValue() {
        String value = "";
        if (adapter.selectPartSet == null) {
            return value;
        }
        for (Integer integer : adapter.selectPartSet) {
            value = value + integer + ",";
        }
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

}
