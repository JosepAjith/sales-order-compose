package com.joseph.salesorderapp.data.printer

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.joseph.salesorderapp.data.local.preferences.AppPreferences
import com.joseph.salesorderapp.domain.printer.BluetoothPrinterService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import android.Manifest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothPrinterManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appPreferences: AppPreferences
) : BluetoothPrinterService {

    override suspend fun printText(receiptText: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val savedPrinterName =
                    appPreferences.getString(AppPreferences.KEY_PRINTER_NAME).firstOrNull()

                if (savedPrinterName.isNullOrEmpty()) {
                    return@withContext Result.failure(Exception("Printer name not set in preferences"))
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val permission = Manifest.permission.BLUETOOTH_CONNECT
                    if (ContextCompat.checkSelfPermission(
                            context,
                            permission
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return@withContext Result.failure(Exception("Missing BLUETOOTH_CONNECT permission"))
                    }
                }

                val connection = BluetoothPrintersConnections().list
                    ?.firstOrNull { it.device?.name.equals(savedPrinterName, ignoreCase = true) }

                if (connection == null) {
                    return@withContext Result.failure(Exception("Bluetooth printer '$savedPrinterName' not found"))
                }

                val printer = EscPosPrinter(connection, 203, 48f, 32)
                printer.printFormattedText(receiptText)

                Result.success(Unit)

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}


