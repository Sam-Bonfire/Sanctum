package com.sanctum.core.feature.compass.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVMediaTypeVideo
import platform.UIKit.UIColor
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraPreviewView(modifier: Modifier) {
    val cameraSession = remember {
        AVCaptureSession().apply {
            val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
            if (device != null) {
                val input = AVCaptureDeviceInput.deviceInputWithDevice(device, null)
                if (input != null && canAddInput(input)) {
                    addInput(input)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        cameraSession.startRunning()
        onDispose {
            cameraSession.stopRunning()
        }
    }

    UIKitView(
        factory = {
            val view = UIView()
            val previewLayer = AVCaptureVideoPreviewLayer(session = cameraSession)
            previewLayer.videoGravity = platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
            view.layer.addSublayer(previewLayer)
            view.backgroundColor = UIColor.blackColor

            // Hack to handle resizing
            view.setNeedsLayout()
            view.layoutIfNeeded()

            view
        },
        modifier = modifier,
        update = { view ->
            val previewLayer = view.layer.sublayers?.firstOrNull() as? AVCaptureVideoPreviewLayer
            previewLayer?.frame = view.bounds
        },
    )
}
