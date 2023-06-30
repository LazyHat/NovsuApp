package com.lazyhat.novsuapp.data.repository

import com.lazyhat.novsuapp.data.models.LessonDay
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getTimeTableFlow(): Flow<List<LessonDay>>
}