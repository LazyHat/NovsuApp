package com.lazyhat.novsuapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LessonDay(
    val lessons: List<Lesson>
)
