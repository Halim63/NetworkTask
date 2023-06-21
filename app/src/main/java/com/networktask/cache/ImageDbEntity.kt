package com.networktask.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ImageDbEntity(

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var data: ByteArray? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
