package org.tmpfs.zoomtest

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
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

    private val firstChild: View
        get() = getChildAt(0)

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            firstChild.translationX -= distanceX
            firstChild.translationY -= distanceY
            return true
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            firstChild.setPivotFromParent(detector.focusX, detector.focusY)
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val factor = detector.scaleFactor
            firstChild.scaleX *= factor
            firstChild.scaleY *= factor
            return true
        }
    }
}

private fun View.setPivot(pX: Float, pY: Float) {
    // calculate the offset that new pivot value would cause
    val offsetX = (pX - pivotX) * (1 - scaleX)
    val offsetY = (pY - pivotY) * (1 - scaleY)

    // move the view
    translationX -= offsetX
    translationY -= offsetY

    // apply the pivot
    pivotX = pX
    pivotY = pY
}

private fun View.setPivotFromParent(pX: Float, pY: Float) {
    // translate pivot point from parent coordinates to child coordinates
    val cX = (pX - left + pivotX * (scaleX - 1) - translationX) / scaleX
    val cY = (pY - top + pivotY * (scaleY - 1) - translationY) / scaleY
    setPivot(cX, cY)
}
