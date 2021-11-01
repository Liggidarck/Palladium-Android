package com.george.vector.develop.notifications

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.george.vector.databinding.ActivityDevelopKotlinBinding
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic"

class DevelopKotlinActivity : AppCompatActivity() {

    val TAG = "DevelopKotlinActivity"
    private lateinit var binding: ActivityDevelopKotlinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDevelopKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            binding.developTokenEditText.editText?.setText(it.token)
        }

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        binding.sendButton.setOnClickListener {
            val title = binding.developTitleEditText.editText?.text.toString()
            val message = binding.developMessageEditText.editText?.text.toString()
            val recipientToken = binding.developTokenEditText.editText?.text.toString()
            if (title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
                PushNotification(
                    NotificationData(title, message),
                    recipientToken
                ).also {
                    sendNotification(it)
                }
            }
        }

    }

    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                RetrofitInstance.api.postNotification(notification)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }

}