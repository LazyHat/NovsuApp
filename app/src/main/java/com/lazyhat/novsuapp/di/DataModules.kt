package com.lazyhat.novsuapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.work.*
import com.lazyhat.novsuapp.data.*
import com.lazyhat.novsuapp.data.datastore.grouplist.GroupList
import com.lazyhat.novsuapp.data.datastore.grouplist.GroupListSerializer
import com.lazyhat.novsuapp.data.datastore.groupspecs.GroupSpecsSerialiser
import com.lazyhat.novsuapp.data.datastore.groupspecs.GroupSpecs
import com.lazyhat.novsuapp.data.datastore.settings.Settings
import com.lazyhat.novsuapp.data.datastore.settings.SettingsSerializer
import com.lazyhat.novsuapp.data.datastore.timedata.TimeData
import com.lazyhat.novsuapp.data.datastore.timedata.TimeDataSerializer
import com.lazyhat.novsuapp.data.db.MainDao
import com.lazyhat.novsuapp.data.db.MainDataBase
import com.lazyhat.novsuapp.data.network.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ru.rustore.sdk.appupdate.manager.RuStoreAppUpdateManager
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory
import java.time.Duration
import javax.inject.Singleton

private const val GS_DATA_STORE_FILE_NAME = "gs_data.pb"
private const val S_DATA_STORE_FILE_NAME = "s_data.pb"
private const val GL_DATA_STORE_FILE_NAME = "gl_data.pb"
private const val TD_DATA_STORE_FILE_NAME = "td_data.pb"

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        workManager: WorkManager,
        mainDao: MainDao,
        gsDataStore: DataStore<GroupSpecs>,
        sDataStore: DataStore<Settings>,
        glDataStore: DataStore<GroupList>,
        tdDataStore: DataStore<TimeData>,
        updateManager: RuStoreAppUpdateManager
    ): MainRepository {
        return DefaultRepository(
            mainDao,
            sDataStore,
            gsDataStore,
            glDataStore,
            workManager,
            tdDataStore,
            OneTimeWorkRequestBuilder<GroupsListWorker>().setConstraints(networkConstraints),
            OneTimeWorkRequestBuilder<LessonsWorker>().setConstraints(networkConstraints),
            PeriodicWorkRequestBuilder<LessonsWorkerPeriodic>(Duration.ofHours(2)).setConstraints(
                networkConstraints
            ),
            PeriodicWorkRequestBuilder<WeekWorkerPeriodic>(Duration.ofDays(1)).setConstraints(
                networkConstraints
            ),
            PeriodicWorkRequestBuilder<GetTimeWorkerPeriodic>(Duration.ofMinutes(30)).setConstraints(
                networkConstraints
            ),
            updateManager
        )
    }

    @Singleton
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): MainDataBase {
        return MainDataBase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideMainDao(db: MainDataBase): MainDao = db.mainDao()

    @Singleton
    @Provides
    fun provideGSDataStore(@ApplicationContext context: Context): DataStore<GroupSpecs> {
        return DataStoreFactory.create(
            serializer = GroupSpecsSerialiser,
            produceFile = { context.dataStoreFile(GS_DATA_STORE_FILE_NAME) },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { GroupSpecs() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideSDataStore(@ApplicationContext context: Context): DataStore<Settings> {
        return DataStoreFactory.create(
            serializer = SettingsSerializer,
            produceFile = { context.dataStoreFile(S_DATA_STORE_FILE_NAME) },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { Settings() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideGLDataStore(@ApplicationContext context: Context): DataStore<GroupList> {
        return DataStoreFactory.create(
            serializer = GroupListSerializer,
            produceFile = { context.dataStoreFile(GL_DATA_STORE_FILE_NAME) },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { GroupList() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideTDDataStore(@ApplicationContext context: Context): DataStore<TimeData> {
        return DataStoreFactory.create(
            serializer = TimeDataSerializer,
            produceFile = { context.dataStoreFile(TD_DATA_STORE_FILE_NAME) },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { TimeData() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideRuStoreInAppUpdateManager(@ApplicationContext context: Context): RuStoreAppUpdateManager =
        RuStoreAppUpdateManagerFactory.create(context)
}