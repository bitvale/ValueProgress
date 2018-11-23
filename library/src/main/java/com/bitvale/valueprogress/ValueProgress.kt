package com.bitvale.valueprogress

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

/**
 * Created by Alexander Kolpakov on 11/23/2018
 */
class ValueProgress @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val ANIMATION_DURATION = 800L
        const val SHADOW_RADIUS = 14.0f
        const val FULL_CIRCLE = 360f
        const val SHADOW_RADIUS_OFFSET = 1.5f
        const val DEFAULT_TEXT = "0"
    }

    private var progressColor: Int = 0
    private var progressDisabledColor: Int = 0
    private var progressBackgroundColor: Int = 0

    private var progressRadius = 0f
    private var progressWidth = 0f
    private var progressValueSymbol: String? = null

    private val progressArc = RectF(0f, 0f, 0f, 0f)

    private val shadowPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    private lateinit var textLayout: StaticLayout

    private var animator = ValueAnimator.ofFloat(0f, 1f).apply {
        addUpdateListener {
            progressValue = it.animatedValue as Float
        }
        duration = ANIMATION_DURATION
    }

    var progressMaxValue = 0f

    var percent = 0f
        set(value) {
            field = if (value > progressMaxValue) progressMaxValue else value
            animateProgress()
        }

    private var progressValue = 0f
        set(value) {
            val currentPercent = lerp(0f, percent, value)
            text = currentPercent.toInt().toString() + progressValueSymbol
            angle = (currentPercent / progressMaxValue) * FULL_CIRCLE
        }

    private var text: String? = null
        set(value) {
            field = value
            createLayout()
        }

    private var angle = 0f
        set(value) {
            field = value
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postInvalidateOnAnimation()
            } else {
                invalidate()
            }
        }

    init {
        attrs?.let { retrieveAttributes(attrs, defStyleAttr) }
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        createLayout()
    }

    private fun animateProgress() {
        animator.cancel()
        animator.start()
    }

    private fun lerp(a: Float, b: Float, t: Float): Float {
        return a + (b - a) * t
    }

    private fun retrieveAttributes(attrs: AttributeSet, defStyleAttr: Int) {
        val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.ValueProgress, defStyleAttr,
                        R.style.ValueProgress)

        progressMaxValue = typedArray.getFloat(R.styleable.ValueProgress_progress_max_value, 100f)
        progressValueSymbol = typedArray.getString(R.styleable.ValueProgress_progress_value_symbol)
        text = DEFAULT_TEXT + progressValueSymbol
        progressWidth = typedArray.getDimension(R.styleable.ValueProgress_progress_width, 0f)

        progressDisabledColor = typedArray.getColor(R.styleable.ValueProgress_progress_disabled_color, 0)
        progressColor = typedArray.getColor(R.styleable.ValueProgress_progress_color, 0)
        progressBackgroundColor = typedArray.getColor(R.styleable.ValueProgress_progress_background_color, 0)

        val progressShadowColor = typedArray.getColor(R.styleable.ValueProgress_progress_shadow_color, 0)
        shadowPaint.apply {
            setShadowLayer(SHADOW_RADIUS, 0.0f, 0.0f, progressShadowColor)
            color = progressBackgroundColor
        }

        val progressTextColor = typedArray.getColor(R.styleable.ValueProgress_progress_text_color, 0)
        val progressTextSize = typedArray.getDimension(R.styleable.ValueProgress_progress_text_size, 0f)

        textPaint.apply {
            color = progressTextColor
            textSize = progressTextSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        typedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        progressRadius = (Math.min(w, h)) / 2f
        progressArc.bottom = progressRadius * 2f
        progressArc.right = progressRadius * 2f
    }

    override fun onDraw(canvas: Canvas?) {
        // disabled
        progressPaint.color = progressDisabledColor
        canvas?.drawCircle(
                progressRadius,
                progressRadius,
                progressRadius,
                progressPaint
        )

        // progressArc
        progressPaint.color = progressColor
        var checkpoint = canvas?.save()
        canvas?.rotate(-90f, progressRadius, progressRadius)
        canvas?.drawArc(progressArc, 0f, angle, true, progressPaint)
        canvas?.restoreToCount(checkpoint as Int)

        // bg
        progressPaint.color = progressBackgroundColor
        canvas?.drawCircle(
                progressRadius,
                progressRadius,
                progressRadius - progressWidth,
                progressPaint
        )

        // shadow
        canvas?.drawCircle(
                progressRadius,
                progressRadius,
                progressRadius / SHADOW_RADIUS_OFFSET,
                shadowPaint
        )

        // text
        checkpoint = canvas?.save()
        canvas?.translate(
                progressRadius - textLayout.width / 2f,
                (height - textLayout.height) / 2f
        )
        textLayout.draw(canvas)
        canvas?.restoreToCount(checkpoint as Int)

        super.onDraw(canvas)
    }

    private fun createLayout() {
        text?.let {
            textLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                StaticLayout.Builder.obtain(
                        it,
                        0,
                        it.length,
                        textPaint,
                        textPaint.measureText(it).toInt()
                )
                        .build()
            } else {
                StaticLayout(
                        text,
                        textPaint,
                        textPaint.measureText(it).toInt(),
                        Layout.Alignment.ALIGN_CENTER,
                        1f,
                        0f,
                        true
                )
            }
        }
    }
}