package com.example.treecare.user.camera

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import com.example.treecare.R
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.treecare.user.MainActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private val cameraPermissionCode = 123
    private val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var viewFinder:PreviewView
    private val btnCapture: ImageView by lazy { findViewById(R.id.btnCapture) }
    private val tvValue: TextView by lazy { findViewById(R.id.tvValue) }
    private val clInfo: ConstraintLayout by lazy { findViewById(R.id.clInfo) }
    private lateinit var imageAnalysis: ImageAnalysis

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(this, "Butuh Izin Camera",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        viewFinder = findViewById(R.id.viewFinder)
        val btnBack:ImageView = findViewById(R.id.btnBack)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnCapture.setOnClickListener {
            captureImage()
        }

        clInfo.setOnClickListener {
            if (tvValue.text == "" || tvValue.text == null){
                Toast.makeText(this, "Scan Hingga Muncul No Pohon", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, PengamatanVisualActivity::class.java)
                intent.putExtra("qr", tvValue.text)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun captureImage() {
        val imageCapture = ImageCapture.Builder().build()

        imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
            val (decodedValue, isQrCodeValid) = decodeQrCode(imageProxy)

            runOnUiThread {
                if (isQrCodeValid) {
                    tvValue.text = decodedValue
                    clInfo.visibility = View.VISIBLE
                } else {
                    clInfo.visibility = View.GONE
                }
            }

            imageProxy.close()
        }

        val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
            ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this as LifecycleOwner,
                    cameraSelector,
                    imageCapture,
                    imageAnalysis
                )

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(viewFinder.surfaceProvider)
                    }

                cameraProvider.bindToLifecycle(
                    this as LifecycleOwner,
                    cameraSelector,
                    preview
                )

            } catch (exc: Exception) {
                // Handle exceptions
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun decodeQrCode(imageProxy: ImageProxy): Pair<String, Boolean> {
        val mediaImage = imageProxy.image
        val inputImage = InputImage.fromMediaImage(
            mediaImage!!,
            imageProxy.imageInfo.rotationDegrees
        )

        val scanner = BarcodeScanning.getClient()
        val result: Task<List<Barcode>> = scanner.process(inputImage)

        val decodedValue = Tasks.await(result)
            .takeIf { it.isNotEmpty() }
            ?.firstOrNull()
            ?.displayValue
            ?: ""

        return Pair(decodedValue, decodedValue.isNotEmpty())
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun startCamera() {
        val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
            ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this as LifecycleOwner,
                    cameraSelector,
                    preview
                )

            } catch (exc: Exception) {
                //handle exception
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraPermissionCode) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                //handle permission not allowed
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}