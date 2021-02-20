package com.example.wordlistroomsample

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    // - the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    val allWords: LiveData<List<Word>> = repository.allWords.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Word>> {
        return repository.searchDatabase(searchQuery).asLiveData()
    }
}
