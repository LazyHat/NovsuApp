package com.lazyhat.novsuapp.data.datastore.grouplist

import kotlinx.serialization.Serializable

@Serializable
data class GroupList(
    val groups: List<String> = listOf(),
    val loading: Boolean = false
)
