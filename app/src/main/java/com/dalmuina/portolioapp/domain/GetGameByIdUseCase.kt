package com.dalmuina.portolioapp.domain

import com.dalmuina.portolioapp.data.GameRepository
import com.dalmuina.portolioapp.domain.model.GameDetail
import javax.inject.Inject

class GetGameByIdUseCase @Inject constructor(private val repository: GameRepository){

    suspend operator fun invoke(id: Int):GameDetail? {
        return repository.getGameById(id)
    }
}
