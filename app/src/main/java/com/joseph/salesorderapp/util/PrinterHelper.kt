package com.joseph.salesorderapp.util

import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderDetailsEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderSummaryEntity
import com.joseph.salesorderapp.data.local.preferences.AppPreferences
import com.joseph.salesorderapp.domain.model.ItemWiseReport
import com.joseph.salesorderapp.domain.printer.BluetoothPrinterService
import com.joseph.salesorderapp.presentation.UiEventManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrinterHelper @Inject constructor(
    private val printerService: BluetoothPrinterService,
    private val uiEventManager: UiEventManager,
    private val appPreferences: AppPreferences
) {

    suspend fun printOrderReport(
        summary: OrderSummaryEntity,
        orderItems: List<OrderDetailsEntity>,
        onSuccess: suspend () -> Unit,
        onFailure: suspend (String) -> Unit
    ) {
        try {
            uiEventManager.showLoader("Printing please wait", true)

            val textToPrint = withContext(Dispatchers.IO) {
                val companyName = appPreferences.getString(AppPreferences.KEY_COMPANY_NAME).first()
                val companyAddress = appPreferences.getString(AppPreferences.KEY_COMPANY_ADDRESS).first()
                val vatNumber = appPreferences.getString(AppPreferences.KEY_COMPANY_VAT_NO).first()
                val crNumber = appPreferences.getString(AppPreferences.KEY_COMPANY_CR_NO).first()
                val invoiceTittle = appPreferences.getString(AppPreferences.KEY_INVOICE_TITLE).first()

                val builder = StringBuilder()
                val date = summary.orderDate
                val time = summary.orderTime

                builder.append("[C]<b>$companyName</b>\n")
                builder.append("[C]$companyAddress\n")
                builder.append("[C]CR No: $crNumber\n")
                builder.append("[C]VAT No: $vatNumber\n")

                builder.append("\n")
                builder.append("[C]$invoiceTittle\n")
                builder.append("[C]--------------------------------\n")

                builder.append("[C]<b>SONO: ${summary.orderID}</b>\n")
                builder.append("[L]Customer: ${summary.customerName}\n")
                builder.append("[L]Date    : $date\n")
                builder.append("[L]Time    : $time\n")
                builder.append("[L]Payment Mode: ${summary.paymentMode}\n")
                builder.append("[C]--------------------------------\n")

                builder.append("[L]Description        Qty     Total\n")
                builder.append("[C]--------------------------------\n")

                orderItems.forEach { item ->
                    val name = item.productName
                    val code = item.productCode.take(15).padEnd(18)  // 18 chars
                    val qty = item.quantity.toString().padStart(4)   // 4 chars, right-aligned
                    val total =
                        "%.3f".format(item.quantity * item.pricePerUnit).padStart(10) // 10 chars

                    builder.append("[L]<b>$name</b>\n")
                    val line = "$code$qty$total"
                    builder.append("[L]$line\n")
                    builder.append("[C]--------------------------------\n")
                }
                val totalQty = orderItems.sumOf { it.quantity }
                val grandTotal = summary.totalAmount

                val discountLabel = "Discount".padEnd(22)//22 chars
                val discountAmtText =
                    "%.3f".format(summary.discountAmount).padStart(10) // ~9–10 chars

                val totalLabel = "Total".padEnd(18) // 14 chars
                val qtyText = totalQty.toString().padStart(4) // 6 chars
                val grandTotalText = "%.3f".format(grandTotal).padStart(10) // ~9–10 chars

                val discountLine = "$discountLabel$discountAmtText"
                val totalLine = "$totalLabel$qtyText$grandTotalText"

                if (summary.discountAmount > 0) {
                    builder.append("[L]$discountLine\n")
                }

                builder.append("[L]$totalLine\n")
                builder.append("[C]--------------------------------\n")
                builder.append("[C]Thank you for shopping with us!\n")
                builder.toString()

            }

            val result = printerService.printText(textToPrint)

            result.onSuccess {
                onSuccess()
            }.onFailure {
                onFailure(it.message ?: "Unknown error")
            }

        } catch (e: Exception) {
            onFailure("Print failed: ${e.message}")
        } finally {
            uiEventManager.showLoader("", false)
        }
    }


    suspend fun printItemWiseReport(
        customer: CustomerEntity?,
        orderItems: List<ItemWiseReport>,
        fromDate: String?,
        toDate: String?,
        onSuccess: suspend () -> Unit,
        onFailure: suspend (String) -> Unit
    ) {
        try {
            uiEventManager.showLoader("Printing please wait", true)

            val textToPrint = withContext(Dispatchers.IO) {
                val builder = StringBuilder()
                val fDate = fromDate
                val tDate = toDate
                val customerName = customer?.name ?: ""

                builder.append("[C]<b>Item Wise Report</b>\n")
                builder.append("[C]--------------------------------\n")
                if (customerName.isNotBlank()) {
                    builder.append("[L]Customer: $customerName\n")
                }

                builder.append("[L]From Date    : $fDate\n")
                builder.append("[L]To Date      : $tDate\n")
                builder.append("[C]--------------------------------\n")

                builder.append("[L]Description        Qty     Total\n")
                builder.append("[C]--------------------------------\n")

                orderItems.forEach { item ->
                    val name = item.productName
                    val code = item.productCode.take(15).padEnd(18)  // 18 chars
                    val qty = item.totalQty.toString().padStart(4)   // 4 chars, right-aligned
                    val total =
                        "%.3f".format(item.totalAmount).padStart(10) // 10 chars

                    builder.append("[L]<b>$name</b>\n")
                    val line = "$code$qty$total"
                    builder.append("[L]$line\n")
                    builder.append("[C]--------------------------------\n")
                }
                val totalQty = orderItems.sumOf { it.totalQty }
                val grandTotal = orderItems.sumOf { it.totalAmount }

                val totalLabel = "Total".padEnd(18) // 14 chars
                val qtyText = totalQty.toString().padStart(4) // 6 chars
                val grandTotalText = "%.3f".format(grandTotal).padStart(10) // ~9–10 chars

                val totalLine = "$totalLabel$qtyText$grandTotalText"
                builder.append("[L]$totalLine\n")
                builder.append("[C]--------------------------------\n")
                builder.toString()

            }

            val result = printerService.printText(textToPrint)

            result.onSuccess {
                onSuccess()
            }.onFailure {
                onFailure(it.message ?: "Unknown error")
            }

        } catch (e: Exception) {
            onFailure("Print failed: ${e.message}")
        } finally {
            uiEventManager.showLoader("", false)
        }
    }
}
