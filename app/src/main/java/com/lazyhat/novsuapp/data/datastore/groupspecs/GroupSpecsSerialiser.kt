package com.lazyhat.novsuapp.data.datastore.groupspecs

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


object GroupSpecsSerialiser : Serializer<GroupSpecs> {
    override val defaultValue = GroupSpecs()

    override suspend fun readFrom(input: InputStream): GroupSpecs {
        try {
            return Json.decodeFromString(
                GroupSpecs.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read GroupSpecs", serialization)
        }
    }

    override suspend fun writeTo(t: GroupSpecs, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(GroupSpecs.serializer(), t).encodeToByteArray()
            )
        }
    }
}