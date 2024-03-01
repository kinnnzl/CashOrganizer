package com.example.cashorganizer.utilities

import android.graphics.Canvas
import android.graphics.Point
import android.view.View

class MyDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {
    private var mScaleFactor: Point? = null
    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        val width: Int = view.width / 2
        val height: Int = view.height / 2
        size[width] = height
        mScaleFactor = size
        touch[width / 2] = height / 2
    }

    override fun onDrawShadow(canvas: Canvas) {
        canvas.scale(
            mScaleFactor!!.x / view.width.toFloat(),
            mScaleFactor!!.y / view.height.toFloat()
        )
        view.draw(canvas)
    }
}