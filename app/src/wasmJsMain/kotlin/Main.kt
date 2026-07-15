import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.sanctum.app.App
import com.sanctum.app.BuildConfig
import com.sanctum.core.core.di.initKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    println("Initializing Web App Config: ${BuildConfig.FLAVOR_ID}")
    initKoin()
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        App()
    }
}
