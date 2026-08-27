package com.sanctum.core.feature.prayer.domain

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.NSBundle
import platform.Foundation.NSURL

class IosAudioPlayer : AudioPlayer {
    private var audioPlayer: AVAudioPlayer? = null

    @OptIn(ExperimentalForeignApi::class)
    override fun play(fileName: String) {
        stop()
        try {
            val name = fileName.substringBeforeLast(".")
            val ext = fileName.substringAfterLast(".", "")

            val urlStr = NSBundle.mainBundle.pathForResource(name, ofType = ext)
            if (urlStr != null) {
                val url = NSURL.fileURLWithPath(urlStr)
                audioPlayer = AVAudioPlayer(contentsOfURL = url, error = null)
                audioPlayer?.prepareToPlay()
                audioPlayer?.play()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stop()
        }
    }

    override fun stop() {
        audioPlayer?.stop()
        audioPlayer = null
    }
}

actual fun getAudioPlayer(): AudioPlayer = IosAudioPlayer()
