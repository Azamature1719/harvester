package com.example.harvester.framework.ui.camera

import android.annotation.SuppressLint
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.harvester.databinding.HarvesterCameraFragmentBinding
import com.example.harvester.model.entities.realm_entities.information_register.data_harvested.DataHarvested
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class HarvesterCameraFragment : Fragment() {

    private var _binding: HarvesterCameraFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService

    private var imageAnalysis: ImageAnalysis? = null

    private val analyzeListener = object : ScanningResultListener {
        var last: String = ""
        override fun onScanned(result: String) {
            if(result != last){
                CoroutineScope(Dispatchers.IO).launch {
                    val processingRes = BarcodeHandler.processingMultiDecodedData(Decode.init(result))
                    MainScope().launch {
                        //val r = (processingRes as? DataHarvested)
//                        Toast.makeText(requireContext(), (processingRes as DataHarvested).product?.description, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            last = result
        }

        override fun onScannedRect(rect: RectF, w: Int, h: Int, isFlipped: Boolean ) {
            binding.overlay.drawQRCodeZone(rect, w, h, isFlipped)
        }
    }

    private var analyzer: ImageAnalysis.Analyzer = MLKitBarcodeAnalyzer(analyzeListener)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        HarvesterCameraFragmentBinding.inflate(inflater, container, false).also { _binding = it }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraExecutor = Executors.newSingleThreadExecutor()

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @SuppressLint("RestrictedApi")
    private fun bindPreview(cameraProvider: ProcessCameraProvider?) {

        if (requireActivity().isDestroyed || requireActivity().isFinishing) {
            //This check is to avoid an exception when trying to re-bind use cases but user closes the activity.
            //java.lang.IllegalArgumentException: Trying to create use case mediator with destroyed lifecycle.
            return
        }

        cameraProvider?.unbindAll()

        val preview: Preview = Preview.Builder()
            .build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(640, 480))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        analyzer = MLKitBarcodeAnalyzer(analyzeListener)
        preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
        cameraProvider?.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.overlay.post {
            binding.overlay.setViewFinder()
        }

        // MotionLayout States
        binding.cameraRoot.setTransitionListener(object: MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                if (progress >= 0.95f) {
                    //  binding.cameraWorkSignal.visibility = View.VISIBLE
                    imageAnalysis!!.setAnalyzer(cameraExecutor) {
                        analyzer.analyze(it)
                    }
                } else {
                    //   binding.cameraWorkSignal.visibility = View.INVISIBLE
                    imageAnalysis?.clearAnalyzer()
                    binding.overlay.clearCodeZone()
                }
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}