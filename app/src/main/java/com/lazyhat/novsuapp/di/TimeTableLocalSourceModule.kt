package com.lazyhat.novsuapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.lazyhat.novsuapp.data.models.TimeTable
import com.lazyhat.novsuapp.data.source.time_table_local.TimeTableListSerializer
import com.lazyhat.novsuapp.data.source.time_table_local.TimeTableLocalSource
import com.lazyhat.novsuapp.data.source.time_table_local.TimeTableLocalSourceImpl
import com.lazyhat.novsuapp.ui.activities.main.screens.timetable.TimeTableVM
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TimeTableLocalSourceModule {
    @Singleton
    @Provides
    fun provideTimeTableLocalSource(
        timeTableListDataStore: DataStore<List<TimeTable>>
    ): TimeTableLocalSource = TimeTableLocalSourceImpl(
        timeTableListDataStore
    )

    @Singleton
    @Provides
    fun provideTimeTableListDataStore(@ApplicationContext context: Context): DataStore<List<TimeTable>> =
        DataStoreFactory.create(
            serializer = TimeTableListSerializer,
            produceFile = { context.dataStoreFile("timetables") },
            corruptionHandler = ReplaceFileCorruptionHandler { emptyList() },
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
}