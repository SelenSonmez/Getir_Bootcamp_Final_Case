package com.getir.finalcase.di

import com.getir.finalcase.data.remote.ApiService
import com.getir.finalcase.di.IoDispatcher
import com.getir.finalcase.domain.repository.ProductListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideProductListRepository(
        apiService: ApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) = ProductListRepository(apiService,ioDispatcher)

}