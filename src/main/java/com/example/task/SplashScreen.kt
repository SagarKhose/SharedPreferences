package com.example.task

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.logging.Handler
import kotlin.system.exitProcess

class SplashScreen : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 3000 // 3 seconds
    private val CHANNEL_ID = "ChannelId"
    private val CHANNEL_NAME = "ChannelName"
    private val TARGET_PACKAGE_NAME = "TARGET_PACKAGE_NAME"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        createNotificationChannel()
        // Use a Handler to delay the start of the main activity
        android.os.Handler().postDelayed({
            if (!isDestroyed) {
                val versionCode = getVersionCode()
                val Api_versionCode: Long = 2

                //  Log.d("verion_Code", versionCode.toString())
                if (versionCode != Api_versionCode) {
                    showNotification(
                        "HKB Development",
                        "New features has been added. Check it out and have fun..!"
                    )
                    Log.d("verion_CodeQQ", versionCode.toString())
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("APP UPDATE")
                        .setMessage("This app is Outdated. \n\n" + "Kindly update the latest apk")
                        .setPositiveButton("Cancel") { dialog, whichButton ->
                            // DO YOUR STAFF
                            this@SplashScreen.finish()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)

                            // Close this activity
                            finish()
                            exitProcess(0)

                        }
                        .setNegativeButton("Download") { dialog, whichButton ->
                            val browserIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
                            startActivity(browserIntent)
                            finish()
                        }
                    dialog.show()

                }else{
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    // Close this activity
                    finish()
                }
                // Start the main activity after the splash time out

            }
        }, SPLASH_TIME_OUT)
    }
    private fun getVersionCode(): Long {
        try {
            val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                packageInfo.versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return -1
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Sample notification channel"
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun showNotification(title: String, message: String) {

        val intent = Intent(this, SplashScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Add any additional data you want to pass to the MainActivity
            // putExtra("key", "value")
        }

        // Create the PendingIntent
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(123, builder.build())
            // }
        }
    }
}