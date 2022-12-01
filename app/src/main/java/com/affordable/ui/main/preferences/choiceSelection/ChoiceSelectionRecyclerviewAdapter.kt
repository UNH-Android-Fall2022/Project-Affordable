package com.affordable.ui.main.preferences.choiceSelection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.affordable.R
import com.affordable.data.models.ChoiceShoppingModel
import com.affordable.databinding.ItemChoiceShoppingBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class ChoiceSelectionRecyclerviewAdapter(
    private val context: Context,
    private val listener: OnPositionClick
) : RecyclerView.Adapter<ChoiceSelectionRecyclerviewAdapter.ViewHolder>() {

    var mainList = mutableListOf<ChoiceShoppingModel>()

    val options: RequestOptions = RequestOptions()
        .fitCenter()
        .placeholder(R.drawable.ic_logo_rounded)
        .error(R.drawable.ic_outline_error_24)

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

                Glide.with(context).load(this.cardUrl).apply(options).into(binding.image);

                binding.radioBtn.isChecked = this.isSelected

                binding.radioBtn.setOnClickListener {
                    this.isSelected = !this.isSelected
                    listener.onItemClick(this@with,position)
                }
                binding.cardview.setOnClickListener {
                    this.isSelected = !this.isSelected
                    listener.onItemClick(this@with,position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    interface OnPositionClick {
        fun onItemClick(model: ChoiceShoppingModel,position:Int)
    }
}