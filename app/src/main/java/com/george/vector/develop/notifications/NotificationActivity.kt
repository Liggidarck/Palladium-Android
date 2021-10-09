package com.george.vector.develop.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.george.vector.R
import com.google.firebase.messaging.FirebaseMessaging


class NotificationActivity : AppCompatActivity() {

    val TAG = "NotificationActivity"
    private val TOPIC = "/topics/topic-test"

    lateinit var btnSend: Button
    lateinit var etTitle: EditText
    lateinit var etMessage: EditText
    lateinit var etToken: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        btnSend = findViewById(R.id.btnSend)
        etTitle = findViewById(R.id.etTitile)
        etMessage = findViewById(R.id.etMessage)
        etToken = findViewById(R.id.etToken)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

    }


}