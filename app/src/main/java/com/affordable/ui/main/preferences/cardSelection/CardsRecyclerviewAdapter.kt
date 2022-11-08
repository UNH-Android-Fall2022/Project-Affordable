package com.affordable.ui.main.preferences.cardSelection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.affordable.data.models.CardsModel
import com.affordable.databinding.ItemCardsBinding


class CardsRecyclerviewAdapter(
    private val context: Context,
    private val listener: OnPositionClick
) : RecyclerView.Adapter<CardsRecyclerviewAdapter.ViewHolder>() {

    private var mainList = mutableListOf<CardsModel>()

    fun setData(list: ArrayList<CardsModel>) {
        mainList = list
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemCardsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCardsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(mainList[position]) {

                binding.image.visibility = View.VISIBLE

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
        fun onItemClick(position: CardsModel)
    }
}