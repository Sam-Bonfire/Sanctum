package com.sanctum.core.feature.compass

import com.sanctum.core.feature.compass.domain.PlatformSensors

actual fun getPlatformSensors(): PlatformSensors {
    return IosPlatformSensors()
}
