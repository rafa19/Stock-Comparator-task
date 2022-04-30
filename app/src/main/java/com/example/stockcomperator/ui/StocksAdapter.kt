package com.example.stockcomperator.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.stockcomperator.databinding.StockItemBinding
import com.example.stockcomperator.utils.ItemClickListener

class StocksAdapter(
    private var stockList: List<String>,
    private val onClick: ItemClickListener
) : RecyclerView.Adapter<StocksAdapter.ViewHolder>() {

    private var checkedList = mutableListOf<Boolean>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            StockItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return stockList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.checkbox.setOnClickListener {
            checkedList[position] = (it as CheckBox).isChecked
            onClick.onClickListener(position)
        }
        holder.binding.checkbox.isChecked = checkedList[position]
        holder.bind(stockList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: List<String>) {
        stockList = newList
        checkedList = MutableList(stockList.size) { true }
        notifyDataSetChanged()
    }

    fun getCheckedList(): List<String> {
        val result = mutableListOf<String>()
        checkedList.forEachIndexed { index, item ->
            if (item) result.add(stockList[index])
        }
        return result
    }

    class ViewHolder(var binding: StockItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.checkbox.text = item
        }
    }

}