package com.saiful.notification.receiver

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri

val notificationSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
fun notificationPlayer(context: Context, status: Boolean) {
    val mediaPlayer = MediaPlayer.create(context, notificationSoundUri)
    if (status) {
        mediaPlayer.start()
    } else {
        mediaPlayer.stop()
    }
}