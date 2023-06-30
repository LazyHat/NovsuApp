package com.lazyhat.novsuapp.data.repository

import com.lazyhat.novsuapp.data.models.LessonDay
import com.lazyhat.novsuapp.data.source.time_table_local.TimeTableLocalSource
import kotlinx.coroutines.flow.Flow

class MainRepositoryImpl(private val timeTableLocalSource: TimeTableLocalSource) : MainRepository {
    override fun getTimeTableFlow(): Flow<List<LessonDay>> {
        TODO("Not yet implemented")
    }
}