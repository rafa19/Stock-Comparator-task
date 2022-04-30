package com.example.stockcomperator.data.model

import com.google.gson.annotations.SerializedName


data class Content (

    @SerializedName("quoteSymbols" ) var quoteSymbols : ArrayList<QuoteSymbols> = arrayListOf()

)