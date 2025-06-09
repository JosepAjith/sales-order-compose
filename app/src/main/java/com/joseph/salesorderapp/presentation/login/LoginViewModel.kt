package com.joseph.salesorderapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.salesorderapp.domain.AppRepository
import com.joseph.salesorderapp.presentation.UiEventManager
import com.joseph.salesorderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AppRepository,
    private val uiEventManager: UiEventManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onUsernameChanged(newUsername: String) {
        _uiState.update { it.copy(username = newUsername) }
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onLoginClicked() {
        val state = _uiState.value

        val usernameError = if (state.username.isBlank()) "Username is required" else null
        val passwordError = if (state.password.isBlank()) "Password is required" else null

        if (usernameError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    usernameError = usernameError,
                    passwordError = passwordError,
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                usernameError = null,
                passwordError = null
            )
        }

        viewModelScope.launch {
            repository.login(state.username, state.password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }

                    is Resource.Success -> {
                        if (result.data?.status == 1) {
                            uiEventManager.showToast("Welcome!!!")
                            uiEventManager.navigate(
                                route = "dashboard",
                                popUpToRoute = "login",
                                popUpToInclusive = true
                            )
                        } else {
                            _uiState.update { it.copy(isLoading = false, error = result.message) }
                            uiEventManager.showToast(result.data?.message.toString())
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update { it.copy(isLoading = false, error = result.message) }

                        viewModelScope.launch {
                            uiEventManager.showToast(result.message ?: "Unknown error")
                        }
                    }
                }

            }
        }
    }

    fun onPasswordVisibilityChanged() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

}

