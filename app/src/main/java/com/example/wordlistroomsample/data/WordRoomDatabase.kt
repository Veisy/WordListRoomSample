package com.example.wordlistroomsample.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.wordlistroomsample.data.Word
import com.example.wordlistroomsample.data.WordDao
import com.example.wordlistroomsample.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
        entities = [Word::class],
        version = 1,
        exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao() : WordDao


    class WordDatabaseCallback
    @Inject
    constructor(
        @ApplicationScope private val applicationScope: CoroutineScope,
        private val database: Provider<WordRoomDatabase>
    )
        : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            database.get()?.let {
                applicationScope.launch {
                    populateDatabase(it.wordDao())
                }
            }
        }

        private suspend fun populateDatabase(wordDao: WordDao) {
            wordDao.deleteAll()
        }

    }
}