package com.example.novsucompose.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novsucompose.data.GroupSpecs
import com.example.novsucompose.data.Institute
import com.example.novsucompose.data.getGroupsList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditGroupViewModel(groupSpecs: GroupSpecs) : ViewModel() {
    private val _uiState = MutableStateFlow(groupSpecs)
    val uiState = _uiState.asStateFlow()

    fun updateInstituteAndGrade(
        newInstitute: Institute = uiState.value.institute,
        newGrade: String = uiState.value.grade,
    ) {
        _uiState.update { groupSpecs ->
            groupSpecs.copy(
                institute = newInstitute,
                grade = newGrade
            )
        }
        calculateGroupList()
    }

    fun updateGroup(newGroup: String) {
        _uiState.update { groupSpecs -> groupSpecs.copy(group = newGroup) }
    }

    fun updateSubGroup(newSubGroup: String) {
        _uiState.update { groupSpecs -> groupSpecs.copy(subGroup = newSubGroup) }
    }

    private fun calculateGroupList() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { groupSpecs ->
                groupSpecs.copy(
                    groupList = getGroupsList(
                        uiState.value.grade,
                        uiState.value.institute.labelRus
                    )
                )
            }
        }
    }
}