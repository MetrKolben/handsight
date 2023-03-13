package com.jindrak.handsight

import android.annotation.SuppressLint
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.jindrak.handsight.Utils.toCharacter
import com.google.common.util.concurrent.ListenableFuture
import com.jindrak.handsight.Utils.word_formation
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.io.File
import java.util.*


class RecognitionFragment : Fragment()
{
    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private var previewView: PreviewView? = null
    lateinit var file: File
    private lateinit var classifier: Classifier
    private lateinit var previewOverlay: PreviewOverlay
    private lateinit var captureButton: Button
    private lateinit var backspaceButton: Button
    private lateinit var deleteButton: Button
    private var _currentBitmap: Bitmap? = null
        @Synchronized set
        @Synchronized get
    companion object {
        lateinit var timer: Timer
        lateinit var switch: Switch
    }

    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(R.layout.fragment_recognition, container, false)
        previewView = rootView.findViewById(R.id.cameraView)
        previewOverlay = rootView.findViewById(R.id.previewOverlay)
        captureButton = rootView.findViewById(R.id.capture_button)
        backspaceButton = rootView.findViewById(R.id.backspace_button)
        deleteButton = rootView.findViewById(R.id.delete_button)
        captureButton.visibility = View.INVISIBLE
        backspaceButton.visibility = View.INVISIBLE
        deleteButton.visibility = View.INVISIBLE
        val cl: ConstraintLayout = rootView.findViewById(R.id.cl1)

        cl.clipToOutline = true

        captureButton.visibility = if (word_formation) View.VISIBLE else View.INVISIBLE
        backspaceButton.visibility = if (word_formation) View.VISIBLE else View.INVISIBLE
        deleteButton.visibility = if (word_formation) View.VISIBLE else View.INVISIBLE
        classifier = Classifier(requireContext())

        val captureButton = rootView.findViewById<Button>(R.id.capture_button)
        timer = Timer(object : TimerTask() {
            override fun run() {
                previewView?.post {
                    val bitmap = previewView!!.bitmap
                    if (bitmap != null) {
                        _currentBitmap = bitmap
                    }
                }
                val cbum = _currentBitmap
                if (cbum != null) {
                    val thread = Thread {
                        previewOverlay.letter = analyze(_currentBitmap!!)
                        previewOverlay.postInvalidate()
                        this.cancel()
                    }
                    thread.start()
                }
            }
        }, 1000)

        captureButton.setOnClickListener {
            previewView?.post {
                val bitmap = previewView!!.bitmap
                if (bitmap != null) {
                    _currentBitmap = bitmap
                }
                val cbum = _currentBitmap
                if (cbum != null) {
                    val thread = Thread {
                        previewOverlay.addLetter(
                            analyze(_currentBitmap!!)
                        )
                        previewOverlay.postInvalidate()
                    }
                    thread.start()
                }
            }
        }

        switch = object : Switch {
            override fun switch(onOff: Boolean) {
                captureButton.post { captureButton.visibility = if (onOff) View.VISIBLE else View.INVISIBLE }
                backspaceButton.post {  backspaceButton.visibility = if (onOff) View.VISIBLE else View.INVISIBLE  }
                deleteButton.post {  deleteButton.visibility = if (onOff) View.VISIBLE else View.INVISIBLE  }
                PreviewOverlay.word_formation = onOff
                previewOverlay.postInvalidate()
            }
        }

        backspaceButton.setOnClickListener {
            previewOverlay.backspace()
        }

        deleteButton.setOnClickListener {
            previewOverlay.delete()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
        cameraProviderFuture!!.addListener({
            try {
                val cameraProvider = cameraProviderFuture!!.get()
                startCameraX(cameraProvider)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }, executor)
    }

    private val executor: Executor
        get() = ContextCompat.getMainExecutor(requireActivity())

    @SuppressLint("RestrictedApi")
    private fun startCameraX(cameraProvider: ProcessCameraProvider) {
        cameraProvider.unbindAll()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        val preview = Preview.Builder()
            .setTargetResolution(Size(1080, 1920))//TODO constant
            .build()
        preview.setSurfaceProvider(previewView!!.surfaceProvider)

        cameraProvider.bindToLifecycle(
            (this as LifecycleOwner),
            cameraSelector,
            preview
        )

    }

    @Synchronized
    fun analyze(image: Bitmap): String {
        val outputs = classifier.classify(image)
        val max = outputs!!.indices.maxOfOrNull { i: Int -> outputs[i] }
        val index = outputs.asList().indexOf(max)
        return index.toCharacter()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Timer", "onDestroy")
        timer.pause()
    }

    override fun onPause() {
        super.onPause()
        Log.d("Timer", "onPause")
        timer.pause()
    }

    override fun onStop() {
        super.onStop()
        Log.d("Timer", "onStop")
        timer.pause()
    }

    override fun onStart() {
        super.onStart()
        Log.d("Timer", "onStart")
        timer.start()
    }

}