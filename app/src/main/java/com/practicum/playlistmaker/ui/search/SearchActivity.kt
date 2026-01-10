package com.practicum.playlistmaker.ui.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.HistoryInteractor
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.player.PlayerActivity

class SearchActivity : AppCompatActivity() {
    private var searchText: String = SEARCH_TEXT_DEF
    private lateinit var tracks: ArrayList<Track>
    private lateinit var etSearch: EditText
    private lateinit var llConnectionError: LinearLayout
    private lateinit var bRefreshPage: Button
    private lateinit var tvEmptySearchOutput: TextView
    private lateinit var recycleView: RecyclerView
    private lateinit var bClear: ImageView
    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var bClearHistory: Button
    private lateinit var llSearchHistory: LinearLayout
    private lateinit var rvSearchHistory: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val searchRunnable = Runnable { loadData() }
    private val handler = Handler(Looper.getMainLooper())
    private val trackIterator = Creator.provideTracksInteractor()
    private lateinit var historyInteractor: HistoryInteractor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        historyInteractor = Creator.provideHistoryInteractor(applicationContext)

        adapter = TrackAdapter() { track ->
            historyInteractor.addTrackToHistory(track)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("TRACK", track)
            startActivity(intent)
        }
        tracks = ArrayList<Track>()
        adapter.list = tracks

        etSearch = findViewById<EditText>(R.id.etSearch)
        etSearch.setText(searchText)

        setUpClickListeners()

        val textWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                bClear.isVisible = !s.isNullOrEmpty()
                searchText = etSearch.text.toString()
                if (etSearch.hasFocus() && s?.isEmpty() == true) showSearchHistory()
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        etSearch.addTextChangedListener(textWatcher)


    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
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
        llConnectionError = findViewById<LinearLayout>(R.id.llErrorInternetConnection)
        bRefreshPage = findViewById<Button>(R.id.bRefreshPage)
        tvEmptySearchOutput = findViewById<TextView>(R.id.tvEmptySearchOutput)
        bRefreshPage = findViewById<Button>(R.id.bRefreshPage)
        recycleView = findViewById<RecyclerView>(R.id.rcView)
        etSearch = findViewById<EditText>(R.id.etSearch)
        bClear = findViewById<ImageView>(R.id.bClear)
        bClearHistory = findViewById<Button>(R.id.bClearSearchHistory)
        llSearchHistory = findViewById<LinearLayout>(R.id.llSearchHistory)
        rvSearchHistory = findViewById<RecyclerView>(R.id.rvSearchHistory)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = adapter
        progressBar = findViewById<ProgressBar>(R.id.progressBar)

        historyAdapter = TrackAdapter() { item ->
            historyInteractor.addTrackToHistory(item)
            historyAdapter.list = historyInteractor.getHistory()
            historyAdapter.notifyDataSetChanged()
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("TRACK", item)
            startActivity(intent)
        }

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && etSearch.text.isNotEmpty()) {
                loadData()
            }
            false
        }

        bRefreshPage.setOnClickListener {
            loadData()
        }

        bClear.setOnClickListener {
            etSearch.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(etSearch.windowToken, 0)
            tracks.clear()
            tvEmptySearchOutput.visibility = View.GONE
            llConnectionError.visibility = View.GONE
            adapter.notifyDataSetChanged()
        }

        etSearch.setOnFocusChangeListener() { view, hasFocus ->
            historyAdapter.list = historyInteractor.getHistory()
            if (hasFocus && etSearch.text.isEmpty()) showSearchHistory()
        }

        bClearHistory.setOnClickListener {
            historyInteractor.clearHistory()
            historyAdapter.list = historyInteractor.getHistory()
            historyAdapter.notifyDataSetChanged()
            llSearchHistory.visibility = View.GONE
        }
        findViewById<Toolbar>(R.id.tbSearch).setOnClickListener {
            finish()
        }
    }

    private fun loadData() {
        if (etSearch.text.isNotEmpty()) {
            showProgressBar()
            trackIterator.searchTracks(
                etSearch.text.toString(), object : TrackInteractor.TracksConsumer {
                    override fun consume(foundTracks: List<Track>) {
                        handler.post {
                            showSearchResult()
                            if (foundTracks != null) {
                                tracks.clear()
                                if (foundTracks.isNotEmpty()) {
                                    tracks.addAll(foundTracks)
                                }
                                if (tracks.isEmpty()) {
                                    showSearchEmptyResult()
                                }
                            } else {
                                showConnectionError()
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }

                })
        }
    }

    private fun showSearchResult() {
        recycleView.visibility = View.VISIBLE
        llConnectionError.visibility = View.GONE
        tvEmptySearchOutput.visibility = View.GONE
        llSearchHistory.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        recycleView.visibility = View.GONE
        llConnectionError.visibility = View.GONE
        tvEmptySearchOutput.visibility = View.GONE
        llSearchHistory.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showSearchEmptyResult() {
        recycleView.visibility = View.GONE
        llConnectionError.visibility = View.GONE
        tvEmptySearchOutput.visibility = View.VISIBLE
        llSearchHistory.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    private fun showConnectionError() {
        recycleView.visibility = View.GONE
        llConnectionError.visibility = View.VISIBLE
        tvEmptySearchOutput.visibility = View.GONE
        llSearchHistory.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    private fun showSearchHistory() {
        rvSearchHistory.layoutManager = LinearLayoutManager(this)
        rvSearchHistory.adapter = historyAdapter
        historyAdapter.list = historyInteractor.getHistory()

        if (historyInteractor.getHistory().isNotEmpty()) {
            recycleView.visibility = View.GONE
            llConnectionError.visibility = View.GONE
            tvEmptySearchOutput.visibility = View.GONE
            llSearchHistory.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        private const val SEARCH_TEXT_DEF = ""
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}