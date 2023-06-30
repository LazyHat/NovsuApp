package com.lazyhat.novsuapp.data.source.time_table_local

import androidx.datastore.core.DataStore
import com.lazyhat.novsuapp.data.models.TimeTable
import com.lazyhat.novsuapp.data.models.TimeTableParameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class TimeTableLocalSourceImpl(
    private val timeTableDataStore: DataStore<List<TimeTable>>,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) :
    TimeTableLocalSource {
    override suspend fun isEmpty(): Boolean = withContext(coroutineDispatcher) {
        timeTableDataStore.data.first().isEmpty()
    }

    override suspend fun getTimeTable(parameters: TimeTableParameters): TimeTable? =
        withContext(coroutineDispatcher) {
            timeTableDataStore.data.first().find { it.parameters == parameters }
        }

    override fun getTimeTablesFlow(): Flow<List<TimeTable>> {
        return timeTableDataStore.data
    }

    override suspend fun insertNewTimeTable(new: TimeTable) = withContext(coroutineDispatcher) {
        if (getTimeTable(new.parameters) == null)
            timeTableDataStore.updateData { it + new }
    }
}