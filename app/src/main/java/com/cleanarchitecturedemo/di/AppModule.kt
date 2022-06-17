package com.cleanarchitecturedemo.di

import android.app.Application
import com.cleanarchitecturedemo.BuildConfig
import com.cleanarchitecturedemo.data.api.NetworkModule
import com.cleanarchitecturedemo.data.books.*
import com.cleanarchitecturedemo.data.db.BooksDatabase
import com.cleanarchitecturedemo.domain.repositories.BooksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers


@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    fun provideDataBase(app: Application): BooksDatabase =
        BooksDatabase.getDatabase(app.applicationContext)

    @Provides
    fun provideBookDao(db: BooksDatabase) = db.bookDao()

    @Provides
    fun provideCoroutineScopeIO() = Dispatchers.IO


    @Provides
    fun provideBookRemoteDataSource(
        remote: BooksRemoteDataSourceImpl
    ): BooksRemoteDataSource = remote

    @Provides
    fun provideBookLocalDataSource(
        impl: BooksLocalDataSourceImpl,
    ): BooksLocalDataSource = impl


    @Provides
    fun provideBookRepo(impl: BooksRepositoryImpl): BooksRepository = impl

    @Provides
    fun provideBookAPI(remote: NetworkModule) =
        remote.createBooksApi(BuildConfig.GOOGLE_APIS_ENDPOINT)

}