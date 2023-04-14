package com.lazyhat.novsuapp.data.datastore.settings

import com.lazyhat.novsuapp.data.DataSource
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val theme: DataSource.ColorSchemes = DataSource.ColorSchemes.Default
)
