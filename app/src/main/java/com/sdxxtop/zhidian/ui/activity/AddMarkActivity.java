package com.sdxxtop.zhidian.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;
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

import com.google.gson.internal.LinkedTreeMap;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.eventbus.VoteRefreshEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.ImageParams;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.popupwindow.SelectTypeWindow;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.TextAndText2View;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import cn.addapp.pickers.picker.DateTimePicker;
import cn.qqtheme.framework.picker.SinglePicker;
import cn.qqtheme.framework.widget.WheelView;

public class AddMarkActivity extends BaseActivity {

    @BindView(R.id.add_mark_senior_setting)
    CheckBox seniorCheckBox; //高级设置开关
    @BindView(R.id.add_mark_gaoji_layout)
    LinearLayout gaojiLayout; //高级设置布局
    @BindView(R.id.add_mark_text_resion)
    TextAndText2View textResion; //投票结果展示
    @BindView(R.id.add_mark_text_time)
    TextAndText2View textTime; //结束时间
    @BindView(R.id.add_mark_text_niming_check)
    CheckBox nimingCheck; //选中问题

    @BindView(R.id.add_mark_edit_title)
    EditText editTitle;
    @BindView(R.id.add_mark_add_img)
    ImageView addImag; //添加图片
    @BindView(R.id.add_mark_add_display_img)
    ImageView addDisplayImag; //覆盖在图片上的默认图片
    @BindView(R.id.add_mark_edit_dec)
    EditText editDec; //描述
    @BindView(R.id.add_mark_text_oo)
    TextAndText2View markTextOO; //打分对象


    @BindView(R.id.add_mark_text_add_man)
    TextAndText2View markAddMan; //参与打分
    @BindView(R.id.add_mark_text_add_mark)
    TextAndText2View markAddMark; //设置分数


    @BindView(R.id.add_mark_add_btn)
    Button addBtn;

    private List<String> singleList;
    private List<String> resionShowList;

    private HashSet<Integer> partListSet;
    private HashSet<Integer> userListSet;

    private HashSet<Integer> userMarkSet;
    private SelectorTime[] addMarKStrings;

    @Override
    protected int getActivityView() {
        return R.layout.activity_add_mark;
    }

    @Override
    protected void initView() {
        super.initView();

        markTextOO.getTextHintText();
        singleList = new ArrayList<>();
        singleList.add("单选");
        singleList.add("多选");

        resionShowList = new ArrayList<>();
        resionShowList.add("结束投票后可见");
        resionShowList.add("发布即可见");

        addMarKStrings = new SelectorTime[]{
                new SelectorTime("5分", 5)
                , new SelectorTime("10分", 10)
                , new SelectorTime("60分", 60)
                , new SelectorTime("100分", 100)

        };

        editTextCountControl(R.id.add_mark_edit_dec, editDec, new TextView(this));
    }

    @Override
    protected void initEvent() {
        super.initEvent();
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

        //图片添加
        addImag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(AddMarkActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .compress(true)
                        .maxSelectNum(1).forResult(102);
            }
        });

        //打分对象
        markTextOO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NoticeReciveRangeActivity.class);
                intent.putExtra("isPartSelect", NoticeReciveRangeActivity.PART_NOT_SELECTOR);
                startActivityForResult(intent, 201);
            }
        });

        //参与打分
        markAddMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NoticeReciveRangeActivity.class);
                startActivityForResult(intent, 202);
            }
        });

        markAddMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> stringList = new ArrayList<>();
                for (SelectorTime addMarKString : addMarKStrings) {
                    stringList.add(addMarKString.time);
                }
                onConstellationPicker(stringList);
            }
        });

        //发送
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
    }

    private int defaultSingle = 1; //单选
    public static final int type_single = 10;
    private int defaultSubmitShow = 2;//发布后即可见
    public static final int type_show = 11;
    private String endTime;
    private String imgPath;
    private int addMarkValue = 5; //默认5分数

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
            showToast("打分说明最多200个字");
            return;
        }

//        if (TextUtils.isEmpty(imgPath)) {
//            showToast("请选择一张图片");
//            return;
//        }

