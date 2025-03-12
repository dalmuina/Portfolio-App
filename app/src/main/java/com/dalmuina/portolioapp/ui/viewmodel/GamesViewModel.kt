package com.dalmuina.portolioapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dalmuina.portolioapp.domain.GetAllGamesUseCase
import com.dalmuina.portolioapp.domain.GetGameByIdUseCase
import com.dalmuina.portolioapp.domain.model.GameItem
import com.dalmuina.portolioapp.domain.model.GameDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class GamesViewModel @Inject constructor(private val getAllGamesUseCase: GetAllGamesUseCase,
    private val getGameByIdUseCase: GetGameByIdUseCase) : ViewModel() {

    private val _games = MutableStateFlow<List<GameItem>>(emptyList())
    val games = _games.asStateFlow()

    var detail by mutableStateOf(GameDetail())
        private set

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

    fun getGameById(id : Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = getGameByIdUseCase(id)
                //val result = repo.getGameById(id)
                detail = detail.copy(
                    name = result?.name ?: "",
                    descriptionRaw = result?.descriptionRaw ?: "",
                    metacritic = result?.metacritic ?: 111,
                    website = result?.website ?: "sin web",
                    backgroundImage = result?.backgroundImage ?: "",
                )
            }
        }
    }

    fun clean(){
        detail = detail.copy(
            name =  "",
            descriptionRaw =  "",
            metacritic =  100,
            website =  "",
            backgroundImage = "",
        )
    }
}
