package com.affordable.di

import com.affordable.repository.MainRepository
import org.koin.dsl.module


val RepositoryModule = module {
    single {
        MainRepository(get())
    }
}