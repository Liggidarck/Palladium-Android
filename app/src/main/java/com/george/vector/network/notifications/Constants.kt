package com.george.vector.network.notifications

import com.george.vector.BuildConfig

class Constants {
    companion object {
        const val BASE_URL = "https://fcm.googleapis.com"
        const val SERVER_KEY = BuildConfig.SERVER_KEY
        const val CONTENT_TYPE = "application/json"
    }

}