package com.lazyhat.novsuapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class TimeTableParameters(
    val grade: Int,
    val institute: String,
    val group: String
)
