package com.lazyhat.novsuapp.data.source.time_table_local

import com.lazyhat.novsuapp.data.models.TimeTable
import com.lazyhat.novsuapp.data.models.TimeTableParameters
import kotlinx.coroutines.flow.Flow

interface TimeTableLocalSource {
    suspend fun isEmpty(): Boolean
    suspend fun getTimeTable(parameters: TimeTableParameters): TimeTable?
    suspend fun insertNewTimeTable(new: TimeTable)
    fun getTimeTablesFlow(): Flow<List<TimeTable>>
}