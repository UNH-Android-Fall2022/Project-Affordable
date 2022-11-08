package com.affordable.ui.main.preferences.choiceSelection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.affordable.data.models.ChoiceShoppingModel
import com.affordable.databinding.ItemChoiceShoppingBinding


class ChoiceShoppingRecyclerviewAdapter(
    private val context: Context,
    private val listener: OnPositionClick
) : RecyclerView.Adapter<ChoiceShoppingRecyclerviewAdapter.ViewHolder>() {

    private var mainList = mutableListOf<ChoiceShoppingModel>()

    fun setData(list: ArrayList<ChoiceShoppingModel>) {
        mainList = list
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemChoiceShoppingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemChoiceShoppingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        fun onItemClick(position: ChoiceShoppingModel)
    }
}