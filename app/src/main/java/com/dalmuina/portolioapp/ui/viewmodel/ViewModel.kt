package com.dalmuina.portolioapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dalmuina.portolioapp.domain.GetQuotesUseCase
import com.dalmuina.portolioapp.domain.GetRandomQuoteUseCase
import com.dalmuina.portolioapp.domain.model.QuoteItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuoteViewModel @Inject constructor(private val getQuotesUseCase : GetQuotesUseCase,
                                         private val getRandomQuoteUseCase : GetRandomQuoteUseCase
): ViewModel() {

    private val _quoteModel = MutableLiveData<QuoteItem>()
    val quoteModel : LiveData<QuoteItem> = _quoteModel

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun randomQuote(){
        viewModelScope.launch {
            _isLoading.postValue(true)
            val quote = getRandomQuoteUseCase()
            quote?.let{
                _quoteModel.postValue(it)
            }
            _isLoading.postValue(false)
        }
    }

    fun onCreate() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val result = getQuotesUseCase()

            if (result.isNotEmpty()){
                _quoteModel.postValue(result[0])
                _isLoading.postValue(false)
            }
        }
    }
}