package com.example.wordlistroomsample

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
import com.example.wordlistroomsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private val adapter: WordListAdapter by lazy { WordListAdapter{ position: Int ->
        onWordClicked(position)
    }}
    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as WordsApplication).repository)
    }

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
        binding.recyclerViewList.adapter = adapter
        binding.recyclerViewList.layoutManager = LinearLayoutManager(this)
    }

    private fun initObserver() {
        wordViewModel.allWords.observe(this, { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.submitList(it) }
            if (adapter.itemCount > 1) {
                binding.recyclerViewList.smoothScrollToPosition(adapter.itemCount - 1)
            }
        })
    }

    private fun initItemTouchHelper() {
        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                wordViewModel.deleteWord(adapter.getWordAt(viewHolder.adapterPosition))
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
            words?.let { adapter.submitList(it) }
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
        editText.setText(adapter.getWordAt(position).text)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Item")
        builder.setCancelable(true)
        builder.setView(editText)

        builder.setNegativeButton("Cancel") { _, _ ->
        }

        builder.setPositiveButton("Update") { _, _ ->
            val id = adapter.getWordAt(position).id
            val newWord = Word(id, editText.text.toString())
            wordViewModel.update(newWord)
        }

        builder.show()
    }
}