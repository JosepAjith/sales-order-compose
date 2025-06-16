package com.joseph.salesorderapp.domain.model

import com.joseph.salesorderapp.data.local.entity.ProductEntity


data class OrderItem(val product: ProductEntity, val quantity: Int)