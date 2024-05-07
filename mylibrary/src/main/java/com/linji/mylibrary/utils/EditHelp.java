package com.linji.mylibrary.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditHelp {
    /*** 禁止EditText输入特殊字符** @param editText EditText输入框*/
    public static void setEditTextInputSpeChat(EditText editText, int length) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;' ,\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？_ ']";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        InputFilter.LengthFilter lengthFilter = new InputFilter.LengthFilter(length);
        editText.setFilters(new InputFilter[]{filter, lengthFilter});
    }
}
