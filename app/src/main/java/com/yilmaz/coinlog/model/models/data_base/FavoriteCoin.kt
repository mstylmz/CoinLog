package com.yilmaz.coinlog.model.models.data_base

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_coins")
data class FavoriteCoin(
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0,
    @ColumnInfo(name = "name")
    val name:String,
    @ColumnInfo(name = "symbol")
    val symbol:String
) {

    override fun toString(): String {
        return "FavoriteCoin(uuid=$uuid, name='$name', symbol='$symbol')"
    }
}