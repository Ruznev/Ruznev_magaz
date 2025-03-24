package com.example.ruznev_magaz.presentation.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ruznev_magaz.R
import com.example.ruznev_magaz.ViewVodel.SharedViewModel
import com.example.ruznev_magaz.domian.Adapters.CartAdapter

class CartFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter
    private lateinit var totalCostTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart,container,false)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        recyclerView = view.findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        totalCostTextView = view.findViewById(R.id.total_item_cost)

        adapter = CartAdapter(
            emptyList(),
            onHeaderImageClick = {item -> sharedViewModel.showDetails(requireContext(),item)},
            onDeleteClick = {item -> sharedViewModel.removeItem(item)},
            onIncreaseQuantity = {item -> sharedViewModel.increaseQuanity(item)},
            onDecreaseQuantity = {item -> sharedViewModel.decreaseQuantity(item)}
        )
        recyclerView.adapter = adapter

        sharedViewModel.selectedItems.observe(viewLifecycleOwner) {
            items -> adapter.updateItems(items)
        }

        sharedViewModel.totalCost.observe(viewLifecycleOwner) { total ->
            totalCostTextView.text = "Общая сумма: $total"
        }

        return view
    }
}