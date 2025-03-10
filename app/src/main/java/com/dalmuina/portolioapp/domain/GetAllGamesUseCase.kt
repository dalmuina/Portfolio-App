package com.dalmuina.portolioapp.domain

import com.dalmuina.portolioapp.data.GameRepository
import com.dalmuina.portolioapp.domain.model.GameItem
import javax.inject.Inject

class GetAllGamesUseCase @Inject constructor(private val repository: GameRepository){

    suspend operator fun invoke():List<GameItem>? {
        return repository.getAllGamesFromApi()
    }
}
