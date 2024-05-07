package com.linji.mylibrary.widget;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import java.nio.charset.StandardCharsets;

public class LengthFilter implements InputFilter {
    int nMax = 0;

    int keep = 0;

    private onMaxLengthListener onMaxLengthListener;

    public LengthFilter(int nMax) {
        this.nMax = nMax;
    }

    public LengthFilter(int nMax, onMaxLengthListener onMaxLengthListener) {
        this.nMax = nMax;
        this.onMaxLengthListener = onMaxLengthListener;
    }


    /**
     * source:新输入字符
     * start：新输入字符起始，一般0
     * end：新输入字符结尾，一般source.length
     * dest:老字符
     * dstart:插入老字符起始位置
     * dend：插入老字符结尾，一般dend=dstart
     *
     * @return null:不修改
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String destAdjust = dest.toString();
            String filterString = "";
            if (dstart != dend) {
                String destAdjust1 = dest.toString().substring(0, dstart);
                String destAdjust2 = dest.toString().substring(dend);
                destAdjust = destAdjust1 + destAdjust2;
                filterString = dest.toString().substring(dstart, dend);
                Log.d("LengthFilter", "filter: destAdjust1 = [" + destAdjust1 + "], destAdjust2 = [" + destAdjust2 + "], destAdjust = [" + end + "]");

            }
            keep = nMax - destAdjust.getBytes(StandardCharsets.UTF_8).length - source.toString().getBytes(StandardCharsets.UTF_8).length;
            Log.d("LengthFilter", "filter: source = [" + source + "], start = [" + start + "], end = [" + end + "], dstart = [" + dstart + "]" + ", dend=" + dend + ", keep=" + keep + ", destAdjust=" + destAdjust);
            if (keep < 0) {
                //此已输入的字符大于限定长度nMax
                if (onMaxLengthListener != null) {
                    onMaxLengthListener.onMaxLengthCallBack();
                }
                return filterString;
            } else {
                //此已输入的字符小于限定长度nMax
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public interface onMaxLengthListener {
        void onMaxLengthCallBack();
    }

    public void setOnMaxLengthListener(onMaxLengthListener onMaxLengthListener) {
        this.onMaxLengthListener = onMaxLengthListener;
    }
}
