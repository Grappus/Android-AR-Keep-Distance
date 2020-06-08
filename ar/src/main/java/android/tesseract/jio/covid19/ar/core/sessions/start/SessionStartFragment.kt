package android.tesseract.jio.covid19.ar.core.sessions.start

import android.graphics.*
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.tesseract.jio.covid19.ar.R
import android.tesseract.jio.covid19.ar.databinding.FragmentStartSessionBinding
import android.tesseract.jio.covid19.ar.tflite.Classifier
import android.tesseract.jio.covid19.ar.tflite.TFLiteObjectDetectionAPIModel
import android.tesseract.jio.covid19.ar.utils.ImageUtils
import android.tesseract.jio.covid19.ar.utils.rotate
import android.tesseract.jio.covid19.ar.utils.scaleBitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.SceneView
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

/**
 * Created by Dipanshu Harbola on 6/6/20.
 */
class SessionStartFragment : Fragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentStartSessionBinding.inflate(
            layoutInflater
        )
    }

    // coroutine scope for background/main thread operations
    private val bgScope = CoroutineScope(Dispatchers.IO)
    private val uiScope = CoroutineScope(Dispatchers.Main)

    private var currentTransformableNode: TransformableNode? = null

    private lateinit var arFragment: ArFragment

    private var renderable: ModelRenderable? = null

    // Camera world position points
    private val mCameraRelativePose = Pose.makeTranslation(0.0f, -0.39f, -0.55f)

    private var oldAnchor: Anchor? = null

    private var oldAnchorNode: AnchorNode? = null

    // AR session
    private var session: Session? = null

    private var sceneView: SceneView? = null

    // classifier to recognise object
    private var detector: Classifier? = null

    private var readyToProcessFrame = false
    private var currentFrameBmp: Bitmap? = null
    private var computingDetection = false
    private var timestamp: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.navigationBarColor = requireActivity().getColor(R.color.baseBgColor)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sceneView = null
        detector?.close()
        arFragment.onDetach()
        bgScope.cancel()
        uiScope.cancel()
    }

    private fun initComponents() {

        arFragment = childFragmentManager.findFragmentById(R.id.sceneformFragment) as ArFragment

        // used to remove the plan detection view
        arFragment.planeDiscoveryController.hide()
        arFragment.arSceneView.planeRenderer.isVisible = false
        arFragment.planeDiscoveryController.setInstructionView(null)

        // Adds a listener to the ARSceneView
        // Called before processing each frame
        arFragment.arSceneView.scene.addOnUpdateListener {
            onSceneUpdate()
            addNodeToScene()
        }

        binding.layoutBottomView.btnStartSession.setOnClickListener {
            addObject()
            sceneView = arFragment.arSceneView as ArSceneView
            session = arFragment.arSceneView.session
            placeObject(R.raw.colored)
            showStartSessionView()
            readyToProcessFrame = true
        }

        binding.layoutSessionInfo.btnEndSession.setOnClickListener {
            clearAnchor()
        }
    }

    // Simply returns the center of the screen
    private fun getScreenCenter(): Point {
        val view = binding.root
        return Point(view.width / 2, view.height / 2)
    }

    /**
     *
     * This method takes our 3D model and performs a hit test to determine where to place it
     */
    private fun addObject() {
        val frame = arFragment.arSceneView.arFrame
        val point = getScreenCenter()
        if (frame != null) {
            val hits = frame.hitTest(point.x.toFloat(), point.y.toFloat())
            for (hit in hits) {
                val trackable = hit.trackable
                if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                    break
                }
            }
        }
    }

    private fun placeObject(srcId: Int) {
        ModelRenderable.builder()
            .setSource(arFragment.context, srcId)
            .build()
            .thenAccept {
                renderable = it
            }
            .exceptionally {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                return@exceptionally null
            }
    }

    private fun addNodeToScene() {
        val frame = arFragment.arSceneView.arFrame ?: return
        val pose =
            frame.camera?.displayOrientedPose?.compose(mCameraRelativePose) // need to change it
        if (frame.camera?.trackingState == TrackingState.TRACKING) {
            val anchor = arFragment.arSceneView.session?.createAnchor(pose?.extractTranslation())
            anchor?.pose?.toMatrix(FloatArray(16), 0)
            if (oldAnchor != null) {
                oldAnchor?.detach()
            }
            oldAnchor = anchor
            val anchorNode = AnchorNode(anchor)
            oldAnchorNode = anchorNode
            anchorNode.setParent(sceneView?.scene)
            // TransformableNode means the user to move, scale and rotate the model
            val transformableNode = TransformableNode(arFragment.transformationSystem)
            transformableNode.renderable = renderable?.apply {
                isShadowReceiver = false
                isShadowCaster = false
            }  // have to add position and angel here
            transformableNode.setParent(anchorNode)
            transformableNode.select()
            currentTransformableNode = transformableNode
        }
    }

    // Simple function to show/hide our start-session
    private fun showStartSessionView() {
        binding.run {
            layoutBottomView.clBottomView.visibility = View.GONE
            layoutBottomView.btnStartSession.visibility = View.GONE
            layoutSessionInfo.llSessionInfo.visibility = View.VISIBLE
            layoutSessionInfo.btnEndSession.visibility = View.VISIBLE
        }
    }

    /**
     * @onSceneUpdate() Function invoke every time when scene update
     */
    private fun onSceneUpdate() {
        if (session == null) {
            return
        }
        try {
            val frame: Frame = arFragment.arSceneView.arFrame ?: return
            val image = frame.acquireCameraImage()

            // Copy the camera stream to a bitmap
            try {
                val bytes = ImageUtils.NV21ToByteArray(
                    ImageUtils.YUV420_888ToNV21(image!!),
                    image.width, image.height
                )
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
                val newBitmap = bitmap.rotate(90f)
                currentFrameBmp = newBitmap
                onPreviewSizeSelect()
                executeKernelTask()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            image?.close()
        } catch (e: java.lang.Exception) {
        }
    }

    /**
     * @onPreviewSizeSelect() Function invoke every time when when we get a frame from ar-camera
     * to setup the default preview size for detection
     */
    private fun onPreviewSizeSelect() {
        try {
            detector = TFLiteObjectDetectionAPIModel.create(
                requireActivity().assets,
                TF_OD_API_MODEL_FILE,
                TF_OD_API_LABELS_FILE,
                TF_OD_API_INPUT_SIZE,
                TF_OD_API_IS_QUANTIZED
            )
        } catch (e: IOException) {
            e.printStackTrace()
            val toast = Toast.makeText(
                requireContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT
            )
            toast.show()
            requireActivity().finish()
        }
        // width = 300, height = 300 cause SSD model support 300 x300 image to detect object
        binding.graphicOverlay.setConfiguration(TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE)
    }

    /**
     * @executeKernelTask() Function is responsible to execute the detection process a short delay,
     * so that our kernel accept new image process request without any inference.
     */
    private fun executeKernelTask() {
        val handlerKernel = Handler()
        val r: Runnable = object : Runnable {
            override fun run() {
                handlerKernel.postDelayed(
                    this,
                    100L
                ) //executing object detection in every 100 millisecond
                if (readyToProcessFrame && !computingDetection) {
                    // scale bitmap to 300x300, cause SSD model support 300 x300 image to detect object
                    processImage(
                        currentFrameBmp!!.scaleBitmap(
                            TF_OD_API_INPUT_SIZE,
                            TF_OD_API_INPUT_SIZE
                        )
                    )
                }
            }
        }
        handlerKernel.postDelayed(r, 100L)
    }

    /**
     * @processImage() Function is responsible for detected result and overlay on detected object.
     * and in background process we calculate the onscreen location points on our 3D object and
     * get the intersect point between detected object and 3D object.
     */
    private fun processImage(newBitmap: Bitmap) {
        ++timestamp
        binding.graphicOverlay.postInvalidate()
        if (computingDetection) {
            return
        }
        computingDetection = true
        bgScope.launch {
            val results: List<Classifier.Recognition> =
                detector!!.recognizeImage(newBitmap)
            val mappedRecognitions: MutableList<Classifier.Recognition> =
                LinkedList<Classifier.Recognition>()
            for (result in results) {
                val location: RectF = result.location
                val location2 = RectF()
                if (result.confidence >= MINIMUM_CONFIDENCE_TF_OD_API) {
                    if (result.title.equals("person", ignoreCase = true)) {
                        val worldPosition = currentTransformableNode?.worldPosition
                        val sceneScreenPoint =
                            arFragment.arSceneView.scene.camera.worldToScreenPoint(worldPosition) // get the screen points(x,y) of the current scene
                        val scale = 7.113333f // scale image to screen visible size
                        val xOffset = 527.0f // xOffset to scale image
                        val yOffset = 0.0f // yOffset to scale image
                        location2.set(
                            location.left * scale,
                            location.top * scale,
                            location.right * scale,
                            location.bottom * scale
                        )
                        location2.offset(-xOffset, -yOffset)
                        if ((sceneScreenPoint.y - xOffset) <= (location2.bottom)) {
                            //updateUi(true)
                            result.location = location
                            result.paint = Paint().apply {
                                color = Color.RED
                                style = Paint.Style.STROKE
                                strokeWidth = 2 * resources.displayMetrics.density
                            }
                            mappedRecognitions.add(result)
                        } else {
                            //updateUi(false)
                            result.location = location
                            result.paint = Paint().apply {
                                color = Color.GREEN
                                style = Paint.Style.STROKE
                                strokeWidth = 2 * resources.displayMetrics.density
                            }
                            mappedRecognitions.add(result)
                        }
                    }
                }
            }
            updateUi(mappedRecognitions)
            binding.graphicOverlay.set(mappedRecognitions)
            computingDetection = false
        }
    }

    private fun updateUi(
        mappedRecognitions: MutableList<Classifier.Recognition>
    ) {
        uiScope.launch {
            if (mappedRecognitions.any { it.paint.color == Color.RED }) {
                placeObject(R.raw.red)
                // other ui changes
                showViolationAlert(true)
            } else {
                placeObject(R.raw.colored)
                // other ui changes
                showViolationAlert(false)
            }
        }
    }

    private fun showViolationAlert(isViolated: Boolean) {
        if (isViolated) {
            binding.layoutSessionInfo.run {
                sessionViolatedLayout.visibility = View.VISIBLE
                sessionWatchLayout.visibility = View.GONE
                btnEndSession.setBackgroundResource(R.drawable.bg_btn_alrt_end_session)
                llSessionInfo.setBackgroundResource(R.drawable.bg_card_alrt_session_info)
            }
        } else {
            binding.layoutSessionInfo.run {
                sessionViolatedLayout.visibility = View.GONE
                sessionWatchLayout.visibility = View.VISIBLE
                btnEndSession.setBackgroundResource(R.drawable.bg_btn_end_session)
                llSessionInfo.setBackgroundResource(R.drawable.bg_card_session_info)
            }
        }
    }

    // Remove the existing scene and anchor
    private fun clearAnchor() {
        if (sceneView != null) {
            oldAnchorNode?.renderable = null
            oldAnchor?.detach()
            session = null
        }
        readyToProcessFrame = false
        findNavController().navigate(R.id.action_sessionStartFragment_to_sessionEndFragment)
    }

    companion object {
        // Configuration values for the prepackaged SSD model.
        private const val TF_OD_API_INPUT_SIZE = 300
        private const val TF_OD_API_IS_QUANTIZED = true
        private const val TF_OD_API_MODEL_FILE = "detect.tflite"
        private const val TF_OD_API_LABELS_FILE = "file:///android_asset/labelmap.txt"

        // Minimum detection confidence to track a detection.
        private const val MINIMUM_CONFIDENCE_TF_OD_API = 0.6f
    }
}