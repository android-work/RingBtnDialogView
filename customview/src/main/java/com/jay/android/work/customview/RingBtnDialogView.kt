package com.jay.android.work.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupWindow
import androidx.core.view.marginTop


public class RingBtnDialogView : View {
    private lateinit var mViewClickListener: ViewClickListener
    private var mPaint: Paint = Paint()
    private var mWidth: Int = 0
    private var mPath: Path = Path()
    private var mPaint1: Paint = Paint()
    private var mPath1: Path = Path()
    private var mHeight: Int = 0
    private var mColor: Int = Color.RED
    private var mDefaultSize: Int = 400
    private var mStrokeWidth: Float = 40f
    private var mRadio: Float = 200f

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context,attributeSet, defStyle )

    init {
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mStrokeWidth
        mPaint.color = mColor

        mPaint1.isAntiAlias = true
        mPaint1.style = Paint.Style.STROKE
        mPaint1.color = Color.WHITE
    }

    /**
     * 设置画笔颜色
     */
    fun setFirstColor(color: Int) {
        mColor = color
    }

    /**
     * 设置默认view大小
     */
    fun setDefaultSize(defaultSize: Int) {
        mDefaultSize = defaultSize
    }

    /**
     * 设置圆环宽度
     */
    fun setStrokeWidth(strokeWidth: Float) {
        mStrokeWidth = strokeWidth
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = getDefaultSizes(mDefaultSize, widthMeasureSpec)
        mHeight = getDefaultSizes(mDefaultSize, heightMeasureSpec)
        setMeasuredDimension(mWidth, mHeight)
    }

    private fun getDefaultSizes(defaultSize: Int, measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        return when (specMode) {
            MeasureSpec.EXACTLY -> specSize
            else -> defaultSize
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.translate(mWidth / 2f, mHeight / 2f)
//        canvas?.drawCircle(0f,0f,mRadio,mPaint)
        mPath.addCircle(0f, 0f, mRadio, Path.Direction.CCW)
        mPath1.addCircle(0f,0f,mRadio,Path.Direction.CCW)
        canvas?.drawPath(mPath, mPaint)
        canvas?.drawPath(mPath1,mPaint1)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action;
        //是否在第一个圈内
        if (action == MotionEvent.ACTION_DOWN) {
            val leftX = x
            val topY = y
            Log.e("tag", "leftX-->$leftX    topY-->$topY")

            judgmentEffectEvent(leftX,topY,event)
        } else {
            Log.i("tag", "other event...")
        }
        return super.onTouchEvent(event)
    }

    /**
     * 判断有效事件
     */
    private fun judgmentEffectEvent(leftX: Float,topY: Float,event: MotionEvent?) {
        //获取点击的位置坐标
        val downX: Float = event?.x ?: 0f
        val downY: Float = event?.y ?: 0f
        Log.e("tag"," x--->$downX      y--->$downY")
        //计算圆心坐标
        val rX = mWidth / 2
        val rY = mWidth / 2
        Log.e("tag"," rX--->$rX     rY--->$rY")
        //计算点击事件的临界值
        val boundX = downX - rX
        val boundY = downY - rY
        Log.e("tag"," boundX--->$boundX     boundY--->$boundY")
        //是否大于半径且小于半径+圆环宽度
        val boundXPow = Math.pow(boundX.toDouble(), 2.0)
        val boundYPow = Math.pow(boundY.toDouble(), 2.0)
        val towBoundPow = boundXPow + boundYPow;
        Log.e(
            "tag",
            "towBoundPow--->$towBoundPow    --->${Math.pow(
                (mRadio + mStrokeWidth).toDouble(),
                2.0
            )}    ${Math.pow(mRadio.toDouble(), 2.0)}"
        )
        if (towBoundPow >= Math.pow(
                (mRadio - mStrokeWidth/2).toDouble(),
                2.0
            ) && towBoundPow < Math.pow((mRadio + mStrokeWidth/2).toDouble(), 2.0)
        ) {
            //在环内,定义点击事件回调
            mViewClickListener.onClick()

            showPopupWindow(leftX+downX,topY+downY)
        }
    }

    private var popupWindow : PopupWindow ?= null
    private fun showPopupWindow(downX: Float, downY: Float) {
        if (popupWindow == null){
            popupWindow = PopupWindow()
        }
        popupWindow!!.dismiss()
        popupWindow!!.contentView = LayoutInflater.from(context).inflate(R.layout.dialog_layout,null)
        popupWindow!!.width = WRAP_CONTENT
        popupWindow!!.height = WRAP_CONTENT
        popupWindow!!.showAtLocation(this,(Gravity.TOP or Gravity.START), downX.toInt(), downY.toInt()+100)
    }

    /**
     * 设置点击事件
     */
    fun setViewClickListener(viewClickListener: ViewClickListener) {
        this.mViewClickListener = viewClickListener
    }

    /**
     * 点击事件的回调接口
     */
    public interface ViewClickListener {
        fun onClick();
    }
}