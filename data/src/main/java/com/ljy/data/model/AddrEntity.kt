package com.ljy.data.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName="addr")
data class AddrEntity (
    @SerializedName("addr")
    val addr:String,

)