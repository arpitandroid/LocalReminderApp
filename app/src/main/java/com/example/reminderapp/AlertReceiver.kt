package com.example.reminderapp

import android.app.NotificationManager

import android.R
import android.app.Notification

import androidx.core.app.NotificationCompat

import android.app.PendingIntent
import android.app.TaskStackBuilder

import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import android.app.NotificationChannel

import android.os.Build

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val notificationIntent = Intent(context, MainActivity::class.java)
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(notificationIntent)
        val pendingIntent: PendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        createNotificationChannel(context)

        val builder = NotificationCompat.Builder(context)
        val notification: Notification = builder.setContentTitle("Reminder App Notification")
            .setContentText("New Notification From Reminder App")
            .setTicker("New Reminder!")
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .setContentIntent(pendingIntent).build()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)

    }

    /**
     * This needs to be called at least once on android API >= 26 before creating a notification.
     */
    fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "my_channel",
                "MyApp notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "They will wake you up in the night"
            channel.enableVibration(true)

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}