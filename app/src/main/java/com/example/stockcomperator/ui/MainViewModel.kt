package com.example.stockcomperator.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockcomperator.data.model.BaseResponse
import com.example.stockcomperator.data.network.Resource
import com.example.stockcomperator.data.network.StockRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val stockRepo: StockRepo) : ViewModel() {

    private val _stockListResult: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
    val stockListResult: LiveData<Resource<BaseResponse>> = _stockListResult

    private val _symbolListResult: MutableLiveData<List<String>> = MutableLiveData()
    val symbolListResult: LiveData<List<String>> = _symbolListResult

    fun getStockList(type: String) {
        viewModelScope.launch {
            _stockListResult.value = Resource.loading()
            //fake network call waiting
            delay(1000)
            _stockListResult.value = stockRepo.getStockList(type)
        }
    }

    fun getSymbolList() {
        viewModelScope.launch {
            _symbolListResult.value =
                _stockListResult.value?.data?.content?.quoteSymbols?.map {
                    it.symbol.orEmpty()
                }
        }
    }
}