package com.sanctum.core.feature.compass

import com.sanctum.core.feature.compass.domain.GeoLocation
import com.sanctum.core.feature.compass.domain.PlatformSensors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@JsFun(
    """
    (onSuccess, onError) => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => { onSuccess(position.coords.latitude, position.coords.longitude); },
                (error) => { onError(error.message); }
            );
        } else {
            onError("Geolocation is not supported by this browser.");
        }
    }
    """,
)
private external fun requestLocationJs(
    onSuccess: (Double, Double) -> Unit,
    onError: (String) -> Unit,
)

class WasmPlatformSensors : PlatformSensors {

    override val deviceHeading: Flow<Float?> = emptyFlow()

    override suspend fun getCurrentLocation(): Result<GeoLocation> {
        return suspendCancellableCoroutine { cont ->
            try {
                requestLocationJs(
                    onSuccess = { lat, lon ->
                        if (cont.isActive) {
                            cont.resume(Result.success(GeoLocation(lat, lon)))
                        }
                    },
                    onError = { error ->
                        if (cont.isActive) {
                            cont.resume(Result.failure(Exception(error)))
                        }
                    },
                )
            } catch (e: Exception) {
                if (cont.isActive) {
                    cont.resume(Result.failure(e))
                }
            }
        }
    }
}
