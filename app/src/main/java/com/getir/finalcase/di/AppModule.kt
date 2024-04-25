package com.getir.finalcase.di

import com.getir.finalcase.data.local.database.BasketRoomDatabase
import com.getir.finalcase.data.local.database.dao.BasketProductDao
import com.getir.finalcase.data.remote.ApiService
import com.getir.finalcase.domain.repository.ProductListRepository
import com.getir.finalcase.presentation.HiltApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
/**
 * Dagger module class providing dependencies for the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBasketProductDao(database: BasketRoomDatabase): BasketProductDao {
        return database.basketProductDao()
    }
}