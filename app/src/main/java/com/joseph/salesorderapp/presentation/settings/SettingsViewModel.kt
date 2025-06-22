package com.joseph.salesorderapp.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.salesorderapp.data.local.preferences.AppPreferences
import com.joseph.salesorderapp.presentation.UiEventManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val uiEventManager: UiEventManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState

    init {
        viewModelScope.launch {
            val savedName =
                appPreferences.getString(AppPreferences.KEY_PRINTER_NAME).firstOrNull() ?: ""
            _uiState.value = _uiState.value.copy(printerName = savedName)
        }
    }

    fun onPrinterNameChange(name: String) {
        _uiState.value = _uiState.value.copy(printerName = name)
    }

    fun savePrinterName() {
        val state = _uiState.value
        val printNameError =
            if (state.printerName.isBlank()) "Printer address is required" else null

        if (printNameError != null) {
            _uiState.update {
                it.copy(
                    printNameError = printNameError,
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            appPreferences.saveString(AppPreferences.KEY_PRINTER_NAME, uiState.value.printerName)
            _uiState.value = _uiState.value.copy(isLoading = false)
            uiEventManager.showToast("Printer address saved successfully")
            onBackPress()
        }
    }

    fun logout() {
        viewModelScope.launch {
            appPreferences.saveBoolean(AppPreferences.KEY_IS_LOGGED_IN, false)
            uiEventManager.showToast("Logout successfully")
            uiEventManager.navigate(
                route = "login",
                popUpToRoute = "settings",
                popUpToInclusive = true
            )
        }
    }

    fun onBackPress() {
        viewModelScope.launch {
            uiEventManager.navigateUp()
        }
    }
}

