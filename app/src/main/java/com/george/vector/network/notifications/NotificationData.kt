package com.george.vector.network.notifications

data class NotificationData(
    val title: String,
    val message: String,
    val taskId: String,
    val collection: String
)