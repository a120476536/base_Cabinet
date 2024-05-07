package com.linji.mylibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.linji.mylibrary.R;

public class BorderTextView extends AppCompatTextView {
    public static final float DEFAULT_STROKE_WIDTH = 0.5f;    // 默认边框宽度, 1dp
    public static final float DEFAULT_CORNER_RADIUS = 2.0f;   // 默认圆角半径, 2dp
    public static final float DEFAULT_LR_PADDING = 6f;      // 默认左右内边距
    public static final float DEFAULT_TB_PADDING = 2f;      // 默认上下内边距

    private int strokeWidth;    // 边框线宽
    private int strokeColor;    // 边框颜色
    private int cornerRadius;   // 圆角半径
    private int solidColor;   // 填充颜色
    private boolean mFollowTextColor; // 边框颜色是否跟随文字颜色

    private boolean mIsGradient; // 是否是渐变背景
    private int solidStartColor;   // 填充颜色
    private int solidEndColor;   // 填充颜色
    private int solidCenterColor;   // 填充颜色
    private int angle;   // 颜色角度

    private Paint mPaint = new Paint();     // 画边框所使用画笔对象
    private RectF mRectF;                   // 画边框要使用的矩形

    public BorderTextView(Context context) {
        this(context, null);
    }

    public BorderTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 将DIP单位默认值转为PX
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        strokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_STROKE_WIDTH, displayMetrics);
        cornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_CORNER_RADIUS, displayMetrics);

        // 读取属性值
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BorderTextView);
        strokeWidth = ta.getDimensionPixelSize(R.styleable.BorderTextView_strokeWidth, strokeWidth);
        cornerRadius = ta.getDimensionPixelSize(R.styleable.BorderTextView_cornerRadius, cornerRadius);
        strokeColor = ta.getColor(R.styleable.BorderTextView_strokeColor, Color.TRANSPARENT);
        solidColor = ta.getColor(R.styleable.BorderTextView_solidColor, Color.TRANSPARENT);
        mFollowTextColor = ta.getBoolean(R.styleable.BorderTextView_followTextColor, false);
        mIsGradient = ta.getBoolean(R.styleable.BorderTextView_isGradient, false);
        solidStartColor = ta.getColor(R.styleable.BorderTextView_solidStartColor, Color.TRANSPARENT);
        solidCenterColor = ta.getColor(R.styleable.BorderTextView_solidCenterColor, Color.TRANSPARENT);
        solidEndColor = ta.getColor(R.styleable.BorderTextView_solidEndColor, Color.TRANSPARENT);
        angle = ta.getInteger(R.styleable.BorderTextView_solidangle, 0);
        ta.recycle();

        mRectF = new RectF();

        // 如果使用时没有设置内边距, 设置默认边距
        int paddingLeft = getPaddingLeft() == 0 ? (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LR_PADDING, displayMetrics) : getPaddingLeft();
        int paddingRight = getPaddingRight() == 0 ? (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LR_PADDING,
                displayMetrics) : getPaddingRight();
        int paddingTop = getPaddingTop() == 0 ? (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TB_PADDING, displayMetrics) : getPaddingTop();
        int paddingBottom = getPaddingBottom() == 0 ? (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TB_PADDING,
                displayMetrics) : getPaddingBottom();
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        mRectF.left = mRectF.top = 0.5f * strokeWidth;
        mRectF.right = getMeasuredWidth() - strokeWidth;
        mRectF.bottom = getMeasuredHeight() - strokeWidth;
        if (mIsGradient) {
            int[] mColors;
            if (solidCenterColor != Color.TRANSPARENT) {
                mColors = new int[]{solidStartColor, solidCenterColor, solidEndColor};
            } else {
                mColors = new int[]{solidStartColor, solidEndColor};
            }
            float x0 = 0, y0 = 0, x1 = 0, y1 = 0;
            angle = angle % 360;
            switch (angle) {
                case 0:
                    x0 = 0;
                    y0 = 0;
                    x1 = getMeasuredWidth();
                    y1 = 0;
                    break;
                case 45:
                    x0 = 0;
                    y0 = getMeasuredHeight();
                    x1 = getMeasuredWidth();
                    y1 = 0;
                    break;
                case 90:
                    x0 = 0;
                    y0 = getMeasuredHeight();
                    x1 = 0;
                    y1 = 0;
                    break;
                case 135:
                    x0 = getMeasuredWidth();
                    y0 = getMeasuredHeight();
                    x1 = 0;
                    y1 = 0;
                    break;
                case 180:
                    x0 = getMeasuredWidth();
                    y0 = 0;
                    x1 = 0;
                    y1 = 0;
                    break;
                case 225:
                    x0 = getMeasuredWidth();
                    y0 = 0;
                    x1 = 0;
                    y1 = getMeasuredHeight();
                    break;
                case 270:
                    x0 = 0;
                    y0 = 0;
                    x1 = 0;
                    y1 = getMeasuredHeight();
                    break;
                case 315:
                    x0 = 0;
                    y0 = 0;
                    x1 = getMeasuredWidth();
                    y1 = getMeasuredHeight();
                    break;
            }
            LinearGradient linearGradient = new LinearGradient(x0, y0, x1, y1, mColors, null, Shader.TileMode.CLAMP);
            mPaint.setShader(linearGradient);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(mRectF, cornerRadius, cornerRadius, mPaint);
            mPaint.setShader(null);
        } else {
            if (solidColor != Color.TRANSPARENT) {
                mPaint.setColor(solidColor);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawRoundRect(mRectF, cornerRadius, cornerRadius, mPaint);
            }
            mPaint.setStyle(Paint.Style.STROKE);     // 空心效果
            mPaint.setAntiAlias(true);               // 设置画笔为无锯齿
            mPaint.setStrokeWidth(strokeWidth);      // 线宽

            // 设置边框线的颜色, 如果声明为边框跟随文字颜色且当前边框颜色与文字颜色不同时重新设置边框颜色
            if (mFollowTextColor && strokeColor != getCurrentTextColor()) {
                strokeColor = getCurrentTextColor();
            }

            mPaint.setColor(strokeColor);
            // 画空心圆角矩形
            canvas.drawRoundRect(mRectF, cornerRadius, cornerRadius, mPaint);
        }
        super.onDraw(canvas);
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        invalidate();
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        invalidate();
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        invalidate();
    }

    public void setSolidColor(int solidColor) {
        this.mIsGradient = false;
        this.solidColor = solidColor;
        invalidate();
    }

    public void setFollowTextColor(boolean mFollowTextColor) {
        this.mFollowTextColor = mFollowTextColor;
        invalidate();
    }

    public void setGradientColor(int startColor, int endColor, int angle) {
        this.mIsGradient = true;
        this.solidStartColor = startColor;
        this.solidEndColor = endColor;
        this.angle = angle;
        invalidate();
    }

    public void setGradientColor(int startColor, int centerColor, int endColor, int angle) {
        this.mIsGradient = true;
        this.solidStartColor = startColor;
        this.solidCenterColor = centerColor;
        this.solidEndColor = endColor;
        this.angle = angle;
        invalidate();
    }
}
