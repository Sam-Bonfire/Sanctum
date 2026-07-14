#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("com.squareup.okhttp3:okhttp:4.12.0")

import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

val client = OkHttpClient.Builder()
    .readTimeout(0, TimeUnit.MILLISECONDS)
    .build()

val request = Request.Builder()
    .url("wss://live-mt-server.wati.io/10168118/chat?sessionId=mqtht37doq23znr7bj&access_token=DxZouSqf7W8aRMSxZmm8XHrDa4GuuoX6kEZHxiDH-BQcWG7DBnkenMMX2XR09wipActw1nlY_wM4SYj7FUm22NEnyy4DhzONLtS4aJg")
    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:152.0) Gecko/20100101 Firefox/152.0")
    .addHeader("Origin", "https://live.wati.io")
    .addHeader("Cookie", "intercom-session-at2ayd3s=TGpUdkFUL0lpR3pIYlRDK3pxTEhZUllSSlk3cmZlNCtWTERsRERkb1J1MW5WVTFZNjBRTWNnck96ZU4rb3NTY0pLK21qa2lWMU5kMk9DZEZ6b3FMTEc1NkNjWlhHOEJDVkJMcHVYb3BQb1h6RW1xd2hpelB1TVMrSXptUEp1YjhOckh5U2ZUNTFId1FyV1RVa0hFcm1pZm40bngrMWNRU1grU2M0d0s0cjQ3UnFaV3UwWjNRVG9NckN0Yk1nazlvM0FKd3A2TzY3a1o2b3lwT3BablEzL1c1azBvZXVrNXlyeHJPTTVnRm9wTT0tLWJqN2ZpaGxQZlMxR09Wd0FrWFNyWlE9PQ==--4b30114b436c729e9f28c383117ca535a78d74da; intercom-device-id-at2ayd3s=87a4b41e-f52f-4fa4-9725-b4b074faf2cd; cf_clearance=mK4m3EpeVlkJe0WQg8SlVoKIaQwbikveXrIIZsYCn0c-1781522115-1.2.1.1-y3r3TpOroNsb35F7boST4IHI.FjZhGHy569BcEgH7.NrluWmEW2uLidB_Yd3lnaxCH43FrL71sZB9b9B_X3ktdBtaMLtPIxonCAN2vHFJ3V72AJgHHhl5Vm6ydT.Y0lb_8_SXMGlVYvdlMy9r.D6mBFzUC5XMX4LjRDDQZPeHBhK6qvjPwZpAC4n5kdbHhmuK4VHABEnbc4gx8aRzcD_vJCXnZNBlFOrkCdQJuwoYA3XpA62jwhCuJCN3QdKEP9smGcU1MXFhw27SJKJE23edVqun856Jp7ZMHb8bv_S62w44VbR4Udeg9EthZSIeZ6v2V2Wn71whZ1P.sQuT8_7mg; _wati_attr=%7B%22landing_page%22%3A%22https%3A%2F%2Fwww.wati.io%2Fpricing%2F%22%2C%22captured_at%22%3A%222026-06-15T11%3A15%3A14.990Z%22%7D; tenantId=10168118; token=DxZouSqf7W8aRMSxZmm8XHrDa4GuuoX6kEZHxiDH-BQcWG7DBnkenMMX2XR09wipActw1nlY_wM4SYj7FUm22NEnyy4DhzONLtS4aJg; i18nextLng=en")
    .build()

val listener = object : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        println("WebSocket Connected!")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        println("Receiving: $text")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        println("Receiving bytes: ${bytes.hex()}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        println("Closing: $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        println("Error: ${t.message}")
        t.printStackTrace()
    }
}

println("Connecting to WebSocket...")
val webSocket = client.newWebSocket(request, listener)

Thread.sleep(15000)
webSocket.close(1000, "Done")
client.dispatcher.executorService.shutdown()
println("Disconnected.")
exitProcess(0)
