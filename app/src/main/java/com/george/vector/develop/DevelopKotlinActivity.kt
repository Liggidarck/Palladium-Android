package com.george.vector.develop

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.george.vector.databinding.ActivityDevelopKotlinBinding
import com.george.vector.notifications.NotificationData
import com.george.vector.notifications.PushNotification
import com.george.vector.notifications.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic1"

class DevelopKotlinActivity : AppCompatActivity() {

    val TAG = "DevelopKotlinActivity"
    private lateinit var binding: ActivityDevelopKotlinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDevelopKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        FirebaseMessaging.getInstance().token.addOnSuccessListener {
//            binding.developTokenEditText.editText?.setText(it)
//        }

//        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        binding.sendButton.setOnClickListener {
            val title = binding.developTitleEditText.editText?.text.toString()
            val message = binding.developMessageEditText.editText?.text.toString()
            val recipientToken = binding.developTokenEditText.editText?.text.toString()
            if (title.isNotEmpty() && message.isNotEmpty()) {
                PushNotification(
                    NotificationData(title, message),
                    TOPIC
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