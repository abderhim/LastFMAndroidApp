package com.io.appsfactorytesttask.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TopAlbums (
    @SerializedName("album")
    var albumsList: List<Album>?,

    ) : Serializable