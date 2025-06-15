package com.joseph.salesorderapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class ProductResponse(

	@field:SerializedName("data")
	val productDataItem: List<ProductDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class ProductDataItem(

	@field:SerializedName("master_id")
	val masterId: Int? = null,

	@field:SerializedName("item_code")
	val itemCode: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("qty")
	val qty: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("tax_percentage")
	val taxPercentage: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("units")
	val units: String? = null
)
