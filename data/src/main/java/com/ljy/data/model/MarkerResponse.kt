package com.ljy.data.model

import com.google.gson.annotations.SerializedName

data class MarkerResponse ( //json

    @SerializedName("markers")
    val markers:List<MarkerEntity>
)