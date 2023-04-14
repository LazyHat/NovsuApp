package com.lazyhat.novsuapp.data.db

import android.content.Context
import androidx.room.*

@Database(
    entities = [LocalLesson::class],
    version = 1,
    exportSchema = true
)
abstract class MainDataBase : RoomDatabase() {

    abstract fun mainDao(): MainDao

    companion object {
        @Volatile
        private var instance: MainDataBase? = null

        fun getInstance(context: Context): MainDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, MainDataBase::class.java, "Lessons.db").build()
    }
}