package com.example.jokesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokesapp.repository.JokesRepo
import com.example.jokesapp.ui.screens.UIstate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JokesViewmodel @Inject constructor(private val jokesRepo: JokesRepo) : ViewModel() {

    private val _homeUIState: MutableStateFlow<UIstate> = MutableStateFlow(UIstate.Initial)
    val homeUIstate = _homeUIState.asStateFlow()

    private val _bookmarkUIState: MutableStateFlow<UIstate> = MutableStateFlow(UIstate.Initial)
    val bookmarkUIstate = _bookmarkUIState.asStateFlow()

    init {
        fetchUnbookMarkedJokes()
    }

    private fun fetchUnbookMarkedJokes(genre: String = "Any", amount: Int = 25) {
        viewModelScope.launch {
            _homeUIState.value = UIstate.Loading
            try {
                jokesRepo.fetchUnBookMarkedJokes(genre = genre, amount = amount)
                    .collect { jokesList ->
                        _homeUIState.value = UIstate.Success(jokesList)
                    }
            } catch (e: Exception) {
                _homeUIState.value = UIstate.Error(e.message.toString())
            }
        }
    }

    fun updateBookmarkStatus(id: Int, bookMarked: Boolean) {
        viewModelScope.launch {
            jokesRepo.updateBookmarkStatus(id, bookMarked)
        }
    }

    fun fetchBookMarkedJokes() {
        _bookmarkUIState.value = UIstate.Loading
        viewModelScope.launch {
            try {
                jokesRepo.fetchBookMarkedJokes().collect { jokesList ->
                    _bookmarkUIState.value = UIstate.Success(jokesList = jokesList)
                }
            } catch (e: Exception) {
                _bookmarkUIState.value = UIstate.Error(e.message.toString())
            }
        }
    }

    fun deleteUnbookmarkedJokes() {
        viewModelScope.launch {
            try {
                jokesRepo.deleteUnbookmarkedJokes()
            } catch (_: Exception) {
            }
        }
    }

    fun deleteJokeViaId(id: Int) {
        viewModelScope.launch {
            try {
                jokesRepo.deleteJokeViaId(id)
            } catch (_: Exception) {
            }
        }
    }
}