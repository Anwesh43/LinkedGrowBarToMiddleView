package com.anwesh.uiprojects.growbartomiddleview

/**
 * Created by anweshmishra on 05/06/20.
 */

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF

val colors : Array<String> = arrayOf("#3F51B5", "#4CAF50", "#2196F3", "#009688", "#F44336")
val bars : Int = 5
val scGap : Float = 0.02f / bars
val backColor : Int = Color.parseColor("#BDBDBD")
val delay : Long = 20
val rot : Float = 90f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawGrowMiddleBar(i : Int, sf : Float, w : Float, h : Float, paint : Paint) {
    val sf1 : Float = sf.divideScale(0, 2)
    val sf1i : Float = sf1.divideScale(i, bars)
    val sf1i1 : Float = sf1i.divideScale(0, 2)
    val sf1i2 : Float = sf1i.divideScale(1, 2)
    val gap : Float = w / bars
    val size : Float = gap * sf1i2
    save()
    translate(-w / 2 + gap * i + gap / 2, (h / 2 - gap / 2) * (1f - sf1i1))
    drawRect(RectF(-size / 2, size / 2, size / 2, size / 2), paint)
    restore()
}

fun Canvas.drawGrowMiddleBars(sf : Float, w : Float, h : Float, paint : Paint) {
    for (i in 0..(bars - 1)) {
        drawGrowMiddleBar(i, sf, w, h, paint)
    }
}

fun Canvas.drawGMBNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val sf : Float = scale.sinify()
    save()
    translate(w / 2, h / 2)
    rotate(rot * sf.divideScale(1, 2))
    drawGrowMiddleBars(sf, w, h, paint)
    restore()
}

class GrowBarMiddleView(ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }
}