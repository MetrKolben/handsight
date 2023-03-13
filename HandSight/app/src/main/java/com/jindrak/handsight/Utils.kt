package com.jindrak.handsight

import android.graphics.*
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


object Utils {

    private val alphabet = arrayOf('A','B','C',"CH",'D',
        'E','F','G','H','I',
        'J','K','L','M','N',
        'O','P','Q','R','S',
        'T','U','V','W','X',
        'Y','Z')

    private const val marginRatio = 1f/6f
    private val screenSize = size
    private val widthDev = screenSize.width
    var font_size = 20
        @Synchronized get
        @Synchronized set
    var word_formation = false
        @Synchronized get
        @Synchronized set
    val marginDev = ((marginRatio) * widthDev.toFloat()).toInt()

    val rectSizeDev = ((4* marginRatio) * widthDev).toInt()
    val maxTextSize = rectSizeDev

    fun Bitmap.crop(x1: Int, y1: Int, x2: Int, y2: Int): Bitmap {
        return Bitmap.createBitmap(this, x1, y1, x2 - x1, y2 - y1)
    }

    fun TensorBuffer.normalize(): TensorBuffer {
        val length = this.flatSize
        val outputBuffer = TensorBuffer.createFixedSize(this.shape, DataType.FLOAT32)
        for (i in 0 until length) {
            outputBuffer.buffer.putFloat(4*i, this.getFloatValue(i) / 255f)
        }
        return outputBuffer
    }

    fun Int.toCharacter(): String {
        return alphabet[this].toString()
    }
}

class Timer(task: Runnable, period: Long) {

    private var task: Runnable
    private var period: Long
    private val executorService = Executors.newSingleThreadScheduledExecutor()
    private var future: ScheduledFuture<*>? = null

    init {
        this.task = task
        this.period = period
    }

    fun start() {
        if (!Utils.word_formation && future == null) {
            future = executorService.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS)
        }
    }

    fun pause() {
        future?.cancel(true)
        future = null
    }
}

class Letter(letter: String, description: String, image: Int, visible: Boolean = false) {
    var letter: String
    var image: Int
    var description: String
    var visible: Boolean

    init {
        this.letter = letter
        this.image = image
        this.description = description
        this.visible = visible
    }
}
