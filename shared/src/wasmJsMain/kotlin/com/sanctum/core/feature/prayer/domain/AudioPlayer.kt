package com.sanctum.core.feature.prayer.domain

import org.w3c.dom.Audio

class WasmAudioPlayer : AudioPlayer {
    private var audio: Audio? = null

    override fun play(fileName: String) {
        stop()
        try {
            audio = Audio(fileName).apply {
                play()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stop()
        }
    }

    override fun stop() {
        audio?.pause()
        audio = null
    }
}

actual fun getAudioPlayer(): AudioPlayer = WasmAudioPlayer()
