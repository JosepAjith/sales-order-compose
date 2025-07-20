package com.joseph.salesorderapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class SettingsResponse(

	@field:SerializedName("data")
	val data: SettingsData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class SettingsData(

	@field:SerializedName("have_total_discount")
	val haveTotalDiscount: Int? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("invoice_title")
	val invoiceTitle: String? = null,

	@field:SerializedName("company_name")
	val companyName: String? = null,

	@field:SerializedName("have_customer_creation")
	val haveCustomerCreation: Int? = null,

	@field:SerializedName("crn_no")
	val crnNo: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("receipt_enable")
	val receiptEnable: Int? = null,

	@field:SerializedName("enable_header")
	val enableHeader: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("have_product_price_edit")
	val haveProductPriceEdit: Int? = null,

	@field:SerializedName("vat_no")
	val vatNo: String? = null
)
