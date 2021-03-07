package com.example.wordlistroomsample.di

import android.content.Context
import androidx.room.Room
import com.example.wordlistroomsample.data.WordRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        wordDatabaseCallback: WordRoomDatabase.WordDatabaseCallback) =
        Room.databaseBuilder(context, WordRoomDatabase::class.java, "word_database")
            .fallbackToDestructiveMigration()
            .addCallback(wordDatabaseCallback)
            .build()

    @Provides
    fun provideWordDao(database: WordRoomDatabase) = database.wordDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() =  CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope