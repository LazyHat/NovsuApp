package com.lazyhat.novsuapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazyhat.novsuapp.R
import com.lazyhat.novsuapp.data.MainRepository
import com.lazyhat.novsuapp.data.datastore.settings.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.rustore.sdk.appupdate.listener.InstallStateUpdateListener
import ru.rustore.sdk.appupdate.model.AppUpdateInfo
import ru.rustore.sdk.appupdate.model.AppUpdateOptions
import ru.rustore.sdk.appupdate.model.InstallStatus
import ru.rustore.sdk.appupdate.model.UpdateAvailability
import ru.rustore.sdk.core.tasks.OnSuccessListener
import javax.inject.Inject

sealed class UpdateProgress {
    object Installing : UpdateProgress()
    data class Downloading(val currentBytes: Long, val maxBytes: Long) : UpdateProgress()
    object Pending : UpdateProgress()
    object Downloaded : UpdateProgress()
    object Failed : UpdateProgress()
    object InProgress : UpdateProgress()
}

sealed class UpdateInfo {
    object NotAvailable : UpdateInfo()
    object Available : UpdateInfo()
    data class InProgress(val progress: UpdateProgress) : UpdateInfo()

    fun toString(context: Context): String? = when (this) {
        is NotAvailable -> null
        is Available -> context.getString(R.string.update_available)
        is InProgress -> when (this.progress) {
            is UpdateProgress.Downloaded -> context.getString(R.string.update_downloaded)
            else -> null
        }
    }

    fun toStringGetButton(context: Context): String? = when (this) {
        is Available -> context.getString(R.string.update_get)
        else -> null
    }

    fun toStringCancelButton(context: Context): String? = when (this) {
        is Available -> context.getString(R.string.update_cancel)
        else -> null
    }

    fun toStringInstallButton(context: Context): String? = when (this) {
        is InProgress -> when (this.progress) {
            is UpdateProgress.Downloaded -> context.getString(R.string.update_install)
            else -> null
        }

        else -> null
    }
}

sealed class MainEvent {
    object GetUpdate : MainEvent()
    object CancelUpdate : MainEvent()
    object Install : MainEvent()
}

data class MainUiState(
    val settings: Settings = Settings(),
    val updateInfo: UpdateInfo = UpdateInfo.NotAvailable,
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val _updateInfo =
        MutableStateFlow<UpdateInfo>(UpdateInfo.NotAvailable)
    private val _appUpdateInfo = MutableStateFlow<AppUpdateInfo?>(null)
    private val _updateListener: InstallStateUpdateListener = InstallStateUpdateListener { info ->
        Log.e("installing", "InstallListened")
        if (info.installStatus != InstallStatus.UNKNOWN)
            _updateInfo.update {
                UpdateInfo.InProgress(
                    when (info.installStatus) {
                        InstallStatus.DOWNLOADING -> UpdateProgress.Downloading(
                            info.bytesDownloaded,
                            info.totalBytesToDownload
                        )

                        InstallStatus.PENDING -> UpdateProgress.Pending
                        InstallStatus.DOWNLOADED -> UpdateProgress.Downloaded
                        InstallStatus.INSTALLING -> UpdateProgress.Installing
                        InstallStatus.FAILED -> UpdateProgress.Failed
                        else -> UpdateProgress.InProgress
                    }
                )
            }
    }
    private val checkUpdatesListener = OnSuccessListener<AppUpdateInfo> { info ->
        Log.e("Updates", "Listened")
        _appUpdateInfo.update { info }
        _updateInfo.update {
            when (info.updateAvailability) {
                UpdateAvailability.UPDATE_AVAILABLE -> UpdateInfo.Available
                UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> UpdateInfo.InProgress(
                    UpdateProgress.InProgress
                )

                else -> UpdateInfo.NotAvailable
            }
        }
    }

    init {
        mainRepository.getUpdateManager().getAppUpdateInfo()
            .addOnSuccessListener(checkUpdatesListener).addOnFailureListener {
                Log.e("updateFail", it.message ?: "")
            }
    }

    val uiState =
        combine(
            mainRepository.getSettingsStream(),
            _updateInfo.asStateFlow()
        ) { settings, updateInfo ->
            MainUiState(settings, updateInfo)
        }.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000),
            MainUiState()
        )

    fun onCreate() {
        viewModelScope.launch {
            mainRepository.initDataBase()
        }
    }

    fun createEvent(event: MainEvent) = onEvent(event)

    @Suppress("ControlFlowWithEmptyBody")
    private fun onEvent(event: MainEvent): Any = when (event) {
        is MainEvent.GetUpdate ->
            if (_appUpdateInfo.value != null && _appUpdateInfo.value!!.updateAvailability == UpdateAvailability.UPDATE_AVAILABLE) {
                mainRepository.getUpdateManager().startUpdateFlow(
                    appUpdateInfo = _appUpdateInfo.value!!,
                    appUpdateOptions = AppUpdateOptions.Builder().build()
                )
                mainRepository.getUpdateManager().registerListener(_updateListener)
            } else {
            }

        is MainEvent.CancelUpdate -> {
            _updateInfo.update { UpdateInfo.NotAvailable }
        }

        is MainEvent.Install -> {

            mainRepository.getUpdateManager().completeUpdate()
        }
    }
}
