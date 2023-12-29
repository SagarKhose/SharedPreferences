package com.example.task

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat

class Demofile : AppCompatActivity() {
    private val NOTIFICATION_CHANNEL_ID = "version_mismatch_channel"
    private val NOTIFICATION_ID = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demofile)
        showVersionMismatchNotification()

    }private fun showVersionMismatchNotification() {
        if(!isDestroyed){
            // Create a notification channel (required for Android Oreo and above)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Version Mismatch",
                    NotificationManager.IMPORTANCE_HIGH
                )

                notificationManager.createNotificationChannel(channel)
            }

            // Build the notification
            val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle("Version Mismatch")
                .setContentText("This version is no longer supported. Please update the app.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            // Show the notification
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

            // Close the activity
            finish()
        }}

}