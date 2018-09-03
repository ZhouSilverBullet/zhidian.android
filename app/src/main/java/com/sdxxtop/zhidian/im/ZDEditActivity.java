package com.sdxxtop.zhidian.im;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.widget.SubTitleView;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;

import butterknife.BindView;

public class ZDEditActivity extends BaseActivity {

    @BindView(R.id.zd_edit_title_view)
    SubTitleView mTitleView;
    @BindView(R.id.zd_edit_text)
    EditText mEditText;
    @BindView(R.id.zd_dele_image)
    ImageView mDeleteImg;
    private String count;
    private String identify;
    private String title;

    @Override
    protected int getActivityView() {
        return R.layout.activity_zdedit;
    }

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            count = getIntent().getStringExtra("count");
            identify = getIntent().getStringExtra("identify");
            title = getIntent().getStringExtra("title");
        }
    }

    @Override
    protected void initView() {
//        if (TextUtils.isEmpty(count)) {
        mTitleView.setTitleValue("群聊名称");
//        } else {
//            mTitleView.setTitleValue("群聊名称 (" + count + ")");
//        }

        mEditText.setText(title);
        mEditText.setSelection(title.length());
    }

    @Override
    protected void initEvent() {
        mTitleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGroupName();
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null) {
                    return;
                }
                String content = s.toString();
                if (!TextUtils.isEmpty(content)) {
                    mDeleteImg.setVisibility(View.VISIBLE);
                } else {
                    mDeleteImg.setVisibility(View.GONE);
                }
            }
        });

        mDeleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
            }
        });
    }

    private void changeGroupName() {
        final String content = mEditText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            showToast("群名称不能为空");
            return;
        }

        if (content.length() > 10) {
            showToast("群名称长度不能超过10个字");
            return;
        }

        TIMGroupManagerExt.ModifyGroupInfoParam param = new TIMGroupManagerExt.ModifyGroupInfoParam(identify);
        param.setGroupName(content);
        TIMGroupManagerExt.getInstance().modifyGroupInfo(param, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                showToast("修改失败");
            }

            @Override
            public void onSuccess() {
                showToast("修改成功");
                Intent intent = new Intent();
                intent.putExtra("content", content);
                setResult(121, intent);
                finish();
            }
        });
    }


}
