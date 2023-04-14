package com.lazyhat.novsuapp.data.network

import com.lazyhat.novsuapp.data.DayModel
import com.lazyhat.novsuapp.data.Week
import com.lazyhat.novsuapp.data.datastore.groupspecs.GroupSpecs

interface NetworkDataSource {
    fun getDays(groupSpecs: GroupSpecs): List<DayModel>
    fun getGroupsList(grade: String, instituteLabel: String): List<String>
    fun getWeek(): Week?
}