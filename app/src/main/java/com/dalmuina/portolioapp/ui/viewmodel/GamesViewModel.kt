package com.dalmuina.portolioapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dalmuina.portolioapp.domain.GetAllGamesUseCase
import com.dalmuina.portolioapp.domain.model.GameItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class GamesViewModel @Inject constructor(private val getAllGamesUseCase: GetAllGamesUseCase) : ViewModel() {

    private val _games = MutableStateFlow<List<GameItem>>(emptyList())
    val games = _games.asStateFlow()

    init {
        fetchGames()
    }

    private fun fetchGames(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = getAllGamesUseCase()
                _games.value = result ?: emptyList()
            }
        }
    }
}
