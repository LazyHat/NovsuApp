package com.lazyhat.novsuapp.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazyhat.novsuapp.data.*
import com.lazyhat.novsuapp.data.datastore.groupspecs.GroupSpecs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EGEvent {
    data class UpdateInstitute(val instituteIndex: Int) : EGEvent()
    data class UpdateGrade(val grade: String) : EGEvent()
    data class UpdateGroup(val group: String) : EGEvent()
    data class UpdateSubGroup(val subGroup: String) : EGEvent()
    object SubGroupEnableChanged : EGEvent()
    object OnResult : EGEvent()
}

data class EGModel(
    val groupSpecs: GroupSpecs = GroupSpecs(),
    val groupsList: List<String> = listOf(),
    val isGroupsListLoading: Boolean = false,
    val buttonEnabled: Boolean = false,
    val ifSubgroupEntered: Boolean = false
)

private const val FULL_UPDATE_KEY = "full_update"
private const val GROUP_SPECS_BUNDLE_KEY = "group_specs"


@HiltViewModel
class EGViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _fullUpdate = savedStateHandle.getStateFlow(FULL_UPDATE_KEY, false)
    val uiState = combine(
        savedStateHandle.getStateFlow(GROUP_SPECS_BUNDLE_KEY, GroupSpecs().toBundle())
            .toGroupSpecs(),
        mainRepository.getGroupsStream(),
        mainRepository.getGroupSpecsStream()
    ) { groupSpecsLocal, groupList, groupSpecs ->
        EGModel(
            groupSpecsLocal,
            groupList.groups,
            groupList.loading,
            groupSpecsLocal.copy(subGroup = "") != groupSpecs.copy(subGroup = "") && groupSpecsLocal.group != "",
            groupSpecsLocal.subGroup.isNotEmpty()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EGModel())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            savedStateHandle[GROUP_SPECS_BUNDLE_KEY] = mainRepository.getGroupSpecs().toBundle()
        }
        updateGroupsLocal(false)
    }

    fun createEvent(event: EGEvent) = onEvent(event)

    @Suppress("ControlFlowWithEmptyBody", "IMPLICIT_CAST_TO_ANY")
    private fun onEvent(event: EGEvent) = when (event) {
        is EGEvent.UpdateGrade -> {
            updateGroupSpecsLocal(grade = event.grade)
            updateGroupsLocal()
        }
        is EGEvent.UpdateInstitute -> {
            updateGroupSpecsLocal(institute = Institute.values()[event.instituteIndex])
            updateGroupsLocal()

        }
        is EGEvent.UpdateGroup -> {
            updateGroupSpecsLocal(group = event.group)
            savedStateHandle[FULL_UPDATE_KEY] = true
        }
        is EGEvent.UpdateSubGroup -> {
            updateSubGroup(event.subGroup)
        }
        is EGEvent.SubGroupEnableChanged -> {
            if (uiState.value.ifSubgroupEntered && uiState.value.groupSpecs.subGroup.isNotEmpty()) {
                updateSubGroup("")
            } else {
                updateSubGroup(DataSource.subGroupsList[0])
            }
        }
        is EGEvent.OnResult -> {
            if (_fullUpdate.value)
                viewModelScope.launch {
                    mainRepository.updateGroupSpecs(uiState.value.groupSpecs)
                    mainRepository.refreshWeek()
                }
            else {
            }
        }
    }

    private fun updateSubGroup(subGroup: String) {
        updateGroupSpecsLocal(subGroup = subGroup)
        viewModelScope.launch {
            mainRepository.updateGroupSpecs(uiState.value.groupSpecs)
        }
    }

    private fun updateGroupsLocal(clearGroup: Boolean = true) {
        if (clearGroup)
            updateGroupSpecsLocal(group = "")
        mainRepository.updateGroups(
            uiState.value.groupSpecs.institute,
            uiState.value.groupSpecs.grade
        )
    }

    private fun updateGroupSpecsLocal(
        institute: Institute = uiState.value.groupSpecs.institute,
        grade: String = uiState.value.groupSpecs.grade,
        group: String = uiState.value.groupSpecs.group,
        subGroup: String = uiState.value.groupSpecs.subGroup
    ) {
        savedStateHandle[GROUP_SPECS_BUNDLE_KEY] =
            GroupSpecs(institute, grade, group, subGroup).toBundle()
    }

    fun getInstitutes(context: Context): List<String> {
        return Institute.values().map {
            context.resources.getString(it.labelRes)
        }
    }

    fun getGrades(): List<String> {
        return DataSource.gradesList
    }
}