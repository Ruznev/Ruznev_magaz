package com.example.ruznev_magaz.domian.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionScene.Transition.TransitionOnClick
import androidx.recyclerview.widget.RecyclerView
import com.example.ruznev_magaz.R
import com.example.ruznev_magaz.domian.Adapters.ItemAdapter.ItemViewHolder
import com.example.ruznev_magaz.domian.Models.Item
import com.google.android.material.imageview.ShapeableImageView

class CartAdapter (
    private var items :List<Item>,
    private val onHeaderImageClick: (Item) -> Unit,
    private val onDeleteClick: (Item) -> Unit,
    private val onIncreaseQuantity: (Item) -> Unit,
    private val onDecreaseQuantity: (Item) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item_card, parent, false)
        return CartViewHolder(
            view,
            onHeaderImageClick,
            onDeleteClick,
            onIncreaseQuantity,
            onDecreaseQuantity
        )
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size


    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }

    class CartViewHolder(
        itemView: View,
        private val onHeaderImageClick: (Item) -> Unit,
        private val onDeleteClick: (Item) -> Unit,
        private val onIncreaseQuantity: (Item) -> Unit,
        private val onDecreaseQuantity: (Item) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val price: TextView = itemView.findViewById(R.id.subhead)
        private val headerImage: ShapeableImageView = itemView.findViewById(R.id.header_image)
        private val deleteButton: ShapeableImageView = itemView.findViewById(R.id.delete_from_cart)

        private val quantityText: TextView = itemView.findViewById(R.id.text_quantity)
        private val increaseButton: ShapeableImageView = itemView.findViewById(R.id.button_increase)
        private val decreaseButton: ShapeableImageView = itemView.findViewById(R.id.button_decrease)

        fun bind(item: Item) {
            title.text = item.title
            price.text = item.price
            headerImage.setImageResource(item.imageRestId)
            quantityText.text = item.quanity.toString()

            headerImage.setOnClickListener {
                onHeaderImageClick(item)
            }

            deleteButton.setOnClickListener{
                onDeleteClick(item)
            }

            increaseButton.setOnClickListener {
                onIncreaseQuantity(item)
            }

            decreaseButton.setOnClickListener {
                onDecreaseQuantity(item)
            }
        }
    }
}