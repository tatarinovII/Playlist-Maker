package com.practicum.playlistmaker.search.models

enum class SearchScreenState(val state: Int) {
    LOADING_STATE(0),
    CONNECTION_ERROR_STATE(1),
    EMPTY_RESULT_STATE(2),
    RESULT_STATE(3),
    DEFAULT_STATE(4)
}