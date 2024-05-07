package com.linji.mylibrary.utils

import android.view.KeyEvent

object Util {
    @JvmStatic
    fun getInputCode(event: KeyEvent): Char {
        val keyCode = event.keyCode
        val aChar: Char = if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            //字母
            ('A' + keyCode - KeyEvent.KEYCODE_A)
        } else if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            //数字
            ('0' + keyCode - KeyEvent.KEYCODE_0)
        } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
            0.toChar()
        } else {
            //其他符号
            event.unicodeChar.toChar()
        }
        return aChar

    }
}