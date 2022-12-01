package com.affordable.ui.main.shopping

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.affordable.data.network.ShoppingPreferenceModel
import com.affordable.databinding.ItemUserShoppingPreferenceBinding

class ShoppingPreferencesRecyclerviewAdapter(
    private val context: Context,
    private val listener: OnPositionClick
) : RecyclerView.Adapter<ShoppingPreferencesRecyclerviewAdapter.ViewHolder>() {

    private var mainList = mutableListOf<ShoppingPreferenceModel>()

    fun setData(list: ArrayList<ShoppingPreferenceModel>) {
        mainList = list
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemUserShoppingPreferenceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemUserShoppingPreferenceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(mainList[position]) {

                binding.text.text = this.name

                binding.delete.setOnClickListener {
                    listener.onItemClick(this@with, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    interface OnPositionClick {
        fun onItemClick(model: ShoppingPreferenceModel, position: Int)
    }
}