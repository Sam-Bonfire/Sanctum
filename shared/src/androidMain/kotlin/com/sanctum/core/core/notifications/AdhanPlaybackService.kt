package com.sanctum.core.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class AdhanPlaybackService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    companion object {
        const val ACTION_STOP = "com.sanctum.core.core.notifications.ACTION_STOP"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopPlayback()
            stopSelf()
            return START_NOT_STICKY
        }

        val title = intent?.getStringExtra("NOTIFICATION_TITLE") ?: "Prayer Time"
        val message = intent?.getStringExtra("NOTIFICATION_MESSAGE") ?: "It is time to pray."
        val soundFileName = intent?.getStringExtra("SOUND_FILE_NAME")

        startForegroundServiceWithNotification(title, message)

        if (soundFileName != null) {
            playAdhan(soundFileName)
        } else {
            stopSelf() // No audio, just a regular notification
        }

        return START_NOT_STICKY
    }

    private fun startForegroundServiceWithNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "adhan_playback_channel",
                "Adhan Playback",
                NotificationManager.IMPORTANCE_HIGH,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val stopIntent = Intent(this, AdhanPlaybackService::class.java).apply {
            action = ACTION_STOP
        }
        val pendingStopIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(this, "adhan_playback_channel")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .addAction(android.R.drawable.ic_media_pause, "Stop Adhan", pendingStopIntent)
            .setOngoing(true)
            .build()

        startForeground(1001, notification)
    }

    private fun playAdhan(soundFileName: String) {
        try {
            // Need a way to read from compose resources or assets.
            // The assets should be copied to androidMain/res/raw or context.assets
            // For this implementation we'll assume it's in raw folder or we read from assets

            // Assuming it's in assets:
            val afd = assets.openFd(soundFileName)
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build(),
                )
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                setOnCompletionListener {
                    stopSelf()
                }
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stopSelf() // Stop if failed to play
        }
    }

    private fun stopPlayback() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
    }

    override fun onDestroy() {
        stopPlayback()
        super.onDestroy()
    }
}
