package com.gdd.presentation.base.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import com.gdd.domain.repository.LocationTrackingRepository
import com.gdd.presentation.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LocationTrackingService_Genseong"

@AndroidEntryPoint
class LocationTrackingService : Service(), LifecycleOwner {

    @Inject
    lateinit var locationTrackingRepository: LocationTrackingRepository

    private lateinit var notificationManager: NotificationManager
    private lateinit var locationProviderController: LocationProviderController
    private val lifecycleRegistry = LifecycleRegistry(this)
    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    private var firstTime: Long? = null

    override fun onCreate() {
        super.onCreate()
        locationProviderController = LocationProviderController(baseContext, this)
        makeNoticeChannel()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val notificationBuilder =
            NotificationCompat
                .Builder(this, LOCATION_TRANCKING_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_location)
                .setContentTitle("플로깅 경로를 기록중입니다.")
                .setContentText("")
                .setOnlyAlertOnce(true)
                .setAutoCancel(false)
                .setOngoing(true)

        val noti = notificationBuilder.build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            startForeground(LOCATION_TRACKING_SERVICE_ID, noti, FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(LOCATION_TRACKING_SERVICE_ID, noti)
        }


        locationProviderController.startTrackingLocation(0) { location, locationTrackingException ->
            if (location != null) {
                Log.d(TAG, "onStartCommand: ${location.longitude}${location.latitude}")
                synchronized(notificationManager) {
                    notificationManager.notify(LOCATION_TRACKING_SERVICE_ID,notificationBuilder.build())
                }

                lifecycleScope.launch {
                    val time = System.currentTimeMillis()
                    locationTrackingRepository.saveLocationTrackingData(
                        time,
                        location.latitude,
                        location.longitude,
                        if (firstTime == null){
                            firstTime = time
                            0
                        } else {
                            ((time - firstTime!!)/1000).toInt()
                        }
                    )
                }
            }
        }


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
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
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