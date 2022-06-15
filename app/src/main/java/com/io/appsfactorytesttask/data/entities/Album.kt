package com.io.appsfactorytesttask.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Album(

    var name: String?,
    @SerializedName("image")
    var imagesList: List<AlbumImage>?,
    var artist: Artist?,

    ) : Serializable