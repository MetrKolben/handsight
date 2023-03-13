package com.jindrak.handsight

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import com.jindrak.handsight.Utils.marginDev
import com.jindrak.handsight.Utils.rectSizeDev
import android.util.AttributeSet
import android.view.View


class PreviewOverlay (context: Context?, attributeSet: AttributeSet?) :
    View(context, attributeSet) {

    private val darkUiId = 33

    companion object {
        var word_formation = false
            @Synchronized set
    }

    private val trie = Trie.getInstance(resources)

    var letter: String = "A"
        @Synchronized set
        @Synchronized get

    private var text: String = ""
        @Synchronized set
        @Synchronized get


    fun addLetter(letter: String) {
        val words = text.split(" ")
        val isWord = trie.search((words[words.lastIndex]+letter).lowercase())
        text += if (isWord) {
            letter
        } else {
            " $letter"
        }
    }

    private val paint = Paint().apply {
        strokeWidth = 15f
        textSize = 400f
        color = this@PreviewOverlay.getColor()
        textAlign = Paint.Align.CENTER
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDrawForeground(canvas)
        paint.pathEffect = DashPathEffect(floatArrayOf(40f, 30f), 0f)

        canvas.let {
            val left = marginDev
            val top = marginDev
            val right = marginDev + rectSizeDev
            val bottom = marginDev + rectSizeDev
            paint.textSize = if (word_formation) Utils.maxTextSize*(Utils.font_size.toFloat()/200f) else Utils.maxTextSize.toFloat()
            paint.pathEffect = DashPathEffect(floatArrayOf(40f, 30f), 0f)
            paint.strokeWidth = 15f
            paint.style = Paint.Style.STROKE
            it.drawRoundRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), 50f, 50f, paint)
            paint.pathEffect = null
            paint.strokeWidth = 20f
            paint.color = getColor()
            paint.style = Paint.Style.FILL
            if (word_formation) {
                for ((i, word) in text.split(" ").withIndex()) {
                    it.drawText(
                        word,
                        marginDev + rectSizeDev / 2f,
                        2*marginDev + rectSizeDev + (i+1) * 1.1f * paint.textSize,
                        paint
                    )
                }
            } else {
                it.drawText(
                    letter,
                    marginDev + rectSizeDev / 2f,
                    3*marginDev + rectSizeDev + 0.85f * paint.textSize,
                    paint
                )
            }
        }
    }

    private fun getColor(): Int {
        val currentTheme = context.applicationContext.resources.configuration.uiMode
        return if (currentTheme == darkUiId) context.getColor(R.color.dark_text)
        else context.getColor(R.color.light_text)
    }

    @Synchronized
    fun backspace() {
        if (text.isNotEmpty()) {
            text = text.dropLast(1)
            if (text.lastOrNull() == ' ') text = text.dropLast(1)
        }
        postInvalidate()
    }

    @Synchronized
    fun delete() {
        text = ""
        postInvalidate()
    }
}