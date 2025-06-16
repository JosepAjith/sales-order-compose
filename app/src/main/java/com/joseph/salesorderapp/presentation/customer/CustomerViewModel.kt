package com.joseph.salesorderapp.presentation.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.joseph.salesorderapp.data.remote.model.CustomersItem
import com.joseph.salesorderapp.domain.AppRepository
import com.joseph.salesorderapp.presentation.UiEventManager
import com.joseph.salesorderapp.presentation.dashboard.DashBoardState
import com.joseph.salesorderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val repository: AppRepository,
    private val uiEventManager: UiEventManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerState())
    val uiState: StateFlow<CustomerState> = _uiState.asStateFlow()

    init {
//        downloadCustomers()
        fetchCustomers("")
    }

    private fun fetchCustomers(query: String) {
        viewModelScope.launch {
            uiEventManager.showLoader("Loading..", true)
            repository.fetchCustomers(query).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(error = null) }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                customers = result.data ?: emptyList(),
                                error = null,
                                success = true
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(error = result.message)
                        }
                    }
                }
            }
            uiEventManager.showLoader("", false)
        }
    }


//    fun downloadCustomers() {
//        viewModelScope.launch {
//            repository.fetchCustomers().collect { result ->
//                when (result) {
//                    is Resource.Loading -> {
//                        _uiState.update { it.copy(isLoading = true, error = null) }
//                    }
//
//                    is Resource.Success -> {
//                        if (result.data?.status == 1) {
//                            val customerList = result.data.customers?.filterNotNull() ?: emptyList()
//                            _uiState.update {
//                                it.copy(
//                                    isLoading = false,
//                                    customers = customerList,
//                                    error = null,
//                                    success = true
//                                )
//                            }
//                        } else {
//                            _uiState.update { it.copy(isLoading = false, error = result.message) }
//                            uiEventManager.showToast(result.data?.message.toString())
//                        }
//                    }
//
//                    is Resource.Error -> {
//                        _uiState.update { it.copy(isLoading = false, error = result.message) }
//                        viewModelScope.launch {
//                            uiEventManager.showToast(result.message ?: "Unknown error")
//                        }
//                    }
//                }
//
//            }
//        }
//    }

}