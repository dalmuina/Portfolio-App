package com.dalmuina.portolioapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dalmuina.portolioapp.domain.GetQuoteUseCase
import com.dalmuina.portolioapp.domain.GetRandomQuoteUseCase
import com.dalmuina.portolioapp.domain.model.QuoteItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuoteViewModel @Inject constructor(private val getQuotesUseCase : GetQuoteUseCase,
                                         private val getRandomQuoteUseCase : GetRandomQuoteUseCase
): ViewModel() {

    val quoteModel = MutableLiveData<QuoteItem>()
    val isLoading = MutableLiveData<Boolean>()



    fun randomQuote(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val quote = getRandomQuoteUseCase()
            quote?.let{
                quoteModel.postValue(it)
            }
            isLoading.postValue(false)
        }
    }

    fun onCreate() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getQuotesUseCase()

            if (result.isNotEmpty()){
                quoteModel.postValue(result[0])
                isLoading.postValue(false)
            }
        }
    }
}
