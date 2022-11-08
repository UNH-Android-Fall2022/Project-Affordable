package com.affordable.ui.main.preferences.shoppingSelection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.affordable.data.models.ShoppingCategoryModel
import com.affordable.databinding.ItemShopppingBinding


class ShoppingCategoryRecyclerviewAdapter(
    private val context: Context,
    private val listener: OnPositionClick
) : RecyclerView.Adapter<ShoppingCategoryRecyclerviewAdapter.ViewHolder>() {

    private var mainList = mutableListOf<ShoppingCategoryModel>()

    fun setData(list: ArrayList<ShoppingCategoryModel>) {
        mainList = list
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemShopppingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemShopppingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(mainList[position]) {

                binding.text.text = this.categoryName

                binding.cardview.setOnClickListener {
                    if (binding.radioBtn.isChecked) {
                        binding.radioBtn.isChecked = false
                        this.isSelected = false
                    } else {
                        binding.radioBtn.isChecked = true
                        this.isSelected = true
                    }
                    listener.onItemClick(this@with)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    interface OnPositionClick {
        fun onItemClick(position: ShoppingCategoryModel)
    }
}