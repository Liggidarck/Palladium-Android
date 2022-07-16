package com.george.vector.common.notifications

data class PushNotification(
    val data: NotificationData,
    val to: String
)