package com.lazyhat.novsuapp.data.source.time_table_local

import androidx.datastore.core.Serializer
import com.lazyhat.novsuapp.data.models.TimeTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object TimeTableListSerializer : Serializer<List<TimeTable>> {
    override val defaultValue: List<TimeTable>
        get() = emptyList()

    override suspend fun readFrom(input: InputStream): List<TimeTable> {
        return Json.decodeFromString(
            ListSerializer(TimeTable.serializer()),
            input.readBytes().decodeToString()
        )
    }

    override suspend fun writeTo(t: List<TimeTable>, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(ListSerializer(TimeTable.serializer()), t).encodeToByteArray()
            )
        }
    }
}
