package com.sdxxtop.zhidian.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.internal.LinkedTreeMap;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.entity.VoteEditImageBean;
import com.sdxxtop.zhidian.eventbus.VoteRefreshEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.ImageParams;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.popupwindow.SelectTypeWindow;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.TextAndText2View;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import cn.addapp.pickers.picker.DateTimePicker;

public class AddVote2Activity extends BaseActivity {
    @BindView(R.id.add_vote_text_btn)
    TextView textBtn;
    @BindView(R.id.add_vote_img_btn)
    TextView imgBtn;
    @BindView(R.id.add_vote_change_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.add_vote_change_add)
    LinearLayout changeAdd;
    @BindView(R.id.add_vote_text_left_line)
    View leftLine;
    @BindView(R.id.add_vote_text_right_line)
    View rightLine;

    @BindView(R.id.add_vote_edit_title)
    EditText editTitle;
    @BindView(R.id.add_vote_text_and_text2)
    TextAndText2View textAndTextPingyi; //参加民主评议
    @BindView(R.id.add_vote_edit_dec)
    EditText editDec; //投票说明
    @BindView(R.id.add_vote_edit_dec_shifou)
    TextView editShiFouShow; //投票说明是否添加
    @BindView(R.id.add_vote_senior_setting)
    CheckBox seniorCheckBox; //高级设置开关
    @BindView(R.id.add_vote_gaoji_layout)
    LinearLayout gaojiLayout; //高级设置布局
    @BindView(R.id.add_vote_text_single)
    TextAndText2View textSingle; //单双选问题
    @BindView(R.id.add_vote_text_resion)
    TextAndText2View textResion; //投票结果展示
    @BindView(R.id.add_vote_text_time)
    TextAndText2View textTime; //结束时间

    @BindView(R.id.add_vote_text_niming_check)
    CheckBox nimingCheck; //选中问题

    @BindView(R.id.add_vote_add_btn)
    Button addBtn;


    private VoteTextAdapter textAdapter;
    private VoteImageAdapter imageAdapter;
    private HashSet<Integer> partListSet;
    private HashSet<Integer> userListSet;

    private List<String> singleList;
    private List<String> resionShowList;


    @Override
    protected int getActivityView() {
        return R.layout.activity_add_vote2;
    }

    @Override
    protected void initView() {
        super.initView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemDivider());
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        textAdapter = new VoteTextAdapter(R.layout.item_vote_text_recycler);
        imageAdapter = new VoteImageAdapter(R.layout.item_vote_img_recycler);
        //默认文字
        setTextAdapter();

        singleList = new ArrayList<>();
        singleList.add("单选");
        singleList.add("多选");

        resionShowList = new ArrayList<>();
        resionShowList.add("结束投票后可见");
        resionShowList.add("发布即可见");

