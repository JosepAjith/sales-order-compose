package com.joseph.salesorderapp.data.remote.model

import com.joseph.salesorderapp.data.local.entity.ProductEntity

data class OrderItem(val product: ProductEntity, val quantity: Int)