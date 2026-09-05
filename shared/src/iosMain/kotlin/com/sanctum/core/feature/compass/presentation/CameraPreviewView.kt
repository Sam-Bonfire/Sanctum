package com.sanctum.core.feature.compass.presentation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.QuartzCore.CALayer
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraPreviewView(modifier: Modifier) {
    val captureSession = remember { AVCaptureSession() }

    DisposableEffect(Unit) {
        val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
        if (device != null) {
            val input = AVCaptureDeviceInput.deviceInputWithDevice(device, null)
            if (input != null && captureSession.canAddInput(input)) {
                captureSession.addInput(input)
            }
            captureSession.startRunning()
        }

        onDispose {
            captureSession.stopRunning()
        }
    }

    UIKitView(
        factory = {
            val cameraContainer = UIView()
            cameraContainer.backgroundColor = platform.UIKit.UIColor.blackColor

            val cameraLayer = AVCaptureVideoPreviewLayer(session = captureSession).apply {
                videoGravity = AVLayerVideoGravityResizeAspectFill
            }
            cameraContainer.layer.addSublayer(cameraLayer)
            cameraContainer
        },
        modifier = modifier.background(Color.Black),
        update = { view ->
            CATransaction.begin()
            CATransaction.setValue(true, kCATransactionDisableActions)
            view.layer.sublayers?.firstOrNull()?.let { layer ->
                (layer as CALayer).frame = view.layer.bounds
            }
            CATransaction.commit()
        },
    )
}
