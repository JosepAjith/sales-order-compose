package com.joseph.salesorderapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class SaveOrderInput(

	@field:SerializedName("order_token")
	val orderToken: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("sales_master_id")
	val salesMasterId: Int? = null,

	@field:SerializedName("details")
	val details: List<OrderItemPayload?>? = null,

	@field:SerializedName("customer_master_id")
	val customerMasterId: Int? = null
)

data class OrderItemPayload(

	@field:SerializedName("product_master_id")
	val productMasterId: Int? = null,

	@field:SerializedName("price")
	val price: Any? = null,

	@field:SerializedName("qty")
	val qty: Int? = null,

	@field:SerializedName("discount")
	val discount: Any? = null,

	@field:SerializedName("tax")
	val tax: Any? = null
)
