package com.george.vector.notifications

data class PushNotification(
    val data: NotificationData,
    val to: String
)