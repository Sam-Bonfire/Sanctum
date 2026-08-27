package com.sanctum.core.feature.prayer.domain

interface AudioPlayer {
    fun play(fileName: String)
    fun stop()
}

expect fun getAudioPlayer(): AudioPlayer
