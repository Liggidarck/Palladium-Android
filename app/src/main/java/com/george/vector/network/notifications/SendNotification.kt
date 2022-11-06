package com.george.vector.network.notifications

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendNotification {

    private val tag = "SendNotification"

    fun sendNotification(title: String, message: String, taskId: String, collection: String, TOPIC: String) {
        PushNotification(
            NotificationData(title, message, taskId, collection),
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