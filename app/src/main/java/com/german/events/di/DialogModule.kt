package com.german.events.di

import com.german.events.ui.dialog.AddEventDialog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DialogModule {

    @Singleton
    @Provides
    fun provideAddEventDialog() =  AddEventDialog()


}