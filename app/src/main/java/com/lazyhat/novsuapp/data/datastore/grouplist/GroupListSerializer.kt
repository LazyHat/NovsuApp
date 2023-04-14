package com.lazyhat.novsuapp.data.datastore.grouplist

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object GroupListSerializer : Serializer<GroupList> {
    override val defaultValue = GroupList()

    override suspend fun readFrom(input: InputStream): GroupList {
        try {
            return Json.decodeFromString(
                GroupList.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read GroupList", serialization)
        }
    }

    override suspend fun writeTo(t: GroupList, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(GroupList.serializer(), t).encodeToByteArray()
            )
        }
    }
}