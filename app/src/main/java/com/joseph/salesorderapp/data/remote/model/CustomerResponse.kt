package com.joseph.salesorderapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class CustomerResponse(

	@field:SerializedName("customers")
	val customers: List<CustomersItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class CustomersItem(

	@field:SerializedName("master_id")
	val masterId: Int? = null,

	@field:SerializedName("address")
	val address: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("phone")
	val phone: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("trd_no")
	val trdNo: String? = null
)
