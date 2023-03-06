package com.example.novsucompose.data

import androidx.compose.runtime.MutableState
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.time.LocalTime

fun getGroupsList(grade: String, institute: Institute): List<String> {
    val url = "https://novsu.ru/university/timetable/ochn/?grade=$grade"
    val doc = Jsoup.connect(url).get()
    val listinst = doc.getElementsByClass("well grouplist")
    val listgroups = listinst.filter { element ->
        element.getElementsByClass("institute")[0].text().trim() == institute.label
    }
    val groups = listgroups[0].getElementsByTag("a").map { element ->
        element.text().trim()
    }
    return groups
}

fun getData(group: Group, model: MutableState<MainModel>): ErrorCodes {
    val groupname =
        group.group.filter { it.isDigit() || it in 'A'..'Z' }
    val grouptype = group.group.filter { !it.isDigit() && it !in 'A'..'Z' }
    val tTUrl = "https://portal.novsu.ru/univer/timetable/"
    val groupTTUrl = "https://www.novsu.ru/university/timetable/" +
            "ochn" +
            "/i.1103357/?page=EditViewGroup&instId=${group.institute.id}&" +
            "name=${groupname}" +
            "&type=${grouptype.ifEmpty { "ДО" }}" +
            "&year=2022"
    return tryCatchIO {
        val result =
            Response(
                group.group,
                group.subGroup,
                Jsoup.connect(tTUrl).get(),
                Jsoup.connect(groupTTUrl).get()
            )
        model.value = getModelFromData(result)
    }
}

private fun parseTimeFromData(element: Element): Time {
    val time = element.childNodes().last().toString().trim()
    val list: MutableList<String> = time.split(", ").toMutableList()
    list.forEachIndexed() { it, item ->
        if (item.length < 5) {
            list[it] = "0$item"
        }
    }
    return Time(
        LocalTime.parse(list.first()),
        LocalTime.parse(list.last().substring(0, 3) + "45"),
        when (list.size) {
            1 -> "1 Час"
            in 2..4 -> "${list.size} Часа"
            else -> "${list.size} Часов"
        }
    )
}

private fun getSubGroupFromData(element: Element): String {
    val text = element.text()
    val start = text.indexOfFirst { it == '(' }
    val end = text.indexOfFirst { it == ')' }
    return when {
        start > end -> text[text.indexOfFirst { it.isDigit() }].toString()
        else -> ""
    }
}

private fun getLessonTypeFromData(element: Element): String {
    val text = element.text()
    val index = text.indexOfFirst { it == '(' }
    return when {
        index != -1 -> text.substring(text.indexOfFirst { it == '(' })
        else -> ""
    }
}

private fun getWeekFromData(element: Element): Week {
    val text = element.text().lowercase()
    with(text) {
        return when {
            contains("верх") -> Week.Upper
            contains("нижн") -> Week.Lower
            else -> Week.All
        }
    }
}

private fun getCommentFromData(element: Element): String {
    val text = element.text()
    with(text) {
        return when {
            contains("по") -> {
                // val textweek = substring(find)
                text
            }
            else -> text
        }
    }
}

private fun getDaysFromData(response: Response): List<DayModel> {
    return response.groupDoc.getElementsByClass("page-content")[0]
        .getElementsByClass("well day-block").map { dayElement ->
            DayModel(
                dayElement.getElementsByClass("dow")[0].text().trim(),
                dayElement.getElementsByClass("lesson").map { lessonElement ->
                    LessonModel(
                        parseTimeFromData(lessonElement.getElementsByClass("time").first()!!),
                        lessonElement.getElementsByClass("title").first()!!.childNodes().last()
                            .toString()
                            .trim(),
                        getLessonTypeFromData(
                            lessonElement.getElementsByClass("title").first()!!
                                .getElementsByClass("type")
                                .first()!!
                        ),
                        getSubGroupFromData(lessonElement.getElementsByClass("type").first()!!),
                        when (val s =
                            lessonElement.getElementsByClass("details").first()!!
                                .getElementsByTag("a")
                                .last()!!.text()) {
                            "." -> ""
                            else -> s
                        },
                        lessonElement.getElementsByClass("teacher").first()!!.text(),
                        getWeekFromData(lessonElement.getElementsByClass("comment").first()!!),
                        getCommentFromData(
                            lessonElement.getElementsByClass("comment").first()!!
                        )
                    )
                }
                    .filter { response.subGroup == "" || it.subGroup == "" || it.subGroup == response.subGroup })
        }
}

private fun getModelFromData(response: Response): MainModel {
    return MainModel(
        response.group,
        response.subGroup,
        getWeekFromData(
            response.mainDoc.getElementById("npe_instance_1103492_npe_content")!!
                .getElementsByClass("block_3padding")[0]
                .getElementsByTag("b")[0]
        ),
        getDaysFromData(response)
    )
}
