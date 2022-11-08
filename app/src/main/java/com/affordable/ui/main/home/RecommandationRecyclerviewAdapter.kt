package com.affordable.ui.main.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.affordable.data.models.ChoiceShoppingModel
import com.affordable.data.models.RecommendationModel
import com.affordable.databinding.ItemChoiceShoppingBinding
import com.affordable.databinding.ItemRecommendationBinding


class RecommandationRecyclerviewAdapter(
    private val context: Context,
    private val listener: OnPositionClick
) : RecyclerView.Adapter<RecommandationRecyclerviewAdapter.ViewHolder>() {

    private var mainList = mutableListOf<RecommendationModel>()

    fun setData(list: ArrayList<RecommendationModel>) {
        mainList = list
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(mainList[position]) {

                binding.image.visibility = View.VISIBLE

                binding.cardview.setOnClickListener {
                    listener.onItemClick(this@with)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    interface OnPositionClick {
        fun onItemClick(position: RecommendationModel)
    }
}