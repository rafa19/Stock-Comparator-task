package com.example.stockcomperator.data.network

import javax.inject.Inject

class StockRepo @Inject constructor(private val stockService: StockService) : BaseDataSource() {

    suspend fun getStockList(type: String) =
        getResult { stockService.getStockList(type) }

}