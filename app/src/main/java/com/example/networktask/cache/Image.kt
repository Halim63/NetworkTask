package com.example.networktask.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Image(

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var data: ByteArray? = null,
    ){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
