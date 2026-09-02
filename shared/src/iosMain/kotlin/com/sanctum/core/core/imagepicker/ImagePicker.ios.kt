package com.sanctum.core.core.imagepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIWindowScene
import platform.darwin.NSObject
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberImagePicker(onImagePicked: (ImageBitmap?) -> Unit): ImagePicker {
    return remember {
        object : ImagePicker {
            // Keep a strong reference to the delegate to prevent it from being garbage collected
            private var delegate: UIImagePickerControllerDelegateProtocol? = null

            override fun launch() {
                val pickerController = UIImagePickerController()
                pickerController.setSourceType(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary)

                val delegateObj = object : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {
                    override fun imagePickerController(
                        picker: UIImagePickerController,
                        didFinishPickingImage: UIImage,
                        editingInfo: Map<Any?, *>?,
                    ) {
                        picker.dismissViewControllerAnimated(true, null)

                        // Using UIImageJPEGRepresentation to safely convert the image to NSData.
                        // This avoids issues with missing URL keys.
                        val data = UIImageJPEGRepresentation(didFinishPickingImage, 0.8)
                        if (data != null) {
                            val bytes = ByteArray(data.length.toInt())
                            bytes.usePinned { pinned ->
                                memcpy(pinned.addressOf(0), data.bytes, data.length)
                            }
                            try {
                                val skiaImage = Image.makeFromEncoded(bytes)
                                onImagePicked(skiaImage.toComposeImageBitmap())
                            } catch (e: Exception) {
                                onImagePicked(null)
                            }
                        } else {
                            onImagePicked(null)
                        }
                        delegate = null
                    }

                    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                        picker.dismissViewControllerAnimated(true, null)
                        onImagePicked(null)
                        delegate = null
                    }
                }

                this.delegate = delegateObj
                pickerController.delegate = delegateObj

                // Get root view controller
                val keyWindow = UIApplication.sharedApplication.connectedScenes.mapNotNull { it as? UIWindowScene }.flatMap { it.windows }.firstOrNull { it.isKeyWindow() }
                keyWindow?.rootViewController?.presentViewController(pickerController, true, null)
            }
        }
    }
}
