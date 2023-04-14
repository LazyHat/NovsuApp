package com.lazyhat.novsuapp.data.datastore.timedata

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object TimeDataSerializer : Serializer<TimeData> {
    override val defaultValue = TimeData()

    override suspend fun readFrom(input: InputStream): TimeData {
        try {
            return Json.decodeFromString(
                TimeData.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read TimeData", serialization)
        }
    }

    override suspend fun writeTo(t: TimeData, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(TimeData.serializer(), t).encodeToByteArray()
            )
        }
    }
}