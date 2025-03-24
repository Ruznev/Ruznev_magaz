package com.example.ruznev_magaz.domian.Services

import com.example.ruznev_magaz.domian.Models.Item

class Filter (private val items: List<Item>) {

    fun filterByPrice(maxPrice : Int) : List<Item> {
        return items.filter {
            val price = it.price.replace("RUB","").toIntOrNull() ?: 0
            price <= maxPrice
        }
    }

    fun filterByCategory(keyword : String) : List<Item> {
        return items.filter {
            it.title.contains(keyword,ignoreCase = true)
        }
    }

    fun resetFilter() : List<Item> {
        return items
    }
}