package com.example.cashorganizer.utilities

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.View

class MyDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {

    private val shadow = ColorDrawable(Color.LTGRAY)
    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        val width: Int = view.width / 2
        val height: Int = view.height / 2
        shadow.setBounds(0, 0, width, height)
        size.set(width, height)
        touch.set(width / 2, height / 2)
    }
    override fun onDrawShadow(canvas: Canvas) {
        shadow.draw(canvas)
    }
}