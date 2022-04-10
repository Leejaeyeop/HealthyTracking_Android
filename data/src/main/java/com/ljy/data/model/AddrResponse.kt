package com.ljy.data.model

import com.google.gson.annotations.SerializedName

data class AddrResponse ( //json

    @SerializedName("addrs")
    val addr:List<AddrEntity>
)