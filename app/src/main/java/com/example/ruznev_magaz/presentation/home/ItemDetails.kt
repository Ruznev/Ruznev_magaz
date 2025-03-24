package com.example.ruznev_magaz.presentation.home

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ruznev_magaz.R
import com.google.android.material.imageview.ShapeableImageView

class ItemDetails : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_details_screen)

        val title = intent.getStringExtra("item_title")
        val price = intent.getStringExtra("item_price")
        val description = intent.getStringExtra("item_description")
        val imageRestId = intent.getIntExtra("item_image",R.drawable.icons_default_avatar)

        findViewById<TextView>(R.id.details_title).text = title
        findViewById<TextView>(R.id.details_price).text = price
        findViewById<TextView>(R.id.details_description).text = description
        findViewById<ShapeableImageView>(R.id.details_image).setImageResource(imageRestId)
    }
}