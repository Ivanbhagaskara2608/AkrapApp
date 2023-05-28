package com.example.akrapapp.model

import com.example.akrapapp.R
import java.text.SimpleDateFormat
import java.util.*

data class DataItemInformation(
    val informationId: Int,
    val title: String,
    val content: String,
    val category: String,
    val created_at: String,
    val updated_at: String
) {
    val getImage = when(category) {
        "Berita" -> R.drawable.news_info
        "Acara" -> R.drawable.event_info
        "Pengumuman" -> R.drawable.announcement_info
        "Pembaruan" -> R.drawable.update_info
        else -> null
    }

    val sdfDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
    val parseDate = sdfDate.parse(created_at)
    val parseUpdatedDate = sdfDate.parse(updated_at)
    val formattedDate = SimpleDateFormat("dd MMM yyyy").format(parseDate)
    val formattedUpdatedDate = SimpleDateFormat("dd MMM yyyy").format(parseUpdatedDate)
}
