package com.getir.finalcase.di

import android.content.Context
import androidx.room.Room
import com.getir.finalcase.data.local.database.BasketRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
/**
 * Dagger module class providing dependencies related to the database.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    /**
     * Provides the BasketRoomDatabase instance.
     *
     * @param context The application context.
     * @return The BasketRoomDatabase instance.
     */
    @Singleton
    @Provides
    fun provideBasketRoomDatabase(@ApplicationContext context: Context): BasketRoomDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            BasketRoomDatabase::class.java,
            "BasketDatabase"
        ).build()
    }
}
