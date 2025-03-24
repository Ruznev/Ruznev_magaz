package com.example.ruznev_magaz.presentation.home.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ruznev_magaz.domian.Adapters.ItemAdapter
import com.example.ruznev_magaz.R
import com.example.ruznev_magaz.ViewVodel.SharedViewModel
import com.example.ruznev_magaz.domian.Models.Item
import com.example.ruznev_magaz.domian.Services.Filter
import com.example.ruznev_magaz.domian.Services.Search
import com.example.ruznev_magaz.presentation.home.ItemDetails

class ItemFragment : Fragment(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var searchEditText: EditText
    private  lateinit var originalItems: List<Item>
    private  lateinit var search: Search
    private  lateinit var filter: Filter
    private  lateinit var filterButton: ImageView
    private  lateinit var gridChangeButton: ImageView
    private  lateinit var sharedViewModel: SharedViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item,container,false)

        gridChangeButton = view.findViewById(R.id.recycler_change_grid_button)
        var isClicked = true

        gridChangeButton.setOnClickListener()
        {
            if (isClicked)
            {
                gridChangeButton.setImageResource(R.drawable.icons_vertical_grid)
                recyclerView.layoutManager = GridLayoutManager(context,2)
            }
            else {
                gridChangeButton.setImageResource(R.drawable.icons_horizontal_grid)
                recyclerView.layoutManager = LinearLayoutManager(context)
            }

            isClicked = !isClicked
        }

        recyclerView = view.findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        originalItems = listOf(
            Item("2K Sport Winder","3990","ДЛИННЫЙ ТЕКСТ 1",R.drawable.product_first),
            Item("Adidas Gazelle","16935","ДЛИННЫЙ ТЕКСТ 2",R.drawable.product_second),
            Item("Adidas Neo Breaknet","11473","ДЛИННЫЙ ТЕКСТ 3",R.drawable.product_third),
            Item("Nike Peagasus Trail 3 GTX","18629","ДЛИННЫЙ ТЕКСТ 4",R.drawable.product_four),
            Item("4K SUPER Ultra","5950","ДЛИННЫЙ ТЕКСТ 5",R.drawable.product_first),
            Item("8K 3D Cross","7890","ДЛИННЫЙ ТЕКСТ 6",R.drawable.product_third),
        )
        filter = Filter(originalItems)

        search = Search(originalItems)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        adapter = ItemAdapter(originalItems,
            onAddToCartClick = {item -> addToCart(item)},
            onDetailsClick = {item -> sharedViewModel.showDetails(requireContext(),item)},
            onHeaderImageClick = {item -> sharedViewModel.showDetails(requireContext(),item)})

        recyclerView.adapter = adapter

        filterButton = view.findViewById(R.id.filter_button)
        filterButton.setOnClickListener()
        {
            showFilterOptions()
        }

        searchEditText = view.findViewById(R.id.search_edit_text)
        searchEditText.addTextChangedListener(textWatcher)

        return view
    }

    private fun showFilterOptions() {
        val options = arrayOf("Дешевле 10000 RUB","Только Adidas","Только Nike","Дешевле 6000 RUB","Сбросить фильтры")

        AlertDialog.Builder(requireContext())
            .setTitle("Выберите фильтр")
            .setItems(options) { _, which ->
                when(which) {
                    0 -> {
                        val filteredItems = filter.filterByPrice(10000)
                        adapter.updateItems(filteredItems)
                    }
                    1 -> {
                        val filteredItems = filter.filterByCategory("Nike")
                        adapter.updateItems(filteredItems)
                    }
                    2 -> {
                        val filteredItems = filter.filterByCategory("Adidas")
                        adapter.updateItems(filteredItems)
                    }
                    3 -> {
                        val resetItems = filter.resetFilter()
                        adapter.updateItems(resetItems)
                    }
                }
            }
            .setNegativeButton("Отмена",null)
            .show()
    }

    private fun showDetails (item: Item){
        val intent = Intent(context,ItemDetails::class.java).apply {
            putExtra("item_title",item.title)
            putExtra("item_price",item.price)
            putExtra("item_description",item.description)
            putExtra("item_image",item.imageRestId)
        }
        startActivity(intent)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val query = s.toString()
            val filteredItems = search.filter(query)
            adapter.updateItems(filteredItems)
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun addToCart(item: Item) {
        Toast.makeText(context,"Товар '${item.title}' добавлен в корзину",Toast.LENGTH_SHORT).show()
        sharedViewModel.addItem(item)
    }

}