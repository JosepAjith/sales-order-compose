package com.joseph.salesorderapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("data")
	val userDataItem: List<UserDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class UserDataItem(

	@field:SerializedName("master_id")
	val masterId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
