package com.sdxxtop.zhidian.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.utils.ATSpan;
import com.sdxxtop.zhidian.utils.KeyboardUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/6/25.
 */

public class SendEditLayout extends LinearLayout {

    private EditText mSendEdit;
    private Button mSendBtn;

    public SendEditLayout(Context context) {
        this(context, null);
    }

    public SendEditLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SendEditLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_send_edit, this, true);
        mSendEdit = (EditText) findViewById(R.id.mine_work_detail_send_edit);
        mSendBtn = (Button) findViewById(R.id.mine_work_detail_send_btn);

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable editable = mSendEdit.getText();

                SendBean sendBean = convertSpannedToRichText(editable);
                if (TextUtils.isEmpty(editable)) {
                    ToastUtil.show("请输入要回复内容");
                    return;
                }

                int comUserId = sendBean.comUserId;
                String content = sendBean.content;
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.show("请输入要回复内容");
                    return;
                }

                if (editSendClickListener != null) {
                    editSendClickListener.sendClick(comUserId, sendBean.parentId, content);
                }
//                submit("", "", content);
            }
        });

        mSendEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectionStart = mSendEdit.getSelectionStart();

                ATSpan[] atSpans = mSendEdit.getText().getSpans(0, mSendEdit.getText().length(), ATSpan.class);
                int length = atSpans.length;

                if (0 == length) {
                    return;
                }

                for (ATSpan atSpan : atSpans) {
                    int start = mSendEdit.getText().getSpanStart(atSpan);
                    int end = mSendEdit.getText().getSpanEnd(atSpan);
                    if (selectionStart >= start && selectionStart <= end) {
                        mSendEdit.setSelection(end);
                        return;
                    }
                }
            }
        });

        mSendEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {

                    int selectionStart = mSendEdit.getSelectionStart();

                    ATSpan[] atSpans = mSendEdit.getText().getSpans(0, mSendEdit.getText().length(), ATSpan.class);
                    int length = atSpans.length;

                    if (0 == length) {
                        return false;
                    }

                    for (ATSpan atSpan : atSpans) {
                        int start = mSendEdit.getText().getSpanStart(atSpan);
                        int end = mSendEdit.getText().getSpanEnd(atSpan);
                        if (selectionStart >= start && selectionStart <= end) {
                            mSendEdit.getText().delete(start, end);
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    public void sendKeyBoard(String name, int userId, int parentId) {
        mSendEdit.setText("");
        CharSequence toCharSequence = toCharSequence(name, userId, parentId);
        mSendEdit.setText(toCharSequence);
        mSendEdit.setSelection(toCharSequence.length());
        KeyboardUtils.showKeyBoard(mSendEdit);
    }

    private CharSequence toCharSequence(String name, int userId, int parentId) {
        SpannableString spannable = new SpannableString("@" + name + " ");
        int length = spannable.length();
        if (length > 0) {
            spannable.setSpan(
                    new ATSpan(userId, parentId),
                    0,
                    length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        return spannable;
    }


    public static SendBean convertSpannedToRichText(Spanned spanned) {
        SendBean sendBean = new SendBean();
        List<CharacterStyle> spanList =
                Arrays.asList(spanned.getSpans(0, spanned.length(), CharacterStyle.class));
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(spanned);
        for (CharacterStyle characterStyle : spanList) {
            int start = stringBuilder.getSpanStart(characterStyle);
            int end = stringBuilder.getSpanEnd(characterStyle);
            if (start >= 0) {
                int[] htmlStyle = handleCharacterStyle(characterStyle,
                        stringBuilder.subSequence(start, end).toString());
                if (htmlStyle != null) {
                    if (htmlStyle.length >= 2) {
                        int comUserId = htmlStyle[0];
                        int parentId = htmlStyle[1];
                        sendBean.comUserId = comUserId;
                        sendBean.parentId = parentId;
                    }
                    stringBuilder.replace(start, end, "");
                }
            }
        }
        sendBean.content = stringBuilder.toString();
        return sendBean;
    }

    private static int[] handleCharacterStyle(CharacterStyle characterStyle, String text) {
        if (characterStyle instanceof ATSpan) {
            if (TextUtils.isEmpty(text)) {
                return null;
            }
            ATSpan span = (ATSpan) characterStyle;
            return new int[]{span.getUserId(), span.getParentId()};
        }
        return null;
    }

    public void setEditSuccess() {
        mSendEdit.setText("");
        KeyboardUtils.hideKeyBoard(mSendEdit);
    }

    static class SendBean {
        int comUserId;
        int parentId;
        String content;
    }

    EditSendClickListener editSendClickListener;

    public void setEditSendClickListener(EditSendClickListener editSendClickListener) {
        this.editSendClickListener = editSendClickListener;
    }

    public interface EditSendClickListener {
        void sendClick(int userId, int parentId, String content);
    }

}
