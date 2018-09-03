package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.internal.LinkedTreeMap;
import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ISelector;
import com.sdxxtop.zhidian.adapter.ParentRecyclerAdapter;
import com.sdxxtop.zhidian.adapter.TeacherRecyclerAdapter;
import com.sdxxtop.zhidian.entity.ContactTeacherBean;
import com.sdxxtop.zhidian.entity.ParentListBean;
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.entity.TeacherBean;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.im.ConversationFragment;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.PartAndParentHelper;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.BottomSelectorView;
import com.sdxxtop.zhidian.widget.ContactNavigationView;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 1.所有接口找出来
 * 2.展示数据 （封装一个bean类，包含部门（老师），学生家长）
 * //学生家长
 * RequestUtils.createRequest().postContactParent(params.getData())
 * //部门和老师
 * RequestUtils.getInstance().buildRequest().postContactPart(NetUtil.getBase64Data(map))
 * <p>
 * 3.完成多选和单选
 * 4.完成全部选择
 */
public class PartAndParentActivity extends BaseActivity {

    public static final String SELECT_TYPE = "select_type"; //选择的类型
    public static final String SELECT_STATUS = "select_status"; //选择的状态

    public static final String SELECT_SET_DATA = "select_set_data"; //选择的类型后的数据


    @BindView(R.id.part_and_parent_title_view)
    SubTitleView mTitleView;
    @BindView(R.id.part_and_parent_navigation_view)
    ContactNavigationView mNavigationView;
    @BindView(R.id.part_and_parent_recycler)
    RecyclerView mParentRecyclerView;
    @BindView(R.id.part_and_parent_teacher_recycler)
    RecyclerView mTeacherRecycler;
    @BindView(R.id.part_and_parent_bottom_view)
    BottomSelectorView bottomSelectorView;
    @BindView(R.id.rl_all_linearLayout)
    LinearLayout allLinearLayout;
    @BindView(R.id.rl_all_company_layout)
    RelativeLayout allCompany;
    @BindView(R.id.view_all_parent_line)
    View allLineView;
    @BindView(R.id.rl_all_parent_layout)
    RelativeLayout allParent;

    @BindView(R.id.part_and_all_background)
    RelativeLayout allSelectRlayout;
    @BindView(R.id.cb_check_all)
    CheckBox checkAllBox;


    private int selectType;
    private int selectStatus;
    private ParentRecyclerAdapter mParentAdapter;
    private TeacherRecyclerAdapter mTeacherAdapter;
    //这个字段没什么用，产看跳过来的名字
    private String skipStringActivityName;
    //这个没办法，我不打算在这个界面加网络请求，但是只能加这里了
    //1 表示 GroupProfileActivity 的跳转过来
    private int activityType;
    private String identify;
    //这个是用于用户添加成群组的identify
    //是ProfileActivity过来的
    private String myIdentify;
    private String conversation;

    @Override
    protected int getActivityView() {
        return R.layout.activity_part_and_parent;
    }

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            selectType = getIntent().getIntExtra(SELECT_TYPE, -1);
            selectStatus = getIntent().getIntExtra(SELECT_STATUS, -1);

            skipStringActivityName = getIntent().getStringExtra("activity");
            activityType = getIntent().getIntExtra("activity_type", -1);
            identify = getIntent().getStringExtra("identify");
            myIdentify = getIntent().getStringExtra("myIdentify");

