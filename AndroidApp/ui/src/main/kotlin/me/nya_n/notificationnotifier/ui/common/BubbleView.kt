package me.nya_n.notificationnotifier.ui.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlin.random.Random

@SuppressLint("ViewConstructor")
class BubbleView(
    context: Context,
    private val screenWidth: Float,
    private val screenHeight: Float
) : SurfaceView(context), SurfaceHolder.Callback {

    companion object {
        private const val MAX_CIRCLE = 7
    }

    private val r = Random(System.currentTimeMillis())
    private val coroutineScope = CoroutineScope(Job())
    private var drawJob: Job? = null
    private val backgroundPaint = Paint().apply {
        shader = LinearGradient(
            0f,
            0f,
            screenWidth,
            screenHeight,
            Color.parseColor("#FF8B67"),
            Color.parseColor("#FFAE5B"),
            Shader.TileMode.CLAMP
        )
        isAntiAlias = true
    }
    private val circlePaint = Paint().apply {
        color = Color.parseColor("#44FFFFFF")
        isAntiAlias = true
    }
    private val circles = Array(MAX_CIRCLE) { Circle(r, screenWidth, screenHeight) }

    init {
        setUp()
    }

    private fun setUp() {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // noop
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        drawJob = coroutineScope.launch {
            while (true) {
                ensureActive()
                // FIXME:
                //   怪しいErrorレベルのログが出ている
                //   クラッシュしないのでとりあえず見なかったことにするけどなんとかしたい
                //   [SurfaceView[me.nya_n.notificationnotifier/me.nya_n.notificationnotifier.ui.MainActivity]#3(BLAST Consumer)3](id:3c6f00000003,api:0,p:-1,c:15471) disconnect: not connected (req=2)
                val canvas = holder.lockCanvas() ?: break
                canvas.drawPaint(backgroundPaint)
                circles.forEach {
                    it.draw(canvas, circlePaint)
                    it.update()
                }
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        drawJob?.cancel()
    }
}

class Circle(
    private val r: Random,
    private val screenWidth: Float,
    private val screenHeight: Float,
    private val isPortrait: Boolean = true
) {
    private var size: Float = 0f
    private var x: Float = 0f
    private var y: Float = 0f
    private var speed: Float = 0f

    init {
        reset()
    }

    private fun reset() {
        size = (r.nextInt((screenWidth / if (isPortrait) 3 else 5).toInt()) + 48).toFloat()
        speed = (r.nextFloat() + r.nextFloat()) % 6 + 1
        x = r.nextInt(screenWidth.toInt()).toFloat()
        y = screenHeight + size + r.nextInt(screenHeight.toInt())
    }

    fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawCircle(
            x, y, size / 2, paint
        )
    }

    fun update() {
        y -= speed
        if (y + size < 0) {
            reset()
        }
    }
}