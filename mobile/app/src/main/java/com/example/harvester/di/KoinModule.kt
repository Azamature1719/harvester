package com.example.harvester.di

import com.example.harvester.framework.ui.main.MainViewModel
import com.example.harvester.model.repository.Repository
import com.example.harvester.model.repository.RepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Repository> { RepositoryImpl() }

    // ViewModels
    viewModel { MainViewModel(get())}
}