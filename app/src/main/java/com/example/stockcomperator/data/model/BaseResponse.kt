package com.example.stockcomperator.data.model
import com.google.gson.annotations.SerializedName


data class BaseResponse (

    @SerializedName("content" ) var content : Content? = Content(),
    @SerializedName("status"  ) var status  : String?  = null

)