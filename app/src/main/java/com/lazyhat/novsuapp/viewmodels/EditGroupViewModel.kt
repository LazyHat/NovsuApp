package com.lazyhat.novsuapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazyhat.novsuapp.data.EditGroupModel
import com.lazyhat.novsuapp.data.GroupSpecs
import com.lazyhat.novsuapp.data.Institute
import com.lazyhat.novsuapp.data.getGroupsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditGroupViewModel(groupSpecs: GroupSpecs) : ViewModel() {
    private val _uiState = MutableStateFlow(EditGroupModel(groupSpecs))
    val uiState = _uiState.asStateFlow()

    init {
        if (uiState.value.groupsList.isEmpty())
            calculateGroupList(false)
    }

    fun updateInstituteAndGrade(
        newInstitute: Institute = uiState.value.groupSpecs.institute,
        newGrade: String = uiState.value.groupSpecs.grade,
    ) {
        _uiState.update { model ->
            model.copy(
                groupSpecs = model.groupSpecs.copy(
                    institute = newInstitute,
                    grade = newGrade
                )
            )
        }
        calculateGroupList(true)
    }

    fun updateGroup(newGroup: String) {
        _uiState.update { model -> model.copy(groupSpecs = model.groupSpecs.copy(group = newGroup)) }
    }

    fun updateSubGroup(newSubGroup: String) {
        _uiState.update { model -> model.copy(groupSpecs = model.groupSpecs.copy(subGroup = newSubGroup)) }
    }

    private fun calculateGroupList(resetGroup: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { model ->
                model.copy(
                    groupsList = getGroupsList(
                        model.groupSpecs.grade,
                        model.groupSpecs.institute.labelRus
                    ),
                    groupSpecs = model.groupSpecs.copy(group = if (resetGroup) "" else model.groupSpecs.group)
                )
            }
        }
    }
}