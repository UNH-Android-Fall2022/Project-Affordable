package com.affordable.di

import com.affordable.ui.main.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    viewModel {
        HomeViewModel(get(), get())
    }
}