package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.NoticeAddBean;
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.eventbus.NoticeReadEvent;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.ImageParams;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;
import com.sdxxtop.zhidian.widget.TextAndEditView;
import com.sdxxtop.zhidian.widget.TextAndTextView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;

public class IssueNotice2Activity extends BaseActivity {

    @BindView(R.id.issue_notice2_title_view)
    SubTitleView titleView;
    @BindView(R.id.issue_notice2_fanwei)
    TextAndTextView fanweiText;
    @BindView(R.id.issue_notice2_title)
    TextAndEditView titleText;
    @BindView(R.id.issue_notice2_name)
    TextAndEditView nameText;
    @BindView(R.id.issue_notice2_photo_selector)
    ImageView selectorImg;
    @BindView(R.id.issue_notice2_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.issue_notice2_recycler_layout)
    LinearLayout recyLinearLayout;
    @BindView(R.id.issue_notice2_content)
    EditText contentEdit;
    @BindView(R.id.issue_notice2_change_text)
    TextView contentText;
    @BindView(R.id.issue_notice2_add_btn)
    Button addBtn;

    private HashSet<Integer> partListSet;
    private HashSet<Integer> userListSet;
    private Notice2Adapter mAdapter;

    @Override
    protected int getActivityView() {
        return R.layout.activity_issue_notice2;
    }

    @Override
    protected void initView() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        mAdapter = new Notice2Adapter(R.layout.item_issue_notice2_recycler);
        recyclerView.setAdapter(mAdapter);


        editTextCountControl(R.id.issue_notice2_content, contentEdit, contentText);
    }

    @Override
    protected void initEvent() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        fanweiText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NoticeReciveRangeActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        selectorImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter != null && mAdapter.getData().size() == 3) {
                    showToast("最多只能添加三张照片");
                    return;
                }
                List<LocalMedia> data = null;
                if (mAdapter != null) {
                    data = mAdapter.getData();
                } else {
                    data = new ArrayList<>();
                }

                PictureSelector.create(IssueNotice2Activity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .compress(true)
                        .selectionMedia(data)
                        .maxSelectNum(3).forResult(101);
            }
        });
    }

    private void send() {
        String editTitleValue = titleText.getEditText().getText().toString();
        String editNameValue = nameText.getEditText().getText().toString();
        String editContentValue = contentEdit.getText().toString();

        if (TextUtils.isEmpty(editTitleValue)) {
            showToast("请输入公告标题");
            return;
        }

        if (TextUtils.isEmpty(editNameValue)) {
            showToast("请输入发布人姓名");
            return;
        }

        if (TextUtils.isEmpty(editContentValue)) {
            showToast("公告内容不能为空");
            return;
        }

        showProgressDialog("");

        ImageParams params = new ImageParams();
        params.put("oi", params.getUserId());
        params.put("pi", getPartValue());
        params.put("ui", getUserValue());
        params.put("te", editTitleValue);
        params.put("un", editNameValue);
        params.put("ct", editContentValue);

        List<LocalMedia> data = mAdapter.getData();
        if (data.size() > 0) {
            List<File> imgList = new ArrayList<>();
            List<String> targetList = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                LocalMedia media = data.get(i);
                imgList.add(new File(media.getPath()));
                targetList.add(getFilesDir() + "/img" + i + ".png");
            }
            params.addCompressImagePathList("img[]", imgList, targetList, 80);
        }

        RequestUtils.createRequest().postNoticeAdd(params.getImgData()).enqueue(new RequestCallback<>(new IRequestListener<NoticeAddBean>() {
            @Override
            public void onSuccess(NoticeAddBean noticeAddBean) {
                closeProgressDialog();
                showToast(noticeAddBean.msg);
                if (noticeAddBean.getData() != null) {
                    int notice_id = noticeAddBean.getData().getNotice_id();
                    Intent intent = new Intent(mContext, NoticeDetail2Activity.class);
                    intent.putExtra("ni", notice_id);
                    startActivity(intent);
                    EventBus.getDefault().post(new NoticeReadEvent());
                    finish();
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
                showToast(errorMsg);
            }
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == NoticeReciveRangeActivity.NOTICE_RESULT_OK && data != null) {
            partListSet = (HashSet<Integer>) data.getSerializableExtra("partList");
            userListSet = (HashSet<Integer>) data.getSerializableExtra("userList");
            List<SelectBean> selectData = (List<SelectBean>) data.getSerializableExtra("selectData");
            String value = "";
            if (selectData != null) {
                for (int i = 0; i < selectData.size(); i++) {
                    SelectBean selectBean = selectData.get(i);
                    if (i == selectData.size() - 1) {
                        value = value + selectBean.getName();
                    } else {
                        value = value + selectBean.getName() + " ";
                    }
                }
            }
            if (!TextUtils.isEmpty(value)) {
                fanweiText.getTextRightText().setText(value);
            } else {
                fanweiText.getTextRightText().setText("默认选择全公司");
            }
        } else if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
//                LocalMedia media = selectList.get(0);
//                String path = media.getPath();
//                imageAdapter.getData().get(requestCode).imagePath = path;
//                imageAdapter.notifyDataSetChanged();
                recyLinearLayout.setVisibility(View.VISIBLE);
                mAdapter.replaceData(selectList);
            } else {
                recyLinearLayout.setVisibility(View.GONE);
            }
        }
    }

    private String getUserValue() {
        String value = "";
        if (userListSet == null) {
            return value;
        }
        for (Integer integer : userListSet) {
            value = value + integer + ",";
        }
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    private String getPartValue() {
        String value = "";
        if (partListSet == null) {
            return value;
        }
        for (Integer integer : partListSet) {
            value = value + integer + ",";
        }
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    private class Notice2Adapter extends BaseQuickAdapter<LocalMedia, BaseViewHolder> {
        public Notice2Adapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final LocalMedia item) {
            ImageView imgView = helper.getView(R.id.item_issue_notice2_img);
            ImageView imgDelete = helper.getView(R.id.item_issue_notice2_delete);
            String path = item.getPath();
            if (!TextUtils.isEmpty(path)) {
                Glide.with(mContext).load(path).into(imgView);
            }

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData().remove(item);
                    int size = getData().size();
                    if (size == 0) {
                        recyLinearLayout.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    protected void editTextCountControl(@IdRes int editId, final EditText contentEdit, final TextView changeText) {
        ViewUtil.editTextInScrollView(editId, contentEdit);
        contentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = contentEdit.getText();
                int len = editable.length();

                if (len > 200) {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, 200);
                    contentEdit.setText(newStr);
                    editable = contentEdit.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    int length = s.length();
                    if (length > 200) {
                        length = 200;
                    }
                    changeText.setText(length + "/200");
                    if (length == 200) {
                        showToast("已超过200字");
                    }
                }
            }
        });
    }
}
