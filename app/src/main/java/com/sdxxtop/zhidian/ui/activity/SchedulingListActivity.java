package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ShowClassBean;
import com.sdxxtop.zhidian.eventbus.SchedulingFinishEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.widget.SubTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 班次列表
 */
public class SchedulingListActivity extends BaseActivity {

    @BindView(R.id.scheduling_list_view)
    SubTitleView titleView;
    @BindView(R.id.scheduling_list_btn)
    Button listBtn;
    @BindView(R.id.scheduling_list_recycler)
    RecyclerView recyclerView;
    private SchedulingAdapter mAdapter;
    private HashMap<String, Boolean> map = new HashMap<>();
    private String partValue;
    private String userValue;
    private String editValue;
    private int rule_id;

    @Override
    protected int getActivityView() {
        return R.layout.activity_scheduling_list;
    }

    @Override
    protected void initVariables() {
        super.initVariables();
        if (getIntent() != null) {
            partValue = getIntent().getStringExtra("part_value");
            userValue = getIntent().getStringExtra("user_value");
            editValue = getIntent().getStringExtra("editValue");

            rule_id = getIntent().getIntExtra("rule_id", -1);
        }
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        if (rule_id != -1) {
            listBtn.setText("完成");
        }

        map.put("check", false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemDivider());
        mAdapter = new SchedulingAdapter(R.layout.view_text_and_text);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        Params params = new Params();
        if (rule_id != -1) {
            params.put("ri", rule_id);
        }
        RequestUtils.createRequest().postShowClass(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<ShowClassBean>() {
            @Override
            public void onSuccess(ShowClassBean showClassBean) {
                ShowClassBean.DataBean data = showClassBean.getData();
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

    @Override
    protected void initEvent() {
        super.initEvent();
        titleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SchedulingNewAddActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowClassBean.DataBean.ClassBean bean = null;
                for (ShowClassBean.DataBean.ClassBean classBean : mAdapter.getData()) {
                    if (classBean.isCheck()) {
                        bean = classBean;
                        break;
                    }
                }

                if (bean == null) {
                    showToast("请选择班次");
                    return;
                }

                if (rule_id != -1) {
                    classesModifyClass(bean);
                } else {
                    String class_id = bean.getClass_id() + "";
                    Intent intent = new Intent(mContext, SchedulingChangeActivity.class);
                    intent.putExtra("editValue", editValue);
                    intent.putExtra("part_value", partValue);
                    intent.putExtra("user_value", userValue);
                    intent.putExtra("class_id", class_id);
                    intent.putExtra("SchedulingList", "SchedulingList");
                    startActivity(intent);
                }
            }
        });
    }

    private void classesModifyClass(ShowClassBean.DataBean.ClassBean bean) {
        Params params = new Params();
        params.put("ri", rule_id);
        params.put("cli", bean.getClass_id());
        params.put("tp", 2);
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

    private void handleData(ShowClassBean.DataBean data) {
        List<ShowClassBean.DataBean.ClassBean> classX = data.getClassX();
        if (classX != null) {
            for (ShowClassBean.DataBean.ClassBean x : classX) {
                if (x.getIs_select() == 1) {
                    x.setCheck(true);
                }
            }
            mAdapter.replaceData(classX);
        }
    }

    class SchedulingAdapter extends BaseQuickAdapter<ShowClassBean.DataBean.ClassBean, BaseViewHolder> {
        public SchedulingAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final ShowClassBean.DataBean.ClassBean item) {
            final TextView textCheck = helper.getView(R.id.text_and_text_check);
            TextView textName = helper.getView(R.id.text_and_text_name);
            TextView textRight = helper.getView(R.id.text_and_text_right);
            View textLine = helper.getView(R.id.text_and_text_line);

            textCheck.setVisibility(View.VISIBLE);
            textLine.setVisibility(View.GONE);

            textName.setText(item.getName());
            textRight.setText(item.getRange());
            setChecked(textCheck, item.isCheck());
            textCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean check = item.isCheck();
                    if (!check) {
                        List<ShowClassBean.DataBean.ClassBean> data = getData();
                        for (ShowClassBean.DataBean.ClassBean datum : data) {
                            datum.setCheck(false);
                        }
                        item.setCheck(true);
                    } else {
                        List<ShowClassBean.DataBean.ClassBean> data = getData();
                        for (ShowClassBean.DataBean.ClassBean datum : data) {
                            datum.setCheck(false);
                        }
                    }
                    notifyDataSetChanged();
                }
            });

            final int class_id = item.getClass_id();
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SchedulingNewAddActivity.class);
                    intent.putExtra("scheduling_list", SchedulingNewAddActivity.LIST_ACTIVITY_IN);
                    intent.putExtra("cli", class_id);
                    intent.putExtra("title", "修改班次");
                    startActivityForResult(intent, 100);
                }
            });
        }
    }

    private void setChecked(TextView textCheck, boolean isCheck) {
//        clickAllMap.put(partId, isCheck);
        if (isCheck) {
            textCheck.setBackgroundResource(R.drawable.selected);
        } else {
            textCheck.setBackgroundResource(R.drawable.unselected);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            initData();
        }
    }

    @Subscribe
    public void schedulingListFinish(SchedulingFinishEvent event) {
        finish();
    }
}
