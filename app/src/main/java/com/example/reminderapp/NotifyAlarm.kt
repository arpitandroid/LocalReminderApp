package com.example.reminderapp

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat

import java.util.Date

class NotifyAlarm : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val reminder = Reminders()
        reminder.setTitle(intent.getStringExtra("Message"))
        reminder.description = intent.getStringExtra("desc")
        reminder.setRemindDate(Date(intent.getStringExtra("RemindDate")))
        reminder.setId(intent.getIntExtra("id", 0))

        val alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val intent1 = Intent(context, MainActivity::class.java)
        intent1.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val taskStackBuilder = TaskStackBuilder.create(context)
        taskStackBuilder.addParentStack(MainActivity::class.java)
        taskStackBuilder.addNextIntent(intent1)
        val intent2 = taskStackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context)
        var channel: NotificationChannel? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel =
                NotificationChannel("my_channel_01", "hello", NotificationManager.IMPORTANCE_HIGH)
        }
        val notification: Notification = builder.setContentTitle("Reminder")
            .setContentText(intent.getStringExtra("Message")).setAutoCancel(true)
            .setSound(alarmsound).setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(intent2)
            .setChannelId("my_channel_01")
            .build()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel!!)
        }
        notificationManager.notify(1, notification)
    }
}