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
import com.sdxxtop.zhidian.entity.ParentListBean;
import com.sdxxtop.zhidian.entity.TeacherBean;
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
import com.xuxin.entry.ParentUserBean;
import com.xuxin.utils.CollectUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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

    //    private HashSet<Integer> partListSet;
//    private HashSet<Integer> userListSet;
    private Notice2Adapter mAdapter;
    //    private List<SelectBean> selectData;
    private HashSet<TeacherBean> teacherCacheSet;
    private HashSet<Integer> userSet;
    private HashSet<Integer> partSet;
    private HashSet<ParentListBean.DataBeanX.DataBean> parentCacheSet;
    private HashSet<Integer> typeSet;
    private HashMap<String, ParentUserBean> parentMap;
    private String userSetString;
    private String partSetString;
    private String typeSetString;
    private String parentMapString;
    private static final String TAG = "IssueNotice2Activity";

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
                Intent intent = new Intent(mContext, PartAndParentActivity.class);
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
        String editTitleValue = titleText.getEditText().getText().toString().trim();
        String editNameValue = nameText.getEditText().getText().toString().trim();
        String editContentValue = contentEdit.getText().toString().trim();

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

        if (TextUtils.isEmpty(partSetString) &&
                TextUtils.isEmpty(userSetString) &&
                TextUtils.isEmpty(typeSetString) &&
                TextUtils.isEmpty(parentMapString)) {
            showToast("请选择接收范围");
            return;
        }

        showProgressDialog("");

        ImageParams params = new ImageParams();
        params.put("oi", params.getUserId());
        params.put("pi", partSetString);
        params.put("ui", userSetString);
        params.put("ti", typeSetString);
        params.put("sui", parentMapString);


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
        if (requestCode == 100 && resultCode == PartAndParentActivity.CUSTOMER_RESULT_OK && data != null) {

            if (teacherCacheSet != null && teacherCacheSet.size() > 0) {
                HashSet<TeacherBean> tempTeacherCacheSet = teacherCacheSet;
                teacherCacheSet = (HashSet<TeacherBean>) data.getSerializableExtra(PartAndParentActivity.TEACHER_CACHE_SET);
                HashSet<TeacherBean> centerTeacherCacheSet = new HashSet<>(teacherCacheSet);
                for (TeacherBean centerTeacherBean : centerTeacherCacheSet) {
                    for (TeacherBean bean : tempTeacherCacheSet) {
                        if (bean.isPart()) {
                            if (bean.getPart_id() == centerTeacherBean.getPart_id()) {
                                teacherCacheSet.remove(centerTeacherBean);
                            }
                        } else {
                            if (bean.getUserid() == centerTeacherBean.getUserid()) {
                                teacherCacheSet.remove(centerTeacherBean);
                            }
                        }
                    }
                }

                teacherCacheSet.addAll(tempTeacherCacheSet);
            } else {
                teacherCacheSet = (HashSet<TeacherBean>) data.getSerializableExtra(PartAndParentActivity.TEACHER_CACHE_SET);
            }


            if (parentCacheSet != null && parentCacheSet.size() > 0) {
                HashSet<ParentListBean.DataBeanX.DataBean> tempParentCacheSet = parentCacheSet;
                parentCacheSet = (HashSet<ParentListBean.DataBeanX.DataBean>) data.getSerializableExtra(PartAndParentActivity.PARENT_CACHE_SET);
                HashSet<ParentListBean.DataBeanX.DataBean> centerParentCacheSet = new HashSet<>(parentCacheSet);
                for (ParentListBean.DataBeanX.DataBean centerDataBean : centerParentCacheSet) {
                    for (ParentListBean.DataBeanX.DataBean dataBean : tempParentCacheSet) {
                        if (centerDataBean.isPart()) {
                            if (dataBean.getType_id() == centerDataBean.getType_id()) {
                                parentCacheSet.remove(centerDataBean);
                            }
                        } else {
                            if (dataBean.getStudent_id() == centerDataBean.getStudent_id()) {
                                parentCacheSet.remove(centerDataBean);
                            }
                        }
                    }
                }

                parentCacheSet.addAll(tempParentCacheSet);
            } else {
                parentCacheSet = (HashSet<ParentListBean.DataBeanX.DataBean>) data.getSerializableExtra(PartAndParentActivity.PARENT_CACHE_SET);
            }


            userSet = CollectUtil.replaceIntegerHashSet(userSet, data, PartAndParentActivity.TEACHER_USER_SET);
            partSet = CollectUtil.replaceIntegerHashSet(partSet, data, PartAndParentActivity.TEACHER_PART_SET);
            typeSet = CollectUtil.replaceIntegerHashSet(typeSet, data, PartAndParentActivity.PARENT_TYPE_SET);
            parentMap = CollectUtil.replaceParentUserBeanHashMap(parentMap, data, PartAndParentActivity.PARENT_PARENT_MAP);


            userSetString = CollectUtil.getIntegerSetToString(userSet);
            partSetString = CollectUtil.getIntegerSetToString(partSet);
            typeSetString = CollectUtil.getIntegerSetToString(typeSet);
            parentMapString = CollectUtil.getParentMapToString(parentMap);

            String value = "";
            for (TeacherBean teacherBean : teacherCacheSet) {
                if (teacherBean.isPart()) {
                    value = value + teacherBean.getPart_name() + " ";
                } else {
                    value = value + teacherBean.getName() + " ";
                }
            }
//            if (partListSet != null) {
//                HashSet<Integer> tempSelectPartSet = partListSet;
//                partListSet = (HashSet<Integer>) data.getSerializableExtra("partList");
//                partListSet.addAll(tempSelectPartSet);
//            } else {
//                partListSet = (HashSet<Integer>) data.getSerializableExtra("partList");
//            }
//
//            if (userListSet != null) {
//                HashSet<Integer> tempSelectUserSet = userListSet;
//                userListSet = (HashSet<Integer>) data.getSerializableExtra("userList");
//                userListSet.addAll(tempSelectUserSet);
//            } else {
//                userListSet = (HashSet<Integer>) data.getSerializableExtra("userList");
//            }
//
//            if (selectData != null && selectData.size() > 0) {
//                List<SelectBean> tempSelectData = selectData;
//                selectData = (List<SelectBean>) data.getSerializableExtra("selectData");
//                List<SelectBean> centerSelectData = new ArrayList<>(selectData);
//                for (SelectBean centerSelectDatum : centerSelectData) {
//                    for (SelectBean tempSelectDatum : tempSelectData) {
//                        if (centerSelectDatum.getId().equals(tempSelectDatum.getId())) {
//                            selectData.remove(centerSelectDatum);
//                        }
//                    }
//                }
//
//                selectData.addAll(0, tempSelectData);
//            } else {
//                selectData = (List<SelectBean>) data.getSerializableExtra("selectData");
//            }


            String gradeName = "";
            String userName = "";
            for (ParentListBean.DataBeanX.DataBean dataBean : parentCacheSet) {
                if (dataBean.isPart()) {
                    String gradeValue = dataBean.getGradeValue();
                    if (!TextUtils.isEmpty(gradeValue)) {
                        gradeName = gradeValue;
                    } else {
                        gradeName = gradeName + " " + dataBean.getName();
                    }
                } else {
                    userName = userName + " " + dataBean.getName();
                }
            }

            String tempValue = value + gradeName + userName;

//            String value = "";
//            if (selectData != null) {
//                for (int i = 0; i < selectData.size(); i++) {
//                    SelectBean selectBean = selectData.get(i);
//                    if (i == selectData.size() - 1) {
//                        value = value + selectBean.getName();
//                    } else {
//                        value = value + selectBean.getName() + " ";
//                    }
//                }
//            }
            if (!TextUtils.isEmpty(tempValue)) {
                fanweiText.getTextRightText().setText(tempValue);
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

//    private String getUserValue() {
//        String value = "";
//        if (userListSet == null) {
//            return value;
//        }
//        for (Integer integer : userListSet) {
//            value = value + integer + ",";
//        }
//        if (value.length() > 0) {
//            value = value.substring(0, value.length() - 1);
//        }
//        return value;
//    }
//
//    private String getPartValue() {
//        String value = "";
//        if (partListSet == null) {
//            return value;
//        }
//        for (Integer integer : partListSet) {
//            value = value + integer + ",";
//        }
//        if (value.length() > 0) {
//            value = value.substring(0, value.length() - 1);
//        }
//        return value;
//    }

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
