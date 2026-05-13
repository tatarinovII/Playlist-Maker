package com.practicum.playlistmaker.player.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.practicum.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var imagePlay: Bitmap? = null
    private var imagePause: Bitmap? = null
    private var isPlay = true
    private var imageRect = RectF(0f, 0f, 0f, 0f)


    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomImageView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                imagePlay = getDrawable(R.styleable.CustomImageView_imagePlay)?.toBitmap()
                imagePause = getDrawable(R.styleable.CustomImageView_imagePause)?.toBitmap()
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val bitmap = if (isPlay) imagePlay else imagePause
        val size = bitmap?.width ?: 0
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        val bitmap: Bitmap? = if (isPlay) imagePlay else imagePause

        bitmap?.let {
            canvas.drawBitmap(bitmap, null, imageRect, null)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }

            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun changeState() {
        isPlay = !isPlay
        invalidate()
    }

    fun setPlayImageAfterEnd() {
        if (!isPlay) {
            isPlay = true
        }
        invalidate()
    }

}