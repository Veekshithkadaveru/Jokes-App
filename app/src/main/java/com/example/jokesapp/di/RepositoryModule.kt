package com.example.jokesapp.di

import com.example.jokesapp.repository.JokesRepo
import com.example.jokesapp.repository.JokesRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindJokesRepository(
        jokesRepositoryImpl: JokesRepoImpl
    ): JokesRepo
}