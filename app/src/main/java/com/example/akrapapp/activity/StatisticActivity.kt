package com.example.akrapapp.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.github.mikephil.charting.data.*
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

        pieDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 15f
        val pieData = PieData(pieDataSet)

        pieChart.data = pieData
        pieChart.description.text = "Pie Chart"
        pieChart.centerText = "List"
        pieChart.animateY(2000)
    }

    private fun showBarChart() {
        val list: ArrayList<BarEntry> = ArrayList()

        list.add(BarEntry(4f,4f))
        list.add(BarEntry(5f,5f))
        list.add(BarEntry(3f,3f))
        list.add(BarEntry(2f,2f))

        val barDataSet = BarDataSet(list, "list")

        barDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        barDataSet.valueTextColor = Color.BLACK
        val barData = BarData(barDataSet)

        barchart.setFitBars(true)
        barchart.data = barData
        barchart.axisRight.setDrawLabels(false)
        barchart.description.text = "Bar Chart"
        barchart.animateY(2000)
    }
}