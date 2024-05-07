package com.linji.mylibrary.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.linji.mylibrary.R


@SuppressLint("NewApi")
/**
 * 下拉列表框控件
 * @author caizhiming
 */
class XCDropDownListView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    LinearLayout(context, attrs, defStyle) {

    private var editText: TextView? = null
    private var imageView: ImageView? = null
    private var popupWindow: PopupWindow? = null
    private var dataList: List<XCDropDownListBean> = ArrayList()
    private var xcGravity: Int = Gravity.CENTER

    //    private var textSize:Float = 12f
//    private var rightDrawable:Drawable? = null
//    private var editTextBackGround:Drawable?=null
    private lateinit var view: View

    var selectItem: XCDropDownListBean? = null
    var listWidth: Float = 0f

    constructor(context: Context) : this(context, null) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
//        val ta: TypedArray =
//            context.obtainStyledAttributes(attrs, R.styleable.XCDropDownListCabintView)
//        textSize = ta.getFloat(R.styleable.XCDropDownListCabintView_android_textSize,12f)
//        editTextBackGround = ta.getDrawable(R.styleable.XCDropDownListCabintView_android_editTextBackground)
//        rightDrawable = ta.getDrawable(R.styleable.XCDropDownListCabintView_editTextRightIcon)
        initView()
    }

    init {
        initView()
    }

    fun initView() {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = layoutInflater.inflate(R.layout.dropdownlist_view, this, true)

        editText = findViewById(R.id.text)
        editText!!.gravity = Gravity.CENTER
//        editText!!.textSize = textSize
//        if (editTextBackGround != null)
//            editText!!.background = editTextBackGround
        imageView = findViewById(R.id.btn)
//        if (rightDrawable != null)
//            imageView?.setImageDrawable(rightDrawable)
        this.setOnClickListener {
            if (popupWindow == null) {
                showPopWindow()
            } else {
                closePopWindow()
            }
        }
    }

    /**
     * 打开下拉列表弹窗
     */
    private fun showPopWindow() {
        imageView!!.rotation = 180f
        // 加载popupWindow的布局文件
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = layoutInflater.inflate(R.layout.dropdownlist_popupwindow, null, false)
//        contentView.setBackgroundColor(Color.parseColor("#468BFF"))
        val listView = contentView.findViewById<RecyclerView>(R.id.listView)
        val adapter = XCDropDownListAdapter(dataList)
        listView.layoutManager = LinearLayoutManager(context)
        listView.addItemDecoration(
            SimpleDividerItemDecoration(context)

        )
        listView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        listView.adapter = adapter
        popupWindow = PopupWindow(
            contentView,
            SizeUtils.dp2px(layoutParams.width.toFloat()),
            LayoutParams.WRAP_CONTENT
        )
        popupWindow!!.width = editText!!.measuredWidth
        popupWindow!!.setBackgroundDrawable(resources.getDrawable(R.color.transparent))
        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.showAsDropDown(view)
        popupWindow!!.setOnDismissListener { closePopWindow() }
    }

    /**
     * 关闭下拉列表弹窗
     */
    private fun closePopWindow() {
        imageView!!.rotation = 0f
        popupWindow!!.dismiss()
        popupWindow = null
    }

    /**
     * 设置数据
     *
     * @param list
     */
    fun setItemsData(list: List<XCDropDownListBean>) {
        dataList = list
    }

    fun setItemsData(strings: Array<String>) {
        val array: ArrayList<XCDropDownListBean> = ArrayList()
        val list = listOf(strings)[0]
        for (i in list.indices) {
            val data = list[i]
            array.add(XCDropDownListBean(i, data))
        }
        dataList = array
    }


    fun currentPosition(position: Int) {
        if (dataList.isNotEmpty()) {
            editText!!.text = dataList[position].name
            selectItem = dataList[position]
        }
    }

    fun currentPosition(name: String) {
        if (dataList.isNotEmpty()) {
            var position = 0
            for (i in dataList.indices) {
                val bean: XCDropDownListBean = dataList[i]
                if (bean.name == name) {
                    position = i
                    break
                }
            }
            editText!!.text = dataList[position].name
            selectItem = dataList[position]
        }
    }

    fun setHint(hint: String) {
        editText!!.hint = hint
        invalidate()
    }

    fun setXCGravity(gravity: Int) {
        this.xcGravity = gravity
        editText!!.gravity = xcGravity
    }

    fun setTextSize(size: Float) {
        editText!!.textSize = size
        invalidate()
    }

    fun setTestColor(color: String) {
        editText!!.setTextColor(Color.parseColor(color))
    }

    fun setTextRightIcon(drawableIcon: Int) {
        imageView!!.setImageResource(drawableIcon)
        invalidate()
    }

    fun setTextRightIcon(drawable: Drawable) {
        imageView!!.setImageDrawable(drawable)
        invalidate()
    }

    fun setEditBackgroundColor(editTextBackGround: String) {
        editText!!.setBackgroundColor(Color.parseColor(editTextBackGround))
        invalidate()
    }

    internal inner class XCDropDownListAdapter(data: List<XCDropDownListBean>?) :
        BaseQuickAdapter<XCDropDownListBean, BaseViewHolder>(R.layout.dropdown_list_item, data) {

        override fun convert(helper: BaseViewHolder, item: XCDropDownListBean) {
            helper.setText(R.id.tv, item.name)
            val text = helper.getView<TextView>(R.id.tv)
            text.gravity = xcGravity
            helper.getView<View>(R.id.layout_container).setOnClickListener { v ->
                editText!!.text = item.name
                selectItem = item

                if (dropDownListener != null) {
                    dropDownListener!!.onClick(item)
                }
                closePopWindow()
            }
        }
    }

    interface XCDropDownListener {
        fun onClick(bean: XCDropDownListBean)
    }

    public var dropDownListener: XCDropDownListener? = null

    open class XCDropDownListBean constructor(id: Int, value: String) {
        var id: Int = 0
        var name: String = ""

        init {
            this.id = id
            this.name = value
        }

        override fun toString(): String {
            return "XCDropDownListBean(id=$id, values='$name')"
        }
    }
}