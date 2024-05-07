package com.linji.mylibrary.widget

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SeekBar
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.linji.mylibrary.R
import com.linji.mylibrary.banner.listener.OnVideoStateListener


/**
 * Created by zhanke on 2019/5/16.
 * Describe
 */
class MyJzvdStd : JzvdStd {

    private var listener: OnVideoStateListener? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun init(context: Context) {
        super.init(context)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        val i = v.id
        if (i == cn.jzvd.R.id.fullscreen) {
            Log.i(Jzvd.TAG, "onClick: fullscreen button")
        } else if (i == R.id.start) {
            Log.i(Jzvd.TAG, "onClick: start button")
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
//        super.onTouch(v, event)
//        val id = v.id
//        if (id == cn.jzvd.R.id.surface_container) {
//            when (event.action) {
//                MotionEvent.ACTION_UP -> {
//                    if (mChangePosition) {
//                        Log.i(Jzvd.TAG, "Touch screen seek position")
//                    }
//                    if (mChangeVolume) {
//                        Log.i(Jzvd.TAG, "Touch screen change volume")
//                    }
//                }
//            }
//        }
        Log.e("TAG ","onTouch")
        if (state != Jzvd.STATE_PAUSE) {
            Jzvd.goOnPlayOnPause()
            Jzvd.goOnPlayOnResume()
        }
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.jz_layout_std
    }

    fun setSeekBarBack(color: Drawable) {
        progressBar.progressDrawable = color
    }

    fun setBottomProgressBarBack(color: Drawable) {
        bottomProgressBar.progressDrawable = color
    }

    override fun startVideo() {
        super.startVideo()
        Log.i(Jzvd.TAG, "startVideo")
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        super.onStopTrackingTouch(seekBar)
        Log.i(Jzvd.TAG, "Seek position ")
    }

    override fun gotoScreenFullscreen() {
        super.gotoScreenFullscreen()
        Log.i(Jzvd.TAG, "goto Fullscreen")
    }

    override fun gotoScreenNormal() {
        super.gotoScreenNormal()
        Log.i(Jzvd.TAG, "quit Fullscreen")
    }

    override fun autoFullscreen(x: Float) {
        super.autoFullscreen(x)
        Log.i(Jzvd.TAG, "auto Fullscreen")
    }

    override fun onClickUiToggle() {
        super.onClickUiToggle()
        Log.i(Jzvd.TAG, "onClickUiToggle")
    }

    //onState 代表了播放器引擎的回调，播放视频各个过程的状态的回调
    override fun onStateNormal() {
        super.onStateNormal()
        Log.i(Jzvd.TAG, "onStateNormal")
    }

    override fun onStatePreparing() {
        super.onStatePreparing()
        Log.i(Jzvd.TAG, "onStatePreparing")
    }

    override fun onStatePlaying() {
        super.onStatePlaying()
        if (listener != null) {
            listener!!.onVideoPlaying()
        }
    }

    override fun onStatePause() {
        super.onStatePause()
//        Jzvd.goOnPlayOnPause()
        Log.i(Jzvd.TAG, "onStatePause")
        if (listener != null) {
            listener!!.onVideoPause()
        }
    }

    override fun onStateError() {
        super.onStateError()
        Log.i(Jzvd.TAG, "onStateError")
//        if (listener != null) {
//            listener!!.onVideoComplete()
//        }
    }

    override fun onStateAutoComplete() {
        super.onStateAutoComplete()
        Log.i(Jzvd.TAG, "onStateAutoComplete")
        if (listener != null) {
            listener!!.onVideoComplete()
        }
    }

    //changeUiTo 真能能修改ui的方法
    override fun changeUiToNormal() {
        super.changeUiToNormal()
    }

    override fun changeUiToPreparing() {
        super.changeUiToPreparing()
    }

    override fun changeUiToPlayingShow() {
//        super.changeUiToPlayingShow()
        findViewById<ProgressBar>(R.id.bottom_progress).visibility = View.GONE
    }

    override fun changeUiToPlayingClear() {
        super.changeUiToPlayingClear()
    }

    override fun changeUiToPauseShow() {
//        super.changeUiToPauseShow()
        findViewById<LinearLayout>(R.id.layout_bottom).visibility = View.GONE
        findViewById<ProgressBar>(R.id.loading).visibility = View.GONE
    }

    override fun changeUiToPauseClear() {
        super.changeUiToPauseClear()
    }

    override fun changeUiToComplete() {
        super.changeUiToComplete()
    }

    override fun changeUiToError() {
        super.changeUiToError()
    }

    override fun onInfo(what: Int, extra: Int) {
        super.onInfo(what, extra)
    }

    override fun onError(what: Int, extra: Int) {
        super.onError(what, extra)
    }

    override fun showWifiDialog() {
        val builder = AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert)
        builder.setMessage(resources.getString(cn.jzvd.R.string.tips_not_wifi))
        builder.setPositiveButton(resources.getString(cn.jzvd.R.string.tips_not_wifi_confirm)) { dialog, _ ->
            dialog.dismiss()
            startVideo()
            Jzvd.WIFI_TIP_DIALOG_SHOWED = true
        }
        builder.setNegativeButton(resources.getString(cn.jzvd.R.string.tips_not_wifi_cancel)) { dialog, _ ->
            dialog.dismiss()
            clearFloatScreen()
        }
        builder.setOnCancelListener { it.dismiss() }
        builder.create().show()
    }

    fun setOnVideoStateListener(listener: OnVideoStateListener) {
        this.listener = listener
    }

}
