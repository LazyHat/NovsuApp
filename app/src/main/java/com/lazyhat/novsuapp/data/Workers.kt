package com.lazyhat.novsuapp.data

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.core.DataStore
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.lazyhat.novsuapp.MainActivity
import com.lazyhat.novsuapp.R
import com.lazyhat.novsuapp.data.datastore.grouplist.GroupList
import com.lazyhat.novsuapp.data.datastore.groupspecs.GroupSpecs
import com.lazyhat.novsuapp.data.datastore.timedata.TimeData
import com.lazyhat.novsuapp.data.db.LocalLesson
import com.lazyhat.novsuapp.data.db.MainDao
import com.lazyhat.novsuapp.data.network.DefaultNetworkDataSource
import com.lazyhat.novsuapp.updateLessonsNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import kotlin.random.Random

const val GroupsListWorkerID = "groupListW"
const val LessonsWorkerID = "lessonsW"

object GroupsListWorkerKeys {
    const val INST_LABEL_RUS = "inst_rus"
    const val GRADE = "grade"
}

@HiltWorker
class GroupsListWorker @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted parameters: WorkerParameters,
    private val glDataStore: DataStore<GroupList>
) :
    CoroutineWorker(ctx, parameters) {
    override suspend fun doWork(): Result {
        glDataStore.updateData { it.copy(loading = true) }
        val groupsList = DefaultNetworkDataSource.getGroupsList(
            inputData.getString(GroupsListWorkerKeys.GRADE)!!,
            inputData.getString(GroupsListWorkerKeys.INST_LABEL_RUS)!!
        )
        glDataStore.updateData { it.copy(loading = false, groups = groupsList) }
        return Result.success()
    }
}

@HiltWorker
class LessonsWorker @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted parameters: WorkerParameters,
    private val mainDao: MainDao,
    private val gsDataStore: DataStore<GroupSpecs>
) :
    CoroutineWorker(ctx, parameters) {

    override suspend fun doWork(): Result {
        mainDao.deleteAll()
        mainDao.upsertAll(LessonsWorkerStatus.Loading.LOCAL)
        val days = DefaultNetworkDataSource.getDays(gsDataStore.data.first()).toLocal()
        mainDao.deleteAll()
        mainDao.upsertAll(days)
        return Result.success()
    }
}

@HiltWorker
class LessonsWorkerPeriodic @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted parameters: WorkerParameters,
    private val mainDao: MainDao,
    private val gsDataStore: DataStore<GroupSpecs>
) :
    CoroutineWorker(ctx, parameters) {

    private val intent = Intent(applicationContext, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    private val pendingIntent: PendingIntent =
        PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    private val builder = NotificationCompat.Builder(applicationContext, updateLessonsNotification)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentText(applicationContext.getString(R.string.notification_update_lessons_in_progress))
        .setContentIntent(pendingIntent).setAutoCancel(true)

    override suspend fun doWork(): Result {
        startForeground()
        val groupSpecs = gsDataStore.data.first()
        val newDays = DefaultNetworkDataSource.getDays(groupSpecs).toLocal()
        val prevDays = mainDao.getAll()
        compareDays(newDays, prevDays, groupSpecs.subGroup)
        mainDao.deleteAll()
        mainDao.upsertAll(newDays)
        return Result.success()
    }

    private fun compareDays(
        newDays: List<LocalLesson>,
        prevDays: List<LocalLesson>,
        subGroup: String
    ) {
        val newFiltered =
            newDays.filter { subGroup == "" || it.subGroup == "" || it.subGroup == subGroup }
                .map { it.copy(id = -1) }
        val prevFiltered =
            prevDays.filter { subGroup == "" || it.subGroup == "" || it.subGroup == subGroup }
                .map { it.copy(id = -1) }
        newFiltered.forEach { new ->
            val f = prevFiltered.find { it == new }
            if (f == null) {
                notifyTimeTableChanged()
                return
            } else if (new != f) {
                notifyTimeTableChanged()
                return
            }
        }
    }

    private fun notifyTimeTableChanged() {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(applicationContext).notify(
                Random.nextInt(),
                builder.setContentText(applicationContext.getString(R.string.notification_timetable_changed))
                    .build()
            )
        }
    }

    private suspend fun startForeground() {
        setForeground(ForegroundInfo(Random.nextInt(), builder.build()))
    }
}

@HiltWorker
class WeekWorkerPeriodic @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted parameters: WorkerParameters,
    private val tdDataStore: DataStore<TimeData>
) :
    CoroutineWorker(ctx, parameters) {
    override suspend fun doWork(): Result {
        var week = DefaultNetworkDataSource.getWeek()
        if (week == null) week = Week.All
        tdDataStore.updateData {
            it.copy(
                week = week
            )
        }
        return Result.success()
    }
}

@HiltWorker
class GetTimeWorkerPeriodic @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted parameters: WorkerParameters,
    private val tdDataStore: DataStore<TimeData>
) : CoroutineWorker(ctx, parameters) {
    override suspend fun doWork(): Result {
        tdDataStore.updateData { it.copy(dayOfWeek = LocalDate.now().dayOfWeek.ordinal) }
        return Result.success()
    }
}

object LessonsWorkerStatus {
    object Loading {
        val LOCAL = listOf(
            LocalLesson(
                1,
                0,
                "loading",
                null,
                null,
                null,
                "", "",
                "",
                null,
                Week.All.name,
                ""
            )
        )
        val EXTERNAL =
            LOCAL.toDaysList().filter { it.lessons.isNotEmpty() }
    }
    //object Fail
}

val networkConstraints = Constraints(requiredNetworkType = NetworkType.CONNECTED)