package com.joseph.salesorderapp.presentation.login

data class LoginState(
    val username: String = "",
    val password: String = "",
    val clientCode: String = "",
    val isDomainAdded: Boolean = false,
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val clientCodeError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val error: String? = null,
    val success: Boolean = false
)

