package com.therxmv.ershu.data.reminders

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES.O
import android.os.Build.VERSION_CODES.S
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.therxmv.ershu.AndroidApp
import com.therxmv.ershu.AppActivity
import com.therxmv.ershu.data.reminders.event.model.EventModel
import com.therxmv.ershu.data.reminders.event.model.ReminderModel
import com.therxmv.ershu.utils.Reminders.REMINDERS_CHANNEL_NAME
import com.therxmv.ershu.utils.Reminders.REMINDER_BODY
import com.therxmv.ershu.utils.Reminders.REMINDER_CHANNEL_ID
import com.therxmv.ershu.utils.Reminders.REMINDER_ID
import com.therxmv.ershu.utils.Reminders.REMINDER_TITLE

actual class RemindersApi {

    actual fun addNotification(eventModel: EventModel, onComplete: (String) -> Unit) {
        val context = AndroidApp.INSTANCE
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        context.createChannel()

        val reminderId = eventModel.reminderModel.reminderId.drop(4).toInt()

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(REMINDER_ID, reminderId)
            putExtra(REMINDER_TITLE, eventModel.title)
            putExtra(REMINDER_BODY, eventModel.description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId,
            intent,
            FLAG_IMMUTABLE,
        )

        isPermissionGranted {
            if (it) {
                alarmManager.setReminder(eventModel.reminderModel.startDate, pendingIntent)
            }
        }

        onComplete(reminderId.toString())
    }

    actual fun deleteNotification(reminderModel: ReminderModel) {
        val context = AndroidApp.INSTANCE
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ReminderReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderModel.reminderId.drop(4).toInt(),
            intent,
            FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT,
        )

        alarmManager.cancel(pendingIntent)
    }

    actual fun requestNotificationPermission() {
        AppActivity.ACTIVITY?.let {
            val permission = buildList {
                if (checkVersion(TIRAMISU)) add(POST_NOTIFICATIONS)
            }.toTypedArray()

            ActivityCompat.requestPermissions(
                it,
                permission,
                0,
            )
        }
    }

    actual fun isPermissionGranted(onComplete: (Boolean) -> Unit) {
        val context = AndroidApp.INSTANCE
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val isNotificationsGranted = if (checkVersion(TIRAMISU)) {
            ContextCompat.checkSelfPermission(
                context,
                POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

        val isAlarmGranted = if (checkVersion(S)) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }

        onComplete(isNotificationsGranted && isAlarmGranted)
    }

    private fun Context.createChannel() {
        if (checkVersion(O)) {
            val channel = NotificationChannel(
                REMINDER_CHANNEL_ID,
                REMINDERS_CHANNEL_NAME,
                IMPORTANCE_HIGH,
            )

            ContextCompat.getSystemService(this, NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
    }

    private fun AlarmManager.setReminder(time: Long, intent: PendingIntent) {
        try {
            setExactAndAllowWhileIdle(
                RTC_WAKEUP,
                time,
                intent,
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun checkVersion(version: Int) = Build.VERSION.SDK_INT >= version
}