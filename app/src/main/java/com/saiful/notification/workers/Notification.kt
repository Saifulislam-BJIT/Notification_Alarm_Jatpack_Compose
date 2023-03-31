package com.saiful.notification.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class Notification(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {

        val message = inputData.getString(nameKey) ?: ""
        makePlantReminderNotification(
            message,
            applicationContext
        )

        return Result.success()
    }

    companion object {
        const val nameKey = "NAME"
    }
}