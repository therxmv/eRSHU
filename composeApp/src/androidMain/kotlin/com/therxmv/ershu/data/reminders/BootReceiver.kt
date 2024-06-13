package com.therxmv.ershu.data.reminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.therxmv.ershu.data.source.local.DatabaseDriverFactory
import com.therxmv.ershu.data.source.local.reminders.RemindersLocalSource

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            resetReminders()
        }
    }

    private fun resetReminders() { // TODO 2 set reminders again after boot
        val driver = DatabaseDriverFactory() // past context here
        val source = RemindersLocalSource(driver)

        source.getAllReminders().forEach {
            source.deleteReminder(it.reminderId)
        }
    }
}