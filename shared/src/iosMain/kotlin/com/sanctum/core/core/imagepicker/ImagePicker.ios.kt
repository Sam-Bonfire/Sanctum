package com.sanctum.core.core.imagepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import platform.UIKit.UIViewController
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.Foundation.NSData
import platform.darwin.NSObject
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.info
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberImagePicker(onImagePicked: (ImageBitmap?) -> Unit): ImagePicker {
    return remember {
        object : ImagePicker {
            override fun launch() {
                // To do: proper iOS image picker implementation
                // For now just pass null to unblock build
                onImagePicked(null)
            }
        }
    }
}
