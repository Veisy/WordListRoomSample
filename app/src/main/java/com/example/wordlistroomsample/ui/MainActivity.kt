package com.example.wordlistroomsample.ui

import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordlistroomsample.R
import com.example.wordlistroomsample.data.Word
import com.example.wordlistroomsample.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private val listAdapter: WordListAdapter by lazy { WordListAdapter{ position: Int ->
        onWordClicked(position)
    }}


    private val wordViewModel: WordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initObserver()
        initItemTouchHelper()
        initFloatingActionButton()
        initSearchView()
    }

    private fun initRecyclerView() {

        binding.apply {
            recyclerViewList.apply {
                adapter = listAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
                setHasFixedSize(true)
            }
        }
    }

    private fun initObserver() {
        wordViewModel.allWords.observe(this) { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { listAdapter.submitList(it) }
            if (listAdapter.itemCount > 1) {
                binding.recyclerViewList.smoothScrollToPosition(listAdapter.itemCount - 1)
            }
        }
    }

    private fun initItemTouchHelper() {
        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                wordViewModel.deleteWord(listAdapter.getWordAt(viewHolder.adapterPosition))
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewList)

    }

    private fun initFloatingActionButton() {
        binding.fab.setOnClickListener {
            val input = binding.editTextNewInput.text

            if(!TextUtils.isEmpty(input)) {
                val word = Word(0, input.toString())
                wordViewModel.insert(word)
                binding.editTextNewInput.setText("")
            }
        }
    }

    private fun initSearchView() {
       binding.searchView.setOnQueryTextListener(this)
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"

        wordViewModel.searchDatabase(searchQuery).observe(this,  { words ->
            words?.let { listAdapter.submitList(it) }
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
       return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null) {
            searchDatabase(query)
        }
        return true
    }

    private fun onWordClicked(position: Int) {
        val editText = EditText(this)
        editText.setText(listAdapter.getWordAt(position).text)

        val builder = AlertDialog.Builder(this, R.style.UpdateDialogTheme)
        builder.setCancelable(true)
        builder.setView(editText)

        builder.setNegativeButton("Cancel") { _, _ ->
        }

        builder.setPositiveButton("Update") { _, _ ->
            val id = listAdapter.getWordAt(position).id
            if(listAdapter.getWordAt(position).text != editText.text.toString()) {
                val newWord = Word(id, editText.text.toString())
                wordViewModel.update(newWord)
            }
        }

        builder.show()
    }
}