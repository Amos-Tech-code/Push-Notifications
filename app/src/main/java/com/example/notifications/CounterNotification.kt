package com.example.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class CounterNotification(
    private val context: Context
) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Define a vibration pattern: wait 0ms, vibrate 300ms, wait 200ms, vibrate 300ms
    private val vibrationPattern = longArrayOf(0, 300, 200, 300)

    fun counterNotification(counter: Int) {
        // Create the notification channel if on Android O or higher
        createNotificationChannel()

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            PendingIntent.FLAG_IMMUTABLE
        else 0

        val intent = Intent(context, MainActivity::class.java)
        val notificationClickPendingIntent = PendingIntent.getActivity(
            context,
            1,
            intent,
            flag
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_add_alert_24)
            .setContentTitle("Counter")
            .setContentText(counter.toString())
            .setStyle(NotificationCompat.BigTextStyle())
            .setOngoing(false)
            .setVibrate(vibrationPattern) // Use only custom vibration pattern
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Set priority for pre-Oreo devices
            .setVibrate(if (isVibrationEnabled()) vibrationPattern else null) // Fallback if vibration isn't available
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) // Set sound as fallback
            .setContentIntent(notificationClickPendingIntent)
            .addAction(
                R.drawable.baseline_add_alert_24,
                "Start",
                getPendingIntentAction(CounterActions.START, flag, 2)
            )
            .addAction(
                R.drawable.baseline_add_alert_24,
                "Stop",
                getPendingIntentAction(CounterActions.STOP, flag, 3)
            )
            .build()

        notificationManager.notify(1, notification)
    }

    // Method to create the notification channel for Android O and above
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Counter Notification"
            val descriptionText = "Notifications for the counter"
            val importance = NotificationManager.IMPORTANCE_HIGH

            // Create the notification channel and set vibration
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)  // Enable vibration for the channel
                vibrationPattern = this@CounterNotification.vibrationPattern  // Set vibration pattern
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    // Method to check if vibration is enabled on the device
    private fun isVibrationEnabled(): Boolean {
        val audioManager = ContextCompat.getSystemService(context, android.media.AudioManager::class.java)
        return audioManager?.ringerMode == android.media.AudioManager.RINGER_MODE_NORMAL
    }


    private fun getPendingIntentAction(
        action: CounterActions,
        flag: Int,
        requestCode: Int
    ): PendingIntent {

        val intent = Intent(context, CounterReceiver::class.java)

        when (action) {
            CounterActions.START -> intent.action = CounterActions.START.name
            CounterActions.STOP -> intent.action = CounterActions.STOP.name
        }

        return PendingIntent.getBroadcast(
            context, requestCode, intent, flag
        )
    }

    companion object {
        const val CHANNEL_ID = "counter_channel1"
    }

    enum class CounterActions {
        START,
        STOP
    }
}
