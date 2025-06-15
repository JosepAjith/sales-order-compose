package com.joseph.salesorderapp.di

import android.content.Context
import com.joseph.salesorderapp.data.local.preferences.AppPreferences
import com.joseph.salesorderapp.presentation.UiEventManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppPreferences(
        @ApplicationContext context: Context
    ): AppPreferences = AppPreferences(context)

    @Singleton
    @Provides
    fun provideUiEventManager(): UiEventManager = UiEventManager()
}
