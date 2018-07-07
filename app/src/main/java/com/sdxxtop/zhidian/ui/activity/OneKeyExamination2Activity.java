package com.sdxxtop.zhidian.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.eventbus.OneKeyEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.fragment.ApplyListFragment;
import com.sdxxtop.zhidian.widget.SubTitleView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class OneKeyExamination2Activity extends BaseActivity {

    private int examineType;

    @BindView(R.id.examine_title_view)
    SubTitleView titleView;
    private ApplyListFragment approval;
    @BindView(R.id.one_key_examine_search_edit)
    EditText searchEdit;
    @BindView(R.id.examine_search_cancel)
    TextView searchCancelText;

    private String editChangeTextValue;
    private View unAgreeBtn;
    private View agreeBtn;

    @Override
    protected int getActivityView() {
        return R.layout.activity_one_key_examination2;
    }

    @Override
    protected void initView() {
        titleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBeforeFragment();
            }
        });

        approval = ApplyListFragment.newInstance(examineType, "approval", 2);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.one_key_frame_layout, approval).commit();

        unAgreeBtn = findViewById(R.id.btn_one_key_approver_unagreed);
        agreeBtn = findViewById(R.id.btn_one_key_approver_agreed);


    }

    @Override
    protected void initEvent() {
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //加入0.4s的间隔
                editChangeTextValue = "";
                if (!TextUtils.isEmpty(s)) {
                    editChangeTextValue = s.toString();
                    searchCancelText.setVisibility(View.VISIBLE);
                } else {
                    searchCancelText.setVisibility(View.GONE);
                }
                refreshFragment(editChangeTextValue);
            }
        });

        searchCancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.setText("");
//                editChangeTextValue = "";
//                refreshFragment(editChangeTextValue);
            }
        });

        unAgreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSubmit()) {
                    submitData(2);
                }
            }
        });

        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSubmit()) {
                    submitData(1);
                }
            }
        });
    }

    private boolean isSubmit() {
        String applyStrings = approval.getApplyStrings();
        if (TextUtils.isEmpty(applyStrings)) {
            showToast("请先勾选申请信息");
            return false;
        }
        return true;
    }

    private void refreshFragment(String editChangeTextValue) {
        approval.searchName(editChangeTextValue);
    }

    private void handleBeforeFragment() {
        approval.showWindow();
    }

    private void submitData(int type) {
        Params params = new Params();
        params.put("ai", approval.getApplyStrings());
        params.put("tp", type);
        RequestUtils.createRequest().postApplyBatch(params.getData())
                .enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
                    @Override
                    public void onSuccess(BaseModel baseModel) {
                        showToast(baseModel.msg);
                        EventBus.getDefault().post(new OneKeyEvent());
                        approval.notifyAdapterForOneKey();
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        showToast(errorMsg);
                    }
                }));
    }

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            examineType = getIntent().getIntExtra(ExamineActivity.EXAMINE_TYPE, -1);
        }
    }
}
