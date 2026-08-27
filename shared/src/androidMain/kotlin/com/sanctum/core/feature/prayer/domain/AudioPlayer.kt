package com.sanctum.core.feature.prayer.domain

import android.media.MediaPlayer
import com.sanctum.core.core.database.applicationContext

class AndroidAudioPlayer : AudioPlayer {
    private var mediaPlayer: MediaPlayer? = null

    override fun play(fileName: String) {
        stop()
        try {
            val afd = applicationContext.assets.openFd(fileName)
            mediaPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                setOnCompletionListener {
                    stop()
                }
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stop()
        }
    }

    override fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
    }
}

actual fun getAudioPlayer(): AudioPlayer = AndroidAudioPlayer()
