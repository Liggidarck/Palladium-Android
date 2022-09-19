package com.george.vector.common.notifications

data class NotificationData(
    val title: String,
    val message: String,
    val taskId: String,
    val collection: String
)