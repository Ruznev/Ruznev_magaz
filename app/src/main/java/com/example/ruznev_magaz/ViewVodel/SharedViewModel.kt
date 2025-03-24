package com.example.ruznev_magaz.ViewVodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ruznev_magaz.domian.Models.Item
import com.example.ruznev_magaz.presentation.home.ItemDetails

class SharedViewModel : ViewModel() {
    val selectedItems = MutableLiveData<List<Item>>()
    val totalCost = MutableLiveData<String>()

    init {
        totalCost.value = "0 RUB"
    }

    private fun updateTotalCost(items: List<Item>) {
        var total = 0
        for (item in items) {
            val price = item.price.replace("RUB","").toIntOrNull() ?:0
            total += price * item.quanity
        }
        totalCost.value = "$total RUB"
    }

    fun addItem(item: Item) {
        val currentList = selectedItems.value ?: mutableListOf()
        val existingItem = currentList.find { it.title == item.title }

        if (existingItem !=null ) {
            existingItem.quanity += item.quanity
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(item)
            selectedItems.value = updatedList
        }
        updateTotalCost(selectedItems.value ?: emptyList())
    }

    fun removeItem(item: Item) {
        val currentList = selectedItems.value ?: mutableListOf()
        val updateList = currentList.toMutableList()
        updateList.remove(item)
        selectedItems.value = updateList

        updateTotalCost(updateList)
    }

    fun increaseQuanity(item: Item) {
        val currentList = selectedItems.value ?: mutableListOf()
        val existingItem = currentList.find { it.title == item.title }

        if (existingItem != null) {
            existingItem.quanity +=1
            selectedItems.value = currentList

            updateTotalCost(currentList)
        }
    }

    fun decreaseQuantity(item: Item) {
        val currentList = selectedItems.value ?: mutableListOf()
        val existingItem = currentList.find { it.title == item.title }
        if (existingItem != null) {
            if(existingItem.quanity >1){
                existingItem.quanity -=1
            } else{
                removeItem(existingItem)
            }
            selectedItems.value = currentList

            updateTotalCost(currentList)
        }
    }

    fun showDetails (context: Context,item: Item) {
        val intent = Intent(context,ItemDetails::class.java).apply {
            putExtra("item_title",item.title)
            putExtra("item_price",item.price)
            putExtra("item_description",item.description)
            putExtra("item_image",item.imageRestId)
        }
        context.startActivity(intent)
    }
}