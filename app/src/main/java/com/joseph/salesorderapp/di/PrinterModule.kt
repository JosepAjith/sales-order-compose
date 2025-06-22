package com.joseph.salesorderapp.di

import com.joseph.salesorderapp.data.printer.BluetoothPrinterManager
import com.joseph.salesorderapp.domain.printer.BluetoothPrinterService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PrinterModule {

    @Provides
    @Singleton
    fun provideBluetoothPrinterService(
        printerManager: BluetoothPrinterManager
    ): BluetoothPrinterService = printerManager
}
