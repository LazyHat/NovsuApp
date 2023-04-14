package com.lazyhat.novsuapp.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MainDao {
    @Query("SELECT * FROM $LESSONS_TABLE")
    fun observeAll(): Flow<List<LocalLesson>>

    @Upsert
    fun upsertAll(lessons: List<LocalLesson>)

    @Query("DELETE FROM $LESSONS_TABLE")
    fun deleteAll()

    @Query("SELECT * FROM $LESSONS_TABLE")
    suspend fun getAll(): List<LocalLesson>
}