package com.jindrak.handsight

import android.content.Context
import android.graphics.Bitmap
import com.jindrak.handsight.Utils.crop
import com.jindrak.handsight.Utils.marginDev
import com.jindrak.handsight.Utils.normalize
import com.jindrak.handsight.Utils.rectSizeDev
import com.jindrak.handsight.ml.Model87
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.util.*


class Classifier(context: Context) {
    private var model: Model87 = Model87.newInstance(context)

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    fun classify(bitmap: Bitmap): FloatArray? {
        val croppedBitmap = crop(bitmap)

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))//TODO from config
            .build()

        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(croppedBitmap)
        tensorImage = imageProcessor.process(tensorImage)

        val tensorBuffer = tensorImage.tensorBuffer.normalize()

        val outputs = model.process(tensorBuffer)
        return outputs.outputFeature0AsTensorBuffer.floatArray
    }

    private fun crop(bmp: Bitmap): Bitmap {
        return bmp.crop(marginDev
            , marginDev
            , marginDev + rectSizeDev
            , marginDev + rectSizeDev
        )
    }
}
