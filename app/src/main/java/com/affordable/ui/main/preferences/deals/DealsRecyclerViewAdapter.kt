package com.affordable.ui.main.preferences.deals

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.affordable.R
import com.affordable.data.models.MyDealModel
import com.affordable.databinding.ItemDealsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DealsRecyclerViewAdapter(
    private val context: Context,
    private val listener: OnPositionClick
) : RecyclerView.Adapter<DealsRecyclerViewAdapter.ViewHolder>() {

    var mainList = mutableListOf<MyDealModel>()

    val options: RequestOptions = RequestOptions()
        .fitCenter()
        .placeholder(R.drawable.ic_logo_rounded)
        .error(R.drawable.ic_outline_error_24)

    fun setData(list: ArrayList<MyDealModel>) {
        mainList = list
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemDealsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(mainList[position]) {

                binding.Category.text = "Category: " + this.shoppingPreferenceModel.name
                binding.deal.text = "Deal: " + this.dealDetails
                binding.basedOn.text = "Based On: " + this.type

                Glide.with(context).load(this.userCardPreferenceModel.imageUrl).apply(options)
                    .into(binding.cardImage)

                Glide.with(context).load(this.storesPreferenceModel.imageUrl).apply(options)
                    .into(binding.storeImage);
            }
        }
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    interface OnPositionClick {
        fun onItemClick(model: MyDealModel, position: Int)
    }
}