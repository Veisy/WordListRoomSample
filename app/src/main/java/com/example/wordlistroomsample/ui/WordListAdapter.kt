package com.example.wordlistroomsample.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wordlistroomsample.data.Word
import com.example.wordlistroomsample.databinding.RecyclerviewItemBinding

class WordListAdapter (private val onWordClicked: (Int) -> Unit)
    : ListAdapter<Word, WordListAdapter.WordViewHolder>(WordsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder.create(parent, onWordClicked)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.text)
    }

    fun getWordAt(position: Int): Word {
        return getItem(position)
    }

    class WordViewHolder(
            private val binding: RecyclerviewItemBinding,
            onWordClicked: (Int) -> Unit)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener{
                onWordClicked(adapterPosition)
            }
        }

        fun bind(text: String?) {
            binding.textViewWord.text = text
        }

        companion object {
            fun create(parent: ViewGroup, onWordClicked: (Int) -> Unit): WordViewHolder {
                val binding: RecyclerviewItemBinding = RecyclerviewItemBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                return WordViewHolder(binding, onWordClicked)
            }
        }
    }

    class WordsComparator : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean =
            oldItem.text == newItem.text

    }
}