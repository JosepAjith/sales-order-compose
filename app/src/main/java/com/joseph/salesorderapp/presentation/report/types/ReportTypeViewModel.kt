package com.joseph.salesorderapp.presentation.report.types

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.salesorderapp.presentation.UiEventManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReportTypeViewModel @Inject constructor(
    private val uiEventManager: UiEventManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportTypeState())
    val uiState: StateFlow<ReportTypeState> = _uiState


    fun onCardClicked(route: String) {
        viewModelScope.launch {
            uiEventManager.navigate(route = route)
        }
    }


    fun onBackPress() {
        viewModelScope.launch {
            uiEventManager.navigateUp()
        }
    }
}