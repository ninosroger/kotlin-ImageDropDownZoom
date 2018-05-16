package com.ninos.imagedropdownzoom

import android.animation.ObjectAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //记录首次按下位置
    private var mFirstPosition = 0f
    //是否正在放大
    private var mScaling = false

    private lateinit var metric: DisplayMetrics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        metric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metric)

        var lp = top_img.layoutParams
        lp.width = metric.widthPixels
        lp.height = metric.widthPixels / 11 * 5
        top_img.layoutParams = lp

        scroll_view.setOnTouchListener(View.OnTouchListener { _, event ->
            var lp = top_img.layoutParams
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    mScaling = false
                    replyImage()
                    return@OnTouchListener false
                }
                MotionEvent.ACTION_MOVE -> {
                    if (!mScaling) {
                        if (scroll_view.scrollY == 0)
                            mFirstPosition = event.y
                        else
                            return@OnTouchListener false
                    }
                    var distance = (event.y - mFirstPosition) * 0.6
                    if (distance < 0)
                        return@OnTouchListener false
                    mScaling = true
                    lp.width = (metric.widthPixels + distance).toInt()
                    lp.height = ((metric.widthPixels + distance) / 11 * 5).toInt()
                    top_img.layoutParams = lp
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun replyImage() {
        val lp = top_img.layoutParams
        val w = top_img.layoutParams.width
        val h = top_img.layoutParams.height
        val newW = metric.widthPixels
        val newH = metric.widthPixels / 11 * 5

        var anim = ObjectAnimator.ofFloat(0.0f, 1.0f).setDuration(200)

        anim.addUpdateListener {
            var cVal: Float = it.animatedValue as Float
            lp.width = (w - (w - newW) * cVal).toInt()
            lp.height = (h - (h - newH) * cVal).toInt()
            top_img.layoutParams = lp
        }
        anim.start()
    }
}