//        //冒失可以是空的
//        String pingYiValue = textAndTextPingyi.getTextRightText().getText().toString();

        showProgressDialog("");

        ImageParams params = new ImageParams();
        params.put("oi", params.getUserId());
        params.put("mi", getuserMarkValue());
        params.put("ui", getUserValue());
        params.put("pi", getPartValue());
        params.put("te", editTile);
        // TODO 说明的长度限制问题
        params.put("vd", editDec.getText().toString());

        params.put("se", addMarkValue); //分数

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

        if (!TextUtils.isEmpty(imgPath)) {
            params.addCompressImagePath("img", new File(imgPath), getCacheDir() + "/img.png", 80);
        }

        RequestUtils.createRequest().postVoteAddMark(params.getImgData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                closeProgressDialog();
                String vote_id = (String) ((LinkedTreeMap) baseModel.getData()).get("vote_id");
                int voteId = Integer.parseInt(vote_id);
                Intent intent = new Intent(mContext, MineInitiateVoteActivity.class);
                intent.putExtra("vi", voteId);
                intent.putExtra("type", 1); //1.从我发起 2.去评议
                intent.putExtra("vote_type", 2);//1.投票 2.打分
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
//        picker.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                String year = picker.getSelectedYear();
//                String month = picker.getSelectedMonth();
//                String day = picker.getSelectedDay();
//                String hour = picker.getSelectedHour();
//                String minute = picker.getSelectedMinute();
//
//                endTime = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":00";
//                textTime.getTextRightText().setText(endTime);
//            }
//        });
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
//                    textSingle.getTextRightText().setText(selectTypeWindow.stringType);
//                    defaultSingle = singleList.indexOf(selectTypeWindow.stringType) + 1;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                LocalMedia media = selectList.get(0);
                imgPath = media.getPath();
                if (!TextUtils.isEmpty(imgPath)) {
                    addDisplayImag.setVisibility(View.GONE);
                    FileInputStream is = null;
                    try {
                        is = new FileInputStream(imgPath);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        addImag.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } else if (requestCode == 201 && resultCode == NoticeReciveRangeActivity.NOTICE_RESULT_OK && data != null) {
            //打分对象
            userMarkSet = (HashSet<Integer>) data.getSerializableExtra("userList");
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

            markTextOO.getTextRightText().setText(value);

        } else if (requestCode == 202 && resultCode == NoticeReciveRangeActivity.NOTICE_RESULT_OK && data != null) {
            //参与打分
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

            markAddMan.getTextRightText().setText(value);
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

    //打分对象设置的
    private String getuserMarkValue() {
        String value = "";
        if (userMarkSet == null) {
            return value;
        }
        for (Integer integer : userMarkSet) {
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

    public void onConstellationPicker(List<String> strings) {
        final SinglePicker<String> picker = new SinglePicker<>(this, strings);
        picker.setTopBackgroundColor(0xFFEEEEEE);
//        picker.setHeight(801);
        picker.setTopHeight(52);
        picker.setTopLineColor(0xFF33B5E5);
        picker.setTopLineHeight(1);
        picker.setTitleText("");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(18);
        picker.setCancelTextColor(0xFF999999);
        picker.setCancelTextSize(18);
        picker.setSubmitTextColor(0xFF33B5E5);
        picker.setSubmitTextSize(18);
        /*picker.setSelectedTextColor(0xFF333333);
        picker.setUnSelectedTextColor(0xFF999999);
        picker.setWheelModeEnable(false);*/
        WheelView.LineConfig config = new WheelView.LineConfig();
        config.setColor(0xFFD3D3D3);//线颜色
        config.setAlpha(120);//线透明度
//        config.setRatio(1);//线比率
        picker.setLineConfig(config);
        picker.setItemWidth(500);
        picker.setBackgroundColor(0xFFffffff);
        //picker.setSelectedItem(isChinese ? "处女座" : "Virgo");
        picker.setSelectedIndex(0);
        picker.show();

        picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                markAddMark.getTextRightText().setText(item);
                addMarkValue = addMarKStrings[index].value;
            }
        });
    }

    class SelectorTime {
        public SelectorTime(String time, int value) {
            this.time = time;
            this.value = value;
        }

        String time;
        int value;
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
