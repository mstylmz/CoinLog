package com.yilmaz.coinlog.service.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yilmaz.coinlog.model.models.data_base.FavoriteCoin
import com.yilmaz.coinlog.utilities.FAVORITE_COINS_DATABASE_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database( entities = arrayOf(FavoriteCoin::class), version = 1, exportSchema = false)
abstract class FavoriteCoinDatabase: RoomDatabase() {

    abstract fun coinDao(): FavoriteCoinsDao

    companion object {
        @Volatile private var INSTANCE: FavoriteCoinDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): FavoriteCoinDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteCoinDatabase::class.java,
                    FAVORITE_COINS_DATABASE_NAME
                )
                .fallbackToDestructiveMigration()
                .addCallback(WordDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class WordDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.coinDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(dao: FavoriteCoinsDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.

        }

    }
}