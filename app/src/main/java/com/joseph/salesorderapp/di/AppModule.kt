package com.joseph.salesorderapp.di

import com.joseph.salesorderapp.presentation.UiEventManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideUiEventManager(): UiEventManager = UiEventManager()
}
