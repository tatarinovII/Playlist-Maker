package com.practicum.playlistmaker.searchActivity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.models.Track
import com.practicum.playlistmaker.retrofit.RetrofitApi
import com.practicum.playlistmaker.retrofit.SongApi
import com.practicum.playlistmaker.retrofit.SongSearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_TEXT_DEF = ""
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    private val retrofit = RetrofitApi().retrofitApi
    private val songApi = retrofit.create(SongApi::class.java)
    private var searchText: String = SEARCH_TEXT_DEF
    private lateinit var list: ArrayList<Track>
    private lateinit var etSearch: EditText
    private lateinit var llConnectionError: LinearLayout
    private lateinit var bRefreshPage: Button
    private lateinit var tvEmptySearchOutput: TextView
    private lateinit var recycleView: RecyclerView
    private lateinit var bClear: ImageView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var bClearHistory: Button
    private lateinit var llSearchHistory: LinearLayout
    private lateinit var rvSearchHistory: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPreferences = getSharedPreferences("MYPREFS", MODE_PRIVATE)

        adapter = TrackAdapter() { track ->
            TrackPreferences(sharedPreferences).addToSharedPrefs(track)
        }
        list = ArrayList<Track>()
        adapter.list = list

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
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        etSearch.addTextChangedListener(textWatcher)


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

        historyAdapter = TrackAdapter() { item ->
            TrackPreferences(sharedPreferences).addToSharedPrefs(item)
            historyAdapter.list = TrackPreferences(sharedPreferences).read()
            historyAdapter.notifyDataSetChanged()
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
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(etSearch.windowToken, 0)
            list.clear()
            tvEmptySearchOutput.visibility = View.GONE
            llConnectionError.visibility = View.GONE
            adapter.notifyDataSetChanged()
        }

        etSearch.setOnFocusChangeListener() { view, hasFocus ->
            historyAdapter.list = TrackPreferences(sharedPreferences).read()
            if (hasFocus && etSearch.text.isEmpty()) showSearchHistory()
        }

        bClearHistory.setOnClickListener {
            TrackPreferences(sharedPreferences).clear()
            historyAdapter.list = TrackPreferences(sharedPreferences).read()
            historyAdapter.notifyDataSetChanged()
            llSearchHistory.visibility = View.GONE
        }
    }

    private fun loadData() {
        songApi.search(etSearch.text.toString()).enqueue(object : Callback<SongSearchResponse> {
            override fun onResponse(
                call: Call<SongSearchResponse>, response: Response<SongSearchResponse>
            ) {
                showSearchResult()
                when (response.code()) {
                    200 -> {
                        list.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            list.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (list.isEmpty() && etSearch.text.isNotEmpty()) {
                            showSearchEmptyResult()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SongSearchResponse>, t: Throwable) {
                showConnectionError()
            }
        })
    }

    private fun showSearchResult() {
        recycleView.visibility = View.VISIBLE
        llConnectionError.visibility = View.GONE
        tvEmptySearchOutput.visibility = View.GONE
        llSearchHistory.visibility = View.GONE
    }

    private fun showSearchEmptyResult() {
        recycleView.visibility = View.GONE
        llConnectionError.visibility = View.GONE
        tvEmptySearchOutput.visibility = View.VISIBLE
        llSearchHistory.visibility = View.GONE
    }

    private fun showConnectionError() {
        recycleView.visibility = View.GONE
        llConnectionError.visibility = View.VISIBLE
        tvEmptySearchOutput.visibility = View.GONE
        llSearchHistory.visibility = View.GONE
    }

    private fun showSearchHistory() {
        rvSearchHistory.layoutManager = LinearLayoutManager(this)
        rvSearchHistory.adapter = historyAdapter
        historyAdapter.list = TrackPreferences(sharedPreferences).read()

        if (TrackPreferences(sharedPreferences).read().isNotEmpty()) {
            recycleView.visibility = View.GONE
            llConnectionError.visibility = View.GONE
            tvEmptySearchOutput.visibility = View.GONE
            llSearchHistory.visibility = View.VISIBLE
        }
    }

}