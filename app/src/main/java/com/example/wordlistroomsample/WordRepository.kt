package com.example.wordlistroomsample

import androidx.annotation.WorkerThread
import com.example.wordlistroomsample.data.Word
import com.example.wordlistroomsample.data.WordDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// We pass in the DAO instead of the whole database, because we only need access to the DAO
class WordRepository @Inject constructor(private val wordDao: WordDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allWords: Flow<List<Word>> = wordDao.getOrderedWords()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.

    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }

    @WorkerThread
    suspend fun update(word: Word) {
        wordDao.update(word)
    }

    @WorkerThread
    suspend fun deleteWord(word: Word) {
        wordDao.deleteWord(word)
    }

    fun searchDatabase(searchQuery: String): Flow<List<Word>> {
        return wordDao.searchDatabase(searchQuery)
    }
}