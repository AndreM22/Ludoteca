package com.andremachicao.ludoteca.di

import com.andremachicao.ludoteca.firebase_MVVM.data.exchange.repository.GameExRepository
import com.andremachicao.ludoteca.firebase_MVVM.data.exchange.repository.GameExRepositoryImp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideExchangeRepository(
        database:FirebaseFirestore
    ): GameExRepository{
        return GameExRepositoryImp(database)
    }

}