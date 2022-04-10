package com.ljy.data.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName="marker")
data class MarkerEntity (
    @SerializedName("id")
    val id:Int,
    @SerializedName("x")
    val x:Float,
    @SerializedName("y")
    val y:Float,
    @SerializedName("alt")
    val alt:Float,
    @SerializedName("name")
    val name:String,
)