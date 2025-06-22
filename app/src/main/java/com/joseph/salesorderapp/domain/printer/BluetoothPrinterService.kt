package com.joseph.salesorderapp.domain.printer


interface BluetoothPrinterService {
    suspend fun printText(receiptText: String): Result<Unit>
}
