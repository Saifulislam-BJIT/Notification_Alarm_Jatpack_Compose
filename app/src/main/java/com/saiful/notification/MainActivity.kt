package com.saiful.notification

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.saiful.notification.receiver.AlarmReceiver
import com.saiful.notification.ui.theme.NotificationTheme
import com.saiful.notification.workers.Notification
import java.util.*


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotificationTheme {
                NotificationApp()
            }
        }

        val requestLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    //main activity
                    startActivity(
                        Intent(
                            this@MainActivity,
                            MainActivity::class.java
                        )
                    )
                } else {
                    //show error message
                    showErrorMessage()
                }
            }
//        requestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) -> {
                // You can use the API that requires the permission.
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }

    private fun showErrorMessage() {
        Toast.makeText(
            this,
            "Permission is not granted",
            Toast.LENGTH_SHORT
        ).show()
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
         val alarmMgr: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent: PendingIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
          PendingIntent.getBroadcast(context, 0, intent,  0)
      }
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 30)
        }

        // here was a dot
        alarmMgr.set(
            AlarmManager.RTC_WAKEUP,
            SystemClock.elapsedRealtime() + 2000,
            alarmIntent
        )
//        alarmMgr?.cancel(alarmIntent)
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        NotificationTheme {
            NotificationApp()
        }
    }
}