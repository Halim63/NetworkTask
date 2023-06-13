package com.example.networktask

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.networktask.cache.Image
import com.example.networktask.databinding.ListItemBinding

class WeatherAdapter() :ListAdapter<Image,WeatherAdapter.WeatherViewHolder>(UserDiffUtil()),
    Parcelable {
    class WeatherViewHolder (val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root){

    }


    constructor(parcel: Parcel) : this() {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflator=LayoutInflater.from(parent.context)
        val binding=ListItemBinding.inflate(inflator,parent,false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather =currentList[position]

//        holder.binding.rvImg.loadAny()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherAdapter> {
        override fun createFromParcel(parcel: Parcel): WeatherAdapter {
            return WeatherAdapter(parcel)
        }

        override fun newArray(size: Int): Array<WeatherAdapter?> {
            return arrayOfNulls(size)
        }
    }


}


class UserDiffUtil:DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.id == newItem.id
    }



    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }
}

