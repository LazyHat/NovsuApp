package com.lazyhat.novsuapp.data.datastore.timedata

import com.lazyhat.novsuapp.data.Week
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class TimeData(
    val dayOfWeek: Int = LocalDate.now().dayOfWeek.ordinal,
    val week: Week? = null
)
