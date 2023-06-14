package com.example.akrapapp.adapter

import android.content.Context
import android.widget.TextView
import com.example.akrapapp.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CustomMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private val markerTextView: TextView = findViewById(R.id.markerTextView)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        // Mendapatkan nilai dari Entry
        val value = e?.y ?: 0f
        // Menampilkan nilai pada TextView di dalam marker view
        markerTextView.text = value.toInt().toString()
        super.refreshContent(e, highlight)
    }

    override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
        val offsetX = -(width / 2).toFloat()
        val offsetY = -height.toFloat()
        return MPPointF(offsetX, offsetY)
    }
}