        editTextCountControl(R.id.add_vote_edit_dec, editDec, new TextView(this));
    }


    @Override
    protected void initEvent() {
        super.initEvent();

        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapterType == 1) {
                    return;
                }
                setTextAdapter();
            }
        });
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapterType == 2) {
                    return;
                }
                setImgAdapter();
            }
        });

        changeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapterType == 1) {
                    VoteEditImageBean voteEditImageBean = new VoteEditImageBean("", "", 1);
                    textAdapter.addData(voteEditImageBean);
                } else if (adapterType == 2) {
                    VoteEditImageBean voteEditImageBean = new VoteEditImageBean("", "", 2);
                    imageAdapter.addData(voteEditImageBean);
                }
            }
        });

        //图片选择
        textAndTextPingyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NoticeReciveRangeActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        seniorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, 7);
                    Date time = calendar.getTime();
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    endTime = sDateFormat.format(time);

                    textTime.getTextRightText().setText(endTime);
                    gaojiLayout.setVisibility(View.VISIBLE);
                } else {
                    gaojiLayout.setVisibility(View.GONE);
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        textSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectType(type_single, singleList);
            }
        });

        textResion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectType(type_show, resionShowList);
            }
        });

        textTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onYearMonthDayTimePicker();
            }
        });

        editShiFouShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShiFouShow.setVisibility(View.GONE);
                editDec.setVisibility(View.VISIBLE);
            }
        });

    }

    private int defaultSingle = 1; //单选
    public static final int type_single = 10;
    private int defaultSubmitShow = 2;//发布后即可见
    public static final int type_show = 11;
    private String endTime;

    private void send() {
        //标题判断
        String editTile = editTitle.getText().toString().trim();

        if (TextUtils.isEmpty(editTile)) {
            showToast("请输入标题");
            return;
        }

        if (editTile.length() > 25) {
            showToast("标题最多25个字");
            return;
        }

        if (editDec.length() > 200) {
            showToast("投票说明最多200个字");
            return;
        }

        //冒失可以是空的
        String pingYiValue = textAndTextPingyi.getTextRightText().getText().toString();

        ImageParams params = new ImageParams();
        params.put("oi", params.getUserId());
        params.put("ui", getUserValue());
        params.put("pi", getPartValue());
        params.put("te", editTile);
        // TODO 说明的长度限制问题
        params.put("vd", editDec.getText().toString());

        if (seniorCheckBox.isChecked()) {
            params.put("it", 1);
            boolean checked = nimingCheck.isChecked();
            params.put("ih", checked ? 2 : 1);
            params.put("et", endTime);
            params.put("io", defaultSingle);
            params.put("is", defaultSubmitShow);
        } else {
            params.put("it", 2);
        }


        if (adapterType == 2) {
            List<VoteEditImageBean> data = imageAdapter.getData();
            List<File> fileList = new ArrayList<>();
            List<String> targetFile = new ArrayList<>();
            String value = "";
            for (int i = 0; i < data.size(); i++) {
                VoteEditImageBean imageBean = data.get(i);
                String imagePath = imageBean.imagePath;
                if (TextUtils.isEmpty(imagePath)) {
                    showToast("选项图片不能为空");
                    return;
                }
                String editValue = imageBean.editValue;
                if (TextUtils.isEmpty(editValue)) {
                    showToast("选项中不能为空");
                    return;
                }

                if (editValue.length() > 20) {
                    showToast("选项文字必须小于20个字");
                    return;
                }
                fileList.add(new File(imagePath));
                targetFile.add(getCacheDir() + "/img" + i + ".png");

                if (i == data.size() - 1) {
                    value = value + editValue;
                } else {
                    value = value + editValue + ",";
                }
            }
            params.put("on", value);
            params.addCompressImagePathList("img[]", fileList, targetFile, 80);
        } else {
            List<VoteEditImageBean> data = textAdapter.getData();
            String value = "";
            for (int i = 0; i < data.size(); i++) {
                VoteEditImageBean imageBean = data.get(i);
                String editValue = imageBean.editValue;
                if (TextUtils.isEmpty(editValue)) {
                    showToast("选项名不能为空");
                    return;
                }

                if (editValue.length() > 20) {
                    showToast("选项文字必须小于20个字");
                    return;
                }

                if (i == data.size() - 1) {
                    value = value + editValue;
                } else {
                    value = value + editValue + ",";
                }
            }
            params.put("on", value);
        }
        showProgressDialog("");
        RequestUtils.createRequest().postVoteAddVote(params.getImgData()).enqueue(new RequestCallback<BaseModel>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                closeProgressDialog();
                String vote_id = (String) ((LinkedTreeMap) baseModel.getData()).get("vote_id");
                int voteId = Integer.parseInt(vote_id);
                Intent intent = new Intent(mContext, MineInitiateVoteActivity.class);
                intent.putExtra("vi", voteId);
                intent.putExtra("type", 1); //1.从我发起 2.去评议
                intent.putExtra("vote_type", 1);//1.投票 2.打分
                startActivity(intent);
                EventBus.getDefault().post(new VoteRefreshEvent());
                finish();
                showToast(baseModel.msg);
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
                showToast(errorMsg);
            }
        }));
    }

    int adapterType;

    private void setTextAdapter() {
        adapterType = 1;
        textBtn.setTextColor(getResources().getColor(R.color.lan));
        imgBtn.setTextColor(getResources().getColor(R.color.notice_contentcolor));
        leftLine.setVisibility(View.VISIBLE);
        rightLine.setVisibility(View.INVISIBLE);

        recyclerView.setAdapter(textAdapter);
        List<VoteEditImageBean> beanList = new ArrayList<>();
        beanList.add(new VoteEditImageBean("", "", 1));
        beanList.add(new VoteEditImageBean("", "", 1));
        textAdapter.replaceData(beanList);
    }

    private void setImgAdapter() {
        textBtn.setTextColor(getResources().getColor(R.color.notice_contentcolor));
        imgBtn.setTextColor(getResources().getColor(R.color.lan));
        rightLine.setVisibility(View.VISIBLE);
        leftLine.setVisibility(View.INVISIBLE);

        adapterType = 2;
        recyclerView.setAdapter(imageAdapter);
        List<VoteEditImageBean> beanList = new ArrayList<>();
        beanList.add(new VoteEditImageBean("", "", 2));
        beanList.add(new VoteEditImageBean("", "", 2));
        imageAdapter.replaceData(beanList);
    }


    private void showSelectType(final int type, final List<String> strings) {

        final SelectTypeWindow selectTypeWindow = new SelectTypeWindow(this, strings);
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = (int) (getResources().getDimension(R.dimen.y145) + getResources().getDimension(R.dimen.y100) * strings.size());
        selectTypeWindow.setWidth(weight);
        selectTypeWindow.setHeight(height);
        selectTypeWindow.setFocusable(true);
        selectTypeWindow.setTouchable(true);
        selectTypeWindow.setOutsideTouchable(true);
        selectTypeWindow.setAnimationStyle(R.style.AnimationRightBottom);

        selectTypeWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
                if (type == type_single && !selectTypeWindow.stringType.equals("")) {
                    textSingle.getTextRightText().setText(selectTypeWindow.stringType);
                    defaultSingle = singleList.indexOf(selectTypeWindow.stringType) + 1;
                } else if (type == type_show && !selectTypeWindow.stringType.equals("")) {
                    textResion.getTextRightText().setText(selectTypeWindow.stringType);
                    defaultSubmitShow = resionShowList.indexOf(selectTypeWindow.stringType) + 1;
                }
            }
        });

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        selectTypeWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_main, null), Gravity.BOTTOM, 0, 0);
    }

    public void onYearMonthDayTimePicker() {

        Calendar calendar = Calendar.getInstance(); //获取当前时间
        calendar.add(Calendar.DATE, 7); //加一周的时间
        Date time = calendar.getTime();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        endTime = sDateFormat.format(time);
        final DateTimePicker picker = new DateTimePicker(mContext, DateTimePicker.HOUR_24);
        picker.setActionButtonTop(true);
        picker.setDateRangeStart(2017, 1, 1);
        picker.setDateRangeEnd(2025, 11, 11);
        picker.setSelectedItem(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        picker.setTimeRangeStart(0, 0);
        picker.setTimeRangeEnd(23, 59);

        picker.setTopBackgroundColor(0xFFEEEEEE);
        int h = (int) getResources().getDimension(R.dimen.y601);
        picker.setHeight(h);
        picker.setTopHeight(52);
        picker.setTopLineColor(0xFF33B5E5);
        picker.setTopLineHeight(1);
        picker.setTitleText("");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(18);
        picker.setCancelTextColor(0xFF999999);
        picker.setCancelTextSize(18);
        picker.setSubmitTextColor(0xFF3296FA);
        picker.setSubmitTextSize(18);

        picker.setTextSize(20);
        picker.setBackgroundColor(0xFFffffff);

        picker.setLabel("年", "月", "日", "时", "分");

        picker.show();

        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                endTime = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":00";
                textTime.getTextRightText().setText(endTime);
            }
        });
    }

    class VoteTextAdapter extends BaseQuickAdapter<VoteEditImageBean, BaseViewHolder> {

        public VoteTextAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final VoteEditImageBean item) {
            final EditText textEdit = helper.getView(R.id.item_vote_text_edit);
            textEdit.setHint("选项" + (helper.getAdapterPosition() + 1) + "，最多20字");

            Button btnDelete = helper.getView(R.id.item_vote_text_btn_delete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getData().size() > 2) {
                        getData().remove(item);
                        notifyDataSetChanged();
                    } else {
                        showToast("至少要有两个选项");
                    }
                }
            });

            if (textEdit.getTag() instanceof TextWatcher) {
                textEdit.removeTextChangedListener((TextWatcher) textEdit.getTag());
            }

            textEdit.setText(item.editValue);
            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    item.editValue = textEdit.getText().toString();
                }
            };
            textEdit.addTextChangedListener(watcher);
            textEdit.setTag(watcher);
        }
    }

    class VoteImageAdapter extends BaseQuickAdapter<VoteEditImageBean, BaseViewHolder> {

        public VoteImageAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final VoteEditImageBean item) {
            final ImageView imgImg = helper.getView(R.id.item_vote_img_img);
            final EditText imgEdit = helper.getView(R.id.item_vote_img_edit);
            int position = helper.getAdapterPosition();
            imgEdit.setHint("选项" + (position + 1) + "，最多20字");
            final ImageView defaultImage = helper.getView(R.id.item_vote_img_default_img);
            Button btnDelete = helper.getView(R.id.item_vote_img_btn_delete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getData().size() > 2) {
                        getData().remove(item);
                        notifyDataSetChanged();
                    } else {
                        showToast("至少要有两个选项");
                    }
                }
            });

            if (imgEdit.getTag() instanceof TextWatcher) {
                imgEdit.removeTextChangedListener((TextWatcher) imgEdit.getTag());
            }

            imgEdit.setText(item.editValue);
            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    item.editValue = imgEdit.getText().toString();
                }
            };
            imgEdit.addTextChangedListener(watcher);
            imgEdit.setTag(watcher);


            String imagePath = item.imagePath;
            if (TextUtils.isEmpty(imagePath)) {
                imgImg.setImageDrawable(null);
                defaultImage.setVisibility(View.VISIBLE);
            } else {
                Glide.with(mContext).load(imagePath).into(imgImg);
                defaultImage.setVisibility(View.INVISIBLE);
            }

            final int tempPosition = position;
            imgImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PictureSelector.create(AddVote2Activity.this)
                            .openGallery(PictureMimeType.ofImage())
                            .compress(true)
                            .maxSelectNum(1).forResult(tempPosition);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                LocalMedia media = selectList.get(0);
                String path = media.getPath();
                imageAdapter.getData().get(requestCode).imagePath = path;
                imageAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == 100 && resultCode == NoticeReciveRangeActivity.NOTICE_RESULT_OK && data != null) {
            //这个是选择人
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

            textAndTextPingyi.getTextRightText().setText(value);

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

