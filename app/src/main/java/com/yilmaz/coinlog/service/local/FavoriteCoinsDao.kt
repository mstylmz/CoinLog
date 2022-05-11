package com.yilmaz.coinlog.service.local

import androidx.room.*
import com.yilmaz.coinlog.model.models.data_base.FavoriteCoin
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCoinsDao {

    //Insert -> INSERT INTO
    //Suspen fun -> coroutine, pause&resume
    //vararg -> multiple Coin objects
    //List<Long> primary keys

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(coin: FavoriteCoin)

    @Delete
    suspend fun delete(coin: FavoriteCoin)

    @Query("SELECT * FROM favorite_coins")
    fun getAllCoins(): Flow<List<FavoriteCoin>>

    @Insert
    suspend fun insertAll(vararg coins: FavoriteCoin): List<Long>

    @Query("SELECT * FROM favorite_coins WHERE uuid = :id")
    suspend fun getCoins(id:Int): FavoriteCoin

    @Query("DELETE FROM favorite_coins")
    suspend fun deleteAllCoins()


}