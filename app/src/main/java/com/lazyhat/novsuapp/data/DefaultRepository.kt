package com.lazyhat.novsuapp.data

import androidx.datastore.core.DataStore
import androidx.work.*
import com.lazyhat.novsuapp.data.datastore.grouplist.GroupList
import com.lazyhat.novsuapp.data.datastore.groupspecs.GroupSpecs
import com.lazyhat.novsuapp.data.datastore.settings.Settings
import com.lazyhat.novsuapp.data.datastore.timedata.TimeData
import com.lazyhat.novsuapp.data.db.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext


class DefaultRepository(
    private val mainDao: MainDao,
    private val sDataStore: DataStore<Settings>,
    private val gsDataStore: DataStore<GroupSpecs>,
    private val glDataStore: DataStore<GroupList>,
    private val workManager: WorkManager,
    private val tdDataStore: DataStore<TimeData>,
    private val groupsListWorkerRequestBuilder: OneTimeWorkRequest.Builder,
    private val lessonsWorkerRequest: OneTimeWorkRequest.Builder,
    private val lessonsWorkerPeriodicRequest: PeriodicWorkRequest.Builder,
    private val weekWorkerPeriodicRequest: PeriodicWorkRequest.Builder,
    private val getTimeWorkerPeriodicRequest: PeriodicWorkRequest.Builder,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : MainRepository {

    override fun getDaysStream(): Flow<List<DayModel>> {
        return filterAndSortLessonsStreamBySubGroup(
            mainDao.observeAll(),
            gsDataStore.data
        ).map { list ->
            list.toDaysList().filter { it.lessons.isNotEmpty() }
        }
    }

    override suspend fun refreshWeek() {
        workManager.enqueueUniqueWork(
            LessonsWorkerID,
            ExistingWorkPolicy.REPLACE,
            lessonsWorkerRequest.build()
        )
    }


//GroupSpecs

    override fun getGroupSpecsStream(): Flow<GroupSpecs> = gsDataStore.data


    override suspend fun updateGroupSpecs(new: GroupSpecs) {
        withContext(coroutineDispatcher) {
            gsDataStore.updateData { new }
        }
    }

    override suspend fun getGroupSpecs(): GroupSpecs = gsDataStore.data.first()


    //Settings
    override fun getSettingsStream(): Flow<Settings> = sDataStore.data


    override suspend fun updateSettings(new: Settings) {
        withContext(coroutineDispatcher) {
            sDataStore.updateData { new }
        }
    }

    //Utils
    override fun updateGroups(institute: Institute, grade: String) {
        workManager.enqueueUniqueWork(
            GroupsListWorkerID,
            ExistingWorkPolicy.REPLACE,
            groupsListWorkerRequestBuilder.setInputData(
                Data.Builder().putString(GroupsListWorkerKeys.INST_LABEL_RUS, institute.labelRus)
                    .putString(GroupsListWorkerKeys.GRADE, grade).build()
            ).build()
        )
    }

    override fun getGroupsStream(): Flow<GroupList> = glDataStore.data

    override suspend fun initDataBase() {
        workManager.cancelAllWork()
        withContext(coroutineDispatcher) {
            if (mainDao.getAll().isEmpty())
                refreshWeek()
        }
        workManager.enqueueUniquePeriodicWork(
            "periodicLessons",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            lessonsWorkerPeriodicRequest.addTag("periodicLessons").build()
        )
        workManager.enqueueUniquePeriodicWork(
            "periodicWeek",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            weekWorkerPeriodicRequest.addTag("periodicWeek").build()
        )
        workManager.enqueueUniquePeriodicWork(
            "periodicTime",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            getTimeWorkerPeriodicRequest.addTag("periodicTime").build()
        )
    }

    override fun getTimeStream(): Flow<TimeData> = tdDataStore.data


//Private

    private fun filterAndSortLessonsStreamBySubGroup(
        lessonsList: Flow<List<LocalLesson>>,
        groupSpecsList: Flow<GroupSpecs>
    ): Flow<List<LocalLesson>> {
        return combine(
            lessonsList,
            groupSpecsList
        ) { lessons, groupSpecs ->
            withContext(coroutineDispatcher) {
                lessons.filter { groupSpecs.subGroup == "" || it.subGroup.isEmpty() || it.subGroup == groupSpecs.subGroup }
                    .map {
                        if (groupSpecs.subGroup != "")
                            it.copy(subGroup = "")
                        else
                            it
                    }.sortedBy { it.timeStart } //Sort
            }
        }
    }
}