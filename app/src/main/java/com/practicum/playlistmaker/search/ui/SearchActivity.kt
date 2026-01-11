package com.practicum.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.player.ui.PlayerActivity

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private var searchText: String = SEARCH_TEXT_DEF
    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, SearchViewModel.getFactory(
                Creator.provideHistoryInteractor(this), Creator.provideTracksInteractor()
            )
        ).get(SearchViewModel::class.java)

        adapter = TrackAdapter() { track ->
            viewModel.addTrackToHistory(track)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("TRACK", track)
            startActivity(intent)
        }

        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter

        historyAdapter = TrackAdapter() { item ->
            viewModel.addTrackToHistory(item)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("TRACK", item)
            startActivity(intent)
        }

        binding.rvSearchHistory.layoutManager = LinearLayoutManager(this)
        binding.rvSearchHistory.adapter = historyAdapter

        binding.etSearch.setText(searchText)
        setUpClickListeners()

        val textWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.bClear.isVisible = !s.isNullOrEmpty()
                searchText = binding.etSearch.text.toString()
                if (binding.etSearch.hasFocus() && s?.isEmpty() == true) {
                    goneEverything()
                    showSearchHistory()
                }
                viewModel.searchDebounce(searchText)
            }

            override fun afterTextChanged(s: Editable?) {}

        }
        binding.etSearch.addTextChangedListener(textWatcher)

        viewModel.observeState().observe(this) {
            Log.i("STATE", "Нахожусь в observeState состояние - ${it.state}")
            historyAdapter.list = it.tracksHistory
            adapter.list = it.tracksSearch

            onStateChanged(it.state)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH_TEXT", searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, SEARCH_TEXT_DEF)
    }

    private fun setUpClickListeners() {

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && binding.etSearch.text.isNotEmpty()) {
                viewModel.search(binding.etSearch.text.toString())
            }
            false
        }

        binding.bRefreshPage.setOnClickListener {
            viewModel.search(binding.etSearch.text.toString())
        }

        binding.bClear.setOnClickListener {
            binding.etSearch.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
            viewModel.clearTracks()
            binding.tvEmptySearchOutput.visibility = View.GONE
            binding.llErrorInternetConnection.visibility = View.GONE
        }

        binding.etSearch.setOnFocusChangeListener() { view, hasFocus ->
            if (hasFocus && binding.etSearch.text.isEmpty()) showSearchHistory()
        }

        binding.bClearSearchHistory.setOnClickListener {
            viewModel.onButtonClearHistoryClicked()
            binding.llSearchHistory.visibility = View.GONE
        }
        findViewById<Toolbar>(R.id.tbSearch).setOnClickListener {
            finish()
        }
    }


    private fun onStateChanged(state: Int) {
        Log.i("STATE", "Нахожусь в onStateChanged состояние - $state")
        when (state) {
            LOADING_STATE -> showProgressBar()
            CONNECTION_ERROR_STATE -> showConnectionError()
            EMPTY_RESULT_STATE -> showSearchEmptyResult()
            RESULT_STATE -> showSearchResult()
            DEFAULT_STATE -> showSearchHistory()
        }
    }

    private fun showSearchResult() {
        goneEverything()
        binding.rcView.visibility = View.VISIBLE
    }

    private fun showProgressBar() {
        goneEverything()
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showSearchEmptyResult() {
        goneEverything()
        binding.tvEmptySearchOutput.visibility = View.VISIBLE
    }

    private fun showConnectionError() {
        goneEverything()
        binding.llErrorInternetConnection.visibility = View.VISIBLE
    }

    private fun showSearchHistory() {
        viewModel.getHistoryList()
        if (historyAdapter.list.isNotEmpty()) {
            goneEverything()
            binding.llSearchHistory.visibility = View.VISIBLE
        }
    }
    private fun goneEverything() {
        binding.rcView.visibility = View.GONE
        binding.llErrorInternetConnection.visibility = View.GONE
        binding.tvEmptySearchOutput.visibility = View.GONE
        binding.llSearchHistory.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    companion object {
        private const val SEARCH_TEXT_DEF = ""
        private const val SEARCH_TEXT = "SEARCH_TEXT"

        private const val LOADING_STATE = 0
        private const val CONNECTION_ERROR_STATE = 1
        private const val EMPTY_RESULT_STATE = 2
        private const val RESULT_STATE = 3
        private const val DEFAULT_STATE = 4
    }
}