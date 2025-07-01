package com.joseph.salesorderapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class SaveCustomerInput(

	@field:SerializedName("master_id")
	val masterId: Int? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("trd_no")
	val trdNo: String? = null
)
