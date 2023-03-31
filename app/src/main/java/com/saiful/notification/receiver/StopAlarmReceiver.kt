package com.saiful.notification.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import androidx.core.app.NotificationManagerCompat

class StopAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Cancel the alarm
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)
        alarmMgr.cancel(pendingIntent)

        // Stop the media player
//        notificationPlayer(context, false)
//        val notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val mediaPlayer = MediaPlayer.create(context, notificationSoundUri)
//        mediaPlayer.stop()
//        mediaPlayer.release()
//        val audioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0)
//        AlarmReceiver.mediaPlayer?.stop()
//        AlarmReceiver.mediaPlayer?.release()
//        AlarmReceiver.mediaPlayer = null

        // Cancel the notification
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(1)
    }
}