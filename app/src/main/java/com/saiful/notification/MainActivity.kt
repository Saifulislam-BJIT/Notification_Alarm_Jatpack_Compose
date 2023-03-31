package com.saiful.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.saiful.notification.receiver.AlarmReceiver
import com.saiful.notification.ui.theme.NotificationTheme
import com.saiful.notification.workers.Notification


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotificationTheme {
                NotificationApp()
            }
        }


    }

    @Composable
    fun NotificationApp(modifier: Modifier = Modifier) {
        var count by remember { mutableStateOf(0) }
        val context = LocalContext.current

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$count",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Button(onClick = {
                count++
                notification(context, count)
            }) {
                Text(text = "Click")
            }
        }
    }

    private fun notification(context: Context, count: Int) {

        val data = Data.Builder()
        data.putString(Notification.nameKey, count.toString())
        val workManager = WorkManager.getInstance(context)
        val workRequestBuilder = OneTimeWorkRequestBuilder<Notification>()
            .setInputData(data.build())
            .build()

//    val workRequestBuilder = OneTimeWorkRequestBuilder<WaterReminderWorker>()
//        .setInitialDelay(duration, unit)
//        .setInputData(data.build())
//        .build()

        workManager.enqueueUniqueWork(
            "plantName + duration",
            ExistingWorkPolicy.REPLACE,
            workRequestBuilder
        )


        // alarm
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val triggerTimeMillis = System.currentTimeMillis() + 2000 // 1 minute from now
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        NotificationTheme {
            NotificationApp()
        }
    }
}