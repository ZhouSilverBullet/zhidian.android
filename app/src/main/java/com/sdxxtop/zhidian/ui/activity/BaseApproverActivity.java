package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ApplyApproverRecyclerAdapter;
import com.sdxxtop.zhidian.adapter.ApplyHorCopyRecyclerAdapter;
import com.sdxxtop.zhidian.adapter.ApplyHorRecyclerAdapter;
import com.sdxxtop.zhidian.entity.ApproverIndexBean;
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.eventbus.PostSuccessEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ViewUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */

public abstract class BaseApproverActivity extends BaseActivity implements ApplyHorRecyclerAdapter.HorListener {

    protected int at;
    protected ApproverIndexBean.DataBean data;

    //吊相机用到
    protected ApplyHorRecyclerAdapter horAdapter;
    private List<SelectBean> selectData;
    private HashSet<Integer> selectUserSet;

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            at = getIntent().getIntExtra("at", -1);
        }
    }

    protected abstract void initChildData(ApproverIndexBean.DataBean data);

    @Override
    protected void initData() {
        Params params = new Params();
        params.put("at", at);
        RequestUtils.createRequest().postApplyApprover("approver", params.getData()).enqueue(new RequestCallback<ApproverIndexBean>(new IRequestListener<ApproverIndexBean>() {
            @Override
            public void onSuccess(ApproverIndexBean approverIndexBean) {
                ApproverIndexBean.DataBean data = approverIndexBean.getData();
                if (data != null) {
                    BaseApproverActivity.this.data = data;
                    initChildData(data);
                } else {
                    showToast("获取审核人数据失败");
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    public void removeLocalListTemp() {
        for (int i = 0; i < localMediaList.size(); i++) {
            if (localMediaList.get(i).getDuration() == -100) {
                localMediaList.remove(localMediaList.get(i));
                break;
            }
        }
    }

    protected void successSkip(BaseModel baseModel) {
        showToast("申请提交成功");

        if (baseModel == null) {
            return;
        }
        Object data = baseModel.getData();
        String apply_id = "";
        if (data != null) {
            apply_id = (String) ((LinkedTreeMap) data).get("apply_id");
        }

        Intent intent = new Intent(mContext, SubmissionActivity.class);
        intent.putExtra("at", at);
        intent.putExtra("apply_id", apply_id);
        intent.putExtra("approvalActivity", 100);
        startActivity(intent);
        EventBus.getDefault().post(new PostSuccessEvent());
        finish();
    }

    public void goGallery() {
        removeLocalListTemp();

        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .compress(true)
                .selectionMedia(localMediaList)
                .maxSelectNum(3).forResult(PictureConfig.CHOOSE_REQUEST);
    }

    protected List<LocalMedia> localMediaList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    if (selectList != null && selectList.size() > 0) {
                        localMediaList.clear();
                        int size = selectList.size();
                        if (size < 3) {
                            localMediaList.addAll(selectList);
                            localMediaList.add(getTemp());
                        } else {
                            localMediaList.addAll(selectList);
                        }
                        onResult(localMediaList);
                    }
                    break;
            }
        } else if (requestCode == 100 && resultCode == NoticeReciveRangeActivity.NOTICE_RESULT_OK && data != null) { //抄送人
            copyPeople(data);
        }
    }

    private void copyPeople(Intent data) {
        if (data == null) {
            return;
        }

        HashSet<Integer> tempSelectUserSet = null;
        if (selectUserSet != null) {
            tempSelectUserSet = selectUserSet;
            selectUserSet = (HashSet<Integer>) data.getSerializableExtra("userList");
            selectUserSet.addAll(tempSelectUserSet);
        } else {
            selectUserSet = (HashSet<Integer>) data.getSerializableExtra("userList");
        }


        List<SelectBean> tempSelectData = null;
        if (selectData != null && selectData.size() > 0) {
            tempSelectData = selectData;
            tempSelectData.remove(tempSelectData.size() - 1);
            selectData = (List<SelectBean>) data.getSerializableExtra("selectData");
            List<SelectBean> centerSelectData = new ArrayList<>(selectData);
            for (SelectBean centerSelectDatum : centerSelectData) {
                for (SelectBean tempSelectDatum : tempSelectData) {
                    if (centerSelectDatum.getId().equals(tempSelectDatum.getId())) {
                        selectData.remove(centerSelectDatum);
                    }
                }
            }

            //删除相同的最后再加入集合中
            selectData.addAll(0, tempSelectData);
        } else {
            selectData = (List<SelectBean>) data.getSerializableExtra("selectData");
        }
        selectData.add(new SelectBean());
        copyAdapter.replaceData(selectData);
    }

    protected String getCopyValue() {
        String value = "";
        if (selectUserSet == null) {
            return value;
        }
        for (Integer integer : selectUserSet) {
            value = value + integer + ",";
        }
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    protected String getApproverValue() {
        String ai = "";
        List<ApproverIndexBean.DataBean.ApproverBean> approver = data.getApprover();
        if (approver == null) {
            return "";
        }
        for (int i = 0; i < approver.size(); i++) {
            if (i == approver.size() - 1) {
                ai = ai + approver.get(i).getUserid();
            } else {
                ai = ai + approver.get(i).getUserid() + ",";
            }
        }
        return ai;
    }

    protected ApplyHorCopyRecyclerAdapter copyAdapter;

    protected void setCopyRecycler(RecyclerView copyRecycler) {
        copyAdapter = new ApplyHorCopyRecyclerAdapter(R.layout.item_apply_hor_copy_start_recycler);
        copyRecycler.setAdapter(copyAdapter);
        copyRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        SelectBean selectBean = new SelectBean();
        copyAdapter.addData(selectBean);

        copyAdapter.setAddListener(new ApplyHorCopyRecyclerAdapter.AddListener() {
            @Override
            public void addClick() {
                Intent intent = new Intent(BaseApproverActivity.this, NoticeReciveRangeActivity.class);
                intent.putExtra("isPartSelect", NoticeReciveRangeActivity.PART_NOT_SELECTOR);
                startActivityForResult(intent, 100);
            }
        });

        copyAdapter.setDeleteListener(new ApplyHorCopyRecyclerAdapter.DeleteListener() {
            @Override
            public void onDelete(SelectBean item) {
                Integer id = item.getId();
                if (selectUserSet != null) {
                    if (selectUserSet.contains(id)) {
                        selectUserSet.remove(id);
                    }
                }
            }
        });
    }

    protected ApplyApproverRecyclerAdapter approverAdapter;

    protected void setApproverRecycler(RecyclerView approverRecycler) {
        approverAdapter = new ApplyApproverRecyclerAdapter(R.layout.item_apply_hor_copy_start_recycler);
        approverRecycler.setAdapter(approverAdapter);
        approverRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        List<ApproverIndexBean.DataBean.ApproverBean> approver = data.getApprover();
        if (approver != null) {
            approverAdapter.addData(approver);
        }
    }


    protected LocalMedia getTemp() {
        LocalMedia localMedia = new LocalMedia();
        localMedia.setDuration(-100);
        return localMedia;
    }

    //图片上传
    protected List<File> getImagePushPath() {
        removeLocalListTemp();
        //设置相片
        List<File> imgList = new ArrayList<>();
        if (localMediaList != null && localMediaList.size() > 0) {
            for (int i = 0; i < localMediaList.size(); i++) {
                imgList.add(new File(localMediaList.get(i).getPath()));
            }
        }
        return imgList;
    }

    @Override
    public void click() {
        goGallery();
    }

    protected void setPhotoRecycler(RecyclerView recycler) {
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        horAdapter = new ApplyHorRecyclerAdapter(R.layout.item_apply_hor_recycler);
        recycler.setAdapter(horAdapter);
        LocalMedia localMedia = new LocalMedia();
        localMedia.setDuration(-100);
        localMediaList.add(localMedia);
        horAdapter.addData(localMediaList);
        horAdapter.setListener(this);
    }

    @Override
    public void delete(LocalMedia item) {
        localMediaList.remove(item);
        for (int i = 0; i < localMediaList.size(); i++) {
            LocalMedia media = localMediaList.get(i);
            if (media.getDuration() == -100) {
                break;
            }
            if (i == localMediaList.size() - 1) {
                localMediaList.add(getTemp());
            }
        }

        //删除也是重新刷新数据
        //本来想定义 onDelete回调，发现也是 adapter.replaceData(),所以也用onResult 了
        onResult(localMediaList);
    }

    protected void onResult(List<LocalMedia> selectList) {
        if (horAdapter != null) {
            horAdapter.replaceData(selectList);
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
