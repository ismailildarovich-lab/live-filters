package com.livefilters.overlay

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")))
        }

        findViewById<Button>(R.id.startBtn).setOnClickListener {
            startService(Intent(this, OverlayFilterService::class.java))
        }
        
        findViewById<Button>(R.id.stopBtn).setOnClickListener {
            stopService(Intent(this, OverlayFilterService::class.java))
        }
    }
}
