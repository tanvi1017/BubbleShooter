package com.tanvi.myapplication

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings

class MusicService:Service() {

    private lateinit var player:MediaPlayer
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        player = MediaPlayer.create(this, R.raw.music53)
        player.setLooping(true)
        player.start()
        return START_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}