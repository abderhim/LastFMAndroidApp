package com.io.appsfactorytesttask.data.network

import com.io.appsfactorytesttask.data.entities.ArtistMatches
import com.io.appsfactorytesttask.data.entities.TopAlbums
import com.io.appsfactorytesttask.data.entities.Tracks

sealed class ApiState{
    object Loading : ApiState()
    class Failure(val msg:Throwable) : ApiState()
    class SuccessGettingTopAlbums(val data:TopAlbums) : ApiState()
    class SuccessSearchingArtists(val data: ArtistMatches?) : ApiState()
    class SuccessGettingAlbumInfo(val data: Tracks?) : ApiState()
    object Empty : ApiState()
}