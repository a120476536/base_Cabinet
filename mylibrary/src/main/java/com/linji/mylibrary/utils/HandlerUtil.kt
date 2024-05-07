package com.linji.mylibrary.utils

import android.os.Handler

object HandlerUtil {
    private var handler: Handler? = null

    val instance: Handler
        @JvmStatic
        @Synchronized get() {
            if (handler == null) {
                handler = Handler()
            }
            return handler!!
        }
}
