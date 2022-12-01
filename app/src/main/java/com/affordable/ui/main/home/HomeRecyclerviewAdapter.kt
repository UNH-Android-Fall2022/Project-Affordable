package com.affordable.ui.main.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.affordable.R
import com.affordable.data.network.google.NearPlaceModel
import com.affordable.data.network.google.Result
import com.affordable.databinding.ItemRecommendationBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class HomeRecyclerviewAdapter(
    private val context: Context,
    private val listener: OnPositionClick
) : RecyclerView.Adapter<HomeRecyclerviewAdapter.ViewHolder>() {

    private var mainList = mutableListOf<Result>()

    val options: RequestOptions = RequestOptions()
        .fitCenter()
        .placeholder(R.drawable.ic_logo_rounded)
        .error(R.drawable.ic_outline_error_24)

    fun setData(list: ArrayList<Result>) {
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

                binding.placename.text = this.name

                binding.directions.setOnClickListener {
                    try {
                        if (this.geometry.location.lat != 0.0 && this.geometry.location.lng != 0.0) {
                            val uri: Uri =
                                Uri.parse("google.navigation:q=" + this.geometry.location.lat + "," + this.geometry.location.lng + "&mode=w")
                            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            context.startActivity(mapIntent)
                        }
                    } catch (e: Exception) {
                    }
                }

                try {
                    if (this.opening_hours.open_now) {
                        binding.openedOrNot.text = "Opened Now"
                    } else {
                        binding.openedOrNot.text = "Closed Now"
                    }
                }catch (e:Exception){}

                binding.rating.text = "Rating: " + this.rating.toString()

                try {
                    Glide.with(context).load(
                        getImageUrl(
                            this.photos.get(0).photo_reference, context.resources.getString(
                                R.string.googleMapApiKey
                            )
                        )
                    ).apply(options).into(binding.image);
                } catch (e: Exception) {
                }

                binding.cardview.setOnClickListener {
                    // listener.onItemClick(this@with)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    interface OnPositionClick {
        fun onItemClick(model: NearPlaceModel)
    }

    private fun getImageUrl(reference: String, apiKey: String) =
        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference=$reference&key=$apiKey"
}