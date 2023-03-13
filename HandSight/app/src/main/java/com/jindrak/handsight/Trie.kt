package com.jindrak.handsight

import android.content.res.Resources
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.streams.toList

object Trie {
    private var root: TrieNode? = null

    fun search(word: String): Boolean {
        var current = root
        for (element in word) {
            current = current?.getChild(element)
            if (current == null) {
                return false
            }
        }
        return true
    }

    private fun insert(word: String) {
        if (root == null) {
            root = TrieNode()
        }
        var current = root
        for (element in word) {
            var node = current?.getChild(element)
            if (node == null) {
                node = TrieNode()
                current?.setChild(element, node)
            }
            current = node
        }
    }

    private fun loadWordsFromFile(resources: Resources): List<String> {
        return try {
            val inputStream = resources.openRawResource(R.raw.words)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val lines = reader.lines()
            lines.toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun initialize(resources: Resources) {
        val words = loadWordsFromFile(resources)
        root = TrieNode()
        for (word in words) {
            insert(word.lowercase())
        }
    }

    fun getInstance(resources: Resources): Trie {
        return synchronized(this) {
            if (root == null) {
                initialize(resources)
            }
            this
        }
    }
}

class TrieNode {
    private val children = mutableMapOf<Char, TrieNode>()

    fun getChild(ch: Char): TrieNode? {
        return children[ch]
    }

    fun setChild(ch: Char, node: TrieNode) {
        children[ch] = node
    }
}
