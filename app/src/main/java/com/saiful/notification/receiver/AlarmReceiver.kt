package com.saiful.notification.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.saiful.notification.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Perform the action you need to do when the alarm goes off

        // Create notification sound
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(context!!, soundUri)
        mediaPlayer.prepare()
        mediaPlayer.start()

        // Create notification
        val notificationManager =
            ContextCompat.getSystemService(context!!, NotificationManager::class.java)
        val notification = NotificationCompat.Builder(context, "alarm_channel_id")
            .setSmallIcon(R.drawable.notification_flat)
            .setContentTitle("Alarm")
            .setContentText("Alarm triggered!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(soundUri)
            .build()

        notificationManager?.notify(1, notification)
    }
}