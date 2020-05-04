package com.pmovil.webservices.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pmovil.webservices.dao.ProductDAODatabase
import com.pmovil.webservices.model.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "pmovil.db"

        private lateinit var instance: AppDatabase

        fun getInstance(context: Context?): AppDatabase {
            if (::instance.isInitialized) {
                return instance
            } else {
                instance = Room.databaseBuilder(
                    context!!.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()

                return instance
            }

        }
    }

    abstract fun productDAO(): ProductDAODatabase
}