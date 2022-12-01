package com.affordable.ui.main.cards

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.affordable.R
import com.affordable.data.models.CardsModel
import com.affordable.data.network.StoresPreferenceModel
import com.affordable.data.network.UserCardPreferenceModel
import com.affordable.databinding.ItemCardsBinding
import com.affordable.databinding.ItemUserCardPreferenceBinding
import com.affordable.ui.main.preferences.cardSelection.CardsRecyclerviewAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class CardPreferenceRecyclerviewAdapter (
    private val context: Context,
    private val listener: OnPositionClick
) : RecyclerView.Adapter<CardPreferenceRecyclerviewAdapter.ViewHolder>() {

    private var mainList = mutableListOf<UserCardPreferenceModel>()

    val options: RequestOptions = RequestOptions()
        .fitCenter()
        .placeholder(R.drawable.ic_logo_rounded)
        .error(R.drawable.ic_outline_error_24)

    fun setData(list: ArrayList<UserCardPreferenceModel>) {
        mainList = list
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemUserCardPreferenceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemUserCardPreferenceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(mainList[position]) {

                binding.image.visibility = View.VISIBLE

                Glide.with(context).load(this.imageUrl).apply(options).into(binding.image);

                binding.delete.setOnClickListener {
                    listener.onItemClick(this@with,position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    interface OnPositionClick {
        fun onItemClick(model: UserCardPreferenceModel, position: Int)
    }
}