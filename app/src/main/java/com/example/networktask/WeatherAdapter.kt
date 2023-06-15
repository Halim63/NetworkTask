package com.example.networktask

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.loadAny
import com.example.networktask.cache.Image
import com.example.networktask.databinding.ListItemBinding

class WeatherAdapter() :ListAdapter<Image,WeatherAdapter.WeatherViewHolder>(UserDiffUtil()){
    class WeatherViewHolder (val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root){

    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflator=LayoutInflater.from(parent.context)
        val binding=ListItemBinding.inflate(inflator,parent,false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather =currentList[position]
//        holder.binding.card?.setCardBackgroundColor(holder.itemView.resources.getColor(R.color.purple_200,
//            null))
        holder.binding.rvImg.load(weather.data)
    }

    fun getImageAT(position: Int):Image {
        return currentList[position]
    }
}


class UserDiffUtil:DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.id == newItem.id
    }



    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.id == newItem.id
    }
}

