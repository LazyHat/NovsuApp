package com.lazyhat.novsuapp.data

import com.lazyhat.novsuapp.R

enum class Institute(val id: String, val labelRes: Int, val labelRus: String) {
    IEIS(id = "815132", R.string.inst_IEIS, "ИЭИС"),
    ICEUS(id = "868341", R.string.inst_ICEUS, "ИЦЭУС"),
    INPO(id = "868342", R.string.inst_INPO, "ИНПО"),
    IBHI(id = "868343", R.string.inst_IBHI, "ИБХИ"),
    IGUM(id = "868344", R.string.inst_IGUM, "ИГУМ"),
    IMO(id = "868345", R.string.inst_IMO, "ИМО"),
    IUR(id = "1786977", R.string.inst_IUR, "ИЮР"),
    IPT(id = "1798800", R.string.inst_IPT, "ИПТ")
}

enum class Week(val labelRes: Int) {
    Upper(R.string.tt_week_upper),
    Lower(R.string.tt_week_lower)
}

enum class Pages(val labelRes: Int) {
    TimeTable(R.string.timetable_label),
    EditGroup(R.string.editgroup_label),
    Settings(R.string.settings_label)
}