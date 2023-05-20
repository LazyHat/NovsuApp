package com.lazyhat.novsuapp.data.models

data class LessonTime(val start: Int, val end: Int) {
    val hours: Int = end - start
}
