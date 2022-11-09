package com.george.vector.network.notifications

data class PushNotification(
    val data: NotificationData,
    val to: String
)