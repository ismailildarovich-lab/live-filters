package com.livefilters.overlay

import android.app.Service
import android.content.Intent
import android.graphics.*
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast

class OverlayFilterService : Service() {
    private var isRunning = true
    private var filterType = 0
    private var isFilterActive = true

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createOverlay()
    }

    private fun createOverlay() {
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        )

        val surfaceView = SurfaceView(this)
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) { startFilterLoop(holder) }
            override fun surfaceChanged(holder: SurfaceHolder, f: Int, w: Int, h: Int) {}
            override fun surfaceDestroyed(holder: SurfaceHolder) { isRunning = false }
        })

        wm.addView(surfaceView, params)
        wm.addView(createControls(), createControlParams())
    }

    private fun createControls(): View {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 100, 20, 20)
            setBackgroundColor(Color.argb(180, 0, 0, 0))
        }

        layout.addView(Button(this).apply {
            text = "🎮 Фильтр: ВКЛ"
            setOnClickListener {
                isFilterActive = !isFilterActive
                text = if (isFilterActive) "🎮 Фильтр: ВКЛ" else "🎮 Фильтр: ВЫКЛ"
            }
        })

        val filters = arrayOf("Ориг", "Кибер", "Винт", "Холод", "Тёпл", "Неон", "Моно", "Негат", "Сепия", "Аква", "Глам")
        val filterLayout = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        filters.forEachIndexed { i, name ->
            filterLayout.addView(Button(this).apply {
                text = name
                textSize = 10f
                setOnClickListener {
                    filterType = i
                    Toast.makeText(this@OverlayFilterService, "Фильтр: $name", Toast.LENGTH_SHORT).show()
                }
            })
        }
        layout.addView(TextView(this).apply { text = "Фильтры:"; setTextColor(Color.WHITE) })
        layout.addView(filterLayout)

        return layout
    }

    private fun createControlParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.END; x = 20; y = 100 }
    }

    private fun startFilterLoop(holder: SurfaceHolder) {
        Thread {
            while (isRunning) {
                val canvas = holder.lockCanvas()
                canvas?.let {
                    val paint = Paint()
                    when (filterType) {
                        1 -> canvas.drawColor(Color.argb(80, 255, 0, 255))
                        2 -> canvas.drawColor(Color.argb(80, 255, 200, 150))
                        3 -> canvas.drawColor(Color.argb(80, 100, 150, 255))
                        4 -> canvas.drawColor(Color.argb(80, 255, 200, 100))
                        5 -> canvas.drawColor(Color.argb(80, 0, 255, 255))
                        6 -> canvas.drawColor(Color.argb(80, 128, 128, 128))
                        7 -> canvas.drawColor(Color.argb(80, 255, 255, 255))
                        8 -> canvas.drawColor(Color.argb(80, 200, 150, 100))
                        9 -> canvas.drawColor(Color.argb(80, 200, 220, 255))
                        10 -> canvas.drawColor(Color.argb(80, 255, 150, 200))
                    }
                    holder.unlockCanvasAndPost(canvas)
                }
                Thread.sleep(33)
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }
}
