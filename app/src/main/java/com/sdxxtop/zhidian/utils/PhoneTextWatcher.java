package com.sdxxtop.zhidian.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 作者：CaiCM
 * 日期：2018/3/24  时间：10:24
 * 邮箱：15010104100@163.com
 * 描述：设置手机号格式工具类（344：eg：188 8888 8888）
 */

public class PhoneTextWatcher implements TextWatcher {

    private EditText etPwd;
    private ImageView ivDelPhone;
    private EditText _text;

    public PhoneTextWatcher(EditText _text) {
        this._text = _text;
    }

    public PhoneTextWatcher(EditText _text, ImageView ivDelPhone, EditText etPwd) {
        this._text = _text;
        this.ivDelPhone = ivDelPhone;
        this.etPwd = etPwd;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        s = trim(s);
        if (s == null || s.length() == 0) {
            if (ivDelPhone != null) {
                ivDelPhone.setVisibility(View.GONE);
            }
            if (etPwd != null) {
                etPwd.setText("");
            }
            return;
        }

        if (ivDelPhone != null && _text.isFocused()) {
            ivDelPhone.setVisibility(View.VISIBLE);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(s.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
        if (!sb.toString().equals(s.toString())) {
            int index = start + 1;
            if (sb.charAt(start) == ' ') {
                if (before == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (before == 1) {
                    index--;
                }
            }
            _text.setText(sb.toString());
            _text.setSelection(index);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public static CharSequence trim(CharSequence cs) {
        int len = cs.length();
        int st = 0;

        while ((st < len) && (cs.charAt(st) <= ' ')) {
            st++;
        }
        while ((st < len) && (cs.charAt(len - 1) <= ' ')) {
            len--;
        }
        return ((st > 0) || (len < cs.length())) ? cs.subSequence(st, len) : cs;
    }
}
