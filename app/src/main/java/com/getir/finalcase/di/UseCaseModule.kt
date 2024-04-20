package com.getir.finalcase.di

import com.getir.finalcase.domain.repository.ProductListRepository
import com.getir.finalcase.domain.usecase.ProductListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideProductListUseCase(foodRepository: ProductListRepository) =
        ProductListUseCase(foodRepository)
}