package com.joseph.salesorderapp.presentation.customer.addcustomer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.salesorderapp.domain.AppRepository
import com.joseph.salesorderapp.presentation.UiEventManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCustomerViewModel @Inject constructor(
    private val repository: AppRepository,
    private val uiEventManager: UiEventManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddCustomerState())
    val uiState: StateFlow<AddCustomerState> = _uiState.asStateFlow()

    fun onNameChanged(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onPhoneChanged(newPhone: String) {
        _uiState.update { it.copy(phoneNo = newPhone) }
    }

    fun onAddressChanged(newAddress: String) {
        _uiState.update { it.copy(address = newAddress) }
    }

    fun onBackPress() {
        viewModelScope.launch {
            uiEventManager.navigateUp()
        }
    }

    fun onAddClicked() {
        val state = _uiState.value

        val nameError = if (state.name.isBlank()) "Name is required" else null

        if (nameError != null) {
            _uiState.update {
                it.copy(
                    nameError = nameError,
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                nameError = null,
            )
        }


        viewModelScope.launch {
            repository.insertCustomer(
                state.name,
                state.phoneNo,
                state.address
            )
            uiEventManager.navigateUp()
            uiEventManager.showToast("Customer saved successfully")
        }

//        viewModelScope.launch {
//            val payload = SaveCustomerInput(
//                customer = listOf(
//                    CustomerPayload(
//                        masterId = 0,
//                        address = uiState.value.address,
//                        phone = uiState.value.phoneNo,
//                        name = uiState.value.name,
//                        trdNo = ""
//                    )
//                )
//            )
//
//            repository.saveCustomer(payload).collect { customerResult ->
//                when (customerResult) {
//                    is Resource.Success -> {
//                        if (customerResult.data?.status == 1) {
//                            uiEventManager.showToast(customerResult.data.message.toString())
//                        } else {
//                            uiEventManager.showToast(customerResult.data?.message.toString())
//                            uiEventManager.navigateUp()
//                        }
//                    }
//
//                    is Resource.Error -> {
//                        uiEventManager.showToast(customerResult.message.toString())
//                    }
//
//                    else -> Unit
//                }
//            }
//        }

    }
}

