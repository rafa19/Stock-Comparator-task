package com.example.stockcomperator.data.model

import com.google.gson.annotations.SerializedName


data class QuoteSymbols (

    @SerializedName("symbol"     ) var symbol     : String?           = null,
    @SerializedName("timestamps" ) var timestamps : ArrayList<Long>   = arrayListOf(),
    @SerializedName("closures"   ) var closures   : ArrayList<Double> = arrayListOf()

)