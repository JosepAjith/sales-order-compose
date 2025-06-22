package com.joseph.salesorderapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class OrderResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
