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




  /*  @Provides
    fun provideFoodRepository(
        apiService: ApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) = FoodRepository(apiService,ioDispatcher)


    @Provides
    fun provideOrderRepository(
        apiService: ApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) = OrderRepository(apiService,ioDispatcher)*/





}

/*
package com.getir.finalcase.di

import com.getir.finalcase.data.repository.ProductListRepositoryImpl
import com.getir.finalcase.domain.repository.ProductListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton

    abstract fun bindProductListRepository(
        productListRepositoryImpl:ProductListRepositoryImpl
    ): ProductListRepository
}*/
