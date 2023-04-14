package com.lazyhat.novsuapp.data.datastore.groupspecs

import com.lazyhat.novsuapp.data.Institute
import kotlinx.serialization.Serializable

@Serializable
data class GroupSpecs(
    val institute: Institute = Institute.IEIS,
    val grade: String = "1",
    val group: String = "2092",
    val subGroup: String = "2"
)