package com.ilnur.mobileowner.data.network.firebase

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ilnur.mobileowner.R

class MyFirebaseService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        createNotification(remoteMessage!!.notification?.title, remoteMessage!!.notification?.body)
    }

    private fun createNotification(tittle: String?, message: String?) {
        val channelId = "${this.packageName}-${this.getString(R.string.app_name)}"
        val notificationBuilder = NotificationCompat.Builder(this, channelId).apply {
            setSmallIcon(R.drawable.ic_report)
            setContentTitle(tittle)
            setContentText(message)
            priority = NotificationCompat.PRIORITY_MAX
            setAutoCancel(true)
        }

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(10, notificationBuilder.build())
    }
}