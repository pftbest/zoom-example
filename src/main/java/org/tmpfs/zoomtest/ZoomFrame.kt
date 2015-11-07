package org.tmpfs.zoomtest

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.FrameLayout

class ZoomFrame : FrameLayout {
    private val gestureDetector: GestureDetector
    private val scaleDetector: ScaleGestureDetector

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : super(context, attrs, defStyleAttr) {
        gestureDetector = GestureDetector(context, GestureListener())
        scaleDetector = ScaleGestureDetector(context, ScaleListener())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var result = scaleDetector.onTouchEvent(event)
        if (!scaleDetector.isInProgress) {
            result = result or gestureDetector.onTouchEvent(event)
        }
        return result
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            //TODO: set offset
            return true
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            //TODO: set pivot
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            //TODO: set scale
            return true
        }
    }
}