            conversation = getIntent().getStringExtra("conversation");

//            if (selectType == TYPE_MULTI_SELECT || selectType == TYPE_SINGLE_SELECT) {
//                bottomSelectorView.setVisibility(View.VISIBLE);
//            } else {
//                bottomSelectorView.setVisibility(View.GONE);
//            }
        }
    }

    @Override
    protected void initView() {
        bottomSelectorView.setVisibility(View.VISIBLE);

        setParentRecyclerAndAdapter();
        setTeacherRecyclerAndAdapter();

        //默认有个全部的值
        mNavigationView.clearAll();
    }

    private void setTeacherRecyclerAndAdapter() {
        mTeacherAdapter = new TeacherRecyclerAdapter(R.layout.item_teacher_recycler);
        mTeacherRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mTeacherRecycler.addItemDecoration(new ItemDivider().setDividerLeftOffset(ViewUtil.dp2px(mContext, 66)));
        mTeacherRecycler.setAdapter(mTeacherAdapter);

        mTeacherAdapter.setBottomSelectorView(bottomSelectorView);
    }

    private void setParentRecyclerAndAdapter() {
        mParentAdapter = new ParentRecyclerAdapter(R.layout.item_parent_recycler);
//        View view = LayoutInflater.from(mContext).inflate(R.layout.view_empty, null);
//        mAdapter.setEmptyView(view);

        mParentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mParentRecyclerView.addItemDecoration(new ItemDivider().setDividerLeftOffset(ViewUtil.dp2px(mContext, 66)));
        mParentRecyclerView.setAdapter(mParentAdapter);

        mParentAdapter.setBottomSelectorView(bottomSelectorView);
        mParentAdapter.setSelectType(selectType);
        mParentAdapter.setSelectStatus(selectStatus);
    }

    @Override
    protected void initEvent() {
        mTitleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mParentAdapter.setSelectorClickListener(new ParentRecyclerAdapter.SelectorClickListener() {
            @Override
            public void onSelectorClick(int position) {
                if (mParentAdapter != null) {
                    ParentListBean.DataBeanX.DataBean dataBean = mParentAdapter.getData().get(position);
                    if (dataBean.isPart()) {
                        loadParentData(mNavigationView.getCount() /*+ 1*/, dataBean.getType_id() + "");
                    } else {
                        ContactDetailActivity.startContactDetailActivityParent(mContext,
                                dataBean.getUserid() + "", dataBean.getStudent_id() + "");
                    }
                }
            }
        });

        mParentAdapter.setCheckClickListener(new ISelector.CheckListener() {
            @Override
            public void onCheck() {
                boolean isCheck = mParentAdapter.iteratorAllValue();
                checkAllBox.setChecked(isCheck);
            }
        });

        mTeacherAdapter.setTeacherClickListener(new TeacherRecyclerAdapter.TeacherClickListener() {
            @Override
            public void onClick(TeacherBean item, boolean isPart) {
                if (isPart) {
                    loadTeacherData(item.getPart_id());
                } else {
                    ContactDetailActivity.startContactDetailActivityTeacher(mContext, item.getUserid() + "");
                }
            }
        });

        mTeacherAdapter.setCheckClickListener(new ISelector.CheckListener() {
            @Override
            public void onCheck() {
                boolean isCheck = mTeacherAdapter.iteratorAllValue();
                checkAllBox.setChecked(isCheck);
            }
        });

        mNavigationView.setNavigationItemClickListener(new ContactNavigationView.NavigationItemClickListener() {
            @Override
            public void click(ParentListBean.DataBeanX.NavBean bean) {

                //家长
                int type = bean.getType();
                //全部
                if (type == -100) {
                    allSelectRlayout.setVisibility(View.GONE);

                    allLinearLayout.setVisibility(View.VISIBLE);
                    mParentAdapter.clearAll();
                    mTeacherAdapter.clearAll();
                    mNavigationView.clearAll();
                } else {
                    allLinearLayout.setVisibility(View.GONE);
                    if (bean.isTeacher()) {
                        if (type == -101) {
                            loadTeacherData(0);
                        } else {
                            loadTeacherData(bean.getPart_id());
                        }
                    } else {
                        loadParentData(type, bean.getType_id() + "");
                    }
                }
            }
        });

        bottomSelectorView.getSubmitBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        allCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allLinearLayout.setVisibility(View.GONE);
                loadTeacherData(0);
                mTeacherRecycler.setVisibility(View.VISIBLE);
                mParentRecyclerView.setVisibility(View.GONE);
            }
        });

        allParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //家长
                allLinearLayout.setVisibility(View.GONE);
                loadParentData(1, AppSession.getInstance().getCompanyId());

                mTeacherRecycler.setVisibility(View.GONE);
                mParentRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        allSelectRlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int partVisibility = mParentRecyclerView.getVisibility();
                int teacherVisibility = mTeacherRecycler.getVisibility();
                if (partVisibility == View.VISIBLE) {
                    parentCheckAll();
                } else if (teacherVisibility == View.VISIBLE) {
                    teacherCheckAll();
                }
            }
        });
    }

    private void parentCheckAll() {
        boolean checked = checkAllBox.isChecked();
        boolean toCheck = !checked;

        mParentAdapter.checkAll(toCheck);

        checkAllBox.setChecked(toCheck);
    }

    private void teacherCheckAll() {
        boolean checked = checkAllBox.isChecked();
        boolean toCheck = !checked;

        mTeacherAdapter.checkAll(toCheck);

        checkAllBox.setChecked(toCheck);
    }

    public static final String TEACHER_CACHE_SET = "teacher_cache_set";
    public static final String TEACHER_USER_SET = "teacher_user_set";
    public static final String TEACHER_PART_SET = "teacher_part_set";

    public static final String PARENT_CACHE_SET = "parent_cache_set";
    public static final String PARENT_PARENT_MAP = "parent_parent_map";
    public static final String PARENT_TYPE_SET = "parent_type_set";

    public static final int CUSTOMER_RESULT_OK = 120;


    private void submit() {
        List<SelectBean> list = bottomSelectorView.getList();
        if (list == null || list.size() == 0) {
//            showToast("请选择后再操作");
            finish();
            return;
        }

        final Intent intent = new Intent();
        intent.putExtra(TEACHER_CACHE_SET, mTeacherAdapter.getCacheSet());
        intent.putExtra(TEACHER_USER_SET, mTeacherAdapter.getUserSet());
        intent.putExtra(TEACHER_PART_SET, mTeacherAdapter.getPartSet());
        intent.putExtra(PARENT_CACHE_SET, mParentAdapter.getCacheSet());
        intent.putExtra(PARENT_PARENT_MAP, mParentAdapter.getParentMap());
        intent.putExtra(PARENT_TYPE_SET, mParentAdapter.getTypeSet());

        if (TextUtils.isEmpty(identify) && TextUtils.isEmpty(conversation)) {
            setResult(PartAndParentActivity.CUSTOMER_RESULT_OK, intent);
            finish();
        } else {
            showProgressDialog("");
            PartAndParentHelper.toAddPeople(identify, myIdentify, mTeacherAdapter.getUserSet(), mTeacherAdapter.getPartSet(),
                    mParentAdapter.getTypeSet(), mParentAdapter.getParentMap(), new IRequestListener<BaseModel>() {
                        @Override
                        public void onSuccess(BaseModel baseModel) {
                            String groupId = "";

                            Object data = baseModel.getData();
                            if (data != null) {
                                groupId = (String) ((LinkedTreeMap) data).get("group_id");
                            }

                            ConversationFragment.group_id = groupId;
                            intent.putExtra("group_id", groupId);
                            setResult(PartAndParentActivity.CUSTOMER_RESULT_OK, intent);
                            closeProgressDialog();
                            finish();
                        }

                        @Override
                        public void onFailure(int code, String errorMsg) {
                            ToastUtil.show(errorMsg);
                            closeProgressDialog();
                        }
                    });
        }
    }

    /**
     * @param type   层级关系
     * @param typeId 查询类型对应的id比
     */
    private void loadParentData(final int type, final String typeId) {
        Params params = new Params();
        params.put("ti", typeId);
        params.put("tp", type);
        showProgressDialog("");
        RequestUtils.createRequest().postContactParent(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<ParentListBean>() {
            @Override
            public void onSuccess(ParentListBean parentListBean) {
                ParentListBean.DataBeanX dataX = parentListBean.getData();
                if (dataX != null) {
                    List<ParentListBean.DataBeanX.NavBean> navBeanList = dataX.getNav();
                    List<ParentListBean.DataBeanX.DataBean> data = dataX.getData();
                    if (navBeanList != null) {
                        setNaviView(navBeanList);
                        if (navBeanList.size() == 4) {
                            ParentListBean.DataBeanX.NavBean navBean = navBeanList.get(3);
                            String name = navBean.getName();
                            for (ParentListBean.DataBeanX.DataBean datum : data) {
                                datum.setGradeValue(name + datum.getName());
                            }
                        }
                    }

                    if (data != null) {
                        mParentAdapter.setNavigationViewList(mNavigationView.getAdapterData());
                        mParentAdapter.replaceData(data);
                    }
                }

                //每次回来的时候遍历一下
                boolean isCheck = mParentAdapter.iteratorAllValue();
                checkAllBox.setChecked(isCheck);

                closeProgressDialog();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
                showToast(errorMsg);
            }
        }));
    }

    private void loadTeacherData(int partId) {
        Params params = new Params();
        String path = "";
        final boolean first = partId == 0;
        if (first) {
            path = "organize";
        } else {
            path = "part";
            params.put("pi", partId);
        }
        showProgressDialog("");
        RequestUtils.createRequest().postContactTeacher(path, params.getData()).enqueue(new RequestCallback<>(new IRequestListener<ContactTeacherBean>() {
            @Override
            public void onSuccess(ContactTeacherBean contactTeacherBean) {
                closeProgressDialog();
                ContactTeacherBean.DataEntity data = contactTeacherBean.getData();
                if (data != null) {
                    handleTeacherData(data, first);
                }

                //每次回来的时候遍历一下
                boolean isCheck = mTeacherAdapter.iteratorAllValue();
                checkAllBox.setChecked(isCheck);
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
                showToast(errorMsg);
            }
        }));
    }

    private void handleTeacherData(ContactTeacherBean.DataEntity data, boolean first) {
        List<ParentListBean.DataBeanX.NavBean> navList = data.getNav();
        if (navList != null) {
            for (ParentListBean.DataBeanX.NavBean navBean : navList) {
                navBean.setTeacher(true);
            }
            ParentListBean.DataBeanX.NavBean navBean = new ParentListBean.DataBeanX.NavBean();
            navBean.setTeacher(true);
            navBean.setType(-101);
            navBean.setPart_name("全公司");
            navList.add(0, navBean);
            setNaviView(navList);
        } else {
            if (first) {
                navList = new ArrayList<>();
                ParentListBean.DataBeanX.NavBean navBean = new ParentListBean.DataBeanX.NavBean();
                navBean.setTeacher(true);
                navBean.setType(-101);
                navBean.setPart_name("全公司");
                navList.add(0, navBean);
                setNaviView(navList);
            }
        }

        mTeacherAdapter.replaceData(data);
    }

    //设置naviView
    private void setNaviView(List<ParentListBean.DataBeanX.NavBean> navBeanList) {
        if (navBeanList != null) {
            ParentListBean.DataBeanX.NavBean navBean = new ParentListBean.DataBeanX.NavBean();
            navBean.setName("全部");
            navBean.setType(-100);
            navBeanList.add(0, navBean);
            mNavigationView.replaceData(navBeanList);
            if (mNavigationView.getCount() >= 2) {
                allSelectRlayout.setVisibility(View.VISIBLE);
            } else {
                allSelectRlayout.setVisibility(View.GONE);
            }
        }
    }
}
