package com.sdxxtop.zhidian.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.internal.LinkedTreeMap;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.HomeworkRecyclerAdapter;
import com.sdxxtop.zhidian.adapter.SelectionSubjectAdapter;
import com.sdxxtop.zhidian.entity.ParentSelectBean;
import com.sdxxtop.zhidian.entity.UserCourseBean;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.ImageParams;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.widget.SubTitleView;
import com.sdxxtop.zhidian.widget.TextAndTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeworkActivity extends BaseActivity {

    @BindView(R.id.home_work_title_view)
    SubTitleView mTitleView;
    @BindView(R.id.home_work_selector_subject)
    TextAndTextView mSelectorSubjectView;
    @BindView(R.id.home_work_selector_class)
    TextAndTextView mSelectorClassView;
    @BindView(R.id.home_work_right_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.home_work_right_drawer_cancel)
    Button mRightDrawerCancelBtn;
    @BindView(R.id.home_work_right_drawer_confirm)
    Button mRightDrawerConfirmBtn;
    @BindView(R.id.home_work_btn_layout)
    LinearLayout mBtnLayout;
    @BindView(R.id.home_work_drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.btn_submission)
    Button btnSubmit;
    @BindView(R.id.home_work_content_text)
    EditText contentEdit;
    @BindView(R.id.home_work_photo_recycler)
    RecyclerView photoRecycler;
    private SelectionSubjectAdapter selectionAdapter;
    private HomeworkRecyclerAdapter horAdapter;
    private List<LocalMedia> localMediaList = new ArrayList<>();
    private ArrayList<ParentSelectBean> parentBeanSet;

    @Override
    protected int getActivityView() {
        return R.layout.activity_homework;
    }

    @Override
    protected void initView() {
        btnSubmit.setText("发布");

        //获取上次选择科目的状态
        int courseId = PreferenceUtils.getInstance(mContext).getIntParam(ConstantValue.COURSE_SELECTOR_ID + getSelectorKey());
        String courseName = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COURSE_SELECTOR_NAME + getSelectorKey());
        if (!TextUtils.isEmpty(courseName)) {
            mSelectorSubjectView.getTextRightText().setText(courseName);
            mSelectorSubjectView.setTag(courseId);
        }

        String userOrGradeName = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.HOMEWORK_SELECTOR_USER_OR_GRADE_NAME + getSelectorKey());
        if (!TextUtils.isEmpty(userOrGradeName)) {
            mSelectorClassView.getTextRightText().setText(userOrGradeName);
        }

        setPhotoRecycler(photoRecycler);
    }

    @Override
    protected void initEvent() {
        mTitleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeworkListActivity.startHomeworkListActivity(mContext);
            }
        });

        mRightDrawerCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });

        mSelectorSubjectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                toOpenCheck();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                for (UserCourseBean.DataBean.CourseBean courseBean : selectionAdapter.getData()) {
                    courseBean.setCheck(false);
                }
                toOpenCheck();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        mSelectorClassView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentListActivity.startParentListActivity(mContext, ParentListActivity.TYPE_MULTI_SELECT, 100);
            }
        });

        mRightDrawerConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectionAdapter != null) {
                    boolean isCheck = false;
                    List<UserCourseBean.DataBean.CourseBean> data = selectionAdapter.getData();
                    for (UserCourseBean.DataBean.CourseBean datum : data) {
                        if (datum.isCheck()) {
                            isCheck = true;
                            String course_name = datum.getCourse_name();
                            int course_id = datum.getCourse_id();
                            mSelectorSubjectView.getTextRightText().setText(course_name);
                            mSelectorSubjectView.setTag(course_id);

                            //保存上次选择的记录
                            PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.COURSE_SELECTOR_NAME + getSelectorKey(), course_name);
                            PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.COURSE_SELECTOR_ID + getSelectorKey(), course_id);
                            break;
                        }
                    }

                    if (!isCheck) {
                        PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.COURSE_SELECTOR_NAME + getSelectorKey(), "");
                        PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.COURSE_SELECTOR_ID + getSelectorKey(), 0);
                        mSelectorSubjectView.getTextRightText().setText("");
                        mSelectorSubjectView.setTag(0);
                    }
                }
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void toOpenCheck() {
        Object tag = mSelectorSubjectView.getTag();
        int courseId = 0;
        if (tag instanceof Integer) {
            courseId = (int) tag;
        }
        if (courseId == 0) {
            for (UserCourseBean.DataBean.CourseBean courseBean : selectionAdapter.getData()) {
                courseBean.setCheck(false);
            }
        } else {
            for (UserCourseBean.DataBean.CourseBean courseBean : selectionAdapter.getData()) {
                if (courseBean.getCourse_id() == courseId) {
                    courseBean.setCheck(true);
                    break;
                }
            }
        }
        selectionAdapter.notifyDataSetChanged();
    }

    private void submit() {
        String courseName = mSelectorSubjectView.getTextRightText().getText().toString();
        Object tag = mSelectorSubjectView.getTag();
        int courseId = 0;
        if (tag instanceof Integer) {
            courseId = (int) tag;
        }
        String contentValue = contentEdit.getText().toString().trim();

        if (TextUtils.isEmpty(courseName) || courseId == 0) {
            showToast("请选择科目");
            return;
        }

        if (TextUtils.isEmpty(contentValue)) {
            showToast("请输入作业内容");
            return;
        }

        String gradeId = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.HOMEWORK_SELECTOR_GRADE_ID + getSelectorKey());
        String studentId = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.HOMEWORK_SELECTOR_STUDENT_ID + getSelectorKey());

        if (TextUtils.isEmpty(gradeId) && TextUtils.isEmpty(studentId)) {
            showToast("请选择班级/人");
            return;
        }

        ImageParams params = new ImageParams();
        //班次id（多个id用逗号隔开）
        params.put("cli", gradeId);
        params.put("oi", params.getUserId());
        //学生id（多个id用逗号隔开）
        params.put("si", studentId);
        params.put("cei", courseId);
        params.put("cen", courseName);
        params.put("ct", contentValue);

        params.addImagePathList("img[]", getImagePushPath());

        showProgressDialog("");
        RequestUtils.createRequest().postUserAddTask(params.getImgData())
                .enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
                    @Override
                    public void onSuccess(BaseModel baseModel) {
                        closeProgressDialog();
                        successSkip(baseModel);
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        closeProgressDialog();
                        showToast(errorMsg);
                    }
                }));
    }

    @Override
    protected void initData() {
        loadCourse();
    }

    private void loadCourse() {
        Params params = new Params();
        RequestUtils.createRequest().postUserCourse(params.getData()).enqueue(new RequestCallback<UserCourseBean>(new IRequestListener<UserCourseBean>() {
            @Override
            public void onSuccess(UserCourseBean userCourseBean) {
                UserCourseBean.DataBean data = userCourseBean.getData();
                if (data != null) {
                    List<UserCourseBean.DataBean.CourseBean> course = data.getCourse();
                    if (course != null) {
                        selectionAdapter = new SelectionSubjectAdapter(R.layout.item_stat_selector_recycler, course);
                        mRecyclerView.addItemDecoration(new ItemDivider());
                        mRecyclerView.setAdapter(selectionAdapter);

                        Object tag = mSelectorSubjectView.getTag();
                        int courseId = 0;
                        if (tag instanceof Integer) {
                            courseId = (int) tag;
                            for (UserCourseBean.DataBean.CourseBean courseBean : selectionAdapter.getData()) {
                                if (courseBean.getCourse_id() == courseId) {
                                    courseBean.setCheck(true);
                                    break;
                                }
                            }
                            selectionAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {

            }
        }));
    }

    //////选择图片  ↓  ///////

    protected void setPhotoRecycler(RecyclerView recycler) {
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        horAdapter = new HomeworkRecyclerAdapter(R.layout.item_home_work_recycler);
        recycler.setAdapter(horAdapter);
        LocalMedia localMedia = new LocalMedia();
        localMedia.setDuration(-100);
        localMediaList.add(localMedia);
        horAdapter.addData(localMediaList);
        horAdapter.setListener(new HomeworkRecyclerAdapter.HorListener() {
            @Override
            public void click() {
                goGallery();
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
        });
    }

    protected void onResult(List<LocalMedia> selectList) {
        if (horAdapter != null) {
            horAdapter.replaceData(selectList);
        }
    }

    public void goGallery() {
        removeLocalListTemp();

        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .compress(true)
                .selectionMedia(localMediaList)
                .maxSelectNum(3).forResult(PictureConfig.CHOOSE_REQUEST);
    }

    protected LocalMedia getTemp() {
        LocalMedia localMedia = new LocalMedia();
        localMedia.setDuration(-100);
        return localMedia;
    }

    public void removeLocalListTemp() {
        for (int i = 0; i < localMediaList.size(); i++) {
            if (localMediaList.get(i).getDuration() == -100) {
                localMediaList.remove(localMediaList.get(i));
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
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
        } else if (requestCode == 100 && resultCode == ParentListActivity.CUSTOMER_RESULT_OK && data != null) {
            parentBeanSet = ((ArrayList<ParentSelectBean>) data.getSerializableExtra(ParentListActivity.SELECT_SET_DATA));
            String gradeName = "";
            String userName = "";
            for (ParentSelectBean selectBean : parentBeanSet) {
                if (selectBean.type == ParentSelectBean.TYPE_GRADE) {
                    gradeName = gradeName + selectBean.value + ",";
                }
            }

            for (ParentSelectBean selectBean : parentBeanSet) {
                if (selectBean.type == ParentSelectBean.TYPE_STUDENT) {
                    userName = userName + selectBean.value + ",";
                }
            }

            if (userName.length() > 0) {
                userName = userName.substring(0, userName.length() - 1);
            }

            if (gradeName.length() > 0) {
                gradeName = gradeName.substring(0, gradeName.length() - 1);
            }

            String tempValue = gradeName + userName;
            mSelectorClassView.getTextRightText().setText(tempValue);

            PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.HOMEWORK_SELECTOR_USER_OR_GRADE_NAME + getSelectorKey(), tempValue);
            PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.HOMEWORK_SELECTOR_GRADE_ID + getSelectorKey(), getGradeId());
            PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.HOMEWORK_SELECTOR_STUDENT_ID + getSelectorKey(), getStudentId());

        }
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

    //////   选择图片  ↑   ///////

    public String getStudentId() {
        String temp = "";
        if (parentBeanSet == null) {
            return temp;
        }

        for (ParentSelectBean selectBean : parentBeanSet) {
            if (selectBean.type == ParentSelectBean.TYPE_STUDENT) {
                temp = temp + selectBean.studentId + ",";
            }
        }
        if (temp.length() > 0) {
            temp = temp.substring(0, temp.length() - 1);
        }

        return temp;
    }

    public String getGradeId() {
        String temp = "";
        if (parentBeanSet == null) {
            return temp;
        }

        for (ParentSelectBean selectBean : parentBeanSet) {
            if (selectBean.type == ParentSelectBean.TYPE_GRADE) {
                temp = temp + selectBean.gradeId + ",";
            }
        }
        if (temp.length() > 0) {
            temp = temp.substring(0, temp.length() - 1);
        }

        return temp;
    }

    protected void successSkip(BaseModel baseModel) {
        showToast("作业提交成功");

        if (baseModel == null) {
            return;
        }
        Object data = baseModel.getData();
        String task_id = "";
        if (data != null) {
            task_id = (String) ((LinkedTreeMap) data).get("task_id");
        }

        HomeworkDetailActivity.startHomeworkDetailActivity(mContext, task_id);
        finish();
    }

    public String getSelectorKey() {
        return AppSession.getInstance().getUserId() + AppSession.getInstance().getCompanyId();
    }

    public static void startHomeworkActivity(Context context) {
        Intent intent = new Intent(context, HomeworkActivity.class);
        context.startActivity(intent);
    }
}
