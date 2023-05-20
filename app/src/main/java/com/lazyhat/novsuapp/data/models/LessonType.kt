package com.lazyhat.novsuapp.data.models

import android.content.Context
import com.lazyhat.novsuapp.R
import com.lazyhat.novsuapp.data.models.LessonType.Companion.LABORATORY
import com.lazyhat.novsuapp.data.models.LessonType.Companion.LECTURE
import com.lazyhat.novsuapp.data.models.LessonType.Companion.PRACTICE

class LessonType(val types: List<Int>) {
    constructor(vararg types: Int) : this(types.toList())

    companion object {
        const val LECTURE = 0
        const val PRACTICE = 1
        const val LABORATORY = 2

        //Order in list is important
        val listResources = listOf(R.string.tt_type_lec, R.string.tt_type_pr, R.string.tt_type_lab)
    }

    fun toString(context: Context): String =
        types.joinToString(separator = "/", prefix = "(", postfix = ")", limit = 3) {
            context.getString(
                listResources[it]
            )
        }

    @Deprecated(
        message = "use toString(context)",
        replaceWith = ReplaceWith("toString(context)"),
        level = DeprecationLevel.ERROR
    )
    override fun toString(): String = throw NotImplementedError()
}

fun c() {
    val LessonType = LessonType(listOf(LECTURE, PRACTICE, LABORATORY, 3))
}