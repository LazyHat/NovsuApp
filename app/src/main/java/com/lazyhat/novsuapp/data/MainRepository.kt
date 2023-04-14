package com.lazyhat.novsuapp.data

import com.lazyhat.novsuapp.data.datastore.grouplist.GroupList
import com.lazyhat.novsuapp.data.datastore.groupspecs.GroupSpecs
import com.lazyhat.novsuapp.data.datastore.settings.Settings
import com.lazyhat.novsuapp.data.datastore.timedata.TimeData
import kotlinx.coroutines.flow.Flow


interface MainRepository {
    //Main
    fun getDaysStream(): Flow<List<DayModel>>
    suspend fun refreshWeek()

    //Settings
    fun getSettingsStream(): Flow<Settings>
    suspend fun updateSettings(new: Settings)

    //GroupSpecs
    fun getGroupSpecsStream(): Flow<GroupSpecs>
    suspend fun updateGroupSpecs(new: GroupSpecs)
    suspend fun getGroupSpecs(): GroupSpecs

    //Utils
    fun updateGroups(institute: Institute, grade: String)
    fun getGroupsStream(): Flow<GroupList>
    fun getTimeStream(): Flow<TimeData>
    suspend fun initDataBase()
}