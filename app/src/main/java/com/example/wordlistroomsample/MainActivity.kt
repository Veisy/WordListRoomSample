package com.example.wordlistroomsample

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wordlistroomsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = WordListAdapter()
    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as WordsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFloatingActionButton()
        initRecyclerView()
        initObserver()
    }

    private fun initRecyclerView() {
        binding.recyclerViewList.adapter = adapter
        binding.recyclerViewList.layoutManager = LinearLayoutManager(this)
    }

    private fun initObserver() {
        wordViewModel.allWords.observe(this, { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.submitList(it) }
        })
    }

    private fun initFloatingActionButton() {
        binding.fab.setOnClickListener {
            val input = binding.editTextNewInput.text

            if(!TextUtils.isEmpty(input)) {
                val word = Word(input.toString())
                wordViewModel.insert(word)
                binding.editTextNewInput.setText("")
            }

        }
    }

}