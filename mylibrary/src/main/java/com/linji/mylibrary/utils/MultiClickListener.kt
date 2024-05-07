package com.linji.mylibrary.utils

import android.os.SystemClock
import android.util.Log
import android.view.View

public abstract class MultiClickListener : View.OnClickListener {
    private var count = 5 // 默认连续点击5次
    private var hits: LongArray = LongArray(count) //记录点击次数
    private var duration: Long = 2000L // 默认有效时间

    constructor()

    constructor(count: Int, duration: Long) {
        this.count = count
        this.duration = duration
    }

    override fun onClick(v: View?) {
        // 将 hits 数组内所有元素左移一个位置
        System.arraycopy(hits, 1, hits, 0, hits.size - 1)
        // 获取当前系统已经启动的时间
        hits[hits.size - 1] = SystemClock.uptimeMillis()
        if (hits[0] >= (SystemClock.uptimeMillis() - duration)) {
            // 在有效时间内已经连续点击了 count 次，算一次有效点击
            onClickValid(v)
            Log.i("MainActivity", "valid click")
            // 将所有时间重置
            hits.forEachIndexed { index, _ ->
                hits[index] = 0
            }
        } else {
            Log.i("MainActivity", "not valid click")
        }
    }

    public abstract fun onClickValid(v: View?)
}
