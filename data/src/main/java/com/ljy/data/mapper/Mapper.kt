package com.ljy.data.mapper

import com.google.gson.annotations.SerializedName
import com.ljy.data.model.AddrEntity
import com.ljy.data.model.MarkerEntity
import com.ljy.domain.model.Addr
import com.ljy.domain.model.Marker


// data-> domain  // marker list로 변환
fun mapperToMarker(markers: List<MarkerEntity>): List<Marker>{

    return markers.toList().map{
        Marker(
            it.id,
            it.x,
            it.y,
            it.alt,
            it.name,
        )
    }

}

fun mapperToString(addrs:List<AddrEntity>):List<String>{
    return addrs.toList().map{
        it.addr
    }
}

