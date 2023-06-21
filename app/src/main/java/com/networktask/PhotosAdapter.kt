package com.networktask

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.networktask.databinding.ListItemBinding
import com.networktask.cache.ImageDbEntity

class PhotosAdapter() :
    ListAdapter<ImageDbEntity, PhotosAdapter.WeatherViewHolder>(UserDiffUtil()) {
    class WeatherViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflator, parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = currentList[position]
//        holder.binding.card?.setCardBackgroundColor(holder.itemView.resources.getColor(R.color.purple_200,
//            null))
        holder.binding.rvImg.load(weather.data)
    }

}


class UserDiffUtil : DiffUtil.ItemCallback<ImageDbEntity>() {
    override fun areItemsTheSame(oldItem: ImageDbEntity, newItem: ImageDbEntity): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem: ImageDbEntity, newItem: ImageDbEntity): Boolean {
        return oldItem.id == newItem.id
    }
}

