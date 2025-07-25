package com.joseph.salesorderapp.presentation.sale

import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.ProductEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderDetailsEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderSummaryEntity
import com.joseph.salesorderapp.domain.model.OrderItem

data class OrderState(
    val customers: List<CustomerEntity> = emptyList(),
    val products: List<ProductEntity> = emptyList(),
    val selectedCustomer: CustomerEntity? = null,
    val selectedProduct: ProductEntity? = null,
    val selectedPaymentMode: String? = null,
    val quantity: String = "",
    val price:String="0",
    val discount: String = "0",
    val totalAmount:Double = 0.0,
    val orderItems: List<OrderItem> = emptyList(),
    val message: String = "",
    val nextOrderID: Int? = null,
    val orderSummary: OrderSummaryEntity? = null,
    val itemList: List<OrderDetailsEntity> = emptyList(),

    val isEditMode:Boolean=false,
    val isEnableDiscount:Boolean=false,
    val isEnablePriceEdit:Boolean=false,
)