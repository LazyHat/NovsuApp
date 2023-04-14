package com.lazyhat.novsuapp.data.network

import com.lazyhat.novsuapp.data.*
import com.lazyhat.novsuapp.data.datastore.groupspecs.GroupSpecs
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

object DefaultNetworkDataSource : NetworkDataSource {
    override fun getDays(groupSpecs: GroupSpecs): List<DayModel> =
        getDaysFromData(getMainDoc(groupSpecs))

    override fun getWeek(): Week? {
        val ttUrl = "https://portal.novsu.ru/univer/timetable/"
        return getWeekFromData(
            Jsoup.connect(ttUrl).get().getElementById("npe_instance_1103492_npe_content")!!
                .getElementsByClass("block_3padding")[0]
                .getElementsByTag("b")[0]
        )
    }

    override fun getGroupsList(grade: String, instituteLabel: String): List<String> {
        val url = "https://novsu.ru/university/timetable/ochn/?grade=$grade"
        val doc = Jsoup.connect(url).get()
        val listInst = doc.getElementsByClass("well grouplist")
        val listGroups = listInst.filter { element ->
            element.getElementsByClass("institute")[0].text().trim() == instituteLabel
        }
        val groups = listGroups[0].getElementsByTag("a").map { element ->
            element.text().trim()
        }
        return groups
    }
}

private fun getMainDoc(groupSpecs: GroupSpecs): Document {
    val groupname =
        groupSpecs.group.filter { it.isDigit() || it in 'A'..'Z' }
    val grouptype = groupSpecs.group.filter { !it.isDigit() && it !in 'A'..'Z' }
    val groupTTUrl = "https://www.novsu.ru/university/timetable/" +
            "ochn" +
            "/i.1103357/?page=EditViewGroup&instId=${groupSpecs.institute.id}&" +
            "name=${groupname}" +
            "&type=${grouptype.ifEmpty { "ДО" }}" +
            "&year=20${22 - (groupSpecs.grade.toInt() - 1)}"
    return Jsoup.connect(groupTTUrl).get()
}

private fun parseTimeFromData(element: Element): Time? {
    val time = element.childNodes().last().toString().trim()
    if (time.isEmpty()) return null
    val list: MutableList<String> = time.split(", ").toMutableList()
    list.forEachIndexed { it, item ->
        if (item.length < 5) {
            list[it] = "0$item"
        }
    }
    return Time(
        list.first(),
        list.last().substring(0, 3) + "45",
        list.size
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

private fun getWeekFromData(element: Element): Week? {
    val text = element.text().lowercase()
    with(text) {
        return when {
            contains("верх") -> Week.Upper
            contains("нижн") -> Week.Lower
            else -> null
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

private fun getDaysFromData(mainDoc: Document): List<DayModel> {
    return mainDoc.getElementsByClass("page-content")[0]
        .getElementsByClass("well day-block").map { dayElement ->
            DayModel(
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
                        lessonElement.getElementsByClass("teacher").first()?.text(),
                        getWeekFromData(lessonElement.getElementsByClass("comment").first()!!)
                            ?: Week.All,
                        getCommentFromData(
                            lessonElement.getElementsByClass("comment").first()!!
                        )
                    )
                })
        }
}