package com.joseph.salesorderapp.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.salesorderapp.data.local.preferences.AppPreferences
import com.joseph.salesorderapp.presentation.UiEventManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val uiEventManager: UiEventManager,
    private val appPreferences: AppPreferences
) : ViewModel() {
    private val _uiState = MutableStateFlow(SplashState())
    val uiState: StateFlow<SplashState> = _uiState.asStateFlow()

    init {
        startSplashTimer()
    }

    private fun startSplashTimer() {
        viewModelScope.launch {
            val isLoggedIn =
                appPreferences.getBoolean(AppPreferences.KEY_IS_LOGGED_IN).first() // collect once

            delay(2000L)
            _uiState.value = _uiState.value.copy(isLoading = false)

            val destination = if (isLoggedIn) "dashboard" else "login"

            uiEventManager.navigate(
                route = destination,
                popUpToRoute = "splash",
                popUpToInclusive = true
            )
        }
    }
}

