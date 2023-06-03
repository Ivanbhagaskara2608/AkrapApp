package com.example.akrapapp.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_statistic.*

class StatisticActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)

        showBarChart()
        showPieChart()
    }

    private fun showPieChart() {
        val list: ArrayList<PieEntry> = ArrayList()

        list.add(PieEntry(4f,4f))
        list.add(PieEntry(5f,5f))
        list.add(PieEntry(3f,3f))
        list.add(PieEntry(2f,2f))

        val pieDataSet = PieDataSet(list, "list")
        pieDataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val percentage = value.toInt() * 10 // misalnya, nilai maksimum adalah 10
                return "$percentage%"
            }
        }
        pieDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 15f
        val pieData = PieData(pieDataSet)

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.centerText = "List"
        pieChart.animateY(2000)
    }

    private fun showBarChart() {
        val list: ArrayList<BarEntry> = ArrayList()

        list.add(BarEntry(3f,20f))
        list.add(BarEntry(2f,20f))
        list.add(BarEntry(1f,14f))
        list.add(BarEntry(4f,12f))
        list.add(BarEntry(5f,17f))
        list.add(BarEntry(6f,16f))
        list.add(BarEntry(7f,19f))
        list.add(BarEntry(8f,15f))
        list.add(BarEntry(9f,18f))
        list.add(BarEntry(10f,19f))
        list.add(BarEntry(11f,20f))
        list.add(BarEntry(12f,12f))

        val barDataSet = BarDataSet(list, "list")

        barDataSet.valueFormatter = DefaultValueFormatter(0)
        barDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        barDataSet.valueTextColor = Color.BLACK
        val barData = BarData(barDataSet)

        barchart.setFitBars(true)
        barchart.data = barData
        barchart.axisRight.setDrawLabels(false)

        barchart.xAxis.setDrawLabels(true)
        barchart.xAxis.setDrawAxisLine(false)
        barchart.xAxis.setDrawGridLines(false)

        barchart.description.isEnabled = false
        barchart.isScaleXEnabled = false
        barchart.isScaleYEnabled = false

        val dataCount = 14 // Jumlah total data pada BarChart
        val visibleCount = 5 // Jumlah data yang akan ditampilkan pada awal

// Set batas minimum dan maksimum pada sumbu x
        barchart.xAxis.axisMinimum = 0f
        barchart.xAxis.axisMaximum = (dataCount - 1).toFloat()

// Mengatur jumlah data yang ditampilkan pada awal
        val range = dataCount - visibleCount
        barchart.xAxis.isEnabled = range > 0 // Menyembunyikan sumbu x jika tidak ada data yang disembunyikan
        if (range > 0) {
            barchart.setVisibleXRange(0f, visibleCount.toFloat())
        } else {
            barchart.setVisibleXRange(0f, dataCount.toFloat())
        }

        barchart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barchart.xAxis.setLabelCount(5, false)
        val months = arrayOf("","Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

        barchart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return months.getOrNull(value.toInt()) ?: ""
            }
        }

        barchart.legend.isEnabled = false
        barchart.animateY(2000)
    }
}