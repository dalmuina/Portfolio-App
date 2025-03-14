package com.dalmuina.portolioapp.ui.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dalmuina.portolioapp.domain.GetAllGamesUseCase
import com.dalmuina.portolioapp.domain.GetGameByIdUseCase
import com.dalmuina.portolioapp.domain.model.GameItem
import com.dalmuina.portolioapp.domain.model.GameDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class GamesViewModel @Inject constructor(private val getAllGamesUseCase: GetAllGamesUseCase,
    private val getGameByIdUseCase: GetGameByIdUseCase) : ViewModel() {

    private val _games = MutableStateFlow<List<GameItem>>(emptyList())
    val games = _games.asStateFlow()

    private val _detail = MutableStateFlow(GameDetail())
    val detail: StateFlow<GameDetail> = _detail.asStateFlow()

    init {
        fetchGames()
    }

    fun fetchGames(){
        viewModelScope.launch {
            withContext(Dispatchers.Main){
                val result = getAllGamesUseCase() ?: emptyList()
                _games.value = result
            }
        }
    }

    fun getGameById(id : Int){
        viewModelScope.launch {
            withContext(Dispatchers.Main){
                val result = getGameByIdUseCase(id)
                _detail.value = GameDetail(
                    name = result?.name ?: "",
                    descriptionRaw = result?.descriptionRaw ?: "",
                    metacritic = result?.metacritic ?: 0,
                    website = result?.website ?: "sin web",
                    backgroundImage = result?.backgroundImage ?: "",
                )
            }
        }
    }

    fun clean(){
        _detail.value = GameDetail(
            name =  "",
            descriptionRaw =  "",
            metacritic =  0,
            website =  "",
            backgroundImage = "",
        )
    }
}
