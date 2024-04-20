package com.getir.finalcase.di

import com.getir.finalcase.data.remote.ApiService
import com.getir.finalcase.domain.repository.ProductListRepository
import com.getir.finalcase.presentation.HiltApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
   /*  @Provides
    @Singleton
    fun provideMyApi():ApiService {
        return Retrofit.Builder()
            .baseUrl("https://65c38b5339055e7482c12050.mockapi.io/api")
            .build()
            .create(ApiService::class.java)

    }
*/
    /*@Provides
    @Singleton
    fun provideMyRepository(api: ApiService, app: HiltApp):ProductListRepository {
        return ProductListRepository(api, app)
    }*/
}