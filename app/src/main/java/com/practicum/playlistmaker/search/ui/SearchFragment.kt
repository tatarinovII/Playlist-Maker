package com.practicum.playlistmaker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.models.SearchScreenState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private var searchText: String = SEARCH_TEXT_DEF
    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TrackAdapter(
            onItemClick = { track ->
                viewModel.addTrackToHistory(track)
                findNavController().navigate(
                    R.id.action_searchFragment_to_playerFragment, PlayerFragment.createArgs(track)
                )
            }, {}
        )

        binding.rcView.layoutManager = LinearLayoutManager(requireContext())
        binding.rcView.adapter = adapter

        historyAdapter = TrackAdapter( { item ->
            viewModel.addTrackToHistory(item)

            findNavController().navigate(
                R.id.action_searchFragment_to_playerFragment, PlayerFragment.createArgs(item)
            )
        },{})

        binding.rvSearchHistory.layoutManager = LinearLayoutManager(requireContext())
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

        viewModel.observeState().observe(viewLifecycleOwner) {
            historyAdapter.list = it.tracksHistory
            adapter.list = it.tracksSearch

            onStateChanged(it.state)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH_TEXT", searchText)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        searchText = savedInstanceState?.getString(SEARCH_TEXT, SEARCH_TEXT_DEF) ?: SEARCH_TEXT_DEF
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
            val inputMethodManager =
                requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
            viewModel.clearTracks()
            goneEverything()
            showSearchHistory()
        }

        binding.etSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.etSearch.text.isEmpty()) showSearchHistory()
        }

        binding.bClearSearchHistory.setOnClickListener {
            viewModel.onButtonClearHistoryClicked()
            binding.llSearchHistory.visibility = View.GONE
        }
    }


    private fun onStateChanged(state: Int) {
        when (state) {
            SearchScreenState.LOADING_STATE.state -> showProgressBar()
            SearchScreenState.CONNECTION_ERROR_STATE.state -> showConnectionError()
            SearchScreenState.EMPTY_RESULT_STATE.state -> showSearchEmptyResult()
            SearchScreenState.RESULT_STATE.state -> showSearchResult()
            SearchScreenState.DEFAULT_STATE.state -> showSearchHistory()
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
    }
}