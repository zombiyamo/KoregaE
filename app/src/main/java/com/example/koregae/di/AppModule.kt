package com.example.koregae.di

import org.koin.dsl.module

val appModule = module {
    includes(NetworkModule)
}
