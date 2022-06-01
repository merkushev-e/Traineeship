package com.testtask.traineeship

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.icu.util.Calendar
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.cos
import kotlin.math.sin

class Clock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var mHeight: Int = 0
    private var mWidth: Int = 0
    private var fontSize: Int = 0
    private val numMarg = 50
    private var minArrow = 0
    private var hourArrow = 0
    private var radius = 0
    private var space = 0
    private var paint: Paint? = null
    private val numbers = listOf<Int>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val rect = Rect()
    private var isInit = false


    private fun initClock() {
        mHeight = height
        mWidth = width
        space = numMarg
        fontSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20F, resources.displayMetrics)
                .toInt()
        val min = Math.min(mHeight, mWidth)
        radius = min / 2 - space
        minArrow = min / 20
        hourArrow = min / 10
        paint = Paint()
        isInit = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!isInit) {
            initClock()
        }
        drawCircle(canvas)
        drawCenter(canvas)
        drawNums(canvas)
        drawArrows(canvas)
        postInvalidateDelayed(500)
        invalidate()
    }

    private fun drawArrows(canvas: Canvas?) {
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR_OF_DAY)
        hour = if (hour > 12) {
            hour - 12
        } else {
            hour
        }
        if (canvas != null) {
            drawArrow(canvas, (hour + c.get(Calendar.MINUTE) / 60) * 5, true, Color.GREEN)
            drawArrow(canvas, c.get(Calendar.MINUTE), false, Color.RED)
            drawArrow(canvas, c.get(Calendar.SECOND), false, Color.BLUE)
        }
    }

    private fun drawArrow(canvas: Canvas, loc: Int, isHour: Boolean, color: Int) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val arrowRad = if (isHour) {
            radius - minArrow - hourArrow
        } else {
            radius - minArrow
        }

        paint?.reset()
        paint?.strokeWidth = 10f
        paint?.color = color
        val x = (mWidth / 2 + cos(angle) * arrowRad).toFloat()
        val y = (mHeight / 2 + sin(angle) * arrowRad).toFloat()
        paint?.let {
            canvas.drawLine(
                mWidth / 2f, mHeight / 2f, x, y, it
            )
        }
    }

    private fun drawNums(canvas: Canvas?) {
        paint?.textSize = fontSize.toFloat()
        paint?.color = Color.BLACK
        for (num in numbers) {
            val n = num.toString()
            paint?.getTextBounds(n, 0, n.length, rect)
            val angle = Math.PI / 6 * (num - 3)
            val x = mWidth / 2 + cos(angle) * radius - rect.width() / 2
            val y = mHeight / 2 + sin(angle) * radius + rect.width() / 2
            paint?.let { canvas?.drawText(n, x.toFloat(), y.toFloat(), it) }
        }


    }


    private fun drawCenter(canvas: Canvas?) {
        paint?.style = Paint.Style.FILL
        paint?.let { canvas?.drawCircle(mWidth / 2f, mHeight / 2f, 10f, it) }
    }

    private fun drawCircle(canvas: Canvas?) {
        paint?.reset()
        paint?.color = ContextCompat.getColor(context, R.color.black)
        paint?.strokeWidth = 15f
        paint?.style = Paint.Style.STROKE
        paint?.isAntiAlias = true
        paint?.let {
            canvas?.drawCircle(
                mWidth / 2f,
                mHeight / 2f,
                (radius + space - 15).toFloat(),
                it
            )
        }
    }
}