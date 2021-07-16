package com.george.vector.develop.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.george.vector.R
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/topic-test"

class NotificationActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    lateinit var btnSend: Button
    lateinit var etTitile: EditText
    lateinit var etMessage: EditText
    lateinit var etToken: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        btnSend = findViewById(R.id.btnSend)
        etTitile = findViewById(R.id.etTitile)
        etMessage = findViewById(R.id.etMessage)
        etToken = findViewById(R.id.etToken)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        btnSend.setOnClickListener {
            val titile = etTitile.text.toString()
            val message = etMessage.text.toString()

            if(titile.isNotEmpty() && message.isNotEmpty()) {
                PushNotification(
                    NotificationData(titile, message),
                    TOPIC
                ).also {
                    sendNotification(it)
                }

            }
        }

    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful){
                Log.d(TAG, "Response: ${Gson().toJson(response)}");
            } else {
                Log.e(TAG, response.errorBody().toString())
            }

        } catch (e : Exception) {
            Log.e(TAG, e.toString())
        }
    }

}