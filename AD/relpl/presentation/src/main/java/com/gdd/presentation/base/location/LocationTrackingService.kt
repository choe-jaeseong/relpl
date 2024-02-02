package com.gdd.presentation.base.location

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import okhttp3.internal.notify

private const val TAG = "LocationTrackingService_Genseong"

class LocationTrackingService : Service(), LifecycleOwner {

    private lateinit var locationProviderController: LocationProviderController
    private val lifecycleRegistry = LifecycleRegistry(this)
    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override fun onCreate() {
        super.onCreate()
        locationProviderController = LocationProviderController(baseContext, this)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        makeNoticeChannel()
        val notificationBuilder =
            Notification
                .Builder(baseContext, LOCATION_TRANCKING_CHANNEL_ID)
                .setContentTitle("플로깅 경로를 기록중입니다.")
                .setContentText("")
                .setOngoing(true)
        val noti = notificationBuilder.build()


        locationProviderController.startTrackingLocation(0) { location, locationTrackingException ->
            if (location != null) {
                Log.d(TAG, "onStartCommand: ${location.longitude}${location.latitude}")
                synchronized(notificationBuilder) {
                    notificationBuilder.setContentText("${location.latitude}\n${location.longitude}")
                    notificationBuilder.notify()
                }

            }
        }
        startForeground(LOCATION_TRACKING_SERVICE_ID, noti)

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    private fun makeNoticeChannel() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(NotificationChannel(
            LOCATION_TRANCKING_CHANNEL_ID,
            "실시간 플로깅 추적 알람",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableLights(false)
        })
    }


    companion object {
        const val LOCATION_TRACKING_SERVICE_ID = 100000
        const val LOCATION_TRANCKING_CHANNEL_ID = "실시간 플로깅 위치 기록"
    }
}