package com.example.showcase.features.MetaData.data.database

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.room.Room

@UnstableApi
object DatabaseProvider {

    @Volatile
    private var INSTANCE: ShowcaseDatabase? = null

    fun getDatabase(
        context: Context
    ): ShowcaseDatabase {

        return INSTANCE ?: synchronized(this) {

            val instance =
                Room.databaseBuilder(
                    context.applicationContext,
                    ShowcaseDatabase::class.java,
                    "showcase.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

            INSTANCE = instance
            instance
        }
    }
}