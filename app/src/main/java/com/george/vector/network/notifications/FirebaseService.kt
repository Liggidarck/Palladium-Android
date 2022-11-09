package com.george.vector.network.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.george.vector.R
import com.george.vector.ui.common.auth.LoadingActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

private const val CHANNEL_NEW_TASKS_CREATE_ID = "Новые заявки"
private const val CHANNEL_DEVELOPER = "Уведомления для отладки"

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseService : FirebaseMessagingService() {

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val intent = Intent(this, LoadingActivity::class.java)
        intent.putExtra("id", message.data["taskId"])
        intent.putExtra("collection", message.data["collection"])
        intent.action = "showmessage"

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, CHANNEL_NEW_TASKS_CREATE_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.notification_logo)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, notification)

        val devNotification = NotificationCompat.Builder(this, CHANNEL_DEVELOPER)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.notification_logo)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, devNotification)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "Новые заявки"
        val channel = NotificationChannel(CHANNEL_NEW_TASKS_CREATE_ID, channelName, IMPORTANCE_HIGH).apply {
                description = "Канал новых заявок"
                enableLights(true)
                lightColor = Color.GREEN
            }
        notificationManager.createNotificationChannel(channel)

        val developChannel = "Уведомления для отладки"
        val dev = NotificationChannel(CHANNEL_DEVELOPER, developChannel, IMPORTANCE_HIGH).apply {
            description = "Канал новых заявок"
            enableLights(true)
            lightColor = Color.RED
        }
        notificationManager.createNotificationChannel(dev)
    }
}