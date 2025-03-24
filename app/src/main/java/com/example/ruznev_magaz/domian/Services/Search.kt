package com.example.ruznev_magaz.domian.Services

import android.app.DownloadManager.Query
import com.example.ruznev_magaz.domian.Models.Item

class Search(private val items: List<Item>) {

    fun filter (query: String):List<Item> {
        return items.filter {
            it.title.contains(query,ignoreCase = true) || it.description.contains(query, ignoreCase = true)
        }
    }
}