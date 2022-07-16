package com.george.vector.common.notifications

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendNotification {

    val tag = "SendNotification"

    fun sendNotification(title: String, message: String, TOPIC: String) {
        PushNotification(
            NotificationData(title, message),
            TOPIC
        ).also {
            sendNotificationAPI(it)
        }
    }

    private fun sendNotificationAPI(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                RetrofitInstance.api.postNotification(notification)
            } catch (e: Exception) {
                Log.e(tag, e.toString())
            }
        }

}