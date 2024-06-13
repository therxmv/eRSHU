package com.therxmv.ershu.data.reminders

import com.therxmv.ershu.data.reminders.event.model.EventModel
import com.therxmv.ershu.data.reminders.event.model.ReminderModel
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSDayCalendarUnit
import platform.Foundation.NSHourCalendarUnit
import platform.Foundation.NSMinuteCalendarUnit
import platform.Foundation.NSMonthCalendarUnit
import platform.Foundation.NSSecondCalendarUnit
import platform.Foundation.NSYearCalendarUnit
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNUserNotificationCenter

actual class RemindersApi {

    actual fun addNotification(eventModel: EventModel, onComplete: (String) -> Unit) {
        val center = UNUserNotificationCenter.currentNotificationCenter()

        isPermissionGranted {
            if (it) {
                val content = UNMutableNotificationContent().apply {
                    setTitle(eventModel.title)
                    setBody(eventModel.description)
                    setSound(UNNotificationSound.defaultSound())
                }
                val calendar = NSCalendar.currentCalendar()
                val futureDate = NSDate.dateWithTimeIntervalSince1970(
                    (eventModel.reminderModel.startDate / 1000).toDouble()
                )
                val components = calendar.components(
                    NSYearCalendarUnit or NSMonthCalendarUnit
                            or NSDayCalendarUnit or NSHourCalendarUnit
                            or NSMinuteCalendarUnit or NSSecondCalendarUnit,
                    futureDate,
                )
                println(futureDate.timeIntervalSince1970)

                val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(components, false)

                val id = eventModel.reminderModel.reminderId
                val request = UNNotificationRequest.requestWithIdentifier(id, content, trigger)

                center.addNotificationRequest(request) {
                    onComplete(id)
                }
            }
        }
    }

    actual fun deleteNotification(reminderModel: ReminderModel) {
        val center = UNUserNotificationCenter.currentNotificationCenter()

        center.removePendingNotificationRequestsWithIdentifiers(listOf(reminderModel.reminderId))
    }

    actual fun requestNotificationPermission() {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        val authOptions = UNAuthorizationOptionAlert or UNAuthorizationOptionBadge or UNAuthorizationOptionSound

        center.requestAuthorizationWithOptions(authOptions) { _, _ -> }
    }

    actual fun isPermissionGranted(onComplete: (Boolean) -> Unit) {
        val center = UNUserNotificationCenter.currentNotificationCenter()

        center.getNotificationSettingsWithCompletionHandler {
            onComplete(it?.authorizationStatus == UNAuthorizationStatusAuthorized)
        }
    }
}