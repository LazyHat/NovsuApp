package com.lazyhat.novsuapp.data.models

import android.content.Context
import com.lazyhat.novsuapp.R
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = LessonTimeSerializer::class)
data class LessonTime(val start: Int, val end: Int) {
    private val hours: Int = end - start

    fun toString(context: Context) =
        "$start:00 - $end:45, $hours ${
            context.resources.getQuantityString(
                R.plurals.tt_time,
                hours
            )
        }"

    @Deprecated(
        message = "use toString(context)",
        replaceWith = ReplaceWith("toString(context)"),
        level = DeprecationLevel.ERROR
    )
    override fun toString(): String = throw NotImplementedError()
}

@Serializable
@SerialName("time")
private class LessonTimeSurrogate(val start: Int, val end: Int)

private object LessonTimeSerializer : KSerializer<LessonTime> {
    override val descriptor: SerialDescriptor
        get() = LessonTimeSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): LessonTime {
        val surrogate = decoder.decodeSerializableValue(LessonTimeSurrogate.serializer())
        return LessonTime(surrogate.start, surrogate.end)
    }

    override fun serialize(encoder: Encoder, value: LessonTime) {
        val surrogate = LessonTimeSurrogate(value.start, value.end)
        return encoder.encodeSerializableValue(LessonTimeSurrogate.serializer(), surrogate)
    }
}