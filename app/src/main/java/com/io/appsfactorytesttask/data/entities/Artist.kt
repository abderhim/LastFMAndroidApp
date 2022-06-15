package com.io.appsfactorytesttask.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Artist (

    var name: String?,
    @SerializedName("image")
    var imagesList : List<ArtistImage>?,

    ) : Serializable