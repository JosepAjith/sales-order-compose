package com.joseph.salesorderapp.presentation.dashboard

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
class DashBoardViewModel @Inject constructor(
    private val repository: AppRepository,
    private val uiEventManager: UiEventManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashBoardState())
    val uiState: StateFlow<DashBoardState> = _uiState.asStateFlow()


    fun onCardClicked(route: String) {
        val state = _uiState.value

        viewModelScope.launch {
            uiEventManager.navigate(route = route)
        }
    }

    fun downloadMasters() {
        viewModelScope.launch {
            uiEventManager.showLoader("Loading..", true)

            repository.downloadCustomers().collect { customerResult ->
                when (customerResult) {

                    is Resource.Success -> {
                        if (customerResult.data?.status == 1) {
                            val customerList =
                                customerResult.data.customers?.filterNotNull() ?: emptyList()
                            repository.deleteAllCustomers()
                            repository.insertCustomers(customerList)
                        } else {
                            uiEventManager.showToast(customerResult.data?.message.toString())
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(error = customerResult.message)
                        }
                    }

                    else -> {}
                }
            }
            repository.downloadProducts().collect { productResult ->
                when (productResult) {

                    is Resource.Success -> {
                        if (productResult.data?.status == 1) {
                            val productList =
                                productResult.data.productDataItem?.filterNotNull() ?: emptyList()
                            repository.deleteAllProducts()
                            repository.insertProducts(productList)
                            uiEventManager.showToast("Masters downloaded successfully")
                        } else {
                            uiEventManager.showToast(productResult.data?.message.toString())
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(error = productResult.message)
                        }
                    }

                    else -> {}
                }
            }


            uiEventManager.showLoader("", false)
        }
    }

}

